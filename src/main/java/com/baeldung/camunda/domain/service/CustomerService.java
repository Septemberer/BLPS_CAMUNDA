package com.baeldung.camunda.domain.service;

import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;

	public Customer getUserByUserName(String username, String password) {
		var customerOpt = customerRepository.findByUserNameAndPassword(username, password);
		if (customerOpt.isPresent()) {
			return customerOpt.get();
		} else {
			throw new BpmnError("credentialsError");
		}
	}

	public List<Customer> getAllUsers() {
		return customerRepository.findAll();
	}
}
