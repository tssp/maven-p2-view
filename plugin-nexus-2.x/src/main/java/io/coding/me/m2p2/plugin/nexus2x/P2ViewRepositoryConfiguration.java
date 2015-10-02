package io.coding.me.m2p2.plugin.nexus2x;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.nexus.proxy.repository.AbstractShadowRepositoryConfiguration;

/**
 * The view repository confiugration
 *
 */
public class P2ViewRepositoryConfiguration extends AbstractShadowRepositoryConfiguration {

    /**
     * Default constructor
     *
     * @param configuration The access to the underlying configuration
     */
    public P2ViewRepositoryConfiguration(final Xpp3Dom configuration) {
        super(configuration);
    }

}
