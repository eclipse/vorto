package org.eclipse.vorto.server.devtool.utils;

import java.util.Comparator;
import java.util.Map;

import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryFileConstants;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;

public class ProjectResourceComparator implements Comparator<ProjectResource> {

	@Override
	public int compare(ProjectResource resourceOne, ProjectResource resourceTwo) {
		Map<String, String> propertiesOne = resourceOne.getProperties();
		Map<String, String> propertiesTwo = resourceTwo.getProperties();

		long creationDateOne = 0L;
		long creationDateTwo = 0L;

		if (propertiesOne.containsKey(ProjectRepositoryFileConstants.META_PROPERTY_CREATIONDATE)) {
			creationDateOne = Long
					.parseLong(propertiesOne.get(ProjectRepositoryFileConstants.META_PROPERTY_CREATIONDATE));
		}

		if (propertiesTwo.containsKey(ProjectRepositoryFileConstants.META_PROPERTY_CREATIONDATE)) {
			creationDateTwo = Long
					.parseLong(propertiesTwo.get(ProjectRepositoryFileConstants.META_PROPERTY_CREATIONDATE));
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
