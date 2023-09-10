package com.baeldung.camunda.task;

import com.baeldung.camunda.domain.entity.Apartment;
import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.entity.Vote;
import com.baeldung.camunda.domain.exceptions.BusinessException;
import com.baeldung.camunda.domain.repository.ApartmentRepository;
import com.baeldung.camunda.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAllOpenedVotes implements JavaDelegate {

	private final VoteRepository voteRepository;

	private final ApartmentRepository apartmentRepository;

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {

		Customer customer = (Customer) delegateExecution.getVariable("currentUser");

		if (customer == null || !customer.isProfessional() || customer.isBanned()) {
			throw new BpmnError("permissionDenied");
		}

		List<Vote> voteList = voteRepository.findAllByOpened(true);

		List<Apartment> apartments = voteList.stream().map(vote -> {
			try {
				return apartmentRepository
						.findByVote(vote)
						.orElseThrow(() -> new BusinessException("Некорректное состояние БД"));
			} catch (BusinessException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());

		delegateExecution.setVariable("openedVotes", voteList);
		delegateExecution.setVariable("apartmentsForOpenedVotes", apartments);
	}
}
