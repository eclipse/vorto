package org.eclipse.vorto.repository.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import org.apache.http.client.HttpClient;
import org.eclipse.vorto.repository.api.IModel;
import org.eclipse.vorto.repository.api.IModelRepository;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelQuery;

import com.google.gson.reflect.TypeToken;

public class DefaultModelRepository extends ImplementationBase implements IModelRepository {
	
	public DefaultModelRepository(HttpClient httpClient, RequestContext requestContext) {
		super(httpClient, requestContext);
	}

	@Override
	public CompletableFuture<Collection<ModelInfo>> search(ModelQuery query) {
		String url = String.format("%s/rest/model/query=%s", getRequestContext().getBaseUrl(), query.getExpression());
		return requestAndTransform(url, transformToType(new TypeToken<ArrayList<ModelInfo>>() {}.getType()));
	}

	@Override
	public CompletableFuture<ModelInfo> getById(ModelId modelId) {
		String url = String.format("%s/rest/model/%s/%s/%s", getRequestContext().getBaseUrl(), modelId.getNamespace(), modelId.getName(), modelId.getVersion());
		return requestAndTransform(url, transformToClass(ModelInfo.class));
	}

	@Override
	public <ModelContent extends IModel> CompletableFuture<ModelContent> getContent(ModelId modelId,
			Class<ModelContent> resultClass) {
		String url = String.format("%s/rest/model/content/%s/%s/%s", getRequestContext().getBaseUrl(), modelId.getNamespace(), modelId.getName(), modelId.getVersion());
		return requestAndTransform(url, transformToClass(resultClass));
	}
}
