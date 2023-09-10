package com.baeldung.camunda.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserInfo implements Serializable {

	private Long id;
	private String username;
	private Boolean professional;
	private Boolean banned;

}
