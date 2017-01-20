package org.eclipse.vorto.server.devtool.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.service.impl.XtextEditorProjectService;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.Injector;

public class ProjectRepositoryServiceTest {
	
	@InjectMocks
	private IEditorProjectService projectRepositoryService = new XtextEditorProjectService();
		
	@Mock
	private IProjectRespositoryDAO projectRespositoryDAO;
	
	@Mock
	private Injector injector;
	
	@Mock
	private WebEditorResourceSetProvider webEditorResourceSetProvider;
	
	private String sessionId = "sessionId";
	private String projectName = "Project";
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test(expected = ProjectAlreadyExistsException.class)
	public void checkProjectExistsTest() throws ProjectAlreadyExistsException{
		when(injector.getInstance(IWebResourceSetProvider.class)).thenReturn(webEditorResourceSetProvider);
		when(projectRespositoryDAO.projectExists(projectName, sessionId)).thenReturn(true);
		projectRepositoryService.createProject(sessionId, projectName);
	}
	
	@Test(expected = ProjectNotFoundException.class)
	public void openNonExistantProjectTest() throws ProjectNotFoundException{
		when(projectRespositoryDAO.openProject(projectName, sessionId)).thenReturn(null);
		projectRepositoryService.openProject(sessionId, projectName);
	}
	
	@Test
	public void getProjectListTest() throws ProjectNotFoundException{
		assertNotNull(projectRepositoryService.getProjects(sessionId));
	}

}
