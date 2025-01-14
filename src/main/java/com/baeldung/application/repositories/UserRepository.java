package com.baeldung.application.repositories;

import com.baeldung.application.entities.Issue;
import com.baeldung.application.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    User findByIssue(Issue issue);

}
