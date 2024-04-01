package com.dosu04.memoWebApp.repositories;

import com.dosu04.memoWebApp.models.MemoTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoTrackingRepository extends JpaRepository<MemoTracking, Long> {

    List<MemoTracking> findByMemoId(Long memoId);

}
