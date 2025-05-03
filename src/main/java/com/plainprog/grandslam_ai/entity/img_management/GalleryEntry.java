package com.plainprog.grandslam_ai.entity.img_management;

import com.plainprog.grandslam_ai.entity.img_gen.Image;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "gallery_entry")
public class GalleryEntry implements Serializable {

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

    @Column(nullable = false)
    private boolean hidden = false;

    @Column
    private Integer position;

    @Column(nullable = false)
    private boolean shortlisted = false;

    // Constructors
    public GalleryEntry() {}

    public GalleryEntry(Image image, GalleryGroup group, boolean hidden, Integer position, boolean shortlisted) {
        this.image = image;
        this.group = group;
        this.hidden = hidden;
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public boolean isShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(boolean shortlisted) {
        this.shortlisted = shortlisted;
    }
}
