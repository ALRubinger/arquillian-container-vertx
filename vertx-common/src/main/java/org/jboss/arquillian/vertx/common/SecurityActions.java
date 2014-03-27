package org.jboss.arquillian.vertx.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Secured actions not to leak out of this package
 * <p/>
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
final class SecurityActions {

    private SecurityActions() {
        throw new UnsupportedOperationException("No instances permitted");
    }

    /**
     * Sets the accessibility of the specified field if not already set
     *
     * @param field
     */
    static void setFieldAccessibility(final Field field, final boolean accessible) {
        final boolean setAccessibility = accessible != field.isAccessible();
        if(setAccessibility){
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    field.setAccessible(accessible);
                    return null;
                }
            });
        }
    }

    /**
     * Obtains all fields on the supplied class source (and its superclasses excluding Object.class) annotated
     * with the specified annotation class
     *
     * @param source
     * @param annotationClass
     * @return A List of fields annotated with the specified annotation class, never null but possibly empty
     */
    static List<Field> getFieldsWithAnnotation(final Class<?> source, final Class<? extends Annotation> annotationClass) {
        assert source != null : "Source class must be specified";
        assert annotationClass != null : "Annotation class must be specified";
        final List<Field> declaredAccessableFields = AccessController.doPrivileged(new PrivilegedAction<List<Field>>() {
            @Override
            public List<Field> run() {
                final List<Field> foundFields = new ArrayList<Field>();
                Class<?> nextSource = source;
                while (nextSource != Object.class) {
                    for (final Field field : nextSource.getDeclaredFields()) {
                        if (field.isAnnotationPresent(annotationClass)) {
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            foundFields.add(field);
                        }
                    }
                    nextSource = nextSource.getSuperclass();
                }
                return foundFields;
            }
        });
        return declaredAccessableFields;
    }
}
