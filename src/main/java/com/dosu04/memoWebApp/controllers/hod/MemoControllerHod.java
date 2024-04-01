package com.dosu04.memoWebApp.controllers.hod;

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
@RequestMapping("/hod/memos")
public class MemoControllerHod {

    private final MemoService memoService;
    private final UserService userService;
    private final MemoTrackingService memoTrackingService;
    private final CommentService commentService;

    @Autowired
    public MemoControllerHod(MemoService memoService, UserService userService, CommentService commentService,
                              MemoTrackingService memoTrackingService) {
        this.memoService = memoService;
        this.userService = userService;
        this.commentService = commentService;
        this.memoTrackingService = memoTrackingService;

    }


    @GetMapping("/sent")
    public String listMemos(Model model, Authentication authentication) {
        User hod = (User) authentication.getPrincipal();
        String userDepartment = hod.getDepartment().getName();

        List<Memo> allMemos = memoService.findAllMemos();

        List<Memo> filteredMemos = allMemos.stream()
                .filter(memo -> isMemoForUser(memo, userDepartment))
                .collect(Collectors.toList());

        filteredMemos.sort(Comparator.comparing(Memo::getCreatedAt).reversed());

        model.addAttribute("memos", filteredMemos);
        return "hod/memo-management";
    }

    private boolean isMemoForUser(Memo memo, String userDepartment) {
        return (userDepartment.equals(memo.getSenderDepartment()));
    }



    @GetMapping("/inbox")
    public String listInboxMemos(Model model, Authentication authentication) {
            User user = (User) authentication.getPrincipal();
            String userDepartment = user.getDepartment().getName();
            String userFaculty = user.getFaculty().getName();

            List<Memo> allMemos = memoService.findAllMemos();

            List<Memo> filteredMemos = allMemos.stream()
                    .filter(memo -> isMemoForUser(memo, userDepartment, userFaculty))
                    .collect(Collectors.toList());

            filteredMemos.sort(Comparator.comparing(Memo::getCreatedAt).reversed());

            model.addAttribute("memos", filteredMemos);
            return "hod/inbox";
        }

        private boolean isMemoForUser(Memo memo, String userDepartment, String userFaculty) {
            return (userDepartment.equals(memo.getSenderDepartment()) || "General".equals(memo.getSenderDepartment()))
                    && (userFaculty.equals(memo.getSenderFaculty()) || "General".equals(memo.getSenderFaculty()));
        }



    @GetMapping("/view/{id}")
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
        model.addAttribute("viewers", viewers);return "hod/view-memo";
    }



    @GetMapping("/send")
    public String showAddMemoForm(Model model, Authentication authentication) {
        model.addAttribute("memo", new Memo());
        User hod = (User) authentication.getPrincipal();
        model.addAttribute("hodId", hod.getId());
        model.addAttribute("hodUsername", hod.getUsername());
        model.addAttribute("memoSenderDepartment", hod.getDepartment().getName());
        model.addAttribute("memoSenderFaculty", hod.getFaculty().getName());

        return "hod/add-memo";
    }



    @PostMapping("/send")
    public String processAddMemoForm(@ModelAttribute("memo") Memo memo,
                                     @RequestParam("memoSenderDepartment") String memoSenderDepartment,
                                     @RequestParam("memoSenderFaculty") String memoSenderFaculty) {
        memo.setSenderDepartment(memoSenderDepartment);
        memo.setSenderFaculty(memoSenderFaculty);

        memoService.saveMemo(memo);

        return "redirect:/hod/memos/sent";
    }




    @PostMapping("/delete/{id}")
    public String deleteMemo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        memoService.deleteMemo(id);
        redirectAttributes.addFlashAttribute("successMessage", "Memo deleted successfully.");
        return "redirect:/hod/memos/sent";
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
        return "hod/inbox";
    }

    @GetMapping("/sent/search")
    public String searchSentMemos(@RequestParam("keyword") String keyword, Model model, Authentication authentication) {

        User hod = (User) authentication.getPrincipal();
        String userDepartment = hod.getDepartment().getName();

        List<Memo> memos = memoService.searchMemos(keyword);

        List<Memo> filteredMemos = memos.stream()
                .filter(memo -> isMemoForUser(memo, userDepartment))
                .collect(Collectors.toList());

        filteredMemos.sort(Comparator.comparing(Memo::getCreatedAt).reversed());

        model.addAttribute("memos", filteredMemos);
        return "hod/memo-management";
    }
}
