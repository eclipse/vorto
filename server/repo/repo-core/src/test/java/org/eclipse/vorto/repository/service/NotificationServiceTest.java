/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.ModelType;
import org.eclipse.vorto.repository.model.User;
import org.eclipse.vorto.repository.notification.EmailNotificationService;
import org.eclipse.vorto.repository.notification.message.CheckinMessage;
import org.eclipse.vorto.repository.notification.message.RegistrationMessage;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.subethamail.wiser.Wiser;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public class NotificationServiceTest {

	private static Wiser wiser;
	
	private static EmailNotificationService notificationService = new EmailNotificationService();
	
	static {
		notificationService.setNeedsAuth("false");
		notificationService.setSmtpHost("localhost");
		notificationService.setSmtpPort("2525");
		notificationService.setMailFrom("noreply@vorto.com");
	}
	
	@BeforeClass
	public static void startEmailServer() {
		wiser = new Wiser();
		wiser.setPort(2525);
		wiser.setHostname("localhost");
		wiser.start();
	}
	
	@AfterClass
	public static void stopEmailServer() {
		wiser.stop();
	}
	
	@Before
	public void clearMessages() {
		wiser.getMessages().clear();
	}
	
	@Test
	public void testSendRegistrationEmail() throws Exception {
		User user = new User();
		user.setEmail("alexander.edelmann@bosch-si.com");
		user.setUsername("aedelmann");
		user.setFirstName("Alexander");
		user.setLastName("Edelmann");
		
		notificationService.sendNotification(new RegistrationMessage(user));
		
		assertEquals(1,wiser.getMessages().size());
		assertTrue(((String)wiser.getMessages().get(0).getMimeMessage().getContent()).contains("registration"));
		assertTrue(((String)wiser.getMessages().get(0).getMimeMessage().getContent()).contains("Dear Alexander Edelmann"));
	}
	
	@Test
	public void testSendRegistrationEmailWithoutFirstAndLastName() throws Exception {
		User user = new User();
		user.setEmail("alexander.edelmann@bosch-si.com");
		user.setUsername("aedelmann");
		
		notificationService.sendNotification(new RegistrationMessage(user));
		
		assertEquals(1,wiser.getMessages().size());
		assertTrue(((String)wiser.getMessages().get(0).getMimeMessage().getContent()).contains("registration"));
		assertTrue(((String)wiser.getMessages().get(0).getMimeMessage().getContent()).contains("Dear aedelmann"));
	}
	
	@Test
	public void testSendCheckedModelEmail() throws Exception {
		User user = new User();
		user.setEmail("alexander.edelmann@bosch-si.com");
		user.setUsername("aedelmann");
		
		ModelResource resource = new ModelResource(new ModelId("Fridge","org.eclipse.vorto.examples","1.0.0"), ModelType.Functionblock);
		resource.setAuthor("andreas");
		resource.setCreationDate(new Date());
		resource.setDescription("A fridge keeps groceries fresh");
		notificationService.sendNotification(new CheckinMessage(user,resource));
		
		assertEquals(1,wiser.getMessages().size());
		assertTrue(((String)wiser.getMessages().get(0).getMimeMessage().getContent()).contains("A new model has just been checked into the Vorto Repository"));
		assertTrue(((String)wiser.getMessages().get(0).getMimeMessage().getContent()).contains("Dear aedelmann"));
	}
}
