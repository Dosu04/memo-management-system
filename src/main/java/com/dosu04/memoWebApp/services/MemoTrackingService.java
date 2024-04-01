package com.dosu04.memoWebApp.services;

import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.MemoTracking;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.repositories.MemoRepository;
import com.dosu04.memoWebApp.repositories.MemoTrackingRepository;
import com.dosu04.memoWebApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service

public class MemoTrackingService {
    private final MemoTrackingRepository memoTrackingRepository;

    @Autowired
    public MemoTrackingService(MemoTrackingRepository memoTrackingRepository) {
        this.memoTrackingRepository = memoTrackingRepository;
    }
    public Set<User> getUsersWhoViewedMemo(Memo memo) {
        return memo.getMemoTrackings().stream()
                .map(MemoTracking::getUser)
                .collect(Collectors.toSet());
    }

    public void addUserToViewers(User user, Memo memo) {
        MemoTracking memoTracking = MemoTracking.createMemoTracking(user, memo);
        memo.getMemoTrackings().add(memoTracking);
        user.getMemoTrackings().add(memoTracking);
        memoTrackingRepository.save(memoTracking);
    }

}
