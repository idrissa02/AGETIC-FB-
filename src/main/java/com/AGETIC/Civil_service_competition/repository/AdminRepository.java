// repository/AdminRepository.java
package com.AGETIC.Civil_service_competition.repository;

import com.AGETIC.Civil_service_competition.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
