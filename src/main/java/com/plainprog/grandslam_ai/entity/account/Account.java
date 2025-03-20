package com.plainprog.grandslam_ai.entity.account;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "hash_pass")
    private String hash_pass;
}