package com.baeldung.camunda.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
public class Apartment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "apartment_id", unique = true)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer owner;

	@Column(name = "price")
	private Long price;

	@Column(name = "floor")
	private Long floor;

	@Column(name = "rooms")
	private Long rooms;

	@Column(name = "address")
	private String address;

	@Column(name = "approved")
	private boolean approved = false;

	@OneToOne
	@JoinColumn(name = "vote_id")
	private Vote vote;

}
