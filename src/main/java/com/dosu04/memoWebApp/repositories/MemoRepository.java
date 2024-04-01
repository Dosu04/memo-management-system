package com.dosu04.memoWebApp.repositories;

import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
//    List<Memo> findByIsDraft(boolean isDraft);

    List<Memo> findByTitleContainingIgnoreCase(String title);


}

