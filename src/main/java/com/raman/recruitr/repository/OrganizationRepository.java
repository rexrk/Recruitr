package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.entity.dto.response.OrganizationProjection;
import com.raman.recruitr.entity.dto.response.OrganizationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query("""
        select v.companyName as companyName,
               v.email as email,
               v.website as website,
               v.address as address,
               v.city as city,
               v.status as status
        from Organization org
        join org.vendors v
        where org.id = :id
    """)
    List<OrganizationProjection> findVendorsByOrgId(Long id);

    @Query("""
        select c.companyName as companyName,
               c.email as email,
               c.website as website,
               c.address as address,
               c.city as city,
               c.status as status
        from Organization org
        join org.clients c
        where org.id = :id
    """)
    List<OrganizationProjection> findClientsByOrgId(Long id);

}