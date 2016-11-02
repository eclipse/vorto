package org.eclipse.vorto.server.devtool.controller.publisher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.http.model.ServerResponse;
import org.eclipse.vorto.repository.model.ModelHandle;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.models.ProjectResourceListWrapper;
import org.eclipse.vorto.server.devtool.utils.Constants;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.inject.Injector;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/publish")
public class PublisherController {

	@Value("${vorto.repository.base.path:http://vorto.eclipse.org}")
	private String repositoryBasePath;

	@Autowired
	Injector injector;

	@Autowired
	DevtoolRestClient devtoolRestClient;

	@ApiOperation(value = "Uploads models to the vorto repository for validation")
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<ServerResponse> uploadModels(
			@RequestBody ProjectResourceListWrapper projectResourceListWrapper,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);
		ResourceSet resourceSet = webEditorResourceSetProvider.getResourceSetFromSession(httpServiceContext);
		ArrayList<Resource> resourceList = new ArrayList<Resource>();
		for (ProjectResource projectResource : projectResourceListWrapper.getProjectResourceList()) {
			Resource resource = resourceSet.getResource(URI.createURI(projectResource.getResourceId()), true);
			resourceList.add(resource);
		}
		String zipFilePath = createZipFile(projectResourceListWrapper.getProjectResourceList(), resourceList);
		ResponseEntity<ServerResponse> response =  devtoolRestClient.uploadMultipleFiles(zipFilePath);
		File zipFile = new File(zipFilePath);
		zipFile.delete();
		return response;
	}

	@ApiOperation(value = "Checks in single model to the vorto repository")
	@RequestMapping(value = "/{handleId:.+}", method = RequestMethod.PUT)
	public ResponseEntity<ServerResponse> checkin(
			@ApiParam(value = "The file name of uploaded vorto model", required = true) final @PathVariable String handleId) {
			return devtoolRestClient.checkInSingleFile(handleId);
	}

	@ApiOperation(value = "Checks in multiple models to the vorto repository")
	@RequestMapping(value = "/checkInMultiple", method = RequestMethod.PUT)
	public ResponseEntity<ServerResponse> checkInMultiple(
			@ApiParam(value = "The file name of uploaded vorto model", required = true) final @RequestBody ModelHandle[] modelHandles) {
				return devtoolRestClient.checkInMultipleFiles(modelHandles);
	}

	private String createZipFile(ArrayList<ProjectResource> projectResourceList, ArrayList<Resource> resourceList) {
		String zipFile = Constants.UPLOAD_ZIP_FILE_DIRECTORY + File.separator
				+ Long.toString(System.currentTimeMillis()) + ".zip";
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
			ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
			for (int index = 0; index < projectResourceList.size(); index++) {
				zipOutputStream.putNextEntry(new ZipEntry(projectResourceList.get(index).getName()));
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				Resource resource = resourceList.get(index);
				resource.save(byteArrayOutputStream, null);
				zipOutputStream.write(byteArrayOutputStream.toByteArray());
				zipOutputStream.closeEntry();
			}
			zipOutputStream.close();
			return zipFile;
		} catch (Exception ex) {
			return null;
		}
	}
}
