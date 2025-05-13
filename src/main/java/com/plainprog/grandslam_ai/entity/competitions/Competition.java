package com.plainprog.grandslam_ai.entity.competitions;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "competition")
public class Competition implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private CompetitionTheme theme;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CompetitionStatus status = CompetitionStatus.OPEN;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "participants_count")
    private Integer participantsCount;

    // Enum for competition status
    public enum CompetitionStatus {
        OPEN, STARTED, FINISHED
    }

    // Constructors
    public Competition() {}

    public Competition(CompetitionTheme theme) {
        this.theme = theme;
        this.createdAt = Instant.now();
        this.status = CompetitionStatus.OPEN;
        this.participantsCount = 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompetitionTheme getTheme() {
        return theme;
    }

    public void setTheme(CompetitionTheme theme) {
        this.theme = theme;
    }

    public CompetitionStatus getStatus() {
        return status;
    }

    public void setStatus(CompetitionStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getParticipantsCount() {
        return participantsCount;
    }

    public void setParticipantsCount(Integer participantsCount) {
        this.participantsCount = participantsCount;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}