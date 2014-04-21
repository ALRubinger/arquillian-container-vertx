package org.jboss.arquillian.vertx.embedded;

import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.context.annotation.ContainerScoped;
import org.jboss.arquillian.container.test.spi.client.protocol.Protocol;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.vertx.common.CommonDeployableContainer;
import org.jboss.arquillian.vertx.common.DeploymentContext;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
public class EmbeddedDeployableContainer extends CommonDeployableContainer<EmbeddedContainerConfiguration> {

    private PlatformManager platformManager;
    private static final String TEMP_FILE_PREFIX = "arquillian-vertx-deployment-module";
    private static final String TEMP_FILE_SUFFIX = ".zip";
    private static final String DEPLOYMENT_CONTEXT_NAME = "Deployment";

    @Inject
    @ApplicationScoped
    private InstanceProducer<Vertx> vertxInstanceProducer;

    @Override
    public Class<EmbeddedContainerConfiguration> getConfigurationClass() {
        return EmbeddedContainerConfiguration.class;
    }

    @Override
    public void setup(final EmbeddedContainerConfiguration config) {
        super.setup(config);
        final PlatformManager pm = PlatformLocator.factory.createPlatformManager();
        this.platformManager = pm;
    }

    @Override
    public void start() throws LifecycleException {
        final Vertx vertx = platformManager.vertx();
        vertxInstanceProducer.set(vertx);
    }

    @Override
    public void stop() throws LifecycleException {

    }

    /**
     * {@inheritDoc}
     * @param archive
     * @return
     * @throws DeploymentException
     */
    @Override
    public ProtocolMetaData deploy(final Archive<?> archive) throws DeploymentException {

        // Create a temp file and mark to remove when done
        //TODO Do direct deployment via InputStream pending https://bugs.eclipse.org/bugs/show_bug.cgi?id=430935
        final File tempFile;
        try{
            tempFile = File.createTempFile(TEMP_FILE_PREFIX,TEMP_FILE_SUFFIX);
        }catch(final IOException ioe){
            throw new DeploymentException("Could not allocate new temp file for internal deployment to Vert.x",ioe);
        }
        tempFile.deleteOnExit();

        // Flush the archive to the temp file
        archive.as(ZipExporter.class).exportTo(tempFile);

        // Grab the container config
        final EmbeddedContainerConfiguration config = this.getConfiguration();

        // Create a handler to block until deployment is done
        final ProtocolMetaData pmd = new ProtocolMetaData();
        final Handler<AsyncResult<String>> deploymentHandler = new DeploymentHandler(pmd);

        // Deploy to platform manager
        //TODO handle JSON config
        //TODO handle number of instances to config
        //TODO handle ClassPath URLs if necessary
        this.platformManager.deployModuleFromZip(tempFile.getAbsolutePath(), null, 1, deploymentHandler);

        // Return
        return pmd;
    }


    //TODO Blocking on result is done correctly?
    private static class DeploymentHandler implements Handler<AsyncResult<String>>{

        private final ProtocolMetaData pmd;

        DeploymentHandler(final ProtocolMetaData pmd) {
            assert pmd != null : ProtocolMetaData.class.getSimpleName() + " must be specified";
            this.pmd = pmd;
        }

        @Override
        public void handle(final AsyncResult<String> stringAsyncResult) {
            // Block on result and handle failure
            if(stringAsyncResult.failed()){
                pmd.addContext(stringAsyncResult.cause());
                return;
            }

            // Success; get the deployment ID
            final String deploymentId = stringAsyncResult.result();
            // Add Deployment Context
            final DeploymentContext deploymentContext = new DeploymentContext(DEPLOYMENT_CONTEXT_NAME, deploymentId);
            pmd.addContext(deploymentContext);
        }
    }

    /**
     * {@inheritDoc}
     * @param archive
     * @throws DeploymentException
     */
    @Override
    public void undeploy(Archive<?> archive) throws DeploymentException {

    }
}
