package org.eclipse.vorto.codegen.kura.templates.cloud.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelIdFactory
import org.eclipse.vorto.core.api.model.model.ModelType

class BoschHubDataService implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''BoschHubDataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»/cloud/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.getJavaPackage(element)».cloud.bosch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

«FOR reference : element.references»
«var modelId = ModelIdFactory.newInstance(ModelType.Functionblock,reference)»
import «Utils.getJavaPackage(element)».model.«modelId.name»;
«ENDFOR»
import «Utils.getJavaPackage(element)».cloud.IDataService;

import com.google.gson.Gson;

public class BoschHubDataService implements IDataService {
	
	private String mqqtHostUrl;
	private String hubTenant;
	private Map<String, BoschHubClient> deviceClients = new HashMap<String, BoschHubClient>();
	private Gson gson = new Gson();
	
	public BoschHubDataService(String mqqtHostUrl, String hubTenant) {
		this.mqqtHostUrl = Objects.requireNonNull(mqqtHostUrl);
		this.hubTenant = Objects.requireNonNull(hubTenant);
	}
	
	«FOR fbProperty : element.properties»
	@Override
	public void publish«fbProperty.name.toFirstUpper»(String resourceId, «fbProperty.type.name» «fbProperty.name») {
		getConnectedHubClient(resourceId).send("telemetry/" + hubTenant + "/" + resourceId, gson.toJson(wrap("«fbProperty.name»", «fbProperty.name»)));
	}
	«ENDFOR»
	
	private <T> Map<String, Map<String, Map<String, T>>> wrap(String name, T functionBlock) {
		Map<String, T> status = new HashMap<String, T>();
		status.put("status", functionBlock);
		
		Map<String, Map<String, T>> properties = new HashMap<String, Map<String, T>>();
		properties.put("properties", status);
		
		Map<String, Map<String, Map<String, T>>> wrapper = new HashMap<String, Map<String, Map<String, T>>>();
		wrapper.put(name, properties);
		return wrapper; 
	}

	private BoschHubClient getConnectedHubClient(String resourceId) {
		BoschHubClient client = deviceClients.get(resourceId);
		if (client == null) {
			client = new BoschHubClient(mqqtHostUrl, resourceId);
			deviceClients.put(resourceId, client);
		}
		
		if (!client.isConnected()) {
			client.connect();
		}
		
		return client;
	}
}
'''
	}
	
}