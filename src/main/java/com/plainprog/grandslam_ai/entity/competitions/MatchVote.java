package com.plainprog.grandslam_ai.entity.competitions;

import com.plainprog.grandslam_ai.entity.BaseEntity;
import com.plainprog.grandslam_ai.entity.account.Account;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "match_vote")
public class MatchVote extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private com.plainprog.grandslam_ai.entity.account.Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private CompetitionMatch match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private CompetitionSubmission submission;

    @Column(name = "insertedAt", nullable = false)
    private Instant insertedAt;

    public MatchVote() {}

    public MatchVote(Account account, CompetitionMatch match, CompetitionSubmission submission) {
        this.account = account;
        this.match = match;
        this.submission = submission;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public CompetitionMatch getMatch() {
        return match;
    }

    public void setMatch(CompetitionMatch match) {
        this.match = match;
    }

    public CompetitionSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(CompetitionSubmission submission) {
        this.submission = submission;
    }

    public Instant getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Instant insertedAt) {
        this.insertedAt = insertedAt;
    }

    @PrePersist
    public void prePersist() {
        if (insertedAt == null) {
            insertedAt = Instant.now();
        }
    }
}