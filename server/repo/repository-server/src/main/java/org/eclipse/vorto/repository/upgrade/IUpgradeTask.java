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
package org.eclipse.vorto.repository.upgrade;

/**
 * An UpgradeTask is executed when the repository is started up.
 *
 */
public interface IUpgradeTask {

	/**
	 * Performs the actual upgrade task 
	 * @throws UpgradeProblem
	 */
	void doUpgrade() throws UpgradeProblem;
	
	/**
	 * @return a short description of the task being performed
	 */
	String getShortDescription();
	
	public class UpgradeProblem extends RuntimeException {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public UpgradeProblem(String msg, Throwable t) {
			super(msg, t);
		}
	}
}
