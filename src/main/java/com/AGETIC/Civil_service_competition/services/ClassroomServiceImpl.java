package com.AGETIC.Civil_service_competition.services;

import com.AGETIC.Civil_service_competition.dto.ClassroomCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ClassroomResponse;
import com.AGETIC.Civil_service_competition.model.Classroom;
import com.AGETIC.Civil_service_competition.model.Center;
import com.AGETIC.Civil_service_competition.repositories.ClassroomRepository;
import com.AGETIC.Civil_service_competition.repositories.CenterRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassroomServiceImpl implements ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final CenterRepository centerRepository;

    public ClassroomServiceImpl(ClassroomRepository classroomRepository, CenterRepository centerRepository) {
        this.classroomRepository = classroomRepository;
        this.centerRepository = centerRepository;
    }

    private ClassroomResponse toResponse(Classroom classroom) {
        if (classroom == null) return null;
        ClassroomResponse response = new ClassroomResponse();
        response.setId(classroom.getId());
        response.setNumber(classroom.getNumber());
        response.setCapacity(classroom.getCapacity());
        if (classroom.getCenter() != null) {
            response.setCenterId(classroom.getCenter().getId());
            response.setCenterName(classroom.getCenter().getName());
        }
        return response;
    }

    @Override
    public List<ClassroomResponse> getAllClassrooms() {
        return classroomRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClassroomResponse getClassroomById(Long id) {
        return toResponse(classroomRepository.findById(id).orElse(null));
    }

    @Override
    public ClassroomResponse addClassroom(ClassroomCreateRequest request) {
        Center center = centerRepository.findById(request.getCenterId()).orElse(null);
        if (center == null) return null;
        Classroom classroom = new Classroom();
        classroom.setNumber(request.getNumber());
        classroom.setCapacity(request.getCapacity());
        classroom.setCenter(center);
        return toResponse(classroomRepository.save(classroom));
    }

    @Override
    public ClassroomResponse updateClassroom(Long id, ClassroomCreateRequest request) {
        Classroom classroom = classroomRepository.findById(id).orElse(null);
        Center center = centerRepository.findById(request.getCenterId()).orElse(null);
        if (classroom != null && center != null) {
            classroom.setNumber(request.getNumber());
            classroom.setCapacity(request.getCapacity());
            classroom.setCenter(center);
            return toResponse(classroomRepository.save(classroom));
        }
        return null;
    }

    @Override
    public void deleteClassroom(Long id) {
        classroomRepository.deleteById(id);
    }
}