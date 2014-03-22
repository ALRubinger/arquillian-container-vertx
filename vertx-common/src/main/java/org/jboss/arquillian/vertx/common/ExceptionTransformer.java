package org.jboss.arquillian.vertx.common;

import org.jboss.arquillian.container.spi.client.container.DeploymentExceptionTransformer;

/**
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
public class ExceptionTransformer implements DeploymentExceptionTransformer {
    @Override
    public Throwable transform(final Throwable throwable) {
        // Pass-through
        return throwable;
    }
}
