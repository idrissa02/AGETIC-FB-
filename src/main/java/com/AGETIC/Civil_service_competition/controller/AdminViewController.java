package com.AGETIC.Civil_service_competition.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {

    // Main admin dashboard (admin.html)
    @GetMapping("/admin")
    public String adminHome(Model model) {
        // you can prefill some counts later
        return "admin"; // -> templates/admin.html
    }

    // Create Exam page (admin-exam-create.html)
    @GetMapping("/admin/exams/create")
    public String createExamPage() {
        return "admin-exam-create";
    }

    // Manage Applications page (admin-applications-manage.html)
    @GetMapping("/admin/applications/manage")
    public String manageApplicationsPage() {
        return "admin-applications-manage";
    }

    // All Applications page (admin-applications.html)
    @GetMapping("/admin/applications")
    public String allApplicationsPage() {
        return "admin-applications";
    }
    
}
