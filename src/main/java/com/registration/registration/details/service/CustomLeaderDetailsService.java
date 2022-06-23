package com.registration.registration.details.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.registration.registration.details.CustomLeaderDetails;
import com.registration.registration.details.CustomParticipantDetails;
import com.registration.registration.objects.Leader;
import com.registration.registration.objects.Participant;
import com.registration.registration.repositories.LeaderRepository;
import com.registration.registration.repositories.ParticipantRepository;

public class CustomLeaderDetailsService implements UserDetailsService{
    
    @Autowired
    private LeaderRepository leaderRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String emailPart = null;
        if(email.contains(":")){
            int colonIndex = email.lastIndexOf(":");
            emailPart = email.substring(0, colonIndex);
        }
        emailPart = emailPart == null ? email : emailPart;

        String rolePart = null;
        if(email.contains(":")){
            int colonIndex = email.lastIndexOf(":");
            rolePart = email.substring(colonIndex + 1, email.length());
        }

        if(rolePart.equals("Camper")){
            Participant camper = participantRepository.findByEmail(emailPart);
            if(camper == null){
                throw new UsernameNotFoundException("Camper not found!");
            }
            return new CustomParticipantDetails(camper);
        }else if(rolePart.equals("Leader")){
            Leader leader = leaderRepository.findByEmail(emailPart);
            if(leader == null){
                throw new UsernameNotFoundException("Leader not found!");
            }
            return new CustomLeaderDetails(leader);
        }
        else{
            return null;
        }
    }
}
