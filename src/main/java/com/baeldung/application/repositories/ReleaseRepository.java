package com.baeldung.application.repositories;

import com.baeldung.application.entities.Load;
import com.baeldung.application.entities.Release;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends CrudRepository<Release,Long> {
    Release findByLoad(Load load);
}
