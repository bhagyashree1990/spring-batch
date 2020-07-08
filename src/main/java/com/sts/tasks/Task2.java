package com.sts.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class Task2 implements Tasklet{
	private static final Logger LOG = LoggerFactory.getLogger(Task2.class);
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOG.info("START Task2");
		//CUSTOM CODE HERE
		LOG.info("END Task2");
		return RepeatStatus.FINISHED;
	}
	
}
