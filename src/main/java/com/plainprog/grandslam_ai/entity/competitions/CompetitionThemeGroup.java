package com.plainprog.grandslam_ai.entity.competitions;

import com.plainprog.grandslam_ai.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "competition_theme_group")
public class CompetitionThemeGroup extends BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled = false;

    // Constructors
    public CompetitionThemeGroup() {}

    public CompetitionThemeGroup(String name) {
        this.name = name;
    }

    // Getters and Setters
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

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}