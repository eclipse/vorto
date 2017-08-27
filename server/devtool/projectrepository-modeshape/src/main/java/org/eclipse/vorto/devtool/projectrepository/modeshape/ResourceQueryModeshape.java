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
package org.eclipse.vorto.devtool.projectrepository.modeshape;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.devtool.projectrepository.model.FolderResource;
import org.eclipse.vorto.devtool.projectrepository.model.ModelResource;
import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;
import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceType;
import org.eclipse.vorto.devtool.projectrepository.modeshape.execption.ModeshapeRepositoryException;
import org.eclipse.vorto.devtool.projectrepository.query.IResourceQuery;
import org.eclipse.vorto.devtool.projectrepository.utils.ProjectRepositoryConstants;
import org.eclipse.vorto.repository.api.ModelType;

public class ResourceQueryModeshape implements IResourceQuery {

	private Session session;

	private Map<String, List<String>> exactSearchCriterionMap;

	private String source;

	private String pathLike;

	private boolean searchByPathLike;

	private final String SELECT_QUERY = "SELECT * FROM";

	private final String WHERE = "WHERE";

	private final String LIKE = "LIKE";

	private final String AND = "AND";

	private final String IN = "IN";

	private final String PATH = "PATH";

	public ResourceQueryModeshape(Session session) {
		this.session = session;
		exactSearchCriterionMap = new HashMap<String, List<String>>();
		source = ProjectRepositoryServiceModeshape.MIXIN_MODEL_RESOURCE;
		searchByPathLike = false;
	}

