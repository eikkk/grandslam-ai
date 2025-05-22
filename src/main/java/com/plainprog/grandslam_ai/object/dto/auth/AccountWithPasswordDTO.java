package com.plainprog.grandslam_ai.object.dto.auth;

import com.plainprog.grandslam_ai.entity.account.Account;

public class AccountWithPasswordDTO {
    private Account account;
    private String password;

    public AccountWithPasswordDTO(Account account, String password) {
        this.account = account;
        this.password = password;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
