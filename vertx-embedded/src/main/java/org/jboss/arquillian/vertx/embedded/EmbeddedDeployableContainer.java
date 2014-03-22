package org.jboss.arquillian.vertx.embedded;

import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.vertx.common.CommonDeployableContainer;
import org.vertx.java.platform.PlatformLocator;
import org.vertx.java.platform.PlatformManager;

/**
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
public class EmbeddedDeployableContainer extends CommonDeployableContainer<EmbeddedContainerConfiguration> {

    private PlatformManager platformManager;

    @Override
    public void setup(final EmbeddedContainerConfiguration config) {
        super.setup(config);

        final PlatformManager pm = PlatformLocator.factory.createPlatformManager();
        this.platformManager = pm;

    }

    @Override
    public void start() throws LifecycleException {

        platformManager.deployModuleFromZip();

    }

    @Override
    public void stop() throws LifecycleException {

    }
}
