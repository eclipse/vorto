package org.eclipse.vorto.server.devtool.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.vorto.server.devtool.models.Project;
import org.junit.Test;
import org.mockito.InjectMocks;

public class ProjectRepositoryDAOTest {
		
	@InjectMocks
	private MapProjectRespositoryDAOImpl projectRepositoryDAO = new MapProjectRespositoryDAOImpl();
	
	private String sessionId = "sessionId";
	private String projectName = "Project";
	
	@Test
	public void uniqueProjectNameTest(){
		Project project1 = new Project(projectName);
		projectRepositoryDAO.createProject(project1, sessionId);
		assertEquals(1, projectRepositoryDAO.getProjects(sessionId).size());
		Project project2 = new Project(projectName);
		projectRepositoryDAO.createProject(project2, sessionId);
		assertEquals(1, projectRepositoryDAO.getProjects(sessionId).size());
	}
	
	@Test
	public void checkProjectExistsTest(){
		Project project1 = new Project(projectName);
		projectRepositoryDAO.createProject(project1, sessionId);
		assertTrue(projectRepositoryDAO.projectExists(projectName, sessionId));
		assertFalse(projectRepositoryDAO.projectExists(Long.toString(System.currentTimeMillis()), sessionId));
	}
	
	
	@Test
	public void openProjectTest(){
		Project project1 = new Project(projectName);
		projectRepositoryDAO.createProject(project1, sessionId);
		assertNotNull(projectRepositoryDAO.openProject(projectName, sessionId));
		assertNull((projectRepositoryDAO.openProject(Long.toString(System.currentTimeMillis()), sessionId)));
	}
	
	@Test
	public void getProjectsTest(){
		Project project1 = new Project(projectName);
		projectRepositoryDAO.createProject(project1, sessionId);
		assertEquals(1, projectRepositoryDAO.getProjects(sessionId).size());
		assertEquals(projectName, projectRepositoryDAO.getProjects(sessionId).get(0).getProjectName());
	}
	
}
