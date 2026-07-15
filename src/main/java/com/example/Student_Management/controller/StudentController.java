package com.example.Student_Management.controller;

import com.example.Student_Management.model.Student;
import com.example.Student_Management.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("students", studentService.findAll(keyword, branch));
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
        model.addAttribute("pageTitle", "Add Student");
        model.addAttribute("formAction", "/students");
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        return "students/form";
    }

    @PostMapping
    public String createStudent(@Valid @ModelAttribute Student student,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Student");
            model.addAttribute("formAction", "/students");
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            return "students/form";
        }

        studentService.create(student);
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
        model.addAttribute("pageTitle", "Edit Student");
        model.addAttribute("formAction", "/students/" + id);
        model.addAttribute("statusOptions", STATUS_OPTIONS);
        return "students/form";
    }

    @PostMapping("/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute Student student,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Student");
            model.addAttribute("formAction", "/students/" + id);
            model.addAttribute("statusOptions", STATUS_OPTIONS);
            return "students/form";
        }

        student.setId(id);
        studentService.update(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully.");
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        studentService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully.");
        return "redirect:/students";
    }
}
