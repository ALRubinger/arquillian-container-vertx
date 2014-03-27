package org.jboss.arquillian.vertx.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes an injection target for Vert.x resources to be supplied by
 * the container in an Arquillian test instance.
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface VertxResource {
}
