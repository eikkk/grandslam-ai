package com.plainprog.grandslam_ai.object.response_models.image_management.gallery;


import java.util.List;

public class GalleryResponse {
    private List<GalleryGroupUI> groups;
    private List<GalleryEntryUI> ungroupedItems;
    private List<GalleryEntryUI> hiddenItems;

    public GalleryResponse(List<GalleryGroupUI> groups, List<GalleryEntryUI> ungroupedItems, List<GalleryEntryUI> hiddenItems) {
        this.groups = groups;
        this.ungroupedItems = ungroupedItems;
        this.hiddenItems = hiddenItems;
    }

    public GalleryResponse() {
    }

    public List<GalleryGroupUI> getGroups() {
        return groups;
    }

    public void setGroups(List<GalleryGroupUI> groups) {
        this.groups = groups;
    }

    public List<GalleryEntryUI> getUngroupedItems() {
        return ungroupedItems;
    }

    public void setUngroupedItems(List<GalleryEntryUI> ungroupedItems) {
        this.ungroupedItems = ungroupedItems;
    }

    public List<GalleryEntryUI> getHiddenItems() {
        return hiddenItems;
    }

    public void setHiddenItems(List<GalleryEntryUI> hiddenItems) {
        this.hiddenItems = hiddenItems;
    }
}