package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Candidate;
import com.raman.recruitr.entity.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestEntityManager entityManager;

    // ---------------------- findAllByOrganizationId ----------------------
    @Test
    void findAllByOrganizationId_shouldReturnCandidates() {
        Organization org = new Organization();
        org.setCompanyName("Org A");
        entityManager.persist(org);

        Candidate c1 = new Candidate();
        c1.setFirstName("Alice");
        c1.setLastName("Smith");
        c1.setEmail("alice@example.com");
        c1.setOrganization(org);
        entityManager.persist(c1);

        Candidate c2 = new Candidate();
        c2.setFirstName("Bob");
        c2.setLastName("Johnson");
        c2.setEmail("bob@example.com");
        c2.setOrganization(org);
        entityManager.persist(c2);

        entityManager.flush();

        Page<Candidate> page = candidateRepository.findAllByOrganizationId(org.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(2)
                .extracting(Candidate::getFirstName)
                .containsExactlyInAnyOrder("Alice", "Bob");
    }

    // ---------------------- findAllByOrganizationIds ----------------------
    @Test
    void findAllByOrganizationIds_shouldReturnCandidates() {
        Organization org1 = new Organization();
        org1.setCompanyName("Org 1");
        Organization org2 = new Organization();
        org2.setCompanyName("Org 2");
        entityManager.persist(org1);
        entityManager.persist(org2);

        Candidate c1 = new Candidate();
        c1.setFirstName("Charlie");
        c1.setLastName("Brown");
        c1.setEmail("charlie@example.com");
        c1.setOrganization(org1);

        Candidate c2 = new Candidate();
        c2.setFirstName("David");
        c2.setLastName("Williams");
        c2.setEmail("david@example.com");
        c2.setOrganization(org2);

        entityManager.persist(c1);
        entityManager.persist(c2);
        entityManager.flush();

        Page<Candidate> page = candidateRepository.findAllByOrganizationIds(
                Set.of(org1.getId(), org2.getId()), PageRequest.of(0, 10)
        );

        assertThat(page.getContent()).hasSize(2)
                .extracting(Candidate::getFirstName)
                .containsExactlyInAnyOrder("Charlie", "David");
    }

    // ---------------------- findByIdAndOrganizationId ----------------------
    @Test
    void findByIdAndOrganizationId_shouldReturnCandidate() {
        Organization org = new Organization();
        org.setCompanyName("Org X");
        entityManager.persist(org);

        Candidate c = new Candidate();
        c.setFirstName("Eve");
        c.setLastName("Davis");
        c.setEmail("eve@example.com");
        c.setOrganization(org);
        entityManager.persist(c);
        entityManager.flush();

        Optional<Candidate> result = candidateRepository.findByIdAndOrganizationId(c.getId(), org.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Eve");
    }

    // ------------------findValidCandidatesForJobAssignment----------------------
    @Test
    void findValidCandidatesForJobAssignment_shouldReturnCandidates() {
        Organization org = new Organization();
        org.setCompanyName("Org Main");
        Organization vendor = new Organization();
        vendor.setCompanyName("VendorOrg");
        entityManager.persist(org);
        entityManager.persist(vendor);

        // establish vendor relationship
        org.getVendors().add(vendor);

        Candidate c1 = new Candidate();
        c1.setFirstName("Fay");
        c1.setLastName("Miller");
        c1.setEmail("fay@example.com");
        c1.setOrganization(org);

        Candidate c2 = new Candidate();
        c2.setFirstName("Gus");
        c2.setLastName("Taylor");
        c2.setEmail("gus@example.com");
        c2.setOrganization(vendor);

        entityManager.persist(c1);
        entityManager.persist(c2);
        entityManager.flush();

        List<Candidate> validCandidates = candidateRepository.findValidCandidatesForJobAssignment(
                Set.of(c1.getId(), c2.getId()), org.getId()
        );

        assertThat(validCandidates).hasSize(2)
                .extracting(Candidate::getFirstName)
                .containsExactlyInAnyOrder("Fay", "Gus");
    }

    // ---------------------- searchCandidatesBySkills
    @Test
    void searchCandidatesBySkills_shouldReturnMatchingCandidates() {
        Organization org = new Organization();
        org.setCompanyName("Org Skills");
        Organization vendor = new Organization();
        vendor.setCompanyName("Vendor Skills");
        entityManager.persist(org);
        entityManager.persist(vendor);
        org.getVendors().add(vendor);

        Candidate c1 = new Candidate();
        c1.setFirstName("Hank");
        c1.setLastName("hank");
        c1.setEmail("hank@example.com");
        c1.setOrganization(org);
        c1.getPrimarySkills().add("java");
        c1.getPrimarySkills().add("spring");

        Candidate c2 = new Candidate();
        c2.setFirstName("Walter");
        c2.setLastName("White");
        c2.setEmail("walter@email.com");
        c2.setOrganization(vendor);
        c2.getPrimarySkills().add("java");

        Candidate c3 = new Candidate();
        c3.setFirstName("skyler");
        c3.setLastName("Black");
        c3.setEmail("skyler@example.com");
        c3.setOrganization(org);
        c3.getPrimarySkills().add("python");

        entityManager.persist(c1);
        entityManager.persist(c2);
        entityManager.persist(c3);
        entityManager.flush();

        List<Candidate> results = candidateRepository.searchCandidatesBySkills(
                Set.of("java"), org.getId()
        );

        assertThat(results).hasSize(2)
                .extracting(Candidate::getFirstName)
                .containsExactlyInAnyOrder("Hank", "Walter");
    }
}
