package com.registration.registration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Long>{
    
}
