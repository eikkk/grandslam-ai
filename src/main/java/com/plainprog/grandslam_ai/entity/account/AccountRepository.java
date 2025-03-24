package com.plainprog.grandslam_ai.entity.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    public Account findByEmail(String email);
}