package com.baeldung.camunda.task;

import com.baeldung.camunda.domain.entity.Customer;
import com.baeldung.camunda.domain.entity.UserInfo;
import com.baeldung.camunda.domain.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CredentialsChecker implements JavaDelegate {

	private final CustomerService customerService;

	@Override
	@Transactional
	public void execute(DelegateExecution delegateExecution) throws Exception {
		String username = (String) delegateExecution.getVariable("username");
		String password = (String) delegateExecution.getVariable("password");

		Customer customer = customerService.getUserByUserName(username, password);

		delegateExecution.setVariable("currentUser", customer);
		delegateExecution.setVariable("isProUser", customer.isProfessional());

		List<Customer> allUsers = customerService.getAllUsers();
		List<UserInfo> userInfoList = new ArrayList<>(allUsers.size());
		for (Customer user : allUsers) {
			if (user.isArchived()) {
				continue;
			}
			UserInfo userInfo = new UserInfo();
			userInfo.setId(user.getId());
			userInfo.setUsername(user.getUserName());
			userInfo.setProfessional(user.isProfessional());
			userInfo.setBanned(user.isBanned());
			userInfoList.add(userInfo);
		}
		delegateExecution.setVariable("userInfo", userInfoList);
	}
}
