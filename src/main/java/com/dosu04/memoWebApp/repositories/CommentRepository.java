package com.dosu04.memoWebApp.repositories;

import com.dosu04.memoWebApp.models.Comment;

import com.dosu04.memoWebApp.models.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMemoId(Long memoId);


}
