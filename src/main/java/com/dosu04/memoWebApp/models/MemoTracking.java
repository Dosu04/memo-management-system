package com.dosu04.memoWebApp.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "memo_tracking")
public class MemoTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;


    public static MemoTracking createMemoTracking(User user, Memo memo) {
        MemoTracking memoTracking = new MemoTracking();
        memoTracking.setUser(user);
        memoTracking.setMemo(memo);
        memoTracking.setViewedAt(LocalDateTime.now());
        return memoTracking;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, viewedAt);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MemoTracking that = (MemoTracking) obj;
        return Objects.equals(id, that.id) && Objects.equals(viewedAt, that.viewedAt);
    }
}