	@SuppressWarnings("unchecked")
	public List<Resource> list() {
		Query query = getQuery();
		List<Resource> result = new ArrayList<Resource>();
		try {
			QueryResult queryResult = query.execute();
			RowIterator rowIterator = queryResult.getRows();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.nextRow();
				Node currentNode = row.getNode();
				if (currentNode.hasProperty(ProjectRepositoryServiceModeshape.META_PROPERTY_RESOURCE_TYPE)) {
					try {
						Resource resouce = createResourceFromNode(currentNode);
						result.add(resouce);
					} catch (Exception ex) {
						System.out.print(ex);
						// model is corrupt ,ignoring....
					}
				}
			}
			return result;
		} catch (RepositoryException e) {
			throw new ModeshapeRepositoryException("Failed to execute query ", e);
		}
	}

	public Resource singleResult() {
		List<Resource> result = list();
		if (result.size() == 1) {
			return result.get(0);
		} else if (result.size() > 1) {
			throw new IllegalStateException("More than 1 result was found with this query");
		} else {
			return null;
		}
	}

	public int count() {
		return list().size();
	}

	@Override
	public IResourceQuery nameLike(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IResourceQuery pathLike(String path) {
		if (path.startsWith(File.separator)) {
			pathLike = path;
		} else {
			pathLike = File.separator + path;
		}
		searchByPathLike = true;
		return this;
	}

	@Override
	public IResourceQuery name(String name) {
		return property(ProjectRepositoryServiceModeshape.META_PROPERTY_NAME, name);
	}

	@Override
	public IResourceQuery path(String path) {
		return name(path);
	}

	@Override
	public IResourceQuery author(String author) {
		return property(ProjectRepositoryServiceModeshape.META_PROPERTY_AUTHOR, author);
	}

	@Override
	public IResourceQuery version(String version) {
		return property(ProjectRepositoryServiceModeshape.META_PROPERTY_VERSION, version);
	}

	@Override
	public IResourceQuery namespace(String namespace) {
		return property(ProjectRepositoryServiceModeshape.META_PROPERTY_NAMESPACE, namespace);
	}

	@Override
	public IResourceQuery type(ResourceType type) {
		if (type.equals(ResourceType.ProjectResource)) {
			source = ProjectRepositoryServiceModeshape.MIXIN_PROJECT_RESOURCE;
		} else {
			source = ProjectRepositoryServiceModeshape.MIXIN_MODEL_RESOURCE;
		}
		return this;
	}

	@Override
	public IResourceQuery lastModified(Date date) {
		return property(ProjectRepositoryServiceModeshape.JCR_LAST_MODIFIED, date.toString());
	}

	@Override
	public IResourceQuery property(String propertyName, String propertyValue) {
		List<String> values = exactSearchCriterionMap.getOrDefault(propertyName, new ArrayList<String>());
		values.add(propertyValue);
		exactSearchCriterionMap.put(propertyName, values);
		return this;
	}

	private String getSearchCriteriaParametersAsString(List<String> list) {
		StringBuilder stringBuilder = new StringBuilder();
		if (list.isEmpty()) {
			return "''";
		} else if (list.size() == 1) {
			stringBuilder.append("'").append(list.get(0)).append("'");
		} else {
			stringBuilder.append("(");
			for (String string : list) {
				stringBuilder.append("'").append(string).append("', ");
			}
			stringBuilder.replace(stringBuilder.lastIndexOf(","), stringBuilder.length() - 1, "");
			stringBuilder.append(")");
		}
		return stringBuilder.toString();
	}

	private String buildJCRQuery() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(SELECT_QUERY).append(" [").append(source).append("] ");
		boolean isExactSearchCriterionPresent = false;
		Iterator<Entry<String, List<String>>> iterator = exactSearchCriterionMap.entrySet().iterator();
		if (iterator.hasNext()) {
			isExactSearchCriterionPresent = true;
			stringBuilder.append(WHERE).append(" ");
		}
		while (iterator.hasNext()) {
			Entry<String, List<String>> entry = iterator.next();
			String property = ProjectRepositoryServiceModeshape.META_PROPERTIES_MAP.getOrDefault(entry.getKey(),
					entry.getKey());
			stringBuilder.append("[").append(source).append("]").append(".").append("[").append(property).append("]");
			if (entry.getValue().size() == 1) {
				stringBuilder.append(" = ").append(getSearchCriteriaParametersAsString(entry.getValue()));
			} else {
				stringBuilder.append(" ").append(IN).append(" ")
						.append(getSearchCriteriaParametersAsString(entry.getValue()));
			}
			stringBuilder.append(" ").append(AND).append(" ");
		}
		if (isExactSearchCriterionPresent) {
			stringBuilder.replace(stringBuilder.lastIndexOf(AND),
					stringBuilder.length() - 1, "");
		}
		if (searchByPathLike) {
			if (!isExactSearchCriterionPresent) {
				stringBuilder.append(WHERE).append(" ");
			} else {
				stringBuilder.append(" ").append(AND).append(" ");
			}
			stringBuilder.append(PATH).append("([")
					.append(ProjectRepositoryServiceModeshape.MIXIN_MODEL_RESOURCE).append("])").append(" ")
					.append(LIKE).append(" '").append(pathLike).append("%' ");
		}
		return stringBuilder.toString();
	}

	private Query getQuery() {
		try {
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			String jcrStatementQuery = buildJCRQuery();
			return queryManager.createQuery(jcrStatementQuery, Query.JCR_SQL2);
		} catch (RepositoryException e) {
			throw new ModeshapeRepositoryException("Failed to create JCR SQL 2 query", e);
		}
	}

	private Resource createResourceFromNode(Node node) throws RepositoryException, IOException {
		String resourceType = node.getProperty(ProjectRepositoryServiceModeshape.META_PROPERTY_RESOURCE_TYPE)
				.getString();
		if (resourceType.equals(ResourceType.ProjectResource.toString())) {
			return createProjectResourceFromNode(node);
		} else if (resourceType.equals(ResourceType.FolderResource.toString())) {
			return new FolderResource();
		} else if (resourceType.equals(ResourceType.FileResource.toString())) {
			return createModelResourceFromNode(node);
		} else {
			throw new ModeshapeRepositoryException("No resource of type " + resourceType);
		}
	}

	private Resource createProjectResourceFromNode(Node node) throws RepositoryException {
		ProjectResource projectResource = new ProjectResource();
		Map<String, String> properties = getNodeProperties(node);
		projectResource.setProperties(properties);
		projectResource.setName(FilenameUtils.getName(properties.get(ProjectRepositoryConstants.META_PROPERTY_NAME)));
		projectResource.setPath(node.getPath());
		return projectResource;
	}

	private ModelResource createModelResourceFromNode(Node node) throws RepositoryException, IOException {
		ModelResource modelResource = new ModelResource();
		Map<String, String> properties = getNodeProperties(node);
		modelResource.setProperties(properties);
		modelResource.setName(properties.get(ProjectRepositoryConstants.META_PROPERTY_NAME));
		modelResource.setNamespace(properties.get(ProjectRepositoryConstants.META_PROPERTY_NAMESPACE));
		modelResource.setVersion(properties.get(ProjectRepositoryConstants.META_PROPERTY_VERSION));
		modelResource
				.setModelType(ModelType.valueOf(properties.get(ProjectRepositoryConstants.META_PROPERTY_MODEL_TYPE)));
		modelResource.setSubType(properties.get(ProjectRepositoryConstants.META_PROPERTY_MODEL_SUB_TYPE));
		modelResource.setPath(node.getPath());
		modelResource.setContent(getFileContent(node));
		return modelResource;
	}

	private Map<String, String> getNodeProperties(Node node) throws RepositoryException {
		Map<String, String> properties = new HashMap<String, String>();
		Iterator<Property> iterator = node.getProperties();
		while (iterator.hasNext()) {
			Property property = iterator.next();
			if (property.getName().startsWith(ProjectRepositoryServiceModeshape.PREFIX_VORTO_META_PROPERTY)) {
				String propertyName = property.getName()
						.replace(ProjectRepositoryServiceModeshape.PREFIX_VORTO_META_PROPERTY, "");
				properties.put(propertyName, property.getString());
			}
		}
		return properties;
	}

	private byte[] getFileContent(Node node) throws RepositoryException, IOException {
		Node contentNode = (Node) node.getPrimaryItem();
		InputStream inputStream = contentNode.getProperty(ProjectRepositoryServiceModeshape.JCR_DATA).getBinary()
				.getStream();
		return IOUtils.toByteArray(inputStream);
	}
}
