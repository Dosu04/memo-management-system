package com.dosu04.memoWebApp.services;

import com.dosu04.memoWebApp.models.IssueReport;
import com.dosu04.memoWebApp.repositories.IssueReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IssueReportService {


    private final IssueReportRepository issueReportRepository;

    @Autowired
    public IssueReportService(IssueReportRepository issueReportRepository) {
        this.issueReportRepository = issueReportRepository;
    }

    public List<IssueReport> getAllIssueReports() {
        return issueReportRepository.findAll();
    }

    public Optional<IssueReport> getIssueReportById(Long id) {
        return issueReportRepository.findById(id);
    }

    public IssueReport saveIssueReport(IssueReport issueReport) {
        return issueReportRepository.save(issueReport);
    }

    public void deleteIssueReport(Long id) {
        issueReportRepository.deleteById(id);
    }

    public List<IssueReport> searchIssueReports(String keyword) {
        return issueReportRepository.findByTitleContainingIgnoreCase(keyword);
    }

}
