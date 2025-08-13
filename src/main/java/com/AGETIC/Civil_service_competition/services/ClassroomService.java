package com.AGETIC.Civil_service_competition.services;

import com.AGETIC.Civil_service_competition.dto.ClassroomCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ClassroomResponse;
import java.util.List;

public interface ClassroomService {
    List<ClassroomResponse> getAllClassrooms();
    ClassroomResponse getClassroomById(Long id);
    ClassroomResponse addClassroom(ClassroomCreateRequest request);
    ClassroomResponse updateClassroom(Long id, ClassroomCreateRequest request);
    void deleteClassroom(Long id);
}