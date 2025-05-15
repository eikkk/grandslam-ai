package com.plainprog.grandslam_ai.entity.img_gen;


import com.plainprog.grandslam_ai.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "img_gen_provider")
public class ImgGenProvider extends BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "creator")
    private String creator;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "cost")
    private Integer cost;

    public ImgGenProvider() {
    }

    public ImgGenProvider(Integer id) {
        this.id = id;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
