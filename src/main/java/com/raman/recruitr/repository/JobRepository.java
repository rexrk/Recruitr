package com.raman.recruitr.repository;

import com.raman.recruitr.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
