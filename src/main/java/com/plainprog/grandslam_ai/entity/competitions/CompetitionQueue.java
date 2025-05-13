package com.plainprog.grandslam_ai.entity.competitions;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "competition_queue")
public class CompetitionQueue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id", nullable = false)
    private CompetitionTheme theme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CompetitionQueueStatus status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "processed_at")
    private Instant processedAt;

    // Enum for queue status
    public enum CompetitionQueueStatus {
        NEW, PROCESSED, FAILED
    }

    // Constructors
    public CompetitionQueue() {}

    public CompetitionQueue(CompetitionTheme theme, CompetitionQueueStatus status) {
        this.theme = theme;
        this.status = status;
        this.createdAt = Instant.now();
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

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public CompetitionQueueStatus getStatus() {
        return status;
    }

    public void setStatus(CompetitionQueueStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}