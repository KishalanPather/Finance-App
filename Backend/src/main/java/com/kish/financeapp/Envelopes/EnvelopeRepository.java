package com.kish.financeapp.Envelopes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvelopeRepository extends JpaRepository<Envelope,Integer>{
     boolean existsByName(String name);
}  
    

