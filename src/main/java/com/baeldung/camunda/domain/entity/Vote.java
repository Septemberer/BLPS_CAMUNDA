package com.baeldung.camunda.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
public class Vote implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "vote_id", unique = true)
	private Long id;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "offer_ids")
	@ToString.Exclude
	private List<Offer> offerList = new ArrayList<>();

	@Column(name = "opened")
	private boolean opened = true;

}
