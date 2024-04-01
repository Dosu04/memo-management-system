package com.dosu04.memoWebApp.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import java.util.HashSet;

import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Table(name = "memos")
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(nullable = false)
    private boolean isDraft = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(name="sender_department", nullable = true)
    private String senderDepartment;

    @Column(name="sender_faculty", nullable = true)
    private String senderFaculty;


    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "memo", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemoTracking> memoTrackings = new HashSet<>();



    public void userViewedMemo(User user) {
        MemoTracking memoTracking = MemoTracking.createMemoTracking(user, this);
        this.getMemoTrackings().add(memoTracking);
        user.getMemoTrackings().add(memoTracking);
    }


}
