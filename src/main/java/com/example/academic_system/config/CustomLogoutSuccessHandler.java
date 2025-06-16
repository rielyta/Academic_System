package com.example.academic_system.config;

import com.example.academic_system.models.ActivityLog;
import com.example.academic_system.repositories.ActivityLogRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.Date;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {
            try {
                String username = authentication.getName();
                String clientIP = getClientIP(request);

                System.out.println("LOGOUT: User " + username + " from IP " + clientIP);

                logToDatabase(request, username, clientIP);

            } catch (Exception e) {
                System.err.println("Error during logout logging: " + e.getMessage());
            }
        }

        response.sendRedirect("/login?logout");
    }

    private void logToDatabase(HttpServletRequest request, String username, String clientIP) {

        new Thread(() -> {
            try {
                WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
                if (context != null) {
                    ActivityLogRepository activityLogRepository = context.getBean(ActivityLogRepository.class);

                    ActivityLog log = new ActivityLog();
                    log.setEntityName("Authentication");
                    log.setEntityId(username);
                    log.setAction("LOGOUT");
                    log.setDetail(String.format("User %s logged out from IP: %s", username, clientIP));
                    log.setPerformedBy(username);
                    log.setTimestamp(new Date());

                    activityLogRepository.save(log);

                    System.out.println("LOGOUT berhasil dicatat ke database: " + username);
                } else {
                    System.err.println("WebApplicationContext tidak tersedia");
                }

            } catch (Exception e) {
                System.err.println("Failed to log logout to database: " + e.getMessage());
                System.out.println("FALLBACK_LOGOUT_LOG: " + username + " | " + clientIP + " | " + java.time.LocalDateTime.now());
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