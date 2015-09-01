package io.coding.me.m2p2.plugin.nexus2x;

import org.sonatype.nexus.plugins.*;
import org.sonatype.nexus.proxy.repository.ShadowRepository;

/**
 * The view repository.
 *
 */
@SuppressWarnings ("deprecation")
@RepositoryType (pathPrefix = "p2")
public interface P2ViewRepository extends ShadowRepository {

}
