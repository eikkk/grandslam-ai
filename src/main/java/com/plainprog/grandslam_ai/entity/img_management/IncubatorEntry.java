package com.plainprog.grandslam_ai.entity.img_management;


import com.plainprog.grandslam_ai.entity.img_gen.Image;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "incubator_entry")
public class IncubatorEntry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "image_id", nullable = false, unique = true,
            foreignKey = @ForeignKey(name = "incubator_image_fk"))
    private Image image;

    @Column(nullable = false)
    private boolean shortlisted = false;

    // Constructors
    public IncubatorEntry() {
    }

    public IncubatorEntry(Image image, boolean shortlisted) {
        this.image = image;
        this.shortlisted = shortlisted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isShortlisted() {
        return shortlisted;
    }

    public void setShortlisted(boolean shortlisted) {
        this.shortlisted = shortlisted;
    }
}
