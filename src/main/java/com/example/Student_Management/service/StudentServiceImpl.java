package com.example.Student_Management.service;

import com.example.Student_Management.model.Student;
import com.example.Student_Management.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> findAll(String keyword, String branch) {
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasBranch = branch != null && !branch.isBlank();

        if (!hasKeyword && !hasBranch) {
            return studentRepository.findAll();
        }
        if (!hasKeyword) {
            return studentRepository.findAllByBranch(branch);
        }
        return studentRepository.search(keyword.trim(), branch);
    }

    @Override
    public List<String> findBranches() {
        return studentRepository.findBranches();
    }

    @Override
    public List<Map<String, Object>> countByBranch() {
        return studentRepository.countByBranch();
    }

    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Student not found with id " + id));
    }

    @Override
    public void create(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void update(Student student) {
        studentRepository.update(student);
    }

    @Override
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }
}
