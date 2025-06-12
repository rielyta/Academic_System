package com.example.academic_system.controllers;

import com.example.academic_system.repositories.ActivityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminActivityLogController {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @GetMapping("/admin/activity_log")
    public String viewActivityLog(Model model) {
        model.addAttribute("logList", activityLogRepository.findAll());
        return "admin/activity_log";
    }
}
