package com.baeldung.camunda.domain.repository;


import com.baeldung.camunda.domain.entity.Offer;
import com.baeldung.camunda.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

	List<Vote> findAllByOpened(boolean isOpened);

	List<Vote> findAllByOfferListContains(Offer offer);
}