package com.baeldung.camunda.task;

import com.baeldung.camunda.domain.entity.Apartment;
import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.entity.Vote;
import com.baeldung.camunda.domain.repository.ApartmentRepository;
import com.baeldung.camunda.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RequestOfferSaver implements JavaDelegate {

	private final VoteRepository voteRepository;
	private final ApartmentRepository apartmentRepository;

	@Override
	@Transactional
	public void execute(DelegateExecution delegateExecution) throws Exception {

		String apartmentAddress = (String) delegateExecution.getVariable("apartment_address");
		Long apartmentPrice = (Long) delegateExecution.getVariable("apartment_price");
		Long apartmentFloor = (Long) delegateExecution.getVariable("apartment_floor");
		Long apartmentRooms = (Long) delegateExecution.getVariable("apartment_rooms");
		Customer customer = (Customer) delegateExecution.getVariable("currentUser");

		Apartment apartment = new Apartment();
		apartment.setAddress(apartmentAddress);
		apartment.setPrice(apartmentPrice);
		apartment.setFloor(apartmentFloor);
		apartment.setRooms(apartmentRooms);
		apartment.setOwner(customer);
		if (customer.isProfessional()) {
			apartment.setApproved(true);
		} else {
			apartment.setApproved(false);
			Vote vote = new Vote();
			vote.setOpened(true);
			apartment.setVote(vote);
			voteRepository.saveAndFlush(vote);
		}
		apartmentRepository.saveAndFlush(apartment);
		delegateExecution.setVariable("currentApartmentOffer", apartment);
	}
}
