package com.plainprog.grandslam_ai.entity.img_management;

import com.plainprog.grandslam_ai.entity.img_gen.Image;
import com.plainprog.grandslam_ai.helper.sorting.Sortable;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "spotlight_entry")
public class SpotlightEntry implements Serializable, Sortable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Column(nullable = false)
    private Long position;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Constructors
    public SpotlightEntry() {}

    public SpotlightEntry(Image image, Long position, Instant createdAt) {
        this.image = image;
        this.position = position;
        this.createdAt = createdAt;
    }

    // Getters and Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

