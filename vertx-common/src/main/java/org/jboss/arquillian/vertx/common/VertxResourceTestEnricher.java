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

    private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];

    /**
     * The Vert.x instance in use
     */
    @Inject
    private Instance<Vertx> vertxInstance;

    /**
     * Performs injection of the Vert.x instance in use into the test instance
     * {@inheritDoc}
     *
     * @param testInstance
     */
    @Override
    public void enrich(final Object testInstance) {
        // Find all fields annotated with @VertxResource
        final List<Field> injectionPoints = SecurityActions.getFieldsWithAnnotation(testInstance.getClass(), VertxResource.class);

        // For each, inject
        for (final Field injectionPoint : injectionPoints) {

            // Get target type
            final Class<?> fieldType = injectionPoint.getType();

            // Inject Vertx
            if (Vertx.class.isAssignableFrom(fieldType)) {
                SecurityActions.setFieldAccessibility(injectionPoint, true);
                try {
                    final Vertx vertxValue = this.vertxInstance.get();
                    assert vertxValue != null : "Vertx value to be injected is null; did you set an instance producer?";
                    injectionPoint.set(testInstance, vertxValue);
                } catch (final IllegalAccessException iae) {
                    throw new RuntimeException("Could not inject " + Vertx.class.getSimpleName() + "into test instance", iae);
                }
            }
        }
    }


    /**
     * NOOP
     * {@inheritDoc}
     *
     * @param method
     * @return
     */
    @Override
    public Object[] resolve(final Method method) {
        return EMPTY_OBJ_ARRAY;
    }
}
