package com.plainprog.grandslam_ai.object.dto.auth;

import com.plainprog.grandslam_ai.entity.account.Account;

public class SessionPayloadDTO {
    private Account account;

    public SessionPayloadDTO() {
    }

    public SessionPayloadDTO(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
