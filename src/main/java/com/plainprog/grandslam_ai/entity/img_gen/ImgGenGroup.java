package com.plainprog.grandslam_ai.entity.img_gen;


import com.plainprog.grandslam_ai.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "img_gen_group")
public class ImgGenGroup extends BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "active")
    private Boolean active;

    public ImgGenGroup() {
    }

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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
