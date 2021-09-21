package com.baeldung.application.repositories;

import com.baeldung.application.entities.Load;
import com.baeldung.application.entities.Promote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoteRepository extends JpaRepository<Promote,Long> {
    Promote findByLoad(Load load);

    @Modifying
    @Query("update Promote u set u.status = :status where u.id = :id")
    void updateStatus(@Param(value = "id") long id, @Param(value = "status") String status);
}
