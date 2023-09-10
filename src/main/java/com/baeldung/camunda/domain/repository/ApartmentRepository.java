package com.baeldung.camunda.domain.repository;


import com.baeldung.camunda.domain.entity.Apartment;
import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

	List<Apartment> findAllByPriceBetweenAndFloorAndRooms(Long minPrice,
														  Long maxPrice,
														  Integer floor,
														  Integer rooms);

	List<Apartment> findAllByOwner(Customer owner);

	List<Apartment> findAllByApprovedIsTrue();

	List<Apartment> findAllByApprovedIsFalse();

	void deleteAllByOwner(Customer customer);

	Optional<Apartment> findByVote(Vote vote);
}