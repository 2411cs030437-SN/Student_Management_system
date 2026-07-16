package com.example.Student_Management.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String path = request.getRequestURI();
        if (isPublicPath(path)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("role") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        String role = (String) session.getAttribute("role");
        if ("ADMIN".equals(role)) {
            return true;
        }

        if ("STUDENT".equals(role) && isOwnStudentProfile(path, session)) {
            return true;
        }

        response.sendRedirect(request.getContextPath() + "/access-denied");
        return false;
    }

    private boolean isPublicPath(String path) {
        return path.equals("/login")
                || path.equals("/login/admin")
                || path.equals("/login/student")
                || path.equals("/logout")
                || path.equals("/access-denied")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/");
    }

    private boolean isOwnStudentProfile(String path, HttpSession session) {
        Object studentId = session.getAttribute("studentId");
        return studentId != null && path.equals("/students/" + studentId);
    }
}
