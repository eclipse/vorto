/**
 * Copyright (c) 2015-2018 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.init;

import java.util.stream.Stream;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AddTechnicalUserInit implements ApplicationListener<ApplicationReadyEvent> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserAccountService userAccountService;
	
	@Value("${oauth2.verification.eidp.technicalUsers:}")
	private String[] ciamTechnicalUsers;
	
	@Value("${oauth2.verification.keycloak.technicalUsers:}")
	private String[] keycloakTechnicalUsers;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		Stream.concat(Stream.of(ciamTechnicalUsers), Stream.of(keycloakTechnicalUsers)).forEach(user -> {
			if (!userAccountService.exists(user)) {
				LOGGER.info("Creating technical user: {}", user);
				userAccountService.create(user);
			}
		});
	}
}
