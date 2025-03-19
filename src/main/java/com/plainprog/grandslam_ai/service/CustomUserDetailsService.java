//package com.plainprog.grandslam_ai.service;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new CustomPasswordEncoder();
//    }
//
//    @Override
//    public User loadUserByUsername(String username) throws UsernameNotFoundException {
//        return new User("qqqq", "wwww", List.of(new SimpleGrantedAuthority("ROLE_USER")));
//    }
//}
