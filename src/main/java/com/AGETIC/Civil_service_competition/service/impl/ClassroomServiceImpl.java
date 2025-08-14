package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.ClassroomCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ClassroomResponse;
// import com.AGETIC.Civil_service_competition.model.Center;
import com.AGETIC.Civil_service_competition.model.Classroom;
import com.AGETIC.Civil_service_competition.repository.CenterRepository;
import com.AGETIC.Civil_service_competition.repository.ClassroomRepository;
import com.AGETIC.Civil_service_competition.service.ClassroomService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomRepository classroomRepo;
    private final CenterRepository centerRepo;

    public ClassroomServiceImpl(ClassroomRepository classroomRepo, CenterRepository centerRepo) {
        this.classroomRepo = classroomRepo;
        this.centerRepo = centerRepo;
    }

    @Override
    public List<ClassroomResponse> getAllClassrooms() {
        return classroomRepo.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ClassroomResponse getClassroomById(Long id) {
        Optional<Classroom> opt = classroomRepo.findById(id);
        return opt.map(this::toResponse).orElse(null);
    }

    @Override
    public ClassroomResponse addClassroom(ClassroomCreateRequest request) {
        Classroom c = new Classroom();
        c.setNumber(request.number());
        c.setCapacity(request.capacity());

        if (request.centerId() != null) {
            centerRepo.findById(request.centerId()).ifPresent(c::setCenter);
        }

        classroomRepo.save(c);
        return toResponse(c);
    }

    @Override
    public ClassroomResponse updateClassroom(Long id, ClassroomCreateRequest request) {
        Optional<Classroom> opt = classroomRepo.findById(id);
        if (opt.isEmpty()) return null;

        Classroom c = opt.get();
        if (request.number() != null) c.setNumber(request.number());
        if (request.capacity() != 0) c.setCapacity(request.capacity());

        if (request.centerId() != null) {
            centerRepo.findById(request.centerId()).ifPresent(c::setCenter);
        }

        return toResponse(c);
    }

    @Override
    public void deleteClassroom(Long id) {
        classroomRepo.deleteById(id);
    }

    private ClassroomResponse toResponse(Classroom c) {
        Long centerId = (c.getCenter() != null) ? c.getCenter().getId() : null;
        return new ClassroomResponse(c.getId(), c.getNumber(), c.getCapacity(), centerId);
    }
}
