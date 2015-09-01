package io.coding.me.m2p2.plugin.nexus2x;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.nexus.templates.TemplateProvider;
import org.sonatype.nexus.templates.TemplateSet;
import org.sonatype.nexus.templates.repository.AbstractRepositoryTemplateProvider;

/**
 *
 * Alike org.sonatype.nexus.templates.repository.DefaultRepositoryTemplateProvider
 *
 */
@Named (P2ViewRepositoryTemplateProvider.PROVIDER_ID)
@Singleton
public class P2ViewRepositoryTemplateProvider extends AbstractRepositoryTemplateProvider implements TemplateProvider {

    static final String PROVIDER_ID = "p2Repo-templates";

    @Override
    public TemplateSet getTemplates() {
        final TemplateSet templates = new TemplateSet(null);

        final String templateId = PROVIDER_ID;
        templates.add(new P2ViewRepositoryTemplate(this, templateId, "Maven/P2 View"));

        return templates;
    }
}
