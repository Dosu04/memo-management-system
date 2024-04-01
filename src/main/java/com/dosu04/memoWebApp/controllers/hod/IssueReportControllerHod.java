package com.dosu04.memoWebApp.controllers.hod;


import com.dosu04.memoWebApp.models.IssueReport;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.IssueReportService;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/hod/issues")
public class IssueReportControllerHod {

    private final IssueReportService issueReportService;
    private final UserService userService;

    @Autowired
    public IssueReportControllerHod(IssueReportService issueReportService, UserService userService) {
        this.issueReportService = issueReportService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String showCreateIssueReportForm(Model model, Principal principal) {
        String username = principal.getName();
        User authenticatedUser = userService.findByUsername(username);

        model.addAttribute("userId", authenticatedUser.getId());
        model.addAttribute("username", username);

        IssueReport issueReport = new IssueReport();
        issueReport.setSender(authenticatedUser);
        model.addAttribute("issueReport", issueReport);

        return "hod/report-issue";
    }


        @PostMapping("/add")
        public String addIssueReport(IssueReport issueReport, Principal principal, RedirectAttributes redirectAttributes) {
            issueReport.setSender(userService.findByUsername(principal.getName()));
            issueReportService.saveIssueReport(issueReport);
            redirectAttributes.addFlashAttribute("successMessageIssueReportAdded", "Issue Report sent successfully.");
            return "redirect:/hod";
        }





}
