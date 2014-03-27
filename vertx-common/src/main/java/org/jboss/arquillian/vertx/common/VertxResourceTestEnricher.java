package org.jboss.arquillian.vertx.common;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.vertx.api.VertxResource;
import org.vertx.java.core.Vertx;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
public class VertxResourceTestEnricher implements TestEnricher {

    @Inject
    private Instance<Vertx> vertx;

    @Override
    public void enrich(final Object testInstance) {
        // Find all fields annotated with @VertxResource
        final List<Field> injectionPoints = SecurityActions.getFieldsWithAnnotation(testInstance.getClass(), VertxResource.class);

        // For each, inject
        for (final Field injectionPoint : injectionPoints) {


            // Get target type

            


            SecurityActions.setFieldAccessibility(injectionPoint, true);
            try {
                injectionPoint.set(testInstance, vertx.get());
            } catch (final IllegalAccessException iae) {
                throw new RuntimeException("Could not inject " + Vertx.class.getSimpleName() + "into test instance", iae);
            }
        }
    }


    @Override
    public Object[] resolve(final Method method) {
        return new Object[0];
    }
}
