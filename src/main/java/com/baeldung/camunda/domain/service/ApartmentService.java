package com.baeldung.camunda.domain.service;

import com.baeldung.camunda.domain.entity.Apartment;
import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.entity.Offer;
import com.baeldung.camunda.domain.entity.Vote;
import com.baeldung.camunda.domain.repository.ApartmentRepository;
import com.baeldung.camunda.domain.repository.OfferRepository;
import com.baeldung.camunda.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentService {

	private final ApartmentRepository apartmentRepository;

	private final OfferRepository offerRepository;

	private final VoteRepository voteRepository;

	@Transactional
	public void deleteAllByOwner(Customer customer) {
		apartmentRepository.deleteAllByOwner(customer);
	}

	@Transactional
	public void unApprove(Customer customer) {
		List<Offer> offerList = offerRepository.findAllByCustomer(customer);
		for (Offer offer : offerList) {
			List<Vote> voteList = voteRepository.findAllByOfferListContains(offer);
			for (Vote vote : voteList) {
				var list = vote.getOfferList();
				vote.setOfferList(
						list.stream().filter(x -> x.getCustomer() != customer).collect(Collectors.toList())
				);
				vote.setOpened(true);
				voteRepository.saveAndFlush(vote);
				var apartment = getApartmentByVote(vote);
				apartment.setApproved(false);
				apartmentRepository.saveAndFlush(apartment);
			}
			offerRepository.delete(offer);
		}
	}

	private Apartment getApartmentByVote(Vote vote) {
		return apartmentRepository
				.findByVote(vote)
				.orElseThrow(() -> new BpmnError("BDError"));
	}

}
