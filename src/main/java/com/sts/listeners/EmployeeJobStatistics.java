package com.sts.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sts.dao.EmployeeRepository;
@Component(value =  "employeeJobStatistics")
public class EmployeeJobStatistics implements JobExecutionListener {
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeJobStatistics.class);
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOG.info("Before JOB: {}",employeeRepository.count());
		
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOG.info("After JOB: {}",employeeRepository.count());
	}

}
