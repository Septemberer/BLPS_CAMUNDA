package com.baeldung.camunda.task;

import com.baeldung.camunda.domain.entity.Apartment;
import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.entity.Offer;
import com.baeldung.camunda.domain.entity.Vote;
import com.baeldung.camunda.domain.repository.ApartmentRepository;
import com.baeldung.camunda.domain.repository.CustomerRepository;
import com.baeldung.camunda.domain.repository.OfferRepository;
import com.baeldung.camunda.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VoteSaver implements JavaDelegate {

	private final VoteRepository voteRepository;

	private final OfferRepository offerRepository;

	private final ApartmentRepository apartmentRepository;

	private final CustomerRepository customerRepository;

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {

		Customer customer = (Customer) delegateExecution.getVariable("currentUser");
		Long voteId = (Long) delegateExecution.getVariable("vote_id");
		Long votePrice = (Long) delegateExecution.getVariable("vote_price");

		Offer offer = new Offer();
		offer.setCustomer(customer);
		offer.setPrice(votePrice);
		offerRepository.saveAndFlush(offer);
		Vote vote = voteRepository.getReferenceById(voteId);
		if (!vote.isOpened()) {
			throw new BpmnError("voteFinished");
		}
		makeOffer(vote, offer, customer);

	}

	@Transactional
	public void makeOffer(Vote vote, Offer offer, Customer customer) {
		var list = vote.getOfferList();
		list.add(offer);
		if (list.size() >= 5) {
			Long price = 0L;
			for (Offer offer_ : list) {
				price += offer_.getPrice();
			}
			price = price / list.size();
			Apartment apartment = getApartmentByVote(vote);
			apartment.setPrice(price);
			apartment.setApproved(true);
			customer.incPositive();
			customerRepository.saveAndFlush(customer);
			apartmentRepository.save(apartment);
			vote.setOpened(false);
		}
		vote.setOfferList(list);
		voteRepository.saveAndFlush(vote);
	}

	private Apartment getApartmentByVote(Vote vote) {
		return apartmentRepository
				.findByVote(vote)
				.orElseThrow(() -> new BpmnError("BDError"));
	}
}
