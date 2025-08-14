package com.AGETIC.Civil_service_competition.repository;

import com.AGETIC.Civil_service_competition.model.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CenterRepository extends JpaRepository<Center, Long> {
    
    List<Center> findByNameContainingIgnoreCase(String name);
    List<Center> findByLocationContainingIgnoreCase(String location);
        boolean existsByNameIgnoreCase(String name);

}

