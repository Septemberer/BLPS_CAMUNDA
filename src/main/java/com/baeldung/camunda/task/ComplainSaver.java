package com.baeldung.camunda.task;

import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.repository.CustomerRepository;
import com.baeldung.camunda.domain.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComplainSaver implements JavaDelegate {

	private final CustomerRepository customerRepository;

	private final ApartmentService apartmentService;

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {

		Long customerId = (Long) delegateExecution.getVariable("user_id");

		Customer thisUser = (Customer) delegateExecution.getVariable("currentUser");
		Customer anotherUser = customerRepository.getReferenceById(customerId);

		if (thisUser.isBanned() ||
				thisUser.isArchived() ||
				anotherUser.isBanned() ||
				anotherUser.isArchived() ||
				!thisUser.isProfessional()) {
			throw new BpmnError("permissionDenied");
		}

		anotherUser.incNegative();
		customerRepository.saveAndFlush(anotherUser);
		if (anotherUser.isBanned()) {
			clearBannedUser(anotherUser);
		}

	}

	private void clearBannedUser(Customer customer) {
		apartmentService.deleteAllByOwner(customer);
		apartmentService.unApprove(customer);
	}
}
