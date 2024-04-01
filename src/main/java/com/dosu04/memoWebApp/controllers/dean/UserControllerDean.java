package com.dosu04.memoWebApp.controllers.dean;

import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dean/lecturers")
public class UserControllerDean {

    private final UserService userService;

    @Autowired
    public UserControllerDean(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model, Authentication authentication) {
        User dean = (User) authentication.getPrincipal();
        String faculty = dean.getFaculty().getName();

        List<User> allUsers = userService.findAllUsers();
        List<User> facultyUsers = allUsers.stream()
                .filter(user -> faculty.equals(user.getFaculty().getName()))
                .collect(Collectors.toList());

        model.addAttribute("users", facultyUsers);
        model.addAttribute("faculty", faculty);

        return "dean/lecturers";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("keyword") String keyword, Model model, Authentication authentication) {
        User dean = (User) authentication.getPrincipal();
        String faculty = dean.getFaculty().getName();

        List<User> users = userService.searchUsers(keyword);
        List<User> facultyUsers = users.stream()
                .filter(user -> faculty.equals(user.getFaculty().getName()))
                .collect(Collectors.toList());

        model.addAttribute("users", facultyUsers);
        model.addAttribute("faculty", faculty);

        return "dean/lecturers";
    }



}
