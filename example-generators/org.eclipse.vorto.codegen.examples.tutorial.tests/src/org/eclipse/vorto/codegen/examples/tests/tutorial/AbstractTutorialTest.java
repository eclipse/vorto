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
package org.eclipse.vorto.codegen.examples.tests.tutorial;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.vorto.codegen.examples.tests.tutorial.helper.PathModelResolver;
import org.eclipse.vorto.core.api.model.datatype.impl.DatatypePackageImpl;
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.model.MappingResourceFactory;
import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup;
import org.eclipse.vorto.editor.functionblock.FunctionblockStandaloneSetup;
import org.eclipse.vorto.editor.infomodel.InformationModelStandaloneSetup;
import org.eclipse.vorto.editor.mapping.MappingStandaloneSetup;
import org.junit.Before;


public class AbstractTutorialTest {
	protected static final String INFOMODEL_FILE = "resources/examples/MappingExamples/LWM2M/src/models/MyQuadcopter.infomodel";
	protected static final String SHARED_MODELS_DIRECTORY = "resources/examples/MappingExamples/LWM2M/src/shared_models/";
	protected static final String SHARED_MAPPING_DIRECTORY = "resources/examples/MappingExamples/LWM2M/src/mappings/";
	
	@Before
	public void setup() {
		MappingStandaloneSetup.doSetup();
		FunctionblockStandaloneSetup.doSetup();
		InformationModelStandaloneSetup.doSetup();
		DatatypeStandaloneSetup.doSetup();

		InformationModelPackageImpl.init();
		FunctionblockPackageImpl.init();
		MappingPackageImpl.init();
		DatatypePackageImpl.init();
	}
	
	protected InformationModel createInformationModel(){
		File infoModelFile = toTutorialFilePath(new File(INFOMODEL_FILE));
		File tutorialPluginSharedModelsDirectory = toTutorialFilePath(new File(SHARED_MODELS_DIRECTORY));
		return new PathModelResolver<InformationModel>().resolveModel(infoModelFile, tutorialPluginSharedModelsDirectory.toPath().getParent());
	}
	
	protected MappingModel createMappingModel(String mappingFileName){
		File modelFile = toTutorialFilePath(new File(SHARED_MAPPING_DIRECTORY
				+ mappingFileName));
		File tutorialPluginSharedModelsDirectory = toTutorialFilePath(new File(SHARED_MAPPING_DIRECTORY));
		return new PathModelResolver<MappingModel>().resolveModel(modelFile, tutorialPluginSharedModelsDirectory.toPath().getParent());
	}
	
	protected IMapping createMapping(String mappingFileName){
		MappingModel mappingModel = createMappingModel(mappingFileName);
		return MappingResourceFactory.getInstance().createMapping(mappingModel, new ArrayList<IMapping>());
	}
	
	private File toTutorialFilePath(File file){
		 String fileAbsolutePath = file.getAbsolutePath();
		 String fileAbsolutePathFromTutorial = fileAbsolutePath.replaceAll("org.eclipse.vorto.codegen.examples.tutorial.tests", "org.eclipse.vorto.codegen.examples.tutorial");
		 return new File(fileAbsolutePathFromTutorial);
	}
}
