package com.dosu04.memoWebApp.services;

import com.dosu04.memoWebApp.models.Comment;
import com.dosu04.memoWebApp.models.Memo;
import com.dosu04.memoWebApp.models.User;
import com.dosu04.memoWebApp.repositories.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public List<Memo> findAllMemos() {
        return memoRepository.findAll();
    }

    public Optional<Memo> findMemoById(Long id) {
        return memoRepository.findById(id);
    }

    public List<Memo> findRecentMemos(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        return memoRepository.findAll(pageable).getContent();
    }
    public void saveMemo(Memo memo) {
        memoRepository.save(memo);
    }

    public void deleteMemo(Long id) {
        memoRepository.deleteById(id);
    }

    public Memo getMemoById(Long memoId) {
        return memoRepository.findById(memoId).orElse(null);
    }

    public List<Memo> searchMemos(String keyword) {
        return memoRepository.findByTitleContainingIgnoreCase(keyword);
    }



}

