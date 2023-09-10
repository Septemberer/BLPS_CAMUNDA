package com.baeldung.camunda.task;

import com.baeldung.camunda.domain.entity.Apartment;
import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.entity.Vote;
import com.baeldung.camunda.domain.repository.ApartmentRepository;
import com.baeldung.camunda.domain.repository.CustomerRepository;
import com.baeldung.camunda.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CheckApproveAndSaveOffer implements JavaDelegate {

	private final ApartmentRepository apartmentRepository;

	private final CustomerRepository customerRepository;

	private final VoteRepository voteRepository;

	@Override
	@Transactional
	public void execute(DelegateExecution delegateExecution) throws Exception {

		Boolean isApproved = (Boolean) delegateExecution.getVariable("isApproved");
		Apartment apartment = (Apartment) delegateExecution.getVariable("currentApartmentOffer");
		Customer customer = (Customer) delegateExecution.getVariable("currentUser");

		if (isApproved) {
			apartment.setApproved(true);
			Vote vote = apartment.getVote();
			apartment.setVote(null);
			voteRepository.delete(vote);
			apartmentRepository.saveAndFlush(apartment);
			customer.incPositive();
			customerRepository.saveAndFlush(customer);
		}

	}
}
