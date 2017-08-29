/**
 * Copyright (c) 2015-2017 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.server.devtool.utils;

import java.util.Comparator;
import java.util.Map;

import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;
import org.eclipse.vorto.devtool.projectrepository.utils.ProjectRepositoryConstants;

public class ProjectResourceComparator implements Comparator<ProjectResource> {

	@Override
	public int compare(ProjectResource resourceOne, ProjectResource resourceTwo) {
		Map<String, String> propertiesOne = resourceOne.getProperties();
		Map<String, String> propertiesTwo = resourceTwo.getProperties();

		long creationDateOne = 0L;
		long creationDateTwo = 0L;

		if (propertiesOne.containsKey(ProjectRepositoryConstants.META_PROPERTY_CREATIONDATE)) {
			creationDateOne = Long
					.parseLong(propertiesOne.get(ProjectRepositoryConstants.META_PROPERTY_CREATIONDATE));
		}

		if (propertiesTwo.containsKey(ProjectRepositoryConstants.META_PROPERTY_CREATIONDATE)) {
			creationDateTwo = Long
					.parseLong(propertiesTwo.get(ProjectRepositoryConstants.META_PROPERTY_CREATIONDATE));
		}

		if (creationDateOne > creationDateTwo) {
			return 1;
		} else if (creationDateOne < creationDateTwo) {
			return -1;
		} else {
			return 0;
		}
	}
}
