/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.server.config.config;

import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.InMemoryTemporaryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Configuration
@EnableScheduling
public class UploadStorageConfiguration {

	@Bean
	public ITemporaryStorage createTempStorage() {
		return new InMemoryTemporaryStorage();
	}
	
	@Service
	public static class ScheduleTask {
	
		@Autowired
		private ITemporaryStorage storage;
	
		@Scheduled(fixedRate = 1000 * 60 * 60)
		public void clearExpiredStorageItems() {
			this.storage.clearExpired();
		}
	}
}
