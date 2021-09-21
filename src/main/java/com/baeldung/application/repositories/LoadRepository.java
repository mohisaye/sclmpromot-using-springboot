package com.baeldung.application.repositories;


import com.baeldung.application.entities.Issue;
import com.baeldung.application.entities.Load;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadRepository extends JpaRepository<Load, Long> {
    List<Load> findByIssues(Issue issue);
    Load findByLoadName(String loadName);
    List<Load> findByIssuesInAndLoadName(List<Issue> issue,String loadName);
    List<Load> findAllByIssuesAndReleaseTurn(Issue issue,String releaseTurn);
}
