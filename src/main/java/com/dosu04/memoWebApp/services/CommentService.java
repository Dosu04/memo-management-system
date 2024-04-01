package com.dosu04.memoWebApp.services;


import com.dosu04.memoWebApp.models.Comment;
import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemoService memoService;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, MemoService memoService, UserService userService) {
        this.commentRepository = commentRepository;
        this.memoService = memoService;
        this.userService = userService;
    }

    public void addComment(Long memoId, String content, String username) {
        Memo memo = memoService.getMemoById(memoId);
        User sender = userService.findByUsername(username);

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setMemo(memo);
        comment.setSender(sender);

        commentRepository.save(comment);
    }
    public List<Comment> findAllComments() {
        return commentRepository.findAll();
    }

    public List<Comment> getCommentsByMemoId(Long memoId) {
        return commentRepository.findByMemoId(memoId);
    }


    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + commentId));

        if (!comment.getSender().getUsername().equals(username)) {
            throw new IllegalArgumentException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }



}
