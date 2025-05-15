package com.plainprog.grandslam_ai.entity.img_management;

import com.plainprog.grandslam_ai.entity.BaseEntity;
import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.helper.sorting.Sortable;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gallery_group")
public class GalleryGroup extends BaseEntity<Integer> implements Sortable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private Long position;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "group")
    private List<GalleryEntry> entries = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Constructors
    public GalleryGroup() {}

    public GalleryGroup(String name, Long position, Account account) {
        this.name = name;
        this.position = position;
        this.account = account;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<GalleryEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<GalleryEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(GalleryEntry entry) {
        entries.add(entry);
        entry.setGroup(this);
    }

    public void removeEntry(GalleryEntry entry) {
        entries.remove(entry);
        entry.setGroup(null);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    // Lifecycle hook to set created_at automatically
    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
