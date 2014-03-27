package org.jboss.arquillian.vertx.embedded.test;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.vertx.api.VertxResource;
import org.junit.runner.RunWith;
import org.vertx.java.core.Vertx;

/**
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
@RunWith(Arquillian.class)
public class VertxEmbeddedTestCase {

    @VertxResource
    private Vertx vertx;
}
