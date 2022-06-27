package com.registration.registration.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Church;
import com.registration.registration.objects.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long>{
    
    //Find Any records with the same email
    Participant findByEmail(String email);

    //Find any records that are approved
    List<Participant> findAllByApprovedTrue();

    //Find any records that are not yet approved
    List<Participant> findAllByApprovedFalse();

    //Find any records that are approved and FirstName is equal to arg1
    List<Participant> findAllByApprovedTrueAndFirstNameContaining(String firstName);

    //Find any records that are not approved and FirstName is equal to arg1
    List<Participant> findAllByApprovedFalseAndFirstNameContaining(String firstName);

    //Find any records that are not yet approved
    List<Participant> findAllByApprovedTrueAndChurchIs(Church church);

    //Find any records that are not yet approved
    List<Participant> findAllByApprovedFalseAndChurchIs(Church church);

    //Find any records that are not approved and FirstName is equal to arg1
    List<Participant> findAllByApprovedFalseAndChurchIsAndFirstNameContaining(Church church, String firstName);

    //Find any records that are not approved and FirstName is equal to arg1
    List<Participant> findAllByApprovedTrueAndChurchIsAndFirstNameContaining(Church church, String firstName);
}
