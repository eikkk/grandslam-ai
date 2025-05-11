package com.plainprog.grandslam_ai.object.response_models.image_management.gallery;

import java.util.List;

public class GalleryGroupUI {
    private Integer id;
    private String name;
    private long position;
    private List<GalleryEntryUI> entries;

    // Constructors
    public GalleryGroupUI() {}

    public GalleryGroupUI(Integer id, String name, long position, List<GalleryEntryUI> entries) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.entries = entries;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public List<GalleryEntryUI> getEntries() {
        return entries;
    }

    public void setEntries(List<GalleryEntryUI> entries) {
        this.entries = entries;
    }
}