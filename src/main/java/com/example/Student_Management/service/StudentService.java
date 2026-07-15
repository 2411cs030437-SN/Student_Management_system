package com.example.Student_Management.service;

import com.example.Student_Management.model.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {

    List<Student> findAll(String keyword, String branch);

    List<String> findBranches();

    List<Map<String, Object>> countByBranch();

    Student findById(Long id);

    void create(Student student);

    void update(Student student);

    void deleteById(Long id);
}
