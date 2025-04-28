package com.plainprog.grandslam_ai.entity.img_gen;


import com.plainprog.grandslam_ai.entity.account.Account;
import jakarta.persistence.*;

@Entity
@Table(name = "img_gen_module")
public class ImgGenModule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ImgGenProvider provider;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private ImgGenGroup group;

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

    public ImgGenProvider getProvider() {
        return provider;
    }

    public void setProvider(ImgGenProvider provider) {
        this.provider = provider;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ImgGenGroup getGroup() {
        return group;
    }

    public void setGroup(ImgGenGroup group) {
        this.group = group;
    }
}
