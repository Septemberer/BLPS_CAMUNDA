package com.baeldung.camunda.task;

import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserSaver implements JavaDelegate {

	private final CustomerRepository customerRepository;

	@Override
	@Transactional
	public void execute(DelegateExecution delegateExecution) throws Exception {

		String username = (String) delegateExecution.getVariable("username");
		String password = (String) delegateExecution.getVariable("password");
		String secret = (String) delegateExecution.getVariable("secret");

		Customer customer = new Customer();
		customer.setBanned(false);
		customer.setPassword(password);
		customer.setUserName(username);
		customer.setKarmaPositive(0);
		customer.setKarmaNegative(0);
		customer.setProfessional(Objects.equals(secret, "pro"));
		customerRepository.saveAndFlush(customer);
	}
}
