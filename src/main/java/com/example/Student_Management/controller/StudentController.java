package com.example.Student_Management.controller;

import com.example.Student_Management.model.Student;
import com.example.Student_Management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Year;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final String[] STATUS_OPTIONS = {"Active", "Discontinued", "Graduated"};

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String listStudents(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String branch,
                               Model model) {
        List<Student> students = studentService.findAll(keyword, branch);
        model.addAttribute("students", students);
        model.addAttribute("totalStudents", students.size());
        model.addAttribute("activeStudents", countByStatus(students, "Active"));
        model.addAttribute("graduatedStudents", countByStatus(students, "Graduated"));
        model.addAttribute("admittedThisYear", countByAdmissionYear(students, Year.now().getValue()));
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedBranch", branch);
        model.addAttribute("branches", studentService.findBranches());
        model.addAttribute("branchCounts", studentService.countByBranch());
        return "students/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        Student student = new Student();
        student.setStatus("Active");
        model.addAttribute("student", student);
        addFormOptions(model, "Add Student", "/students");
        return "students/form";
    }

    @PostMapping
    public String createStudent(@Valid @ModelAttribute Student student,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormOptions(model, "Add Student", "/students");
            return "students/form";
        }

        try {
            normalizeStudent(student);
            studentService.create(student);
        } catch (DuplicateKeyException ex) {
            bindingResult.rejectValue("rollNo", "duplicate", "Roll number already exists");
            addFormOptions(model, "Add Student", "/students");
            return "students/form";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Student added successfully.");
        return "redirect:/students";
    }

    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        return "students/view";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", studentService.findById(id));
        addFormOptions(model, "Edit Student", "/students/" + id);
        return "students/form";
    }

    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute Student student,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormOptions(model, "Edit Student", "/students/" + id);
            return "students/form";
        }

        student.setId(id);
        try {
            normalizeStudent(student);
            studentService.update(student);
        } catch (DuplicateKeyException ex) {
            bindingResult.rejectValue("rollNo", "duplicate", "Roll number already exists");
            addFormOptions(model, "Edit Student", "/students/" + id);
            return "students/form";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully.");
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully.");
        return "redirect:/students";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleMissingStudent(NoSuchElementException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/students";
    }

    private long countByStatus(List<Student> students, String status) {
        return students.stream()
                .filter(student -> status.equals(student.getStatus()))
                .count();
    }

    private long countByAdmissionYear(List<Student> students, int admissionYear) {
        return students.stream()
                .filter(student -> student.getAdmissionYear() != null && student.getAdmissionYear() == admissionYear)
                .count();
    }

    private void addFormOptions(Model model, String pageTitle, String formAction) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("formAction", formAction);
        model.addAttribute("statusOptions", STATUS_OPTIONS);
    }

    private void normalizeStudent(Student student) {
        if (student.getBranch() != null) {
            student.setBranch(student.getBranch().trim().toUpperCase());
        }
    }
}
