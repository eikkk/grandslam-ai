package com.plainprog.grandslam_ai.entity.competitions;

import com.plainprog.grandslam_ai.entity.BaseEntity;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "competition")
public class Competition extends BaseEntity<Long> {

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

    @Column(name = "vote_target")
    private Integer voteTarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_image_id")
    private Image winnerImage;

    //OPEN - competition is open for submissions
    //STARTED - competition has started, but no matches yet
    //RUNNING - competition is running, matches are ongoing
    //FINISHED - competition is finished, no more matches
    public enum CompetitionStatus {
        OPEN, STARTED, RUNNING, FINISHED
    }

    // Constructors
    public Competition() {}

    public Competition(CompetitionTheme theme, Integer participantsCount, Integer voteTarget) {
        this.theme = theme;
        this.createdAt = Instant.now();
        this.status = CompetitionStatus.OPEN;
        this.participantsCount = participantsCount;
        this.voteTarget = voteTarget;
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

    public Integer getVoteTarget() {
        return voteTarget;
    }

    public void setVoteTarget(Integer voteTarget) {
        this.voteTarget = voteTarget;
    }

    public Image getWinnerImage() {
        return winnerImage;
    }

    public void setWinnerImage(Image winnerImage) {
        this.winnerImage = winnerImage;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}