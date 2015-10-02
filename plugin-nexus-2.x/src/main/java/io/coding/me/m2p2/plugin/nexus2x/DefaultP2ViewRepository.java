package io.coding.me.m2p2.plugin.nexus2x;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.inject.Typed;
import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.sisu.Description;
import org.sonatype.nexus.ApplicationStatusSource;
import org.sonatype.nexus.configuration.model.CRepository;
import org.sonatype.nexus.configuration.model.CRepositoryExternalConfigurationHolderFactory;
import org.sonatype.nexus.plugins.RepositoryType;
import org.sonatype.nexus.proxy.IllegalOperationException;
import org.sonatype.nexus.proxy.ItemNotFoundException;
import org.sonatype.nexus.proxy.LocalStorageException;
import org.sonatype.nexus.proxy.ResourceStoreRequest;
import org.sonatype.nexus.proxy.events.RepositoryRegistryEvent;
import org.sonatype.nexus.proxy.events.RepositoryRegistryEventAdd;
import org.sonatype.nexus.proxy.events.RepositoryRegistryEventRemove;
import org.sonatype.nexus.proxy.item.DefaultStorageCollectionItem;
import org.sonatype.nexus.proxy.item.DefaultStorageFileItem;
import org.sonatype.nexus.proxy.item.FileContentLocator;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;
import org.sonatype.nexus.proxy.item.StorageCollectionItem;
import org.sonatype.nexus.proxy.item.StorageFileItem;
import org.sonatype.nexus.proxy.item.StorageItem;
import org.sonatype.nexus.proxy.item.StorageLinkItem;
import org.sonatype.nexus.proxy.registry.ContentClass;
import org.sonatype.nexus.proxy.registry.RepositoryRegistry;
import org.sonatype.nexus.proxy.repository.AbstractRepositoryConfigurator;
import org.sonatype.nexus.proxy.repository.AbstractShadowRepository;
import org.sonatype.nexus.proxy.repository.DefaultRepositoryKind;
import org.sonatype.nexus.proxy.repository.IncompatibleMasterRepositoryException;
import org.sonatype.nexus.proxy.repository.Repository;
import org.sonatype.nexus.proxy.repository.RepositoryKind;
import org.sonatype.nexus.proxy.repository.ShadowRepository;
import org.sonatype.nexus.proxy.storage.UnsupportedStorageOperationException;

import com.google.common.eventbus.Subscribe;

/**
 * Virtual repository that allows access to P2 content.xml and artifacts.xml
 * and forwards other requests to the underlying M2 repository.
 */
@SuppressWarnings ("deprecation")
@Named (DefaultP2ViewRepository.REPOSITORY_HINT)
@Description ("P2 View Repository")
@Typed (P2ViewRepository.class)
@RepositoryType (pathPrefix = "p2")
public class DefaultP2ViewRepository extends AbstractShadowRepository implements P2ViewRepository {

    static final String REPOSITORY_HINT = "io.coding.me.m2p2.plugin.nexus2x.DefaultP2ViewRepository";

    private final P2ViewRepositoryConfigurator configurator;

    private final ContentClass contentClass;

    private final ContentClass masterContentClass;

    private RepositoryKind repositoryKind;

    /**
     * Default constructor
     *
     * @param repositoryRegistry Automatically injected
     * @param configurator Automatically injected
     * @param contentClass Automatically injected
     * @param masterContentClass Automatically injected
     * @param statusSource Automatically injected
     */
    @Inject
    public DefaultP2ViewRepository(final RepositoryRegistry repositoryRegistry,
        final P2ViewRepositoryConfigurator configurator, @Named ("maven2") final ContentClass contentClass,
        @Named ("maven2") final ContentClass masterContentClass, final ApplicationStatusSource statusSource) {

        this.configurator = configurator;
        this.contentClass = contentClass;
        this.masterContentClass = masterContentClass;

        // TODO: Initialize P2 View System
    }

    @Override
    public void setMasterRepository(Repository masterRepository) throws IncompatibleMasterRepositoryException {

        super.setMasterRepository(masterRepository);

        // TODO: Initialize convenience access to P2 view
    }

    /**
     *
     * @return Returns the absolute path of the underlying repository
     */
    File getAbsoluteRepositoryPath() {

        final String pathPrefix = getMasterRepository().getPathPrefix();
        final File repositoryPath = new File(getApplicationConfiguration().getWorkingDirectory("storage"), pathPrefix);

        return repositoryPath;
    }

    /**
     * Automatically called on Nexus scheduled tasks and repository startup
     */
    @Override
    public void synchronizeWithMaster() {

        log.info("Synchronizing P2 View with master repository {}", getMasterRepository().getId());

        // TODO: Synchronize with  P2 view
    }

    @Override
    protected AbstractRepositoryConfigurator getConfigurator() {
        return configurator;
    }

    @Override
    public ContentClass getRepositoryContentClass() {
        return contentClass;
    }

    @Override
    public ContentClass getMasterRepositoryContentClass() {
        return masterContentClass;
    }

