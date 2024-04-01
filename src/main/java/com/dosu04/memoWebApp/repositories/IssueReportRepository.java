package com.dosu04.memoWebApp.repositories;

import com.dosu04.memoWebApp.models.IssueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueReportRepository extends JpaRepository<IssueReport, Long> {

    List<IssueReport> findByTitleContainingIgnoreCase(String keyword);

}
