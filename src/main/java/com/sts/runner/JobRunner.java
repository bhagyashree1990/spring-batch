package com.sts.runner;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class JobRunner implements CommandLineRunner{

	private static final Logger LOG = LoggerFactory.getLogger(JobRunner.class);
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Resource(name = "readCSVJob")
	private Job readCSVJob;
	
	@Resource(name = "sequentialStepsJob")
	private Job sequentialStepsJob;
	
	@Resource(name = "parallelFlowJob")
	private Job parallelFlowJob;
	
	@Override
	public void run(String... args) throws Exception {
		LOG.info("INVOKED JobRunner");
		
		JobParameters csvJobParameters = new JobParametersBuilder()
					.addString("JobID", String.valueOf(System.currentTimeMillis()))
					.toJobParameters();
		jobLauncher.run(readCSVJob, csvJobParameters);
		
		JobParameters sequentialJobParameters = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(sequentialStepsJob, sequentialJobParameters);
		
		JobParameters parallelJobParameters = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(parallelFlowJob, parallelJobParameters);
		
	}

}
