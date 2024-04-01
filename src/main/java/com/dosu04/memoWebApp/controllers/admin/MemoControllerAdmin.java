package com.dosu04.memoWebApp.controllers.admin;

import com.dosu04.memoWebApp.models.Comment;
import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.MemoTracking;
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
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin/memos")
public class MemoControllerAdmin {
private final CommentService commentService;
    private final MemoService memoService;
    private final UserService userService;
    private final MemoTrackingService memoTrackingService;

    @Autowired
    public MemoControllerAdmin(MemoService memoService, CommentService commentService,  MemoTrackingService memoTrackingService,
                               UserService userService) {
        this.memoService = memoService;
        this.commentService = commentService;
        this.userService = userService;
        this.memoTrackingService = memoTrackingService;
    }

    @GetMapping
    public String listMemos(Model model) {
        List<Memo> memos = memoService.findAllMemos();
        memos.sort(Comparator.comparing(Memo::getCreatedAt).reversed());
        model.addAttribute("memos", memos);
        return "admin/memo-management";
    }


    @GetMapping("/view/{id}")
    public String viewMemo(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());

        Memo memo = memoService.findMemoById(id).orElse(null);
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

        return "admin/view-memo";
    }



    @GetMapping("/add")
    public String showAddMemoForm(Model model, Authentication authentication) {
        model.addAttribute("memo", new Memo());

        User admin = (User) authentication.getPrincipal();
        model.addAttribute("adminId", admin.getId());
        model.addAttribute("adminUsername", admin.getUsername());
        model.addAttribute("memoSenderDepartment", admin.getDepartment().getName());
        model.addAttribute("memoSenderFaculty", admin.getFaculty().getName());

        return "admin/add-memo";
    }

    @PostMapping("/add")
    public String processAddMemoForm(@ModelAttribute("memo") Memo memo,
                                     @RequestParam("memoSenderDepartment") String memoSenderDepartment,
                                     @RequestParam("memoSenderFaculty") String memoSenderFaculty,
                                     RedirectAttributes redirectAttributes) {
        memo.setSenderDepartment(memoSenderDepartment);
        memo.setSenderFaculty(memoSenderFaculty);
        memoService.saveMemo(memo);
        redirectAttributes.addFlashAttribute("successMessageMemoAdded", "Memo sent successfully.");
        return "redirect:/admin/memos";
    }


    @PostMapping("/delete/{id}")
    public String deleteMemo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        memoService.deleteMemo(id);
        redirectAttributes.addFlashAttribute("successMessageMemoDeleted", "Memo deleted successfully.");
        return "redirect:/admin/memos";
    }

    @GetMapping("/search")
    public String searchMemos(@RequestParam("keyword") String keyword, Model model) {
        List<Memo> memos = memoService.searchMemos(keyword);
        model.addAttribute("memos", memos);
        return "admin/memo-management";
    }

}
