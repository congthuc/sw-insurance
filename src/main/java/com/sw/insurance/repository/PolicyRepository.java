package com.sw.insurance.repository;

import com.sw.insurance.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Long> {
    @Query("SELECT p FROM Policy p WHERE p.personId = :personId AND p.status = 'ACTIVE'")
    List<Policy> findActivePoliciesByPersonId(@Param("personId") Long personId);
}
