package com.dosu04.memoWebApp.controllers.lecturer;

import com.dosu04.memoWebApp.models.Comment;
import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.services.CommentService;
import com.dosu04.memoWebApp.services.MemoService;
import com.dosu04.memoWebApp.services.MemoTrackingService;
import com.dosu04.memoWebApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("")
public class MemoControllerUser {

    private final MemoService memoService;
    private final UserService userService;
    private final MemoTrackingService memoTrackingService;
    private final CommentService commentService;

    @Autowired
    public MemoControllerUser(MemoService memoService, UserService userService, CommentService commentService,
                              MemoTrackingService memoTrackingService) {
        this.memoService = memoService;
        this.userService = userService;
        this.commentService = commentService;
        this.memoTrackingService = memoTrackingService;

    }

    // List all memos
    @GetMapping("/inbox")
    public String listMemos(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userDepartment = user.getDepartment().getName();
        String userFaculty = user.getFaculty().getName();

        // Fetch all memos
        List<Memo> allMemos = memoService.findAllMemos();

        List<Memo> filteredMemos = allMemos.stream()
                .filter(memo -> isMemoForUser(memo, userDepartment, userFaculty))
                .collect(Collectors.toList());

        filteredMemos.sort(Comparator.comparing(Memo::getCreatedAt).reversed());

        model.addAttribute("memos", filteredMemos);
        return "lecturer/inbox";
    }

    private boolean isMemoForUser(Memo memo, String userDepartment, String userFaculty) {
        return (userDepartment.equals(memo.getSenderDepartment()) || "General".equals(memo.getSenderDepartment()))
                && (userFaculty.equals(memo.getSenderFaculty()) || "General".equals(memo.getSenderFaculty()));
    }

    @GetMapping("/memo/{id}")
    public String viewMemo(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());

        Memo memo = memoService.findMemoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid memo ID: " + id));
        List<Comment> comments = commentService.getCommentsByMemoId(id);
        if (memo == null) {
            redirectAttributes.addFlashAttribute("errorMessageMemoNotFound", "Memo not found.");
            return "redirect:/admin/memos";
        }

        Set<User> viewers = memoTrackingService.getUsersWhoViewedMemo(memo);
        if (!viewers.contains(currentUser)) {
            memoTrackingService.addUserToViewers(currentUser, memo);
        }

        model.addAttribute("memo", memo);
        model.addAttribute("comments", comments);
        model.addAttribute("viewers", viewers);
        return "lecturer/view-memo";
    }


    @GetMapping("/inbox/search")
    public String searchInboxMemos(@RequestParam("keyword") String keyword, Model model, Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        String userDepartment = user.getDepartment().getName();
        String userFaculty = user.getFaculty().getName();

        List<Memo> memos = memoService.searchMemos(keyword);

        List<Memo> filteredMemos = memos.stream()
                .filter(memo -> isMemoForUser(memo, userDepartment, userFaculty))
                .collect(Collectors.toList());

        filteredMemos.sort(Comparator.comparing(Memo::getCreatedAt).reversed());

        model.addAttribute("memos", filteredMemos);
        return "lecturer/inbox";
    }
}
