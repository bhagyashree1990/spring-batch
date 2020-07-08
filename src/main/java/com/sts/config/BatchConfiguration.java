package com.sts.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.sts.model.Employee;
import com.sts.tasks.ConsoleItemWriter;
import com.sts.tasks.ValidationProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobFactory;
	
	@Autowired
	private StepBuilderFactory stepFactory;
	
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
	public ConsoleItemWriter<Employee> employeeWriter(){
		return new ConsoleItemWriter<>();
	}
	
	@Bean
	public ItemProcessor<Employee, Employee> employeeProcessor(){
		return new ValidationProcessor();
	}
	
	@Bean
	public Step readCSVJobStepOne() {
		return stepFactory.get("readJobStepOne").<Employee,Employee>chunk(5)
				.reader(employeeReader())
				.processor(employeeProcessor())
				.writer(employeeWriter())
				.build();
	}
	
	@Bean
	public Job readCSVJob() {
		return jobFactory.get("readJob").incrementer(new RunIdIncrementer())
				.start(readCSVJobStepOne())				
				.build();
	}		
}
