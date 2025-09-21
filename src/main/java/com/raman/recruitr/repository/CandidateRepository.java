package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    Page<Candidate> findAllByOrganizationId(Long orgId, Pageable pageable);

    @Query("select c from Candidate c join c.organization org where org.id in :orgIds")
    Page<Candidate> findAllByOrganizationIds(@Param("orgIds") Set<Long> orgIds, Pageable pageable);

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

    @Query("""
        SELECT DISTINCT c FROM Candidate c
        JOIN c.primarySkills s
        WHERE LOWER(s) IN :skills
          AND (
              c.organization.id = :orgId
              OR c.organization.id IN (
                  SELECT v.id FROM Organization o
                  JOIN o.vendors v
                  WHERE o.id = :orgId
              )
          )
    """)
    List<Candidate> searchCandidatesBySkills(@Param("skills") Set<String> skills,
                                             @Param("orgId") Long orgId);
}
