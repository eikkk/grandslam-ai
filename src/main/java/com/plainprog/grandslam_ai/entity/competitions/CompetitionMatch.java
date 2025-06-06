package com.plainprog.grandslam_ai.entity.competitions;

import com.plainprog.grandslam_ai.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "competition_match")
public class CompetitionMatch extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private Competition competition;

    @Column(name = "round", nullable = false)
    private int round;

    @Column(name = "round_inverse", nullable = false)
    private int roundInverse;

    @Column(name = "match_index", nullable = false)
    private int matchIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission1_id")
    private CompetitionSubmission submission1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission2_id")
    private CompetitionSubmission submission2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_submission_id")
    private CompetitionSubmission winnerSubmission;

    @Column(name = "vote_deadline")
    private Instant voteDeadline;

    @Column(name = "vote_target")
    private Integer voteTarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_match_id")
    private CompetitionMatch nextMatch;

    @Column(name = "inserted_at")
    private Instant insertedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    public CompetitionMatch() {
    }

    public CompetitionMatch(Competition competition, int round, int roundInverse, int matchIndex, int voteTarget, CompetitionMatch nextMatch) {
        this.competition = competition;
        this.round = round;
        this.roundInverse = roundInverse;
        this.matchIndex = matchIndex;
        this.voteTarget = voteTarget;
        this.nextMatch = nextMatch;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getRoundInverse() {
        return roundInverse;
    }

    public void setRoundInverse(int roundInverse) {
        this.roundInverse = roundInverse;
    }

    public int getMatchIndex() {
        return matchIndex;
    }

    public void setMatchIndex(int matchIndex) {
        this.matchIndex = matchIndex;
    }

    public CompetitionSubmission getSubmission1() {
        return submission1;
    }

    public void setSubmission1(CompetitionSubmission submission1) {
        this.submission1 = submission1;
    }

    public CompetitionSubmission getSubmission2() {
        return submission2;
    }

    public void setSubmission2(CompetitionSubmission submission2) {
        this.submission2 = submission2;
    }

    public CompetitionSubmission getWinnerSubmission() {
        return winnerSubmission;
    }

    public void setWinnerSubmission(CompetitionSubmission winnerSubmission) {
        this.winnerSubmission = winnerSubmission;
    }

    public Instant getVoteDeadline() {
        return voteDeadline;
    }

    public void setVoteDeadline(Instant voteDeadline) {
        this.voteDeadline = voteDeadline;
    }

    public Integer getVoteTarget() {
        return voteTarget;
    }

    public void setVoteTarget(Integer voteTarget) {
        this.voteTarget = voteTarget;
    }

    public CompetitionMatch getNextMatch() {
        return nextMatch;
    }

    public void setNextMatch(CompetitionMatch nextMatch) {
        this.nextMatch = nextMatch;
    }

    public Instant getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Instant insertedAt) {
        this.insertedAt = insertedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }

    @PrePersist
    public void prePersist() {
        if (insertedAt == null) {
            insertedAt = Instant.now();
        }
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
