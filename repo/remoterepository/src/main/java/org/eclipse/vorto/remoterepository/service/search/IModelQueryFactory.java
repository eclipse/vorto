package org.eclipse.vorto.remoterepository.service.search;

import org.eclipse.vorto.remoterepository.model.ModelType;

/**
 * Factory for model query
 * 
 *
 */
public interface IModelQueryFactory {
	/**
	 * Returns a new model query
	 * 
	 * @return
	 */
	IModelQuery newModelQuery();

	/**
	 * Returns a new model query predicated to a particular model type
	 * 
	 * @param modelType
	 * @return
	 */
	IModelQuery newModelQuery(ModelType modelType);
}
