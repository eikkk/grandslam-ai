package com.plainprog.grandslam_ai.entity.account;

import com.plainprog.grandslam_ai.entity.BaseEntity;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "account")
public class Account extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "created_at")
    private Instant createdAt;

    public Account() {
    }

    public Account(String email, Boolean emailVerified, Instant createdAt) {
        this.email = email;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}