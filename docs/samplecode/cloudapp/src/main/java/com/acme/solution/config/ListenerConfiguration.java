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
package com.acme.solution.config;

import javax.jms.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.SimpleMessageListenerContainer;
import com.acme.solution.service.AMQPListener;

@Configuration
public class ListenerConfiguration {

	@Value("${amqp.queue}")
	private String msgQueue;
	@Value("${session.cache.size:1}")
	private int sessionCacheSize;
	
	@Autowired
	private ConnectionFactory qpidConnectionFactory;

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setTargetConnectionFactory(qpidConnectionFactory);
		cachingConnectionFactory.setSessionCacheSize(sessionCacheSize);
		return cachingConnectionFactory;
	}

	@Bean
	AMQPListener receiver() {
		return new AMQPListener();
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setMessageListener(receiver());
		container.setConnectionFactory(connectionFactory);
		container.setDestinationName(msgQueue);
		return container;
	}

}
