package com.example.Student_Management.controller;

import com.example.Student_Management.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final StudentService studentService;

    public HomeController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        if (session.getAttribute("role") == null) {
            return "redirect:/login";
        }
        model.addAttribute("totalStudents", studentService.findAll(null, null).size());
        return "index";
    }
}
