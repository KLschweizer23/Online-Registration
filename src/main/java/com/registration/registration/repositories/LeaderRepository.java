package com.registration.registration.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Leader;

public interface LeaderRepository extends JpaRepository<Leader, Long>{
    
    Leader findByEmail(String email);

    //Find all leaders where approve is either true or false
    List<Leader> findAllByApprovedIs(boolean approve);

}
