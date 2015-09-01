package io.coding.me.m2p2.plugin.nexus2x;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.sisu.EagerSingleton;
import org.sonatype.nexus.plugin.PluginIdentity;

/**
 * The identity of the plugin
 *
 */
@Named
@EagerSingleton
public class P2ViewPluginIdentity extends PluginIdentity {

    /**
     * The default constructor
     *
     * @throws Exception In case of an error
     */
    @Inject
    public P2ViewPluginIdentity() throws Exception {

        super("io.coding.me.m2p2.plugin.nexus2x", "p2-view-plugin");
    }
}
