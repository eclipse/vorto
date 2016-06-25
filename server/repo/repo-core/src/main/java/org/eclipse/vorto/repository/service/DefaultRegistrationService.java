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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.repository.model.Role;
import org.eclipse.vorto.repository.model.User;
import org.eclipse.vorto.repository.model.UserDto;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.RegistrationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class DefaultRegistrationService implements IRegistrationService{
	
	@Autowired
    private UserRepository userRepository;
    
	@Autowired
	private INotificationService notificationService;
	
	public INotificationService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(INotificationService notificationService) {
		this.notificationService = notificationService;
	}
	
	public void registerUser(UserDto userDto){
		
		User user = new User();
		
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setUsername(userDto.getUsername().toLowerCase());
		user.setPassword(userDto.getPassword());
		user.setHasWatchOnRepository(false);
		user.setEmail(userDto.getEmail());
		user.setRoles(Role.USER);
		     
		User registered = userRepository.save(user);			
		notifyUser(registered);	
	}
	
	public void notifyUser(User user){
		Map<String,Object> context = new HashMap<String, Object>(1);
		context.put("user", user);
		notificationService.sendNotification(new RegistrationMessage(user));
	}
}
