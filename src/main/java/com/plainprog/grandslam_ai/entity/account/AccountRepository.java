package com.plainprog.grandslam_ai.entity.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account findByEmail(String email);
}