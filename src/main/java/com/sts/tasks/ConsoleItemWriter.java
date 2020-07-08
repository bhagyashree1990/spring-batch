package com.sts.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

public class ConsoleItemWriter<T> implements ItemWriter<T> {
	private static final Logger LOG = LoggerFactory.getLogger(ConsoleItemWriter.class);
	
	@Override
	public void write(List<? extends T> items) throws Exception {
		LOG.info("ConsoleWriter");
		for(T item : items) {
			LOG.info("{}",item);
		}		
	}

}
