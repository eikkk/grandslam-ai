package com.plainprog.grandslam_ai.entity.competitions;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.img_gen.Image;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "image_match_record")
public class ImageMatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private CompetitionMatch match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_account_id", nullable = false)
    private Account ownerAccount;

    @Column(name = "opponent_image_id", nullable = false)
    private Long opponentImageId;

    @Column(name = "starting_elo", nullable = false)
    private int startingElo;

    @Column(name = "ending_elo", nullable = false)
    private int endingElo;

    @Column(name = "votes_count")
    private Integer votesCount;

    @Column(name = "opponent_votes_count")
    private Integer opponentVotesCount;

    @Column(name = "insertedAt")
    private Instant insertedAt;

    public ImageMatchRecord() {}

    public ImageMatchRecord(CompetitionMatch match, Image image, Account ownerAccount, Long opponentImageId, int startingElo, int endingElo, Integer votesCount, Integer opponentVotesCount) {
        this.match = match;
        this.image = image;
        this.ownerAccount = ownerAccount;
        this.opponentImageId = opponentImageId;
        this.startingElo = startingElo;
        this.endingElo = endingElo;
        this.votesCount = votesCount;
        this.opponentVotesCount = opponentVotesCount;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompetitionMatch getMatch() {
        return match;
    }

    public void setMatch(CompetitionMatch match) {
        this.match = match;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Account getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(Account ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public Long getOpponentImageId() {
        return opponentImageId;
    }

    public void setOpponentImageId(Long opponentImageId) {
        this.opponentImageId = opponentImageId;
    }

    public int getStartingElo() {
        return startingElo;
    }

    public void setStartingElo(int startingElo) {
        this.startingElo = startingElo;
    }

    public int getEndingElo() {
        return endingElo;
    }

    public void setEndingElo(int endingElo) {
        this.endingElo = endingElo;
    }

    public Integer getVotesCount() {
        return votesCount;
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public Integer getOpponentVotesCount() {
        return opponentVotesCount;
    }

    public void setOpponentVotesCount(Integer opponentVotesCount) {
        this.opponentVotesCount = opponentVotesCount;
    }

    public Instant getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Instant insertedAt) {
        this.insertedAt = insertedAt;
    }
}
