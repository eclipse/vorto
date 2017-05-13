package org.eclipse.vorto.server.devtool.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.devtool.projectrepository.ResourceAlreadyExistsError;
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryFileConstants;
import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.server.devtool.models.ModelResource;
import org.eclipse.vorto.server.devtool.service.IProjectService;
import org.eclipse.vorto.server.devtool.service.IServletInitializerService;
import org.eclipse.vorto.server.devtool.utils.Constants;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class XtextSeverletIntializerServiceImpl implements IServletInitializerService {
	
	@Value("${dummy.repository}")
	private String dummyRepository;
	
	@Value("${project.repository.path}")
	private String projectRepositoryPath;
		
	@Value("${dummy.repository.author}")
	private String dummyRepositoryAuthor;
	
	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private DevtoolRestClient devtoolRestClient;
	
	@Override
	public void initializeXtextServlets() {
		setUpDummyResourceDirectory();
		LinkedHashMap<String, ModelType> resourceIdModelTypeMap = loadDummyResources();
		Iterator<Entry<String, ModelType>> iterator = resourceIdModelTypeMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, ModelType> entry = iterator.next();
			devtoolRestClient.getResource(entry.getKey(), entry.getValue());
		}
		try {
			FileUtils.deleteDirectory(new File(projectRepositoryPath + File.separator + dummyRepository));
		} catch (IOException e) {

		}
		
	}
		
	private LinkedHashMap<String, ModelType> loadDummyResources() {
		List<ModelResource> modelResourceList = getDummyResourcesList();
		LinkedHashMap<String, ModelType> resourceIdModelTypeMap = new LinkedHashMap<>();
		for(ModelResource modelResource : modelResourceList){
			Resource resource = projectService.createProjectResource(dummyRepository,dummyRepositoryAuthor, modelResource);
			String resourceId = resource.getProperties().get(ProjectRepositoryFileConstants.META_PROPERTY_RESOURCE_ID);
			resourceIdModelTypeMap.put(resourceId, modelResource.getModelType());
		}
		return resourceIdModelTypeMap;
	}
	
	private void setUpDummyResourceDirectory() {
		try{
			FileUtils.deleteDirectory(new File(projectRepositoryPath + File.separator + dummyRepository));
			projectService.createProject(dummyRepository, dummyRepositoryAuthor);
		}catch(ResourceAlreadyExistsError resourceAlreadyExistsError){
			
		}catch (IOException e) {

		}
	}
	
	private List<ModelResource> getDummyResourcesList() {
		ArrayList<ModelResource> modelResourceList = new ArrayList<>();

		ModelResource infomodelResource = new ModelResource();
		infomodelResource.setName(Constants.DUMMY_RESOURCE_INFOMODEL_NAME);
		infomodelResource.setNamespace(Constants.DUMMY_RESOURCE_NAMESPACE);
		infomodelResource.setVersion(Constants.DUMMY_RESOURCE_VERSION);
		infomodelResource.setModelType(ModelType.InformationModel);
		infomodelResource.setSubType("");
		infomodelResource.setDescription(Constants.DUMMY_RESOURCE_DESCRIPTION);
		infomodelResource.setFilename(Constants.DUMMY_RESOURCE_INFOMODEL_FILE_NAME);
		
		ModelResource functionblockResource = new ModelResource();
		functionblockResource.setName(Constants.DUMMY_RESOURCE_FUNCTIONBLOCK_NAME);
		functionblockResource.setNamespace(Constants.DUMMY_RESOURCE_NAMESPACE);
		functionblockResource.setVersion(Constants.DUMMY_RESOURCE_VERSION);
		functionblockResource.setModelType(ModelType.Functionblock);
		functionblockResource.setSubType("");
		functionblockResource.setDescription(Constants.DUMMY_RESOURCE_DESCRIPTION);
		functionblockResource.setFilename(Constants.DUMMY_RESOURCE_FUNCTIONBLOCK_FILE_NAME);
		
		ModelResource dataTypeResource = new ModelResource();
		dataTypeResource.setName(Constants.DUMMY_RESOURCE_DATATYPE_NAME);
		dataTypeResource.setNamespace(Constants.DUMMY_RESOURCE_NAMESPACE);
		dataTypeResource.setVersion(Constants.DUMMY_RESOURCE_VERSION);
		dataTypeResource.setModelType(ModelType.Datatype);
		dataTypeResource.setSubType(Constants.SUBTYPE_ENTITY);
		dataTypeResource.setDescription(Constants.DUMMY_RESOURCE_DESCRIPTION);
		dataTypeResource.setFilename(Constants.DUMMY_RESOURCE_DATATYPE_FILE_NAME);
		
		modelResourceList.add(infomodelResource);
		modelResourceList.add(functionblockResource);
		modelResourceList.add(dataTypeResource);
		
		return modelResourceList;
	}	
}
