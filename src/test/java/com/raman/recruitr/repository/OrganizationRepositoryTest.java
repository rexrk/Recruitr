package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Organization;
import com.raman.recruitr.entity.dto.response.OrganizationProjection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class OrganizationRepositoryTest {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findVendorsByOrgId_shouldReturnVendors() {
        // given
        Organization org = new Organization();
        org.setCompanyName("Main Org");

        Organization vendor1 = new Organization();
        vendor1.setCompanyName("Vendor One");

        Organization vendor2 = new Organization();
        vendor2.setCompanyName("Vendor Two");

        org.getVendors().add(vendor1);
        org.getVendors().add(vendor2);

        entityManager.persist(vendor1);
        entityManager.persist(vendor2);
        entityManager.persist(org);
        entityManager.flush();

        // when
        List<OrganizationProjection> vendors = organizationRepository.findVendorsByOrgId(org.getId());

        // then
        assertThat(vendors).hasSize(2);
        assertThat(vendors).extracting(OrganizationProjection::getCompanyName)
                .containsExactlyInAnyOrder("Vendor One", "Vendor Two");
    }

    @Test
    void findVendorsByOrgId_shouldReturnEmpty_whenNoVendors() {
        // given
        Organization org = new Organization();
        org.setCompanyName("Org Without Vendors");

        entityManager.persist(org);
        entityManager.flush();

        // when
        List<OrganizationProjection> vendors = organizationRepository.findVendorsByOrgId(org.getId());

        // then
        assertThat(vendors).isEmpty();
    }

    @Test
    void findClientsByOrgId_shouldReturnClients() {
        // given
        Organization client = new Organization();
        client.setCompanyName("Client One");
        entityManager.persist(client);

        Organization org = new Organization();
        org.setCompanyName("Main Org");

        client.getVendors().add(org);
        org.getClients().add(client);
        entityManager.persist(org);

        entityManager.flush();
        entityManager.clear();

        // when
        List<OrganizationProjection> clients = organizationRepository.findClientsByOrgId(org.getId());

        // then
        assertThat(clients).hasSize(1);
        assertThat(clients.getFirst().getCompanyName()).isEqualTo("Client One");
    }

    @Test
    void findClientsByOrgId_shouldReturnEmpty_whenNoClients() {
        // given
        Organization org = new Organization();
        org.setCompanyName("Lonely Org");

        entityManager.persist(org);
        entityManager.flush();

        // when
        List<OrganizationProjection> clients = organizationRepository.findClientsByOrgId(org.getId());

        // then
        assertThat(clients).isEmpty();
    }

    @Test
    void findVendorIdsByOrganizationId_shouldReturnVendorIds() {
        // given
        Organization org = new Organization();
        org.setCompanyName("Main Org");

        Organization vendor = new Organization();
        vendor.setCompanyName("VendorX");

        org.getVendors().add(vendor);

        entityManager.persist(vendor);
        entityManager.persist(org);
        entityManager.flush();

        // when
        Set<Long> vendorIds = organizationRepository.findVendorIdsByOrganizationId(org.getId());

        // then
        assertThat(vendorIds).containsExactly(vendor.getId());
    }

    @Test
    void findVendorIdsByOrganizationId_shouldReturnEmpty_whenNoVendors() {
        // given
        Organization org = new Organization();
        org.setCompanyName("No Vendor Org");

        entityManager.persist(org);
        entityManager.flush();

        // when
        Set<Long> vendorIds = organizationRepository.findVendorIdsByOrganizationId(org.getId());

        // then
        assertThat(vendorIds).isEmpty();
    }
}
