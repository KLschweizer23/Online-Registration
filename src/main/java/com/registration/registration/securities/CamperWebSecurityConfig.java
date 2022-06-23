package com.registration.registration.securities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.registration.registration.details.service.CustomParticipantDetailsService;

@Configuration
public class CamperWebSecurityConfig {

    @Bean
    public UserDetailsService camperUserDetailsService(){
        return new CustomParticipantDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder camperPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider camperAuthenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(camperUserDetailsService());
        authProvider.setPasswordEncoder(camperPasswordEncoder());

        return authProvider;
    }
    
    @Bean
    public SecurityFilterChain camperFilterChain(HttpSecurity http) throws Exception{
        http.authorizeRequests()
        .antMatchers("/camper").authenticated()
        .anyRequest().permitAll()
        .and()
        .formLogin()
            .loginPage("/login?val=camper")
            .usernameParameter("email")
            .defaultSuccessUrl("/camper")
            .permitAll()
        .and().logout().logoutSuccessUrl("/").permitAll();

        return http.build();
    }

}
