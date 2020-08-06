package com.sts.tasks;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sts.dao.EmployeeRepository;
import com.sts.model.Employee;
@Component
public class DBItemWriter implements ItemWriter<Employee> {
	private static final Logger LOG = LoggerFactory.getLogger(DBItemWriter.class);

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	@Transactional
	public void write(List<? extends Employee> employeeList) throws Exception {
		LOG.info("DBItemWriter: Count {}",employeeList.size());
		employeeRepository.saveAll(employeeList);
		LOG.info("DBItemWriter: {}",employeeList);
	}

}
