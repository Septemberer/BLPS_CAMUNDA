package com.baeldung.camunda;

import com.baeldung.camunda.domain.entity.Offer;
import com.baeldung.camunda.domain.entity.Vote;
import com.baeldung.camunda.domain.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class WebInitializer extends SpringBootServletInitializer {

	private final VoteRepository voteRepository;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CamundaApplication.class);
	}

	@Scheduled(fixedDelay = 10000)
	private void getOpenedVotes() {
		List<Vote> voteList = getAllOpenedVotes();
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

	private List<Vote> getAllOpenedVotes() {
		return voteRepository.findAllByOpened(true);
	}
}