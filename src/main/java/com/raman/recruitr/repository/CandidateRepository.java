package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    List<Candidate> findAllByOrganizationId(Long orgId);

    @Query("select c from Candidate c join c.organization org where org.id in :orgIds")
    List<Candidate> findAllByOrganizationIds(@Param("orgIds") Set<Long> orgIds);

    Optional<Candidate> findByIdAndOrganizationId(Long candidateId, Long organizationId);

    @Query("""
    SELECT c FROM Candidate c
    WHERE c.id IN :candidateIds
      AND (c.organization.id = :orgId
           OR c.organization.id IN (
               SELECT v.id FROM Organization o
               LEFT JOIN o.vendors v
               WHERE o.id = :orgId
           ))
    """)
    List<Candidate> findValidCandidatesForJobAssignment(
            @Param("candidateIds") Set<Long> candidateIds,
            @Param("orgId") Long orgId
    );
}
