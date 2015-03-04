package com.aestheticsw.jobkeywords.service.termextractor.repository;

import org.springframework.data.repository.CrudRepository;

import com.aestheticsw.jobkeywords.service.termextractor.domain.JobSummary;

public interface JobSummaryRepository extends CrudRepository<JobSummary, Long> {

}
