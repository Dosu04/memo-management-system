package com.dosu04.memoWebApp.controllers.lecturer;

import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class DashboardUser {


    private final MemoService memoService;

    @Autowired
    public DashboardUser(MemoService memoService) {
        this.memoService = memoService;
    }

    @GetMapping("")
    public String dashboard(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userDepartment = user.getDepartment().getName();
        String userFaculty = user.getFaculty().getName();

        String fullName = user.getSurname() + " " + user.getName() + " " + user.getOtherName();
        model.addAttribute("fullName", fullName);
        model.addAttribute("department", userDepartment);

        List<Memo> allRecentMemos = memoService.findRecentMemos(5);


        List<Memo> filteredRecentMemos = allRecentMemos.stream()
                .filter(memo -> isMemoForUser(memo, userDepartment, userFaculty))
                .collect(Collectors.toList());

        model.addAttribute("recentMemos", filteredRecentMemos);
        return "lecturer/dashboard";
    }

    private boolean isMemoForUser(Memo memo, String userDepartment, String userFaculty) {
        return (userDepartment.equals(memo.getSenderDepartment()) || "General".equals(memo.getSenderDepartment()))
                && (userFaculty.equals(memo.getSenderFaculty()) || "General".equals(memo.getSenderFaculty()));
    }
}
