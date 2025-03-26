package com.plainprog.grandslam_ai.spring.security;

import com.plainprog.grandslam_ai.entity.account.Account;
import com.plainprog.grandslam_ai.entity.account.AccountRepository;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurity;
import com.plainprog.grandslam_ai.entity.account_security.AccountSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountSecurityRepository accountSecurityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("Account not found");
        }
        AccountSecurity accountSecurity = accountSecurityRepository.findByAccountId(account.getId());
        if (accountSecurity == null) {
            throw new UsernameNotFoundException("Account security not found");
        }
        return new AccountDetails(account.getEmail(), accountSecurity.getHashPass());
    }
}
