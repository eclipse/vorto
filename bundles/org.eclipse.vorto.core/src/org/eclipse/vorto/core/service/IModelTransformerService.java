package org.eclipse.vorto.core.service;

import java.io.InputStream;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * Provide transformation for model from one representation to another
 */
public interface IModelTransformerService {

	/**
	 * Transform model from xmi format to dsl format
	 * @param xmiStream
	 * @return stirng in dsl format
	 */
	String xmiToDsl(InputStream xmiStream);
	
	/**
	 * Transform model in XMI format to EMF Object model
	 * @param xmiStream: stream in XMI format
	 * @param modelClass: Class of EMF model to transform to
	 * @return
	 */
	<M extends Model> M xmiToModel(InputStream xmiStream, Class<M> modelClass);

	/**
	 * serialize emf model to Xmi representation
	 * 
	 * @param model
	 * @return XMI String of the model
	 */
	String toXmi(Model model);

	/**
	 * serialize emf model to xtext dsl representation
	 * 
	 * @param model
	 * @return DSL string of the model
	 */
	String toDsl(Model model);
}

