package io.coding.me.m2p2.plugin.nexus2x;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.sonatype.nexus.configuration.model.CRepository;
import org.sonatype.nexus.configuration.model.CRepositoryCoreConfiguration;
import org.sonatype.nexus.configuration.model.CRepositoryExternalConfigurationHolderFactory;
import org.sonatype.nexus.configuration.model.DefaultCRepository;
import org.sonatype.nexus.proxy.maven.maven2.Maven2ContentClass;
import org.sonatype.nexus.proxy.repository.RepositoryWritePolicy;
import org.sonatype.nexus.templates.repository.AbstractRepositoryTemplate;
import org.sonatype.nexus.templates.repository.AbstractRepositoryTemplateProvider;

/**
 * Alike org.sonatype.nexus.templates.repository.maven.Maven1Maven2ShadowRepositoryTemplate
 *
 */
public class P2ViewRepositoryTemplate extends AbstractRepositoryTemplate {

    /**
     * Default constructor
     * @param provider The provider
     * @param id The id
     * @param description The description
     */
    public P2ViewRepositoryTemplate(final AbstractRepositoryTemplateProvider provider, final String id,
        final String description) {
        super(provider, id, description, new Maven2ContentClass(), P2ViewRepository.class);
    }

    @Override
    protected CRepositoryCoreConfiguration initCoreConfiguration() {
        final CRepository repo = new DefaultCRepository();

        repo.setId("");
        repo.setName("");
        repo.setProviderRole(P2ViewRepository.class.getName());
        repo.setProviderHint(DefaultP2ViewRepository.REPOSITORY_HINT);

        final Xpp3Dom ex = new Xpp3Dom(DefaultCRepository.EXTERNAL_CONFIGURATION_NODE_NAME);
        repo.setExternalConfiguration(ex);

        final P2ViewRepositoryConfiguration exConf = new P2ViewRepositoryConfiguration(ex);
        repo.externalConfigurationImple = exConf;

        repo.setWritePolicy(RepositoryWritePolicy.READ_ONLY.name());

        final CRepositoryCoreConfiguration result =
            new CRepositoryCoreConfiguration(getTemplateProvider().getApplicationConfiguration(), repo,
                new CRepositoryExternalConfigurationHolderFactory<P2ViewRepositoryConfiguration>() {

                    @Override
                    public P2ViewRepositoryConfiguration createExternalConfigurationHolder(final CRepository config) {
                        return new P2ViewRepositoryConfiguration((Xpp3Dom)config.getExternalConfiguration());
                    }
                });

        return result;
    }

}
