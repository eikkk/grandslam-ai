package com.plainprog.grandslam_ai.entity.competitions;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "competition_theme_group")
public class CompetitionThemeGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 255)
    private String name;

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
}