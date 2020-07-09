package com.sts.config;


import javax.annotation.Resource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.sts.model.Employee;
import com.sts.tasks.DBItemWriter;
import com.sts.tasks.DeleteFilesTask;
import com.sts.tasks.Task2;
import com.sts.tasks.ValidationProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobFactory;
	
	@Autowired
	private StepBuilderFactory stepFactory;
	
	@Value(value = "${app.tasklet.input.file-path}")
	private String directory;
	
	@Autowired
	private ValidationProcessor validationProcessor;
	
	@Autowired
	private DBItemWriter dbItemWriter;
	
	@Resource(name = "employeeJobStatistics")
	private JobExecutionListener jobExecutionListener;
	
	@Bean
	public FlatFileItemReader<Employee> employeeReader(){
		FlatFileItemReader<Employee> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("employee.csv"));
		reader.setLinesToSkip(1);//Use it if file has header rows.
		reader.setLineMapper(lineMapper());
		return reader;
	}
	
	@Bean
	public LineMapper<Employee> lineMapper(){
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames("id","firstName","lastName","gender");
		
		BeanWrapperFieldSetMapper<Employee> mapper = new BeanWrapperFieldSetMapper<>();
		mapper.setTargetType(Employee.class);
		
		DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(mapper);
		
		return lineMapper;
	}
	
	@Bean
	public Step readCSVJobStepOne() {
		return stepFactory.get("readJobStepOne").<Employee,Employee>chunk(5)
				.reader(employeeReader())
				.processor(validationProcessor)
				.writer(dbItemWriter)				
				.build();
	}
	
	@Bean
	public Job readCSVJob() {
		return jobFactory.get("readJob")
				.incrementer(new RunIdIncrementer())
				.listener(jobExecutionListener)
				.start(readCSVJobStepOne())				
				.build();
	}	
	
	
	@Bean
	public Step demoJobStepOne() {
		return stepFactory.get("stepOne").tasklet(new DeleteFilesTask(new FileSystemResource(directory))).build();
	}

	@Bean
	public Step demoJobStepTwo() {
		return stepFactory.get("stepTwo").tasklet(new Task2()).build();
	}

	@Bean
	public Job demoJob() {
		return jobFactory.get("demoJob").incrementer(new RunIdIncrementer())
				.start(demoJobStepOne())
				.next(demoJobStepTwo())
				.build();
	}
}
