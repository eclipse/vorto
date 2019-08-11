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
package com.acme.solution;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GeolocationApp {

	private static final Logger LOG = LoggerFactory.getLogger(GeolocationApp.class);
	
	@Value(value = "${amqp.url}")
	private String url;
	
	@PostConstruct
    private void start() throws Exception {
        LOG.info("Started listening for telemetry payload from "+url+"...");
    }
	
	public static void main(final String[] args) {
        SpringApplication.run(GeolocationApp.class, args);
    }
}
