package com.plainprog.grandslam_ai.entity.img_management;

import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.helper.sorting.Sortable;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "gallery_entry")
public class GalleryEntry implements Serializable, Sortable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "image_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "gallery_entry_image_fk")
    )
    private Image image;

    @ManyToOne
    @JoinColumn(
            name = "group_id",
            foreignKey = @ForeignKey(name = "gallery_entry_group_fk")
    )
    private GalleryGroup group;

    @Column(name = "hidden_at")
    private Instant hiddenAt;

    @Column
    private Long position;

    @Column(nullable = false)
    private boolean shortlisted = false;

    @Column(name = "created_at")
    private Instant createdAt;

    // Constructors
    public GalleryEntry() {}

    public GalleryEntry(Image image, GalleryGroup group, Instant hiddenAt, Long position, boolean shortlisted) {
        this.image = image;
        this.group = group;
        this.hiddenAt = hiddenAt;
        this.position = position;
        this.shortlisted = shortlisted;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public GalleryGroup getGroup() {
        return group;
    }

    public void setGroup(GalleryGroup group) {
        this.group = group;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getHiddenAt() {
        return hiddenAt;
    }

    public void setHiddenAt(Instant hiddenAt) {
        this.hiddenAt = hiddenAt;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public boolean isShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(boolean shortlisted) {
        this.shortlisted = shortlisted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
