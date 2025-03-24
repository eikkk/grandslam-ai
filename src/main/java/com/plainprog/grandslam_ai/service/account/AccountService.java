package com.plainprog.grandslam_ai.service.account;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import com.plainprog.grandslam_ai.object.dto.auth.AccountCreationDTO;
import com.plainprog.grandslam_ai.object.dto.util.OperationResultDTO;
import com.plainprog.grandslam_ai.object.request_models.auth.VerifyEmailRequest;
import com.plainprog.grandslam_ai.service.account.helpers.PassGenHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountSecurityRepository accountSecurityRepository;

    public AccountCreationDTO createAccount(String email) {
        String pass = PassGenHelp.randomPassword();
        String randomSalt = PassGenHelp.randomSalt();
        String hashPass = PassGenHelp.hashPassword(pass, randomSalt);

        Account acc = new Account(email, false);
        Account account = accountRepository.save(acc);

        AccountSecurity accSec = new AccountSecurity(account.getId(), hashPass, randomSalt, false);
        AccountSecurity accountSecurity = accountSecurityRepository.save(accSec);

        return new AccountCreationDTO(account, pass);
    }
    public OperationResultDTO verifyEmail(VerifyEmailRequest payload) {
        Account account = accountRepository.findByEmail(payload.getEmail());
        if (account == null) {
            return new OperationResultDTO(false, "Account not found");
        }
        if (account.getEmailVerified()) {
            return new OperationResultDTO(false, "Email already verified");
        }
        AccountSecurity accountSecurity = accountSecurityRepository.findByAccountId(account.getId());
        if (accountSecurity == null) {
            return new OperationResultDTO(false, "Account security not found");
        }
        if (!accountSecurity.getVerifyEmailToken().equals(payload.getToken())) {
            return new OperationResultDTO(false, "Invalid token");
        }
        account.setEmailVerified(true);
        accountSecurityRepository.save(accountSecurity);
        return new OperationResultDTO(true, "Email verified successfully");
    }
    public boolean sendRegistrationEmail(String email, String pass) {
        //TODO: the password should be sent to the user's email during registration
        return true;
    }
}
