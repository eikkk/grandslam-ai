package com.plainprog.grandslam_ai.entity.competitions;

import com.plainprog.grandslam_ai.entity.BaseEntity;
import com.plainprog.grandslam_ai.entity.img_gen.ImgGenModule;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "competition_theme")
public class CompetitionTheme extends BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_group_id", nullable = false)
    private CompetitionThemeGroup themeGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private ImgGenModule module;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "disabled", nullable = false)
    private boolean disabled = false;

    // Constructors
    public CompetitionTheme() {}

    public CompetitionTheme(CompetitionThemeGroup themeGroup, String name, String description) {
        this.themeGroup = themeGroup;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CompetitionThemeGroup getThemeGroup() {
        return themeGroup;
    }

    public void setThemeGroup(CompetitionThemeGroup themeGroup) {
        this.themeGroup = themeGroup;
    }

    public ImgGenModule getModule() {
        return module;
    }

    public void setModule(ImgGenModule module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}