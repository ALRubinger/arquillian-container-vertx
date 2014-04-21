package org.jboss.arquillian.vertx.common;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;

/**
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
public abstract class CommonDeployableContainer<T extends CommonContainerConfiguration> implements DeployableContainer<T> {

    private T containerConfig;

    public void setup(T config) {
        assert config !=null : "Config must not be null";
        containerConfig = config;
    }

    protected T getConfiguration(){
        return containerConfig;
    }

    @Override
    public ProtocolDescription getDefaultProtocol() {
        return null;
    }

    @Override
    public void deploy(final Descriptor descriptor) throws DeploymentException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void undeploy(final Descriptor descriptor) throws DeploymentException {
        throw new UnsupportedOperationException("Not supported");
    }
}
