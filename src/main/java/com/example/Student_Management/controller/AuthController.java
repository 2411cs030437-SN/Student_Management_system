package com.example.Student_Management.controller;

import com.example.Student_Management.model.Student;
import com.example.Student_Management.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AuthController {

    private final StudentService studentService;
    private final Map<String, String> adminAccounts;

    public AuthController(StudentService studentService,
                          @Value("${app.admin.accounts:admin:admin123}") String adminAccounts) {
        this.studentService = studentService;
        this.adminAccounts = parseAdminAccounts(adminAccounts);
    }

    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        if ("ADMIN".equals(session.getAttribute("role"))) {
            return "redirect:/students";
        }
        if ("STUDENT".equals(session.getAttribute("role"))) {
            return "redirect:/students/" + session.getAttribute("studentId");
        }
        return "login";
    }

    @PostMapping("/login/admin")
    public String adminLogin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {
        String normalizedUsername = username.trim();
        if (password.equals(adminAccounts.get(normalizedUsername))) {
            session.invalidate();
            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("role", "ADMIN");
            newSession.setAttribute("displayName", normalizedUsername);
            return "redirect:/students";
        }

        redirectAttributes.addFlashAttribute("errorMessage", "Invalid admin username or password.");
        return "redirect:/login";
    }

    @PostMapping("/login/student")
    public String studentLogin(@RequestParam String rollNo,
                               @RequestParam String phone,
                               HttpSession session,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {
        Optional<Student> student = studentService.findByRollNoAndPhone(rollNo, phone);
        if (student.isPresent()) {
            session.invalidate();
            HttpSession newSession = request.getSession(true);
            Student loggedInStudent = student.get();
            newSession.setAttribute("role", "STUDENT");
            newSession.setAttribute("studentId", loggedInStudent.getId());
            newSession.setAttribute("displayName", loggedInStudent.getName());
            return "redirect:/students/" + loggedInStudent.getId();
        }

        redirectAttributes.addFlashAttribute("errorMessage", "No student matched that roll number and phone.");
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Logged out successfully.");
        return "redirect:/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("message", "You do not have access to that page.");
        return "access-denied";
    }

    private Map<String, String> parseAdminAccounts(String accounts) {
        return Arrays.stream(accounts.split(","))
                .map(String::trim)
                .filter(account -> account.contains(":"))
                .map(account -> account.split(":", 2))
                .collect(Collectors.toMap(parts -> parts[0].trim(), parts -> parts[1].trim(), (first, second) -> second));
    }
}
