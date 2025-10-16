package com.sw.insurance.repository;

import com.sw.insurance.model.PolicyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyDetailsRepository extends JpaRepository<PolicyDetails, Long> {
    
    /**
     * Find policy details by policy ID
     * @param policyId The ID of the policy
     * @return Optional containing the policy details if found
     */
    Optional<PolicyDetails> findByPolicyId(Long policyId);
    
    /**
     * Check if policy details exist for a given policy ID
     * @param policyId The ID of the policy
     * @return true if details exist, false otherwise
     */
    boolean existsByPolicyId(Long policyId);
}
