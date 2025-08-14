package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.ClassroomCreateRequest;
import com.AGETIC.Civil_service_competition.dto.ClassroomResponse;
import com.AGETIC.Civil_service_competition.service.ClassroomService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public List<ClassroomResponse> getAllClassrooms() {
        return classroomService.getAllClassrooms();
    }

    @GetMapping("/{id}")
    public ClassroomResponse getClassroomById(@PathVariable Long id) {
        return classroomService.getClassroomById(id);
    }

    @PostMapping
    public ClassroomResponse addClassroom(@RequestBody ClassroomCreateRequest request) {
        return classroomService.addClassroom(request);
    }

    @PutMapping("/{id}")
    public ClassroomResponse updateClassroom(@PathVariable Long id, @RequestBody ClassroomCreateRequest request) {
        return classroomService.updateClassroom(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteClassroom(@PathVariable Long id) {
        classroomService.deleteClassroom(id);}
    }