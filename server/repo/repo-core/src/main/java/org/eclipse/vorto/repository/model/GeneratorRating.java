/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.model;

public enum GeneratorRating {
	NONE,
	FAIR,
	GOOD,
	VERY_GOOD,
	EXCELLENT;

	public static GeneratorRating performRating(int invocationCount) {
		if (invocationCount == 0) {
			return NONE;
		} else if (invocationCount <= 10) {
			return FAIR;
		} else if (invocationCount > 10 && invocationCount <=20) {
			return GOOD;
		} else if (invocationCount > 20 && invocationCount <=40) {
			return VERY_GOOD;
		} else {
			return EXCELLENT;
		}
	}
}
