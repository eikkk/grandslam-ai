package com.plainprog.grandslam_ai.object.dto.auth;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;

public class AccountCreationDTO {
    private Account account;
    private AccountSecurity accountSecurity;
    private String password;
    private String errorMessage;
    private boolean alreadyExists;

    public AccountCreationDTO(Account account, AccountSecurity accountSecurity, String password, String errorMessage, boolean alreadyExists) {
        this.account = account;
        this.accountSecurity = accountSecurity;
        this.password = password;
        this.errorMessage = errorMessage;
        this.alreadyExists = alreadyExists;
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

    public AccountSecurity getAccountSecurity() {
        return accountSecurity;
    }

    public void setAccountSecurity(AccountSecurity accountSecurity) {
        this.accountSecurity = accountSecurity;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isAlreadyExists() {
        return alreadyExists;
    }

    public void setAlreadyExists(boolean alreadyExists) {
        this.alreadyExists = alreadyExists;
    }
}
