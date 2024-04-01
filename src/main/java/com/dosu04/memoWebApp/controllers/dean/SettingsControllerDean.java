package com.dosu04.memoWebApp.controllers.dean;

import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dean/settings")
public class SettingsControllerDean {

    @Autowired
    public SettingsControllerDean(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping("")
    public String showSettings(Model model, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();

        model.addAttribute("user", principal);

        model.addAttribute("username", principal.getUsername());
        model.addAttribute("password", "");
        model.addAttribute("surname", principal.getSurname());
        model.addAttribute("name", principal.getName());
        model.addAttribute("othername", principal.getOtherName());
        model.addAttribute("role", principal.getRoles());
        model.addAttribute("faculty", principal.getFaculty().getName());
        model.addAttribute("department", principal.getDepartment().getName());

        return "dean/profile";
    }

    @PostMapping("/update-password")
    public String updateSettings(@ModelAttribute("user") User updatedUser,
                                 RedirectAttributes redirectAttributes,
                                 Authentication authentication) {
        User principal = (User) authentication.getPrincipal();

        String newPassword = updatedUser.getPassword();
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            principal.setPassword(newPassword);
        }

        userService.saveUser(principal);


        redirectAttributes.addFlashAttribute("message", "Settings updated successfully.");

        return "redirect:/dean/settings";
    }
}
