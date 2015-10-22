/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.core.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingModel;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingModel;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingModel;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.internal.model.ModelFileLookupHelper;
import org.eclipse.vorto.core.internal.model.ModelParserFactory;
import org.eclipse.vorto.core.internal.model.mapping.EntityMappingResource;
import org.eclipse.vorto.core.internal.model.mapping.EnumMappingResource;
import org.eclipse.vorto.core.internal.model.mapping.FunctionBlockMappingResource;
import org.eclipse.vorto.core.internal.model.mapping.InfoModelMappingResource;
import org.eclipse.vorto.core.parser.IModelParser;

/**
 * Factory to create IMapping
 */
public class MappingResourceFactory {

	private static MappingResourceFactory singleton = null;

	private MappingResourceFactory() {
	}

	public static MappingResourceFactory getInstance() {
		if (singleton == null) {
			singleton = new MappingResourceFactory();
		}
		return singleton;
	}

	/**
	 * Create IMapping implementation instance based on given IModelElement and targetPlatform 
	 * Search for mapping will be based on context of ownerModelElement, 
	 * eg.g if ownerModelElement is a information model project "MyDevice.infomodel", 
	 * then IMapping that defined for "MyDevice.infomodel" whose targetplatform matches given <targetPlatform> will be returned 
	 * If Current information model project contains function block e.g. "MyFunctionBlock.fbmodel", 
	 * then IMapping for function blocks will be added as child mapping for information model mapping.
	 * The search for child mapping will be recursive. e.g If "MyFunctionBlock.fbmodel" refers to entity type "Color.type", 
	 * then mapping "MyColor.mapping" defined for "Color.type" will be added as child mapping for IMapping instance of  "MyTargetFunctionBlock.mapping"
	 * @param ownerModelElement:
	 *            ModelElement that the mapping is defined for
	 * @param targetPlatform:
	 *            Target platform mapping designed for
	 * @return Instance of IMapping
	 * @throws UnsupportedOperationException
	 *             if modelProject is not supported
	 */
	public IMapping createMapping(IModelElement ownerModelElement, String targetPlatform) {
		MappingModel mappingModel = getMappingModel(ownerModelElement, targetPlatform);
		List<IMapping> referenceMappings = this.getReferenceMappings(ownerModelElement, targetPlatform);
		return createMapping(mappingModel, referenceMappings);
	}

	/**
	 * Create IMapping instance based on given Mapping Model and child IMapping 
	 * e.g. Assuming there are two model definitions,  "MyDevice.infomodel" and "MyFunctionBlock.fbmodel", and information model
	 * "MyDevice" uses function block model "MyFunctionBlock"
	 * And there are two mapping definitions for the model "MyTargetDevice.mapping", "MyTargetFunctionBlock.mapping"
	 * "MyTargetFunctionBlock.mapping" will be considered as child of "MyTargetDevice.mapping" as MyTargetFunctionBlock is child of MyDevice, 
	 * Even it is not explicitly referenced in "MyTargetDevice.mapping"
	 * The search for child mapping will be recursive. e.g If "MyFunctionBlock.fbmodel" refers to entity type "Color.type", 
	 * then mapping "MyColor.mapping" defined for "Color.type" will be added as child mapping for IMapping instance of  "MyTargetFunctionBlock.mapping"
	 * @param mappingModel: Instance of MappingModel
	 * @param referenceMappings: Mapping of direct child models 
	 * @return Instance of IMapping, with referenceMappings defined as child mapping
	 * @throws UnsupportedOperationException
	 *             if mappingModel is not supported	 * 
	 */
	public IMapping createMapping(MappingModel mappingModel, List<IMapping> referenceMappings) {

		if (isInfomationModelMapping(mappingModel)) {
			return new InfoModelMappingResource(mappingModel, referenceMappings);
		} else if (isFunctionBlockMapping(mappingModel)) {
			return new FunctionBlockMappingResource(mappingModel, referenceMappings);
		} else if (isEntityMapping(mappingModel)) {
			return new EntityMappingResource(mappingModel, referenceMappings);
		} else if (isEnumMapping(mappingModel)) {
			return new EnumMappingResource(mappingModel, referenceMappings);
		}
		throw new UnsupportedOperationException("Mapping not supported for type : " + mappingModel);
	}

	private boolean isInfomationModelMapping(Model model) {
		return (model instanceof InformationModel) || (model instanceof InfoModelMappingModel);
	}

