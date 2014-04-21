package org.jboss.arquillian.vertx.common;

import org.jboss.arquillian.container.spi.client.container.DeploymentExceptionTransformer;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.TestEnricher;

/**
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
public class CommonContainerExtension implements LoadableExtension {

    @Override
    public void register(final ExtensionBuilder extensionBuilder) {
        extensionBuilder.service(DeploymentExceptionTransformer.class, ExceptionTransformer.class);
        extensionBuilder.service(TestEnricher.class, VertxResourceTestEnricher.class);
    }
}
