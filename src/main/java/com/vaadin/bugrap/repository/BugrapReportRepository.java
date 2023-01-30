package com.vaadin.bugrap.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vaadin.bugrap.domain.entities.Project;
import org.vaadin.bugrap.domain.entities.Report;
import org.vaadin.bugrap.domain.entities.Reporter;

public interface BugrapReportRepository extends JpaRepository<Report, Long>{
	List<Report> findAllByProject(Project p);
    Page<Report> findAllByProject(Project p, Pageable pageable);
    List<Report> findAllByOrderByTimestampDesc();

    long countByProjectAndStatusIsNull(Project p);
    long countByProjectAndStatus(Project p, Report.Status s);
    long countByProjectAndStatusOrStatusIsNull(Project p, Report.Status s);
    long countByProjectAndStatusNot(Project p, Report.Status s);
    long countByProjectAndStatusNotAndStatusIsNotNull(Project p, Report.Status s);
    long countByProjectAndAssignedIsNull(Project p);
    long countByProjectAndAssigned(Project p, Reporter r);

    @Query("SELECT r FROM Report r WHERE (lower(r.summary) LIKE :term OR lower(r.description) LIKE ?1)")
    List<Report> searchByTerm(String term);
}
