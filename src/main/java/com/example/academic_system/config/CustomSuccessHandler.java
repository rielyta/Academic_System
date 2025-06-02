package com.example.academic_system.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Debug log (opsional)
        System.out.println("LOGIN BERHASIL, ROLE: " + authentication.getAuthorities());

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MAHASISWA"))) {
            response.sendRedirect("/mahasiswa/dashboard_mahasiswa");
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOSEN"))) {
            response.sendRedirect("/dosen/dashboard_dosen");
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            response.sendRedirect("/admin/dashboard_admin");
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tidak memiliki role yang valid.");
        }
    }
}
