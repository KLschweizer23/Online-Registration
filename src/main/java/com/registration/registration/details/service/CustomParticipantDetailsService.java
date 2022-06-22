package com.registration.registration.details.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.registration.registration.details.CustomParticipantDetails;
import com.registration.registration.objects.Participant;
import com.registration.registration.repositories.ParticipantRepository;

public class CustomParticipantDetailsService implements UserDetailsService{

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Participant participant = participantRepository.findByEmail(email);
        if(participant == null){
            throw new UsernameNotFoundException("User not found!");
        }
        return new CustomParticipantDetails(participant);
    }
    
}
