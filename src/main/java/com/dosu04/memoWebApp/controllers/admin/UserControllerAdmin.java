package com.dosu04.memoWebApp.controllers.admin;

import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class UserControllerAdmin {

    private final UserService userService;

    @Autowired
    public UserControllerAdmin(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {

        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);

        return "admin/user-management";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.findAllRoles());
        model.addAttribute("departments", userService.findAllDepartments());
        model.addAttribute("faculties", userService.findAllFaculties());
        return "admin/add-user";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessageUserAdded", "User has been added successfully.");
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("roles", userService.findAllRoles());
            model.addAttribute("departments", userService.findAllDepartments());
            model.addAttribute("faculties", userService.findAllFaculties());
            return "admin/edit-user";
        } else {
            redirectAttributes.addFlashAttribute("errorMessageUserNotFound", "User not found.");
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User updatedUser, RedirectAttributes redirectAttributes) {
        User existingUser = userService.findUserById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setSurname(updatedUser.getSurname());
            existingUser.setName(updatedUser.getName());
            existingUser.setOtherName(updatedUser.getOtherName());
            existingUser.setFaculty(updatedUser.getFaculty());
            existingUser.setDepartment(updatedUser.getDepartment());
            existingUser.setRoles(updatedUser.getRoles());

            String newPassword = updatedUser.getPassword();
            if (newPassword != null && !newPassword.trim().isEmpty()) {
                existingUser.setPassword(newPassword);
            }

            userService.updateUser(existingUser);

            redirectAttributes.addFlashAttribute("successMessageUserUpdated", "User has been updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessageUserNotFound", "User not found.");
        }

        return "redirect:/admin/users";
    }


    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessageUserDeleted", "User has been deleted successfully.");
        return "redirect:/admin/users";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("keyword") String keyword, Model model) {
        List<User> users = userService.searchUsers(keyword);
        model.addAttribute("users", users);
        return "admin/user-management";
    }




}
