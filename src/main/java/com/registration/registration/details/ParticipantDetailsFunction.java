package com.registration.registration.details;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.registration.registration.repositories.ChurchRepository;
import com.registration.registration.repositories.ParticipantRepository;
import com.registration.registration.objects.Church;
import com.registration.registration.objects.Participant;
import com.registration.registration.repositories.SportRepository;

public class ParticipantDetailsFunction {
    
    
    @Autowired
    private ParticipantRepository participantRepository;
    
    @Autowired
    private ChurchRepository churchRepository;

    @Autowired
    private SportRepository sportRepository;
    
    //getCampers either approved or not
    public List<Participant> getCampers(boolean approved){
        List<Participant> participants = approved ? participantRepository.findByApprovedTrue() : participantRepository.findByApprovedFalse();
        return participants;
    }

    //getCampers either approved or not and contains a certain keyword
    public List<Participant> getCampers(boolean approved, String keyword){
        List<Participant> participants = approved ? participantRepository.findByApprovedTrueAndFirstNameContaining(keyword) : participantRepository.findByApprovedFalseAndFirstNameContaining(keyword);
        return participants;
    }

    //getCampers either approved or not and contains a certain keyword and is equal to a certain church
    public List<Participant> getCampers(boolean approved, String keyword, Church church){
        List<Participant> participants = approved ? participantRepository.findByApprovedFalseAndChurchIsAndFirstNameContaining(church, keyword) : participantRepository.findByApprovedTrueAndChurchIsAndFirstNameContaining(church, keyword);
        return participants;
    }

    //getCampers either approved or not and is equal to a certain church
    public List<Participant> getCampers(boolean approved, Church church){
        List<Participant> participants = approved ? participantRepository.findByApprovedFalseAndChurchIs(church) : participantRepository.findByApprovedTrueAndChurchIs(church);
        return participants;
    }
}
