package com.sts.tasks;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

public class DeleteFilesTask implements Tasklet{
	private static final Logger LOG = LoggerFactory.getLogger(DeleteFilesTask.class);
	
	private Resource directory;
	
	public DeleteFilesTask(Resource directory) {
		super();
		this.directory = directory;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		LOG.info("START DeleteFilesTask");
		File dir = directory.getFile();
		if(dir.isDirectory()) {
			File[] files = dir.listFiles();
			for(File file: files) {
				boolean deleted = file.delete();
				if(!deleted)
					throw new UnexpectedJobExecutionException("Could not delete file "+file.getPath());
				else
					LOG.info("{} is deleted",file.getPath());
			}
		}
		LOG.info("END DeleteFilesTask");
		return RepeatStatus.FINISHED;
	}

}
