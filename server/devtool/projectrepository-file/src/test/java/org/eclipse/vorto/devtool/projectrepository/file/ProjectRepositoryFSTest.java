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
package org.eclipse.vorto.devtool.projectrepository.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.ProjectRepositoryTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for file system implementation of project repository.
 * 
 */
public class ProjectRepositoryFSTest extends ProjectRepositoryTest {

	static String projectsDirectory;
	
	@Override
	protected IProjectRepositoryService createService() {
		return new ProjectRepositoryServiceFS("projects");
	}

	@After
	public void deleteAllProjects() throws IOException {
		projectsDirectory = ((ProjectRepositoryServiceFS) repoService).getProjectsDirectory();
		deleteProjectsDirectory();
	}

	@Before
	public void createProjectsDirectory() throws IOException {
		projectsDirectory = ((ProjectRepositoryServiceFS) repoService).getProjectsDirectory();
		new File(projectsDirectory).mkdirs();
	}

	@AfterClass
	public static void deleteRootDirectory() throws IOException {
		FileUtils.deleteDirectory(new File(projectsDirectory));
	}

	// Unsupported features in ProjectRepositoryServiceFS
	@Override
	@Ignore
	@Test
	public void queryCreationDateAndPath() throws InterruptedException {
	}

	@Override
	@Ignore
	@Test
	public void queryCreationDateWrongDate() {
	}

	@Override
	@Ignore
	@Test
	public void queryByAuthorVersionTypeAndName() {
	}

	@Override
	@Ignore
	@Test
	public void queryByAuthorTypeAndNameWithoutVersion() {
	}

	@Override
	@Ignore
	@Test
	public void createVersion() {
	}

	@Override
	@Ignore
	@Test
	public void createVersionOfTwoProjects() {
	}

	@Override
	@Ignore
	@Test
	public void createVersionOfTwoProjectsAndDeleteVersion() {
	}

	@Override
	@Ignore
	@Test
	public void tagProject() {
	}

	@Override
	@Ignore
	@Test
	public void tagFolder() {
	}

	@Override
	@Ignore
	@Test
	public void tagFile() {
	}

	@Override
	@Ignore
	@Test
	public void lockUnlockAndCheckLockFProject() {
	}

	@Override
	@Ignore
	@Test
	public void lockUnlockAndCheckLockFolder() {
	}

	@Override
	@Ignore
	@Test
	public void lockUnlockAndCheckLockFFile() {
	}

	@Override
	@Ignore
	@Test
	public void doubleLockResource() {
	}

	@Override
	@Ignore
	@Test
	public void unlockUnlockedResource() {
	}

	private void deleteProjectsDirectory() throws IOException {
		FileUtils.deleteDirectory(new File(((ProjectRepositoryServiceFS) repoService).getProjectsDirectory()));
	}
}

/* EOF */
