package com.example.academic_system.config;

import com.example.academic_system.models.ActivityLog;
import com.example.academic_system.repositories.ActivityLogRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        try {
            System.out.println("LOGIN BERHASIL, ROLE: " + authentication.getAuthorities());

            String username = authentication.getName();
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            String clientIP = getClientIP(request);


            System.out.println("LOGIN: User " + username + " with role " + role + " from IP " + clientIP);


            logToDatabase(request, username, role, clientIP);


            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MAHASISWA"))) {
                response.sendRedirect("/mahasiswa/dashboard_mahasiswa");
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOSEN"))) {
                response.sendRedirect("/dosen/dashboard_dosen");
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin/dashboard_admin");
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Tidak memiliki role yang valid.");
            }
        } catch (Exception e) {
            System.err.println("Error during login: " + e.getMessage());

            response.sendRedirect("/login?error");
        }
    }

    private void logToDatabase(HttpServletRequest request, String username, String role, String clientIP) {

        new Thread(() -> {
            try {
                // Manual lookup ActivityLogRepository dari WebApplicationContext
                WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
                if (context != null) {
                    ActivityLogRepository activityLogRepository = context.getBean(ActivityLogRepository.class);


                    ActivityLog log = new ActivityLog();
                    log.setEntityName("Authentication");
                    log.setEntityId(username);
                    log.setAction("LOGIN");
                    log.setDetail(String.format("User %s logged in with role %s from IP: %s", username, role, clientIP));
                    log.setPerformedBy(username);
                    log.setTimestamp(new Date());

                    // Simpan ke database
                    activityLogRepository.save(log);

                    System.out.println("LOGIN berhasil dicatat ke database: " + username);
                } else {
                    System.err.println("WebApplicationContext tidak tersedia");
                }

            } catch (Exception e) {
                System.err.println("Failed to log login to database: " + e.getMessage());
                // Fallback ke console log
                System.out.println("FALLBACK_LOGIN_LOG: " + username + " | " + role + " | " + clientIP + " | " + java.time.LocalDateTime.now());
            }
        }).start();
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.trim().isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}