package com.example.academic_system.controllers;

import com.example.academic_system.repositories.ActivityLogRepository;
import com.example.academic_system.models.ActivityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class AdminActivityLogController {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @GetMapping("/admin/activity_log")
    public String viewActivityLog(Model model, @RequestParam(required = false) String search,
                                  @RequestParam(required = false) String filter) {
        List<ActivityLog> logs;

        if (search != null && !search.trim().isEmpty()) {
            logs = activityLogRepository.searchLogs(search.trim());
            model.addAttribute("searchKeyword", search);
        } else if (filter != null && !filter.trim().isEmpty()) {
            switch (filter.toUpperCase()) {
                case "CREATE":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("CREATE");
                    break;
                case "UPDATE":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("UPDATE");
                    break;
                case "DELETE":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("DELETE");
                    break;
                case "LOGIN":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("LOGIN");
                    break;
                case "LOGOUT":
                    logs = activityLogRepository.findByActionContainingIgnoreCaseOrderByTimestampDesc("LOGOUT");
                    break;
                default:
                    logs = activityLogRepository.findAllByOrderByTimestampDesc();
            }
            model.addAttribute("activeFilter", filter);
        } else {
            logs = activityLogRepository.findAllByOrderByTimestampDesc();
        }

        model.addAttribute("logList", logs);
        model.addAttribute("totalLogs", logs.size());

        // Add statistics
        long createCount = logs.stream().filter(log -> "CREATE".equals(log.getAction())).count();
        long updateCount = logs.stream().filter(log -> "UPDATE".equals(log.getAction())).count();
        long deleteCount = logs.stream().filter(log -> "DELETE".equals(log.getAction())).count();
        long loginCount = logs.stream().filter(log -> "LOGIN".equals(log.getAction())).count();
        long logoutCount = logs.stream().filter(log -> "LOGOUT".equals(log.getAction())).count();

        model.addAttribute("createCount", createCount);
        model.addAttribute("updateCount", updateCount);
        model.addAttribute("deleteCount", deleteCount);
        model.addAttribute("loginCount", loginCount);
        model.addAttribute("logoutCount", logoutCount);

        return "admin/activity_log";
    }

    @PostMapping("/admin/activity_log/search")
    public String searchActivityLog(@RequestParam String searchKeyword, Model model) {
        return "redirect:/admin/activity_log?search=" + searchKeyword;
    }

    @GetMapping("/admin/activity_log/filter")
    public String filterActivityLog(@RequestParam String action, Model model) {
        return "redirect:/admin/activity_log?filter=" + action;
    }
}