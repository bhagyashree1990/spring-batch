package com.sts.tasks;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sts.dao.EmployeeRepository;
import com.sts.model.Employee;
@Component
public class ValidationProcessor implements ItemProcessor<Employee, Employee> {

	private static final Logger LOG = LoggerFactory.getLogger(ValidationProcessor.class);

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Override
	public Employee process(Employee employee) throws Exception {
		if(employee.getGender() == null) {
			LOG.info("Missing gender information : {}",employee.getId());
		}else if(employee.getGender().trim().length()>1) {
			LOG.info("Invalid gender information : {}",employee.getId());
		}else {
			boolean valid = false;
			switch (employee.getGender()) {
			case "M":
			case "m":
			case "F":
			case "f":
				valid=true;
				break;				
			default:
				LOG.info("Invalid gender information : {}",employee.getId());
				break;
			}
			if(valid) {
				Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());
				if(optionalEmployee.isPresent()) {
					return null;
				}
				return employee;
			}
		}
		return null;
	}

}
