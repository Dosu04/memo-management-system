package com.dosu04.memoWebApp.controllers.admin;


import com.dosu04.memoWebApp.models.IssueReport;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.IssueReportService;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin/issues")
public class IssueReportControllerAdmin {

    private final IssueReportService issueReportService;
    private final UserService userService;

    @Autowired
    public IssueReportControllerAdmin(IssueReportService issueReportService, UserService userService) {
        this.issueReportService = issueReportService;
        this.userService = userService;
    }


    @GetMapping
    public String listIssues(Model model) {
        List<IssueReport> issues = issueReportService.getAllIssueReports();
        issues.sort(Comparator.comparing(IssueReport::getCreatedAt).reversed());
        model.addAttribute("issues", issues);
        return "admin/issue-management";
    }

    @GetMapping("/{issueId}")
    public String viewIssue(@PathVariable Long issueId, Model model) {
        IssueReport issue = issueReportService.getIssueReportById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid issueId: " + issueId));
        model.addAttribute("issue", issue);
        return "admin/view-issue";
    }

    @PostMapping("/delete/{issueId}")
    public String deleteIssue(@PathVariable Long issueId, RedirectAttributes redirectAttributes) {
        issueReportService.deleteIssueReport(issueId);
        redirectAttributes.addFlashAttribute("successMessageIssueReportDeleted", "Issue Report deleted successfully.");
        return "redirect:/admin/issues";
    }

    @GetMapping("/search")
    public String searchIssues(@RequestParam("keyword") String keyword, Model model) {
        List<IssueReport> issues = issueReportService.searchIssueReports(keyword);
        issues.sort(Comparator.comparing(IssueReport::getCreatedAt).reversed());
        model.addAttribute("issues", issues);
        return "admin/issue-management";
    }


}
