package com.baeldung.application.repositories;


import com.baeldung.application.entities.Load;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadRepository extends CrudRepository<Load, Long> {
    List<Load> findByIssueKey(String issueKey);
}
