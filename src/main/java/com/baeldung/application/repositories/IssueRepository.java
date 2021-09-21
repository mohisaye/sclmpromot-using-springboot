package com.baeldung.application.repositories;

import com.baeldung.application.entities.Issue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends CrudRepository<Issue,Long> {
   List<Issue> findByFormNumber(String formNumber);
   Issue findByIssueKeyAndFormNumber(String issueKey,String formNumber);
   Issue findByIssueKey(String issueKey);
}
