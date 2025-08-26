package com.AGETIC.Civil_service_competition.controller.web;

import com.AGETIC.Civil_service_competition.dto.ApplicationResponse;
import com.AGETIC.Civil_service_competition.service.ApplicationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class InscriptionsController {

   private final ApplicationService applicationService;

    public InscriptionsController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * Page: /mes-inscriptions?ninaNumber=XXXX
     * Binds Page<ApplicationResponse> as "apps" for inscriptions.html when a NINA is provided.
     */
    @GetMapping("/mes-inscriptions")
    public String myApplications(@RequestParam(required = false) String ninaNumber,
                                 @PageableDefault(size = 10) Pageable pageable,
                                 Model model) {

        // keep the typed value in the input (your template uses ${param.ninaNumber})
        // model.addAttribute("ninaNumber", ninaNumber); // optional

        if (StringUtils.hasText(ninaNumber)) {
            Page<ApplicationResponse> page = applicationService.myApplications(ninaNumber.trim(), pageable);
            model.addAttribute("apps", page);
        }
        return "inscriptions"; // -> templates/inscriptions.html
    }
}
