/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.account.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class DefaultUserAccountService implements IUserAccountService {

	private static final String USER_ANONYMOUS = "anonymous";

	@Value("${server.admin:#{null}}")
	private String admins;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IModelRepository modelRepository;

	public void create(String username) {

		User user = new User();

		user.setUsername(username);
		user.setDateCreated(new Timestamp(System.currentTimeMillis()));
		user.setLastUpdated(new Timestamp(System.currentTimeMillis()));
		user.setAckOfTermsAndCondTimestamp(new Timestamp(System.currentTimeMillis()));
		user.setRole(toRole(username));

		user = userRepository.save(user);
		if (user != null) {
			refreshSpringSecurityUser(user);
		}
	}

	private void refreshSpringSecurityUser(User user) {
		// We only need to replace the authorities as that might be the only thing that changed
		OAuth2Authentication oauth2Auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

		UsernamePasswordAuthenticationToken oldAuth = (UsernamePasswordAuthenticationToken) oauth2Auth.getUserAuthentication();

		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(oldAuth.getPrincipal(),
				oldAuth.getCredentials(), AuthorityUtils.createAuthorityList("ROLE_" + user.getRole().toString()));
		newAuth.setDetails(oldAuth.getDetails());

		SecurityContextHolder.getContext().setAuthentication(new OAuth2Authentication(oauth2Auth.getOAuth2Request(), newAuth));
	}

	private Role toRole(String username) {
		if (admins != null && Arrays.asList(admins.split(";")).contains(username)) {
			return Role.ADMIN;
		}

		return Role.USER;
	}

	@Override
	public void delete(final String userId) {
		User userToDelete = userRepository.findByUsername(userId);

		if (userToDelete != null) {

			makeModelsAnonymous(userToDelete.getUsername());

			userRepository.delete(userToDelete);
		}
	}

	private void makeModelsAnonymous(String username) {
		List<ModelInfo> userModels = this.modelRepository.search("author:" + username);

		for (ModelInfo model : userModels) {
			model.setAuthor(USER_ANONYMOUS);
			this.modelRepository.updateMeta(model);
		}
	}

	public IUserRepository getUserRepository() {
		return userRepository;
	}

	public void setUserRepository(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public IModelRepository getModelRepository() {
		return modelRepository;
	}

	public void setModelRepository(IModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	@Override
	public boolean exists(String userId) {
		return userRepository.findByUsername(userId) != null;
	}
}
