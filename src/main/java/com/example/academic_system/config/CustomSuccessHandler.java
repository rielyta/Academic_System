package com.example.academic_system.config;

import com.example.academic_system.services.ActivityLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final ActivityLogService activityLogService;

    public CustomSuccessHandler(@Lazy ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        System.out.println("LOGIN BERHASIL, ROLE: " + authentication.getAuthorities());

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String detail = String.format("User %s logged in with role %s from IP: %s",
                username, role, getClientIP(request));
        activityLogService.log("Authentication", username, "LOGIN", detail, username);

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

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
