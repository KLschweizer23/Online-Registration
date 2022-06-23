package com.registration.registration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Leader;

public interface LeaderRepository extends JpaRepository<Leader, Long>{
    
    Leader findByEmail(String email);

}
