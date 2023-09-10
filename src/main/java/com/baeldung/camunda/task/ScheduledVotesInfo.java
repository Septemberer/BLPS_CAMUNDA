package com.baeldung.camunda.task;

import com.baeldung.camunda.domain.entity.Offer;
import com.baeldung.camunda.domain.entity.Vote;
import com.baeldung.camunda.domain.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ScheduledVotesInfo implements JavaDelegate {

	private final VoteRepository voteRepository;

	@Override
	public void execute(DelegateExecution delegateExecution) throws Exception {
		List<Vote> voteList = voteRepository.findAllByOpened(true);
		System.out.println("-----Opened Votes-----");
		for (Vote vote : voteList) {
			System.out.printf("ID: %s\n", vote.getId());
			System.out.printf("Players: %s\n", vote.getOfferList().size());
			int i = 1;
			for (Offer offer : vote.getOfferList()) {
				System.out.printf("%s. %s : %s\n", i, offer.getCustomer().getUserName(), offer.getPrice());
				i++;
			}
			System.out.println("+_+_+_+_+_+_+_+_+_+_+");
		}
	}
}
