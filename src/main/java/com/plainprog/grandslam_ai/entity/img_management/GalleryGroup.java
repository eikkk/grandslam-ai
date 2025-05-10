package com.plainprog.grandslam_ai.entity.img_management;

import com.plainprog.grandslam_ai.helper.sorting.Sortable;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gallery_group")
public class GalleryGroup implements Serializable, Sortable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(nullable = false)
    private Long position;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "group")
    private List<GalleryEntry> entries = new ArrayList<>();

    // Constructors
    public GalleryGroup() {}

    public GalleryGroup(String name, Long position) {
        this.name = name;
        this.position = position;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<GalleryEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<GalleryEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(GalleryEntry entry) {
        entries.add(entry);
        entry.setGroup(this);
    }

    public void removeEntry(GalleryEntry entry) {
        entries.remove(entry);
        entry.setGroup(null);
    }

    // Lifecycle hook to set created_at automatically
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
