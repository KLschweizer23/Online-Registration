package com.registration.registration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.registration.registration.objects.Church;

public interface ChurchRepository extends JpaRepository<Church, Long>{
    
}