    @Override
    public RepositoryKind getRepositoryKind() {
        if (repositoryKind == null) {
            repositoryKind =
                new DefaultRepositoryKind(P2ViewRepository.class,
                    Arrays.asList(new Class<?>[] {ShadowRepository.class }));
        }

        return repositoryKind;
    }

    @Override
    protected CRepositoryExternalConfigurationHolderFactory<?> getExternalConfigurationHolderFactory() {
        return new CRepositoryExternalConfigurationHolderFactory<P2ViewRepositoryConfiguration>() {

            @Override
            public P2ViewRepositoryConfiguration createExternalConfigurationHolder(final CRepository config) {
                return new P2ViewRepositoryConfiguration((Xpp3Dom)config.getExternalConfiguration());
            }
        };
    }

    /**
     * Callback for repository registry events
     *
     * @param evt The event
     */
    @Subscribe
    public void onRepositoryRegistryEvent(RepositoryRegistryEvent evt) {

        if (evt instanceof RepositoryRegistryEventRemove) {

            RepositoryRegistryEventRemove rem = (RepositoryRegistryEventRemove)evt;

            final String repositoryId = rem.getRepository().getId();

            if (repositoryId.equals(getMasterRepository().getId())) {

                log.info("Deleting virtual repository {}", repositoryId);
                // TODO
            }

        } else if (evt instanceof RepositoryRegistryEventAdd) {

            RepositoryRegistryEventAdd add = (RepositoryRegistryEventAdd)evt;

            final String repositoryId = add.getRepository().getId();

            if (repositoryId.equals(getMasterRepository().getId())) {

                log.info("Adding virtual repository {}", repositoryId);
                // TODO
            }
        }
    }

    /**
     * Returns a virtual item
     *
     * @param request The request
     * @param virtualFile The virtual file
     * @param mimeType The mime type of the virtual file
     * @param name The name to display
     * @return A representation that can be used by Nexus
     * @throws IllegalOperationException In case of a Nexus error
     * @throws ItemNotFoundException In case of a Nexus error
     * @throws LocalStorageException In case of a Nexus error
     */
    private StorageItem retrieveVirtualItem(final ResourceStoreRequest request, File virtualFile,
        String mimeType, String name)
        throws IllegalOperationException, ItemNotFoundException, LocalStorageException {

        FileContentLocator fc = new FileContentLocator(virtualFile, mimeType, false);

        DefaultStorageFileItem storageItem =
            new DefaultStorageFileItem(getMasterRepository(), request, true, false, fc);

        storageItem.setPath("/" + name);

        return storageItem;
    }

    @Override
    protected StorageItem doRetrieveItem(final ResourceStoreRequest request) throws IllegalOperationException,
        ItemNotFoundException, LocalStorageException {

    	System.out.println("REQUEST PATH "+request.getRequestPath());
    	
    	try {
    
    		StorageItem masterItem = getMasterRepository().getLocalStorage().retrieveItem(getMasterRepository(), request);
    		
    		if(masterItem instanceof StorageCollectionItem) {
    			
    			Collection<StorageItem> list = ((StorageCollectionItem) masterItem).list();
    		
    			//RepositoryItemUid 
    			
    			System.out.println("YEEHAW");
    		}
    		
    		
	    	if("/".equals(request.getRequestPath())) {
	
	    		DefaultStorageCollectionItem dtci= new DefaultStorageCollectionItem(getMasterRepository(), request, true, false);
	    		
	    		//dtci.setPath(path);
	    		return dtci;
	    		
	    		/*StorageItem masterItem = getMasterRepository().getLocalStorage().retrieveItem(getMasterRepository(), request);
	    		
	    		if(masterItem instanceof StorageCollectionItem) {
	    		
	    			VirtualStorageCollectionItemFacade virtualItem= new VirtualStorageCollectionItemFacade((StorageCollectionItem)masterItem);
	        		
	    			return virtualItem;
	    		}*/
	    		 
	    		//throw new LocalStorageException("Root path is not a collecction"); 
	    		
	    	} else {
	
	            return masterItem;
	    	}
	    	
    	} catch (Exception /*AccessDeniedException|StorageException|NoSuchResourceStoreException*/ ex) {
    		
    		throw new LocalStorageException(ex);
    	}
    }

    @Override
    protected StorageLinkItem createLink(final StorageItem item) throws UnsupportedStorageOperationException,
        IllegalOperationException, LocalStorageException {

        if (item instanceof StorageFileItem
            && ((StorageFileItem)item).getContentLocator() instanceof FileContentLocator) {

            FileContentLocator fcl = (FileContentLocator)((StorageFileItem)item).getContentLocator();

            // TODO
        }

        return null;
    }

    @Override
    protected void deleteLink(final StorageItem item) throws UnsupportedStorageOperationException,
        IllegalOperationException, ItemNotFoundException, LocalStorageException {

        if (item instanceof StorageFileItem
            && ((StorageFileItem)item).getContentLocator() instanceof FileContentLocator) {

            FileContentLocator fcl = (FileContentLocator)((StorageFileItem)item).getContentLocator();

            // TODO
        }

    }
}