	private boolean isFunctionBlockMapping(Model model) {
		return (model instanceof FunctionblockModel) || (model instanceof FunctionBlockMappingModel);
	}

	private boolean isEntityMapping(Model model) {
		return (model instanceof Entity) || (model instanceof EntityMappingModel);
	}

	private boolean isEnumMapping(Model model) {
		return (model instanceof Enum) || (model instanceof EnumMappingModel);
	}

	private MappingModel getMappingModel(IModelElement ownerModelElement, String targetPlatform) {
		MappingModel mappingModel = getMappingModelFromFolder(ownerModelElement, targetPlatform,
				ownerModelElement.getModelFile().getProject().getFolder("src/mappings"));

		if (mappingModel == null) {
			mappingModel = getMappingModelFromFolder(ownerModelElement, targetPlatform,
					ownerModelElement.getModelFile().getProject().getFolder("src/models"));
		}
		
		if (mappingModel == null) {
			mappingModel = getMappingModelFromFolder(ownerModelElement, targetPlatform,
					ownerModelElement.getModelFile().getProject().getFolder("src/shared_models"));
		}
		
		if (mappingModel == null) {
			mappingModel = createEmptyMappingModel(ownerModelElement.getModel());
		}
		
		return mappingModel;
	}

	private List<IMapping> getReferenceMappings(IModelElement ownerModelElement, String targetPlatform) {
		List<IMapping> referenceMappings = new ArrayList<IMapping>();
		for (IModelElement referenceModelElement : ownerModelElement.getReferences()) {
			referenceMappings.add(createMapping(referenceModelElement, targetPlatform));
		}
		return referenceMappings;
	}

	private MappingModel getMappingModelFromFolder(IModelElement ownerModelElement, String targetPlatform,
			IFolder modelPath) {
		ModelFileLookupHelper modelFileLookupHelper = new ModelFileLookupHelper(
				ownerModelElement.getModelFile().getProject());
		List<IFile> mappingFiles = modelFileLookupHelper.getFilesByExtension(modelPath, "mapping");
		return getMappingModelIfMatchesCriteria(ownerModelElement, targetPlatform, mappingFiles);
	}

	private MappingModel getMappingModelIfMatchesCriteria(IModelElement ownerModelElement, String targetPlatform,
			List<IFile> mappingFiles) {
		IModelParser modelParser = ModelParserFactory.getInstance().getModelParser();
		for (IFile file : mappingFiles) {
			MappingModel fileMappingModel = modelParser.parseModel(file, MappingModel.class);
			if (isMappingModelMatchesTargetPlatform(fileMappingModel, targetPlatform)
					&& this.isMappingModelContainsOwnerModelElement(ownerModelElement, fileMappingModel)) {
				return fileMappingModel;
			}
		}
		return null;
	}

	/**
	 * Create a empty mapping to avoid null point exception
	 * @param ownerModelElement
	 * @return
	 */
	private MappingModel createEmptyMappingModel(Model model) {
		if(this.isInfomationModelMapping(model)){
			return MappingFactory.eINSTANCE.createInfoModelMappingModel();
		}else if(this.isFunctionBlockMapping(model)){
			return MappingFactory.eINSTANCE.createFunctionBlockMappingModel();
		}else if(this.isEntityMapping(model)){
			return MappingFactory.eINSTANCE.createEntityMappingModel();
		}else if(this.isEnumMapping(model)){
			return MappingFactory.eINSTANCE.createEnumMappingModel();
		}
		throw new UnsupportedOperationException("Mapping not supported for type : " + model.getClass().getName());
	}

	private boolean isMappingModelMatchesTargetPlatform(MappingModel fileMappingModel, String targetPlatform) {
		if (StringUtils.equalsIgnoreCase(fileMappingModel.getTargetPlatform(), targetPlatform)) {
			return true;
		}
		return false;
	}

	private boolean isMappingModelContainsOwnerModelElement(IModelElement ownerModelElement,
			MappingModel fileMappingModel) {
		for (ModelReference modelReference : fileMappingModel.getReferences()) {
			ModelId ownerModelId = ModelIdFactory.newInstance(ownerModelElement.getModel());
			ModelId referenceModelId = ModelIdFactory.newInstance(ownerModelId.getModelType(), modelReference);
			if (ownerModelId.equals(referenceModelId)) {
				return true;
			}
		}
		return false;
	}
}
