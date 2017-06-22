package org.eclipse.vorto.devtool.projectrepository.file;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.vorto.devtool.projectrepository.model.Resource;

public class ProjectRepositoryFileConstants {

	public static final String META_PROPERTY_AUTHOR = Resource.META_PROPERTY_AUTHOR;

	public static final String META_PROPERTY_RESOURCE_ID = "resourceId";

	public static final String META_PROPERTY_NAME = "name";

	public static final String META_PROPERTY_NAMESPACE = "namespace";

	public static final String META_PROPERTY_VERSION = "version";

	public static final String META_PROPERTY_MODEL_TYPE = "modelType";

	public static final String META_PROPERTY_MODEL_SUB_TYPE = "modelSubType";

	public static final String META_PROPERTY_IS_PROJECT = "isProject";

	public static final String META_PROPERTY_REFERENCES = "references";
	
	public static final String META_PROPERTY_CREATIONDATE = "meta.createdOn";
	
	public static final String META_PROPERTY_FILENAME = ".meta.props";

	public static Set<String> IMMUTABLE_META_PROPERTY_SET = new HashSet<String>(Arrays.asList(
		META_PROPERTY_AUTHOR,
		META_PROPERTY_IS_PROJECT,
		META_PROPERTY_RESOURCE_ID,
		META_PROPERTY_NAME,
		META_PROPERTY_NAMESPACE,
		META_PROPERTY_VERSION,
		META_PROPERTY_MODEL_SUB_TYPE,
		META_PROPERTY_MODEL_TYPE
	));

}
