package com.dosu04.memoWebApp.controllers.hod;

import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.dosu04.memoWebApp.services.MemoService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hod")
public class DashboardHod {

    private final MemoService memoService;
    @Autowired
    public DashboardHod(MemoService memoService) {
        this.memoService = memoService;
    }


    @GetMapping
    public String showDashboard(Model model, Authentication authentication) {
        User hod = (User) authentication.getPrincipal();
        String fullName = hod.getSurname() + " " + hod.getName() + " " + hod.getOtherName();
        String userDepartment = hod.getDepartment().getName();
        String userFaculty = hod.getFaculty().getName();

        model.addAttribute("fullName", fullName);
        model.addAttribute("department", userDepartment);


        List<Memo> allRecentMemos = memoService.findRecentMemos(5);

        List<Memo> filteredRecentMemos = allRecentMemos.stream()
                .filter(memo -> isMemoForUser(memo, userDepartment, userFaculty))
                .collect(Collectors.toList());

        model.addAttribute("recentMemos", filteredRecentMemos);
        return "hod/dashboard";
    }

    private boolean isMemoForUser(Memo memo, String userDepartment, String userFaculty) {
        return (userDepartment.equals(memo.getSenderDepartment()) || "General".equals(memo.getSenderDepartment()))
                && (userFaculty.equals(memo.getSenderFaculty()) || "General".equals(memo.getSenderFaculty()));
    }


}