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
package org.eclipse.vorto.devtool.projectrepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryServiceFS;
import org.eclipse.vorto.devtool.projectrepository.model.FileResource;
import org.eclipse.vorto.devtool.projectrepository.model.FileUploadHandle;
import org.eclipse.vorto.devtool.projectrepository.model.FolderResource;
import org.eclipse.vorto.devtool.projectrepository.model.FolderUploadHandle;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectUploadHandle;
import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceContent;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceType;
import org.eclipse.vorto.devtool.projectrepository.query.NotExistingResourceError;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Abstract JUnit test class for project repository
 * 
 */
public class ProjectRepositoryFSTest {

	protected IProjectRepositoryService repoService;

	private static String projectsDirectory;

	@Before
	public void setUp() {
		this.repoService = new ProjectRepositoryServiceFS("projects");
	}

	@After
	public void deleteAllProjects() throws IOException {
		projectsDirectory = ((ProjectRepositoryServiceFS) repoService).getProjectsDirectory();
		deleteProjectsDirectory();
	}
	
	private void deleteProjectsDirectory() throws IOException {
		FileUtils.deleteDirectory(new File(((ProjectRepositoryServiceFS) repoService).getProjectsDirectory()));
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
	
	@Test
	public void createProject() {
		ProjectResource project = createTestProject("myProject");
		assertEquals("myProject", project.getName());
		assertNull(project.getAuthor());
		assertNotNull(project.getCreationDate());
		assertNull(project.getParent());
		assertEquals("myProject",project.getPath());
		assertEquals(2,project.getProperties().size());
		assertEquals(ResourceType.ProjectResource,project.getType());
	}

    @Test
    public void createProjectWithProperties() {
        Map<String,String> properties = new HashMap<String, String>();
        properties.put("city","singapore");
        ProjectResource project = repoService.createProject("myProject",properties, "JUnit test for creating a project");
        assertEquals(3,project.getProperties().size());
        assertEquals("singapore",project.getProperties().get("city"));
    }
    
    @Test
    public void queryByProperty() {
    	 Map<String,String> props1 = new HashMap<String, String>();
         props1.put("city","singapore");
         repoService.createProject("myProject",props1, "JUnit test for creating a project");
         
         Map<String,String> props2 = new HashMap<String, String>();
         props2.put("city","berlin");
         repoService.createProject("myProject2",props2, "JUnit test for creating a project");
         
         Map<String,String> props3 = new HashMap<String, String>();
         props3.put("city","berlin");
         repoService.createProject("myProject3",props2, "JUnit test for creating a project");
         
         assertEquals(2,repoService.createQuery().property("city","berlin").list().size());
         assertEquals("myProject2",repoService.createQuery().property("city","berlin").list().get(0).getName());
         assertEquals("myProject3",repoService.createQuery().property("city","berlin").list().get(1).getName());
    }

    @Test
    public void changeProjectProperties() {
        Map<String,String> properties = new HashMap<String, String>();
        properties.put("city","singapore");
        ProjectResource project = repoService.createProject("myProject",properties, "JUnit test for creating a project");
        assertEquals("singapore",project.getProperties().get("city"));

        ProjectUploadHandle handle = new ProjectUploadHandle("myProject");
        properties.put("city","boston");
        handle.setProperties(properties);
        repoService.uploadResource("changed to boston",handle);
        project = (ProjectResource)repoService.createQuery().name("myProject").type(ResourceType.ProjectResource).singleResult();
        assertEquals("boston",project.getProperties().get("city"));
    }

	@Test
	public void queryAndDeleteProject() {
		String projectName = "myDeletableProject";
		ProjectResource project = repoService.createProject(projectName,
				"JUnit test for creating a project");

		assertEquals(projectName, project.getName());
		repoService.deleteResource(project);
		assertNull(repoService.createQuery().name(projectName).singleResult());
	}

	@Test
	public void addMultipleFilesAndFoldersToProject() {
		ProjectResource project = createTestProject("myProject");

		FileUploadHandle handle1 = new FileUploadHandle(project,"someFile",
				"content".getBytes(), null);
		FileUploadHandle handle2 = new FileUploadHandle(project,
				"someOtherFile", "content".getBytes(), null);
		FolderUploadHandle handle3 = new FolderUploadHandle(project,
				"someFolder");

		repoService.uploadResource("someComment", handle1, handle2, handle3);

		List<Resource> results = repoService.createQuery()
				.pathLike("myProject").list();
		assertEquals(4, results.size());
	}

	@Test(expected = ResourceAlreadyExistsError.class)
	public void createDuplicateProject() {
		String name = "createResourceTest";
		String commitMessage = "JUnit test for creating a Project";
		repoService.createProject(name, commitMessage);
		repoService.createProject(name, commitMessage);
	}

	@Test
	public void addFolderToProject() {
		ProjectResource project = repoService.createProject("someProject", "commitMessage");

		FolderUploadHandle handle = new FolderUploadHandle(project,
				"someFolder");
		repoService.uploadResource("someComment", handle);

		Resource result = repoService.createQuery()
				.path("someProject/someFolder").singleResult();
		assertTrue(result.getPath().contains("someFolder"));
		assertEquals(ResourceType.FolderResource,result.getType());
	}

    @Test
    public void addFolderWithPropertiesToProject() {
        ProjectResource project = repoService.createProject("someProject","commitMessage");

        FolderUploadHandle handle = new FolderUploadHandle(project,
                "someFolder");
        Map<String,String> properties = new HashMap<String, String>();
        properties.put("author","alex");
        handle.setProperties(properties);
        repoService.uploadResource("someComment", handle);

        Resource result = repoService.createQuery()
                .path("someProject/someFolder").singleResult();
        assertTrue(result.getPath().contains("someFolder"));
		assertEquals(ResourceType.FolderResource,result.getType());
        assertEquals(2, result.getProperties().size());
    }

	@Test
	public void addFileWithPropertiesToProject() {
		ProjectResource project = repoService.createProject("someProject","commitMessage");

		FileUploadHandle handle = new FileUploadHandle(project,"someFile",
				"content".getBytes(), null);
        Map<String,String> properties = new HashMap<String, String>();
        properties.put(Resource.META_PROPERTY_AUTHOR,"alex");
        handle.setProperties(properties);
		repoService.uploadResource("someComment", handle);

		Resource result = repoService.createQuery()
				.path("someProject/someFile").singleResult();
		assertEquals(ResourceType.FileResource,result.getType());
		assertEquals(2, result.getProperties().size());
		ResourceContent content = repoService
				.getResourceContent((FileResource) result);
		assertTrue(Arrays.equals("content".getBytes(), content.getContent()));
        
	}

    @Test
    public void addFileToProject() {
        ProjectResource project = repoService.createProject("someProject", "commitMessage");

        FileUploadHandle handle = new FileUploadHandle(project,"someFile",
                "content".getBytes(), null);
        repoService.uploadResource("someComment", handle);

        Resource result = repoService.createQuery()
                .path("someProject/someFile").singleResult();
		assertEquals(ResourceType.FileResource,result.getType());

        ResourceContent content = repoService
                .getResourceContent((FileResource) result);
        assertTrue(Arrays.equals("content".getBytes(), content.getContent()));

    }

    @Test
    public void addFilesWithPropertiesToProject() {
        ProjectResource project = repoService.createProject("someProject","commitMessage");

        FileUploadHandle handle = new FileUploadHandle(project,"someFile",
                "content".getBytes(), null);
        Map<String,String> properties = new HashMap<String, String>();
        properties.put("author","alex");
        handle.setProperties(properties);
        repoService.uploadResource("someComment", handle);

        FileUploadHandle handle2 = new FileUploadHandle(project,"anotherFile",
                "content".getBytes(), null);
        Map<String,String> properties2 = new HashMap<String, String>();
        properties2.put("author","stefan");
        handle2.setProperties(properties2);
        repoService.uploadResource("someComment", handle2);

        Resource result = repoService.createQuery()
                .path("someProject/someFile").singleResult();
        assertEquals(FileResource.class, result.getClass());

        ResourceContent content = repoService
                .getResourceContent((FileResource) result);
        assertTrue(Arrays.equals("content".getBytes(), content.getContent()));
        assertEquals("alex", result.getProperties().get("author"));

        result = repoService.createQuery()
                .path("someProject/anotherFile").singleResult();
        assertEquals(FileResource.class, result.getClass());

        content = repoService
                .getResourceContent((FileResource) result);
        assertTrue(Arrays.equals("content".getBytes(), content.getContent()));
        assertEquals("stefan", result.getProperties().get("author"));
    }

	@Test(expected = NotExistingResourceError.class)
	public void addFileToNonExistingProject() {
		ProjectResource project = new ProjectResource();
		project.setName("someProject");
		FolderUploadHandle handle = new FolderUploadHandle(project,
				"someFolder");
		repoService.uploadResource("someComment", handle);
	}

	@Test(expected = ResourceAlreadyExistsError.class)
	public void addDuplicateFolderToProject() {
		ProjectResource project = repoService.createProject("someProject", "commitMessage");

		FolderUploadHandle handle = new FolderUploadHandle(project,
				"someFolder");
		repoService.uploadResource("someComment", handle);
		repoService.uploadResource("someComment", handle);
	}

	@Test
	public void removeFolderFromProject() {
		ProjectResource project = repoService.createProject("someProject","commitMessage");

		FolderUploadHandle handle = new FolderUploadHandle(project,
				"someFolder");
		repoService.uploadResource("someComment", handle);

		Resource resource = repoService.createQuery().path("someProject")
				.nameLike("someFolder").singleResult();
		repoService.deleteResource(resource);
		assertNull(repoService.createQuery().path("someProject").nameLike("someFolder")
				.singleResult());
	}

	@Test
	public void removeFileFromProject() {
		final String testFolderName = "folder1";
		final String testFileName = "file1";
		ProjectResource project = repoService.createProject("someProject","commitMessage");

		FolderUploadHandle handle = new FolderUploadHandle(project,testFolderName);
		repoService.uploadResource("uploading comment", handle);

		FileUploadHandle handle2 = new FileUploadHandle(project,testFileName, "".getBytes());
		repoService.uploadResource("someComment", handle2);
		
		Resource resource = repoService.createQuery()
				.path(testFolderName)
				.nameLike(testFileName).singleResult();
		repoService.deleteResource(resource);
        assertFalse(new File(System.getProperty("user.dir") + "/projects/someProject/file1.meta.props").exists());
		assertNull(repoService.createQuery().path("removeFileFromProject")
				.nameLike("removeFileFromProjectFile").singleResult());
	}

	@Test
	public void removeProject() {
		repoService.createProject("someProject", "commitMessage");

		Resource resource = repoService.createQuery().nameLike("someProject")
				.singleResult();
		repoService.deleteResource(resource);
		assertNull(repoService.createQuery().nameLike("someProject").singleResult());
	}

	@Test
	public void deleteResourceNonExistant() {
		Resource resource = new FileResource();

		// Delete folder and file
		resource.setPath("foo");
		repoService.deleteResource(resource);
	}

	@Test
	public void getResourceContentNotExists() {
		FileResource resource = new FileResource();
		resource.setPath("nothing");
		assertNull(repoService.getResourceContent(resource).content);
	}

	@Test
	public void getResourceContentExistant() {
		ProjectResource project = createTestProject("myProject");

		FileUploadHandle handle = new FileUploadHandle(project,"someFile",
				"content".getBytes());
		repoService.uploadResource("someComment", handle);

		ResourceContent content = repoService
				.getResourceContent((FileResource) repoService.createQuery()
						.path("myProject/someFile").singleResult());
		assertTrue(Arrays.equals("content".getBytes(), content.getContent()));
	}

	@Test
	public void queryNameLike() {
		String name = "queryNameLikeTest";
		createTestProject(name);

		Resource resource = repoService.createQuery().nameLike(name).singleResult();

		assertEquals(resource.getName(), name);
	}

	@Test
	public void queryNameLikeNonExistant() {
		String name = "queryNameLikeNonExistantTest";
		createTestProject(name);

		assertNull(repoService.createQuery().nameLike("doesNotExist").singleResult());
	}

	@Test
	public void queryPathSingleResult() {
		ProjectResource project = createTestProject("queryPathTestProject");
		createTestFolder(project,"queryPathTestFolder/queryPathTestSubFolder");

		Resource resource = repoService.createQuery()
				.path("queryPathTestProject/queryPathTestFolder")
				.singleResult();

		assertEquals("queryPathTestProject" + "/" + "queryPathTestFolder",
				resource.getPath());
	}

	@Test
	public void queryPathNonExistant() {
		ProjectResource project = createTestProject("queryPathTestProject");

		createTestFolder(project,"queryPathTestFolder/queryPathTestSubFolder");
		assertNull(repoService.createQuery().path("doesNotExist").singleResult());
	}

	@Test
	public void queryPathLikeMultipleResults() {
		ProjectResource project = createTestProject("queryPathTestProject");

		createTestFolder(project,"queryPathTestFolder"
				+ "/queryPathTestSubFolder1");
		createTestFolder(project,"queryPathTestFolder"
				+ "/queryPathTestSubFolder2");
		createTestFolder(project,"queryPathTestFolder"
				+ "/queryPathTestSubFolder3");

		List<Resource> resources = repoService.createQuery()
				.pathLike("queryPathTestProject/queryPathTestFolder").list();

		assertEquals(4, resources.size());
	}

	@Test
	public void queryPathLike() {
		ProjectResource project = createTestProject("queryPathTestProject");

		createTestFolder(project,"queryPathTestFolder");

		List<Resource> resources = repoService.createQuery()
				.pathLike("queryPathTestProject/queryPathTestFolder").list();

		assertEquals(1, resources.size());
	}

	@Test
	public void queryResourcePathAndName() {
		ProjectResource project = createTestProject("someProject");

		createTestFolder(project,"someFolder");

		Resource resource = repoService.createQuery().pathLike("someProject")
				.nameLike("someFolder").singleResult();
		assertEquals("someProject" + "/" + "someFolder", resource.getPath());
		assertEquals(FolderResource.class, resource.getClass());
	}

	@Test
	public void queryResourcePathAndNameWrong() {
		ProjectResource projectResource = createTestProject("someProject");
		createTestFolder(projectResource,"someFolder");

		Resource resource = repoService.createQuery().path("someProject")
				.nameLike("someFolder").singleResult();
		assertNull(resource);
	}

	private FolderResource createTestFolder(ProjectResource project, String path) {

		repoService.uploadResource("commitComment",
				new FolderUploadHandle(project, path));
		return (FolderResource)repoService.createQuery().path(path).type(ResourceType.FolderResource).singleResult();
	}
	
	private void createTestFolder(FolderResource resource, String path, String author) {

		FolderUploadHandle handle = new FolderUploadHandle(resource,path);
		handle.setProperty(Resource.META_PROPERTY_AUTHOR,author);
		repoService.uploadResource("commitComment",
				handle);
	}

	@Test
	public void queryProjectResource() {
		createTestProject("queryProjectResourceTest");

		List<Resource> resource = repoService.createQuery().list();
		assertEquals(resource.get(0).getClass(), ProjectResource.class);
	}

	@Test
	public void queryFolderResource() {
		ProjectResource projectResource = createTestProject("queryFolderResourceTest");

		createTestFolder(projectResource,"folder");

		List<Resource> resource = repoService.createQuery()
				.path("queryFolderResourceTest/folder").list();
		assertEquals(resource.get(0).getClass(), FolderResource.class);
	}

	@Test
	public void queryFileResource() {
		ProjectResource project = createTestProject("queryFileResourceTest");
		createTestFile(project,"file");

		List<Resource> resource = repoService.createQuery()
				.path("queryFileResourceTest/file").list();
		assertEquals(resource.get(0).getClass(), FileResource.class);
	}

	private void createTestFile(FolderResource folderResource, String path) {
		FileUploadHandle handle = new FileUploadHandle(folderResource,path,
				"content".getBytes(), null);
		repoService.uploadResource("someComment", handle);
	}

	private void createTestFile(FolderResource resource, String path, String author) {
		FileUploadHandle handle = new FileUploadHandle(resource, path, "content".getBytes(), null);

		handle.setProperty(Resource.META_PROPERTY_AUTHOR, author);
		repoService.uploadResource("someComment", handle);
	}
	
	@Test
	public void queryLastModifiedAndPath() {
		ProjectResource project = createTestProject("queryLastModified");
		createTestFile(project,"file");

		Resource resource1 = repoService.createQuery()
				.path("queryLastModified/file").singleResult();
		Resource resource2 = repoService.createQuery()
				.lastModified(resource1.getLastModified())
				.pathLike("queryLastModified/file").singleResult();
		assertEquals(resource1.getLastModified().toString(), resource2
				.getLastModified().toString());
	}

	@Test
	public void queryLastModifiedWrongDate() {
		ProjectResource project = createTestProject("queryLastModified");
		createTestFile(project, "file");
		assertNull(repoService.createQuery().lastModified(new Date(0)).singleResult());
	}

	@Test
	public void queryByTypeFolder() {
		ProjectResource projectResource = createTestProject("queryByTypeFolder");

		createTestFolder(projectResource,"queryFolder");
		createTestFile(projectResource,"file");

		Resource resource = repoService.createQuery()
				.type(ResourceType.FolderResource).singleResult();
		assertEquals(FolderResource.class, resource.getClass());
		assertEquals("queryFolder", resource.getName());
	}

	@Test
	public void queryByTypeFile() {
		ProjectResource project = createTestProject("queryByTypeFile");
		createTestFile(project,"file");

		Resource resource = repoService.createQuery()
				.type(ResourceType.FileResource).singleResult();
		assertEquals(FileResource.class, resource.getClass());
		assertEquals("file", resource.getName());
	}

	@Test
	public void queryByTypeProject() {
		ProjectResource project = createTestProject("queryByTypeProject");
		createTestFile(project,"file");

		Resource resource = repoService.createQuery()
				.type(ResourceType.ProjectResource).singleResult();
		assertEquals(ProjectResource.class, resource.getClass());
		assertEquals("queryByTypeProject", resource.getName());
	}

	@Test
	public void queryByTypeProjectMultipleResults() {
		ProjectResource project = createTestProject("queryByTypeProject1");
		createTestProject("queryByTypeProject2");
		createTestProject("queryByTypeProject3");
		createTestFile(project,"file");

		List<Resource> resource = repoService.createQuery()
				.type(ResourceType.ProjectResource).list();
		assertEquals(3, resource.size());
	}

	@Test
	public void queryAllResourcesOfProject() {
		ProjectResource project = createTestProject("myproject");
		createTestFolder(project,"folder");
		ProjectResource anotherProject = createTestProject("anotherproject");
		createTestFile(anotherProject,"file");

		List<Resource> resource = repoService.createQuery()
				.pathLike("myproject").list();
		assertEquals(2, resource.size());
	}

	@Test
	public void queryByAuthor() {
		ProjectResource project1 = createTestProject("queryByTypeProject1","admin");
		createTestProject("queryByTypeProject3","admin");
		createTestFolder(project1,"folder","admin");
		createTestFile(project1,"file","alex");

		List<Resource> resource = repoService.createQuery().author("admin")
				.list();
		assertEquals(3, resource.size());
	}

	@Test
	@Ignore
	public void queryByAuthorVersionTypeAndName() {
		ProjectResource project = createTestProject("queryByTypeProject1");
		createTestFolder(project,"folder");
		ProjectResource project2 = createTestProject("queryByTypeProject3");
		createTestFile(project2,"file");
		createTestFile(project,"file");
		repoService
				.createVersionOfProject("testVersion", "queryByTypeProject1");
		repoService.createVersionOfProject("testVersion2",
				"queryByTypeProject3");
		repoService.createVersionOfProject("testVersion2",
				"queryByTypeProject1");

		List<Resource> resource = repoService.createQuery().author("admin")
				.type(ResourceType.FileResource).version("testVersion")
				.name("file").list();
		assertEquals(1, resource.size());
		assertEquals("queryByTypeProject1/file", resource.get(0).getPath());
		assertEquals("testVersion", resource.get(0).getVersion());
		assertEquals(FileResource.class, resource.get(0).getClass());
		assertEquals("admin", resource.get(0).getAuthor());
	}

	@Test
	@Ignore
	public void queryByAuthorTypeAndNameWithoutVersion() {
		ProjectResource project = createTestProject("queryByTypeProject1");
		createTestFolder(project,"folder");
		ProjectResource project2 = createTestProject("queryByTypeProject3");
		createTestFile(project2,"file");
		createTestFile(project,"file");
		repoService
				.createVersionOfProject("testVersion", "queryByTypeProject1");
		repoService.createVersionOfProject("testVersion2",
				"queryByTypeProject3");
		repoService.createVersionOfProject("testVersion2",
				"queryByTypeProject1");

		List<Resource> resource = repoService.createQuery().author("admin")
				.type(ResourceType.FileResource).name("file").list();
		assertEquals(2, resource.size());
		assertEquals("HEAD", resource.get(0).getVersion());
		assertEquals(FileResource.class, resource.get(0).getClass());
		assertEquals("admin", resource.get(0).getAuthor());
	}

	@Test
	@Ignore
	public void createVersion() {
		createTestProject("versionTest");
		repoService.createVersionOfProject("testVersion", "versionTest");

		Resource resource = repoService.createQuery().version("testVersion")
				.singleResult();
		assertEquals("testVersion", resource.getVersion());
		assertEquals("versionTest", resource.getName());
	}

	@Test
	@Ignore
	public void createVersionOfTwoProjects() {
		createTestProject("versionTest");
		createTestProject("blabla");
		repoService.createVersionOfProject("testVersion", "versionTest");
		repoService.createVersionOfProject("testVersion", "blabla");

		List<Resource> resource = repoService.createQuery()
				.version("testVersion").list();
		assertEquals(2, resource.size());
	}

	@Test
	@Ignore
	public void createVersionOfTwoProjectsAndDeleteVersion() {
		createTestProject("versionTest");
		createTestProject("blabla");
		repoService.createVersionOfProject("testVersion", "versionTest");
		repoService.createVersionOfProject("testVersion", "blabla");

		List<Resource> resource = repoService.createQuery()
				.version("testVersion").list();
		assertEquals(2, resource.size());
		repoService.deleteVersion("testVersion");
		resource = repoService.createQuery().version("testVersion").list();
		assertEquals(0, resource.size());
	}

	@Test
	@Ignore
	public void tagProject() {
		createTestProject("tagTest");

		Resource resource = repoService.createQuery().singleResult();
		repoService.tag(resource, "testTag");
		resource = repoService.createQuery().singleResult();
		assertEquals("vr:testTag", resource.getTags()[0]);
	}

	@Test
	@Ignore
	public void tagFolder() {
		ProjectResource project = createTestProject("tagTest");
		createTestFolder(project,"folder");

		Resource resource = repoService.createQuery().path("tagTest/folder")
				.singleResult();
		repoService.tag(resource, "testTag");
		resource = repoService.createQuery().path("tagTest/folder")
				.singleResult();
		assertEquals("vr:testTag", resource.getTags()[0]);
	}

	@Test
	@Ignore
	public void tagFile() {
		ProjectResource project = createTestProject("tagTest");
		createTestFile(project,"file");

		Resource resource = repoService.createQuery().path("tagTest/file")
				.singleResult();
		repoService.tag(resource, "testTag");
		resource = repoService.createQuery().path("tagTest/file")
				.singleResult();
		assertEquals("vr:testTag", resource.getTags()[0]);
	}

	@Test
	@Ignore
	public void lockUnlockAndCheckLockFProject() {
		createTestProject("lockTest");

		Resource resource = repoService.createQuery().singleResult();
		repoService.lock(resource);
		assertTrue(repoService.isLocked(resource));
		repoService.unlock(resource);
		assertFalse(repoService.isLocked(resource));
	}

	@Test
	@Ignore
	public void lockUnlockAndCheckLockFolder() {
		ProjectResource project = createTestProject("tagTest");
		createTestFolder(project,"folder");

		Resource resource = repoService.createQuery().path("tagTest/folder")
				.singleResult();
		repoService.lock(resource);
		assertTrue(repoService.isLocked(resource));
		repoService.unlock(resource);
		assertFalse(repoService.isLocked(resource));
	}

	@Test
	@Ignore
	public void lockUnlockAndCheckLockFFile() {
		ProjectResource project = createTestProject("tagTest");
		createTestFile(project,"file");

		Resource resource = repoService.createQuery().path("tagTest/file")
				.singleResult();
		repoService.lock(resource);
		assertTrue(repoService.isLocked(resource));
		repoService.unlock(resource);
		assertFalse(repoService.isLocked(resource));
	}

	@Test
	@Ignore
	public void doubleLockResource() {
		ProjectResource project = createTestProject("tagTest");
		createTestFile(project,"file");

		Resource resource = repoService.createQuery().path("tagTest/file")
				.singleResult();
		repoService.lock(resource);
		assertTrue(repoService.isLocked(resource));
		repoService.lock(resource);
		assertTrue(repoService.isLocked(resource));
	}

	@Test
	@Ignore
	public void unlockUnlockedResource() {
		ProjectResource project = createTestProject("tagTest");
		createTestFile(project,"file");

		Resource resource = repoService.createQuery().path("tagTest/file")
				.singleResult();
		repoService.unlock(resource);
		assertFalse(repoService.isLocked(resource));
		repoService.unlock(resource);
		assertFalse(repoService.isLocked(resource));
	}

	protected ProjectResource createTestProject(String name) {
		return repoService.createProject(name,
				"JUnit test for creating a project");
	}
	
	protected ProjectResource createTestProject(String name, String author) {
		Map<String,String> properties = new HashMap<String, String>();
		properties.put(Resource.META_PROPERTY_AUTHOR, author);
		return repoService.createProject(name,properties,
				"JUnit test for creating a project");
	}
}

/* EOF */
