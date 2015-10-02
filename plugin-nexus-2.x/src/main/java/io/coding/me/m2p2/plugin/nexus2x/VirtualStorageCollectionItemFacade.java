package io.coding.me.m2p2.plugin.nexus2x;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sonatype.nexus.proxy.*;
import org.sonatype.nexus.proxy.attributes.Attributes;
import org.sonatype.nexus.proxy.item.DefaultStorageCollectionItem;
import org.sonatype.nexus.proxy.item.RepositoryItemUid;
import org.sonatype.nexus.proxy.item.StorageCollectionItem;
import org.sonatype.nexus.proxy.item.StorageItem;

/**
 * Adds virtual files to a collection of storage items.
 *
 */
@SuppressWarnings ("deprecation")
class VirtualStorageCollectionItemFacade implements StorageCollectionItem {

    final StorageCollectionItem masterCollectionItem;
    final List<StorageItem> itemList = new ArrayList<>();

    /**
     * Creates a new instance
     *
     * @param masterCollectionItem The real collection
     * @throws AccessDeniedException In case of an Nexus error
     * @throws StorageException In case of an Nexus error
     * @throws NoSuchResourceStoreException In case of an Nexus error
     * @throws IllegalOperationException In case of an Nexus error
     * @throws ItemNotFoundException In case of an Nexus error
     */
    public VirtualStorageCollectionItemFacade(StorageCollectionItem masterCollectionItem)
        throws AccessDeniedException, StorageException, NoSuchResourceStoreException, IllegalOperationException,
        ItemNotFoundException {

        this.masterCollectionItem = masterCollectionItem;
        itemList.addAll(masterCollectionItem.list());
    }

    /**
     * Adds an item to the internal list
     *
     * @param item The item
     */
    public void addVirtualStorageItem(StorageItem item) {

        itemList.add(item);
    }

    @Override
    public ResourceStoreRequest getResourceStoreRequest() {

        return masterCollectionItem.getResourceStoreRequest();
    }

    @Override
    public RepositoryItemUid getRepositoryItemUid() {

        return masterCollectionItem.getRepositoryItemUid();
    }

    @Override
    public void setRepositoryItemUid(RepositoryItemUid repositoryItemUid) {

        masterCollectionItem.setRepositoryItemUid(repositoryItemUid);
    }

    @Override
    public String getRepositoryId() {

        return masterCollectionItem.getRepositoryId();
    }

    @Override
    public long getCreated() {

        return masterCollectionItem.getCreated();
    }

    @Override
    public long getModified() {

        return masterCollectionItem.getModified();
    }

    @Override
    public long getStoredLocally() {

        return masterCollectionItem.getStoredLocally();
    }

    @Override
    public void setStoredLocally(long ts) {

        masterCollectionItem.setStoredLocally(ts);
    }

    @Override
    public long getRemoteChecked() {

        return masterCollectionItem.getRemoteChecked();
    }

    @Override
    public void setRemoteChecked(long ts) {

        masterCollectionItem.setRemoteChecked(ts);
    }

    @Override
    public long getLastRequested() {

        return masterCollectionItem.getLastRequested();
    }

    @Override
    public void setLastRequested(long ts) {

        masterCollectionItem.setLastRequested(ts);
    }

    @Override
    public boolean isVirtual() {

        return masterCollectionItem.isVirtual();
    }

    @Override
    public boolean isReadable() {

        return masterCollectionItem.isReadable();
    }

    @Override
    public boolean isWritable() {

        return masterCollectionItem.isWritable();
    }

    @Override
    public boolean isExpired() {

        return masterCollectionItem.isExpired();
    }

    @Override
    public void setExpired(boolean expired) {

        masterCollectionItem.setExpired(expired);
    }

    @Override
    public String getPath() {

        return masterCollectionItem.getPath();
    }

    @Override
    public String getName() {

        return masterCollectionItem.getName();
    }

    @Override
    public String getParentPath() {

        return masterCollectionItem.getParentPath();
    }

    @Override
    public int getPathDepth() {

        return masterCollectionItem.getPathDepth();
    }

    @Override
    public String getRemoteUrl() {

        return masterCollectionItem.getRemoteUrl();
    }

    @Override
    public Attributes getRepositoryItemAttributes() {

        return masterCollectionItem.getRepositoryItemAttributes();
    }

    @Override
    public RequestContext getItemContext() {

        return masterCollectionItem.getItemContext();
    }

    @Override
    public Collection<StorageItem> list() throws AccessDeniedException, NoSuchResourceStoreException,
        IllegalOperationException, ItemNotFoundException, StorageException {

        return itemList;
    }
}
