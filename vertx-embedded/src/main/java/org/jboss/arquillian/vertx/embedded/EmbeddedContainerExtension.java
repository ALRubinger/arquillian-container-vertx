package org.jboss.arquillian.vertx.embedded;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.vertx.common.CommonContainerExtension;

/**
 * {@link org.jboss.arquillian.core.spi.LoadableExtension} implementation for
 * the Vert.x Embedded Container Adaptor
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
public class EmbeddedContainerExtension extends CommonContainerExtension {

    /**
     * {@inheritDoc}
     * @param extensionBuilder
     */
    @Override
    public void register(final ExtensionBuilder extensionBuilder) {
        super.register(extensionBuilder);
        extensionBuilder.service(DeployableContainer.class, EmbeddedDeployableContainer.class);
    }
}
