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

import java.util.List;

import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.account.Role;
import org.eclipse.vorto.repository.account.UserAccount;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.notification.INotificationService;
import org.eclipse.vorto.repository.notification.message.DeleteAccountMessage;
import org.eclipse.vorto.repository.notification.message.RegistrationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Service
public class DefaultUserAccountService implements IUserAccountService{
	
	private static final String USER_ANONYMOUS = "anonymous";

	@Autowired
    private IUserRepository userRepository;
    
	@Autowired
	private INotificationService notificationService;
	
	@Autowired
	private IModelRepository modelRepository;
	
	public void create(UserAccount account){
		
		User user = new User();
		
		user.setUsername(account.getUsername());
		user.setPassword(account.getPassword());
		user.setHasWatchOnRepository(false);
		user.setEmail(account.getEmail());
		user.setRoles(Role.USER);
		     
		User registered = userRepository.save(user);	
		
		notificationService.sendNotification(new RegistrationMessage(registered));
	}
	

	@Override
	public void delete(final String userId) {
		User userToDelete = userRepository.findByUsername(userId);
		
		if (userToDelete != null) {
			
			makeModelsAnonymous(userToDelete.getUsername());
			
			userRepository.delete(userToDelete);
			notificationService.sendNotification(new DeleteAccountMessage(userToDelete));
		}
	}

	private void makeModelsAnonymous(String username) {
		List<ModelInfo> userModels = this.modelRepository.search("author:"+username);
		
		for (ModelInfo model : userModels) {
			model.setAuthor(USER_ANONYMOUS);
			this.modelRepository.updateMeta(model);
		}	
	}
	
	public INotificationService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(INotificationService notificationService) {
		this.notificationService = notificationService;
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
}
