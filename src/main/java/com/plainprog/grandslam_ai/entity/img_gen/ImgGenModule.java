package com.plainprog.grandslam_ai.entity.img_gen;


import jakarta.persistence.*;

@Entity
@Table(name = "img_gen_module")
public class ImgGenModule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "provider_id")
    private Integer providerId;

    @Column(name = "group_id")
    private Integer groupId;

    @Column(name = "active")
    private Boolean active;

    public ImgGenModule() {
    }

    public ImgGenModule(Integer id) {
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

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
