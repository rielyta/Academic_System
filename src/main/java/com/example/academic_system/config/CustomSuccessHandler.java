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

        if (Boolean.TRUE.equals(request.getSession().getAttribute("FROM_SIGNUP"))) {
            request.getSession().removeAttribute("FROM_SIGNUP");
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MAHASISWA"))) {
                response.sendRedirect("/profil/profil_mahasiswa");
                return;
            } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOSEN"))) {
                response.sendRedirect("/profil/profil_dosen");
                return;
            }
        }


        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MAHASISWA"))) {
            response.sendRedirect("/dashboard/mahasiswa");
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DOSEN"))) {
            response.sendRedirect("/dashboard/dosen");
        } else {
            response.sendRedirect("/dashboard");
        }
    }

}
