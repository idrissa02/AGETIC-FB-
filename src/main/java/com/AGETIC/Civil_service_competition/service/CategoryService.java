
package com.AGETIC.Civil_service_competition.service;

import com.AGETIC.Civil_service_competition.dto.CategoryCreateRequest;
import com.AGETIC.Civil_service_competition.dto.CategoryResponse;
import java.util.List;

public interface CategoryService {
    
    List<CategoryResponse> getAll();
    CategoryResponse getById(Long id);
    CategoryResponse create(CategoryCreateRequest req);
    CategoryResponse update(Long id, CategoryCreateRequest req);
    void delete(Long id);
    List<CategoryResponse> searchByName(String q);
}
