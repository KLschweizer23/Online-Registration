package com.registration.registration.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Church;
import com.registration.registration.objects.Participant;
import com.registration.registration.objects.Team;

public interface ParticipantRepository extends JpaRepository<Participant, Long>{
    
    //Find Any records with the same email
    Participant findByEmail(String email);

    //Find all Participants where approved either true or false
    List<Participant> findAllByApprovedIs(boolean approved);

    //Find any records that are approved and FirstName is equal to arg1
    List<Participant> findAllByApprovedTrueAndFirstNameContaining(String firstName);

    //Find any records that are not approved and FirstName is equal to arg1
    List<Participant> findAllByApprovedFalseAndFirstNameContaining(String firstName);

    //Find any records that are not yet approved and church is equal to a certain church
    List<Participant> findAllByApprovedTrueAndChurchIs(Church church);

    //Find any records that are not yet approved and church is equal to a certain church
    List<Participant> findAllByApprovedFalseAndChurchIs(Church church);

    //Find any records that are not approved and FirstName is equal to arg1
    List<Participant> findAllByApprovedFalseAndChurchIsAndFirstNameContaining(Church church, String firstName);

    //Find any records that are not approved and FirstName is equal to arg1
    List<Participant> findAllByApprovedTrueAndChurchIsAndFirstNameContaining(Church church, String firstName);

    //Find any records that are approved players and their team
    List<Participant> findAllByApprovedTrueAndPlayerFalseAndTeamIs(Team team);

    //Find any records that are approved players and their team
    Long countByApprovedTrueAndPlayerFalseAndTeamIs(Team team);

    //Find any records that are approved players and their team
    List<Participant> findAllByApprovedTrueAndPlayerFalseAndSexIs(String sex);
}
