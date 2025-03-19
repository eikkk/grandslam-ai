//package com.plainprog.grandslam_ai.bean;
//
//import com.plainprog.grandslam_ai.service.CustomUserDetailsService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(csrf -> csrf.disable()) // Updated way to disable CSRF
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/anonymous-endpoint").permitAll() // Allowing anonymous access
//                        .requestMatchers("/api/auth/login").permitAll()
//                        .requestMatchers("/hello").authenticated()
//                        .anyRequest().authenticated()
//                )
//
////                .formLogin( // Updated way to configure form login)
////                        formLogin -> formLogin
////                                .loginPage("/login")
////                                .permitAll()
////                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
//                .httpBasic(Customizer.withDefaults())
//                .build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder auth =
//                http.getSharedObject(AuthenticationManagerBuilder.class);
//        auth
//                .userDetailsService(customUserDetailsService)
//                .passwordEncoder(passwordEncoder);
//        return auth.build();
//    }
//}