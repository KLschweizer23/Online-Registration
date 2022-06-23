package com.registration.registration.details.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.registration.registration.details.CustomLeaderDetails;
import com.registration.registration.objects.Leader;
import com.registration.registration.repositories.LeaderRepository;

public class CustomLeaderDetailsService implements UserDetailsService{
    
    @Autowired
    private LeaderRepository leaderRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Leader leader = leaderRepository.findByEmail(email);
        if(leader == null){
            throw new UsernameNotFoundException("Leader not found!");
        }
        return new CustomLeaderDetails(leader);
    }
}
