package com.dosu04.memoWebApp.controllers.admin;


import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardAdmin<authentication> {

    private final UserService userService;

    @Autowired
    public DashboardAdmin(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String showDashboard(Model model, Authentication authentication) {
        long totalUsers = userService.countUsers();
        model.addAttribute("totalUsers", totalUsers);

        User admin = (User) authentication.getPrincipal();
        String fullName = admin.getSurname() + " " + admin.getName() + " " + admin.getOtherName();
        model.addAttribute("fullName", fullName);

        return "admin/dashboard";
    }
}
