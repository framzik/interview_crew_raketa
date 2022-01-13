package ru.khrebtov.crew_raketa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.khrebtov.crew_raketa.entity.Values;


public interface ValueRepo extends JpaRepository<Values, Long>, JpaSpecificationExecutor<Values> {
    @Modifying
    @Query(
            value = "truncate table values",
            nativeQuery = true
    )
    void removeAll();


}
