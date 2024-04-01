package com.dosu04.memoWebApp.controllers.hod;

import com.dosu04.memoWebApp.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/hod/memos/view/{memoId}")
public class CommentControllerHod {
    private final CommentService commentService;

    @Autowired
    public CommentControllerHod(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add-comment")
    public String addComment(@PathVariable Long memoId, @RequestParam String content, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            commentService.addComment(memoId, content, username);
            redirectAttributes.addFlashAttribute("successMessageCommentAdded", "Comment sent successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessageComment", "Error adding comment: " + e.getMessage());
        }
        return "redirect:/hod/memos/view/{memoId}";

    }


    @PostMapping("/delete-comment/{commentId}")
    public String deleteComment(@PathVariable Long commentId, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String username = principal.getName();
            commentService.deleteComment(commentId, username);
            redirectAttributes.addFlashAttribute("successMessageCommentDeleted", "Comment deleted successfully.");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("errorMessageComment", "Invalid comment ID: " + commentId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessageComment", "Error deleting comment: " + e.getMessage());
        }
        return "redirect:/hod/memos/view/{memoId}";
    }


}
