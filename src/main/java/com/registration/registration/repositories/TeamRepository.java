package com.registration.registration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Team;

public interface TeamRepository extends JpaRepository<Team, Long>{
    
    //Find team that name contains arg1
    Team findByName(String name);

}
