package com.baeldung.application.repositories;

import com.baeldung.application.entities.Load;
import com.baeldung.application.entities.Promote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoteRepository extends CrudRepository<Promote,Long> {
    Promote findByIssueKeyAndLoad(String issueKey, Load load);
    List<Promote> findAllByIssueKey(String issueKey);

    @Query("SELECT a.issueKey,a.jobNum,a.status,a.load \n" +
            "FROM Promote AS a \n" +
            "JOIN Load AS b\n" +
            "ON a.load= b.promote\n" +
            "where a.jobNum='' and a.issueKey= ?1")
     List<Object> findAllByDscriptionQuery(String issueKey);
}
