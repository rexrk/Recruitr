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
class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findAllByOrganizationId_shouldReturnJobs() {
        Organization org = new Organization();
        org.setCompanyName("Main Org");
        entityManager.persist(org);

        Job job1 = new Job();
        job1.setTitle("Java Developer");
        job1.setOrganization(org);
        job1.setExperienceLevel(Job.ExperienceLevel.LEAD);
        entityManager.persist(job1);

        Job job2 = new Job();
        job2.setTitle("Spring Boot Developer");
        job2.setOrganization(org);
        job2.setExperienceLevel(Job.ExperienceLevel.MID);
        entityManager.persist(job2);

        entityManager.flush();

        List<Job> jobs = jobRepository.findAllByOrganizationId(org.getId());

        assertThat(jobs).hasSize(2);
        assertThat(jobs).extracting(Job::getTitle)
                .containsExactlyInAnyOrder("Java Developer", "Spring Boot Developer");
    }

    @Test
    void findByIdAndOrganizationId_shouldReturnJob() {
        Organization org = new Organization();
        org.setCompanyName("Main Org");
        entityManager.persist(org);

        Job job = new Job();
        job.setTitle("Backend Engineer");
        job.setOrganization(org);
        job.setExperienceLevel(Job.ExperienceLevel.JUNIOR);
        entityManager.persist(job);
        entityManager.flush();

        Optional<Job> result = jobRepository.findByIdAndOrganizationId(job.getId(), org.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Backend Engineer");
    }

    @Test
    void findByIdAndOrganizationId_shouldReturnEmptyWhenOrgMismatch() {
        Organization org1 = new Organization();
        org1.setCompanyName("Org One");
        entityManager.persist(org1);

        Organization org2 = new Organization();
        org2.setCompanyName("Org Two");
        entityManager.persist(org2);

        Job job = new Job();
        job.setTitle("Frontend Engineer");
        job.setOrganization(org1);
        job.setExperienceLevel(Job.ExperienceLevel.MID);
        entityManager.persist(job);
        entityManager.flush();

        Optional<Job> result = jobRepository.findByIdAndOrganizationId(job.getId(), org2.getId());

        assertThat(result).isEmpty();
    }

    @Test
    void findByIdAndOrganizationIdAndStatus_shouldReturnJob() {
        Organization org = new Organization();
        org.setCompanyName("Main Org");
        entityManager.persist(org);

        Job job = new Job();
        job.setTitle("DevOps Engineer");
        job.setOrganization(org);
        job.setExperienceLevel(Job.ExperienceLevel.LEAD);
        job.setStatus(Job.JobStatus.OPEN);
        entityManager.persist(job);
        entityManager.flush();

        Optional<Job> result = jobRepository.findByIdAndOrganizationIdAndStatus(
                job.getId(), org.getId(), Job.JobStatus.OPEN);

        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo(Job.JobStatus.OPEN);
    }

    @Test
    void countByOrganizationId_shouldReturnCorrectCount() {
        Organization org = new Organization();
        org.setCompanyName("Main Org");
        entityManager.persist(org);

        Job job1 = new Job();
        job1.setTitle("QA Engineer");
        job1.setOrganization(org);
        job1.setExperienceLevel(Job.ExperienceLevel.LEAD);
        entityManager.persist(job1);

        Job job2 = new Job();
        job2.setTitle("Data Engineer");
        job2.setOrganization(org);
        job2.setExperienceLevel(Job.ExperienceLevel.JUNIOR);
        entityManager.persist(job2);

        entityManager.flush();

        long count = jobRepository.countByOrganizationId(org.getId());

        assertThat(count).isEqualTo(2);
    }

    @Test
    void countJobsWithSelectedAssignments_shouldReturnCount() {
        Organization org = new Organization();
        org.setCompanyName("Main Org");
        entityManager.persist(org);

        Job job = new Job();
        job.setTitle("AI Engineer");
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

        long count = jobRepository.countJobsWithSelectedAssignments(org.getId());

        assertThat(count).isEqualTo(1); // only one job has a SELECTED assignment
    }
}
