package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Job;
import com.raman.recruitr.entity.JobAssignment;
import com.raman.recruitr.entity.Organization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class JobAssignmentRepositoryTest {

    @Autowired
    private JobAssignmentRepository jobAssignmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllByJobId_shouldReturnAssignments() {
        // given
        Organization org = new Organization();
        org.setCompanyName("Main Org");
        entityManager.persist(org);

        Job job = new Job();
        job.setTitle("Backend Developer");
        job.setOrganization(org);
        job.setExperienceLevel(Job.ExperienceLevel.SENIOR);
        entityManager.persist(job);

        JobAssignment ja1 = new JobAssignment();
        ja1.setJob(job);
        ja1.setStatus(JobAssignment.ApplicationStatus.SELECTED);
        entityManager.persist(ja1);

        JobAssignment ja2 = new JobAssignment();
        ja2.setJob(job);
        ja2.setStatus(JobAssignment.ApplicationStatus.REJECTED);
        entityManager.persist(ja2);

        entityManager.flush();

        // when
        List<JobAssignment> assignments = jobAssignmentRepository.findAllByJobId(job.getId());

        // then
        assertThat(assignments).hasSize(2)
                .extracting(JobAssignment::getStatus)
                .containsExactlyInAnyOrder(JobAssignment.ApplicationStatus.SELECTED, JobAssignment.ApplicationStatus.REJECTED);
    }

    @Test
    void findAllByJobId_shouldReturnEmpty_whenNoAssignments() {
        // given
        Organization org = new Organization();
        org.setCompanyName("Main Org");
        entityManager.persist(org);

        Job job = new Job();
        job.setExperienceLevel(Job.ExperienceLevel.LEAD);
        job.setTitle("Ai took your job");
        job.setOrganization(org);
        entityManager.persist(job);
        entityManager.flush();

        // when
        List<JobAssignment> assignments = jobAssignmentRepository.findAllByJobId(job.getId());

        // then
        assertThat(assignments).isEmpty();
    }

    @Test
    void findByIdAndJobOrganizationId_shouldReturnAssignment() {
        // given
        Organization org = new Organization();
        org.setCompanyName("Org One");
        entityManager.persist(org);

        Job job = new Job();
        job.setTitle("Frontend Developer");
        job.setExperienceLevel(Job.ExperienceLevel.JUNIOR);
        job.setOrganization(org);
        entityManager.persist(job);

        JobAssignment ja = new JobAssignment();
        ja.setJob(job);
        entityManager.persist(ja);
        entityManager.flush();

        // when
        Optional<JobAssignment> result = jobAssignmentRepository.findByIdAndJobOrganizationId(
                ja.getId(), org.getId()
        );

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(JobAssignment.ApplicationStatus.APPLIED);
    }

    @Test
    void findByIdAndJobOrganizationId_shouldReturnEmpty_whenOrgMismatch() {
        // given
        Organization org1 = new Organization();
        org1.setCompanyName("Org One");
        entityManager.persist(org1);

        Organization org2 = new Organization();
        org2.setCompanyName("Org Two");
        entityManager.persist(org2);

        Job job = new Job();
        job.setTitle("DevOps Engineer");
        job.setOrganization(org1);
        job.setExperienceLevel(Job.ExperienceLevel.SENIOR);
        entityManager.persist(job);

        JobAssignment ja = new JobAssignment();
        ja.setJob(job);
        entityManager.persist(ja);
        entityManager.flush();

        // when
        Optional<JobAssignment> result = jobAssignmentRepository.findByIdAndJobOrganizationId(
                ja.getId(), org2.getId()
        );

        // then
        assertThat(result).isEmpty();
    }
}
