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
package org.eclipse.vorto.repository.account;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
public interface IUserAccountService {
	
	/**
	 * creates a new account in the Vorto Repository
	 * @param account
	 */
	void create(String username);
	
	/**
	 * @post Information Models that had been created by the user are made anonymous
	 * 
	 * Removes the account for the given user
	 * @param userId
	 */
	void delete(String userId);

	/**
	 * Checks if the account for the given user exists.
	 * @param userId
	 * @return
	 */
	boolean exists(String userId);
	
}
