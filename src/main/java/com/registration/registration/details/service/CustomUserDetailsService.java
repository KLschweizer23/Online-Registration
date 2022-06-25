package com.registration.registration.details.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.registration.registration.details.CustomLeaderDetails;
import com.registration.registration.details.CustomParticipantDetails;
import com.registration.registration.objects.Leader;
import com.registration.registration.objects.Participant;
import com.registration.registration.repositories.LeaderRepository;
import com.registration.registration.repositories.ParticipantRepository;

public class CustomUserDetailsService implements UserDetailsService{
    
    @Autowired
    private LeaderRepository leaderRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String role = req.getParameter("role");
        if(role.equals("camper")){
            Participant camper = participantRepository.findByEmail(email);
            if(camper == null){
                throw new UsernameNotFoundException("Camper not found!");
            }
            return new CustomParticipantDetails(camper);
        }else if(role.equals("leader")){
            Leader leader = leaderRepository.findByEmail(email);
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
