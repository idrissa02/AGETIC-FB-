package com.AGETIC.Civil_service_competition.repository;

import com.AGETIC.Civil_service_competition.model.Classroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    
 List<Classroom> findByCenterId(Long centerId);
  boolean existsByCenterIdAndNumberIgnoreCase(Long centerId, String number);
}
