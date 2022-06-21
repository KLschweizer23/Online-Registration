package com.registration.registration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Sport;

public interface SportRepository extends JpaRepository<Sport, Long> {
    
}
