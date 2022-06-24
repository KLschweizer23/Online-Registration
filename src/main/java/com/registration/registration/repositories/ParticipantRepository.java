package com.registration.registration.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Church;
import com.registration.registration.objects.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long>{
    
    //Find Any records with the same email
    Participant findByEmail(String email);

    //Find any records that are approved
    List<Participant> findByApprovedTrue();

    //Find any records that are approved and FirstName is equal to arg1
    List<Participant> findByApprovedTrueAndFirstNameContaining(String firstName);

    //Find any records that are not yet approved
    List<Participant> findByApprovedFalse();

    //Find any records that are approved and FirstName is equal to arg1
    List<Participant> findByApprovedFalseAndFirstNameContaining(String firstName);

    //Find any records that are not yet approved
    List<Participant> findByApprovedFalseAndChurchIs(Church church);
}
