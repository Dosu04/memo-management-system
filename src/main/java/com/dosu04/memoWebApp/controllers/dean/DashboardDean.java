package com.dosu04.memoWebApp.controllers.dean;

import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.MemoService;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dean")
public class DashboardDean {

    private final UserService userService;
    private final MemoService memoService;

    @Autowired
    public DashboardDean(MemoService memoService, UserService userService) {
        this.memoService = memoService;
        this.userService = userService;
    }


//    @GetMapping
//    public String showDashboard(Model model, Authentication authentication) {
//        User dean = (User) authentication.getPrincipal();
//        String fullName = dean.getSurname() + " " + dean.getName() + " " + dean.getOtherName();
//        String faculty = dean.getFaculty().getName();
//        model.addAttribute("fullName", fullName);
//        model.addAttribute("faculty", faculty);
//
//        return "dean/dashboard";
//    }

    @GetMapping
    public String showDashboard(Model model, Authentication authentication) {
        User dean = (User) authentication.getPrincipal();
        String fullName = dean.getSurname() + " " + dean.getName() + " " + dean.getOtherName();
        String userDepartment = dean.getDepartment().getName();
        String userFaculty = dean.getFaculty().getName();

        model.addAttribute("fullName", fullName);
        model.addAttribute("faculty", userFaculty);


        List<Memo> allRecentMemos = memoService.findRecentMemos(5);
        List<Memo> filteredRecentMemos = allRecentMemos.stream()
                .filter(memo -> isMemoForUser(memo, userDepartment, userFaculty))
                .collect(Collectors.toList());

        model.addAttribute("recentMemos", filteredRecentMemos);
        return "dean/dashboard";
    }

    private boolean isMemoForUser(Memo memo, String userDepartment, String userFaculty) {
        return (userDepartment.equals(memo.getSenderDepartment()) || "General".equals(memo.getSenderDepartment()))
                && (userFaculty.equals(memo.getSenderFaculty()) || "General".equals(memo.getSenderFaculty()));
    }


}