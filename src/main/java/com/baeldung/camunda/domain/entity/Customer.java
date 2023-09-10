package com.baeldung.camunda.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "customer_id", unique = true)
	private Long id;

	@Column(name = "username")
	private String userName;

	@Column(name = "password")
	private String password;

	@ToString.Exclude
	@Column(name = "token")
	private String token;

	@Column(name = "karma_positive")
	private int karmaPositive = 0;

	@Column(name = "pro_user")
	private boolean professional = false;

	@Column(name = "karma_negative")
	private int karmaNegative = 0;

	@Column(name = "banned")
	private boolean banned = false;

	@Column(name = "archived")
	private boolean archived = false;

	public void incPositive() {
		karmaPositive++;
		if (karmaPositive >= 5 && !professional) {
			professional = true;
		}
	}

	public void incNegative() {
		karmaNegative++;
		if (karmaNegative >= 5 && !banned) {
			this.setBanned(true);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Customer)) {
			return false;
		}
		Customer customer = (Customer) o;
		return Objects.equals(getUserName(), customer.getUserName()) && Objects.equals(getPassword(), customer.getPassword());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserName(), getPassword());
	}

}
