
package com.AGETIC.Civil_service_competition.repository;

import com.AGETIC.Civil_service_competition.model.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  boolean existsByTypeIgnoreCase(String type);
List<Category> findByTypeContainingIgnoreCase(String q);

}
