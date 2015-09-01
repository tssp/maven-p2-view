package io.coding.me.m2p2.plugin.nexus2x;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.proxy.repository.AbstractShadowRepositoryConfigurator;

/**
 * The repository configuration
 */
@Named
@Singleton
public class P2ViewRepositoryConfigurator extends AbstractShadowRepositoryConfigurator {

    /**
     * Default constructor
     */
    public P2ViewRepositoryConfigurator() {

    }
}
