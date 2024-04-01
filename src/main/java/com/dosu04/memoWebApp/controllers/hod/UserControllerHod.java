package com.dosu04.memoWebApp.controllers.hod;

import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hod/lecturers")
public class UserControllerHod {

    private final UserService userService;

    @Autowired
    public UserControllerHod(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model, Authentication authentication) {

        User hod = (User) authentication.getPrincipal();
        String department = hod.getDepartment().getName();
        List<User> allUsers = userService.findAllUsers();
        List<User> departmentUsers = allUsers.stream()
                .filter(user -> department.equals(user.getDepartment().getName()))
                .collect(Collectors.toList());

        model.addAttribute("users", departmentUsers);
        model.addAttribute("department", department);

        return "hod/lecturers";
    }


    @GetMapping("/search")
    public String searchUsers(@RequestParam("keyword") String keyword, Model model, Authentication authentication) {

        User hod = (User) authentication.getPrincipal();
        String department = hod.getDepartment().getName();
        List<User> users = userService.searchUsers(keyword);
        List<User> departmentUsers = users.stream()
                .filter(user -> department.equals(user.getDepartment().getName()))
                .collect(Collectors.toList());

        model.addAttribute("users", departmentUsers);
        model.addAttribute("department", department);

        return "hod/lecturers";
    }

}
