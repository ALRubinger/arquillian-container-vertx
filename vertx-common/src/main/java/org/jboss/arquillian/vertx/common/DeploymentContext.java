package org.jboss.arquillian.vertx.common;

import org.jboss.arquillian.container.spi.client.protocol.metadata.NamedContext;

/**
 * author <a href="mailto:alr@alrubinger.com">Andrew Lee Rubinger</a>
 */
public class DeploymentContext extends NamedContext {

    private final String deploymentId;

    /**
     * Creates a new context with the specified (required) name and deploymentId
     * @param name
     * @param deploymentId
     */
    public DeploymentContext(final String name, final String deploymentId) {
        super(name);
        assert name != null && !name.isEmpty() : "Name must be supplied";
        assert deploymentId != null && !deploymentId.isEmpty() : "deploymentId must be supplied";
        this.deploymentId = deploymentId;
    }
}
