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
package org.eclipse.vorto.codegen.examples.javabean;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.examples.javabean.tasks.JavaClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.javabean.tasks.JavaEnumGeneratorTask;
import org.eclipse.vorto.codegen.examples.javabean.tasks.JavaFunctionblockConfigurationGeneratorTask;
import org.eclipse.vorto.codegen.examples.javabean.tasks.JavaFunctionblockFaultGeneratorTask;
import org.eclipse.vorto.codegen.examples.javabean.tasks.JavaFunctionblockImplGeneratorTask;
import org.eclipse.vorto.codegen.examples.javabean.tasks.JavaFunctionblockInterfaceGeneratorTask;
import org.eclipse.vorto.codegen.examples.javabean.tasks.JavaFunctionblockStatusGeneratorTask;
import org.eclipse.vorto.codegen.examples.javabean.tasks.template.PomFileTemplate;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class JavabeanGenerator implements IVortoCodeGenerator {
	
	public static final String KEY = "javabean";
	
	public static final String JAVA_PROJECT_SUFFIX = "_Java";
	public static final String JAVA_FILE_EXTENSION = ".java";
	public static final String JAVA_INTERFACE_PREFIX = "I";
	public static final String JAVA_IMPL_SUFFIX = "Impl";

	public static final String ENTITY_TARGET_PATH = "java.example.model/src/main/java/model";
	public static final String FB_TARGET_PATH = "java.example.model/src/main/java/model/functionblocks";
	public static final String IM_TARGET_PATH = "java.example.model/src/main/java/model/informationmodels";

	public static final String ENTITY_PACKAGE = "model";
	public static final String FB_PACKAGE = "model.functionblocks";
	public static final String IM_PACKAGE = "model.informationmodels";

	public static final String GETTER_PREFIX = "get";
	public static final String SETTER_PREFIX = "set";
	
	public static final String CONFIG_SUFFIX = "Configuration";
	public static final String STATUS_SUFFIX = "Status";
	public static final String FAULT_SUFFIX = "Fault";

	public IGenerationResult generate(InformationModel infomodel, InvocationContext invocationContext,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		
		GenerationResultZip zipOutputter = new GenerationResultZip(infomodel,getServiceKey());
		
		for (FunctionblockProperty fbp : infomodel.getProperties()) {
			this.generateForFunctionBlock(infomodel, fbp.getType(), zipOutputter);

			FunctionBlock fb = fbp.getType().getFunctionblock();
			for (Entity entity : Utils.getReferencedEntities(fb)) {
				generateForEntity(infomodel, entity, zipOutputter);
			}
			for (Enum en : Utils.getReferencedEnums(fb)) {
				generateForEnum(infomodel, en, zipOutputter);
			}
		}
		
		new GeneratorTaskFromFileTemplate<InformationModel>(new PomFileTemplate()).generate(infomodel, invocationContext,zipOutputter);
		
		return zipOutputter;
	}

	private void generateForFunctionBlock(InformationModel infomodel, FunctionblockModel fbm,
			IGeneratedWriter outputter) {
		ChainedCodeGeneratorTask<FunctionblockModel> generator = new ChainedCodeGeneratorTask<FunctionblockModel>();
		if (fbm.getFunctionblock().getStatus() != null) {
			generator.addTask(new JavaFunctionblockStatusGeneratorTask(JAVA_FILE_EXTENSION, FB_TARGET_PATH, FB_PACKAGE,
					JAVA_INTERFACE_PREFIX, STATUS_SUFFIX, ENTITY_PACKAGE));	
		}
		if (fbm.getFunctionblock().getConfiguration() != null) {
			generator.addTask(new JavaFunctionblockConfigurationGeneratorTask(JAVA_FILE_EXTENSION, FB_TARGET_PATH, FB_PACKAGE,
					JAVA_INTERFACE_PREFIX, CONFIG_SUFFIX, ENTITY_PACKAGE));	
		}
		if (fbm.getFunctionblock().getFault() != null) {
			generator.addTask(new JavaFunctionblockFaultGeneratorTask(JAVA_FILE_EXTENSION, FB_TARGET_PATH, FB_PACKAGE,
					JAVA_INTERFACE_PREFIX, FAULT_SUFFIX, ENTITY_PACKAGE));	
		}
		generator.addTask(new JavaFunctionblockImplGeneratorTask(JAVA_FILE_EXTENSION, FB_TARGET_PATH, FB_PACKAGE,
				JAVA_INTERFACE_PREFIX, JAVA_IMPL_SUFFIX, ENTITY_PACKAGE));
		generator.addTask(new JavaFunctionblockInterfaceGeneratorTask(JAVA_FILE_EXTENSION, FB_TARGET_PATH, FB_PACKAGE, JAVA_INTERFACE_PREFIX, ENTITY_PACKAGE));
		generator.generate(fbm,null, outputter);
	}

	private void generateForEntity(InformationModel infomodel, Entity entity, IGeneratedWriter outputter) {
		new JavaClassGeneratorTask(JAVA_FILE_EXTENSION, ENTITY_TARGET_PATH, ENTITY_PACKAGE,
				GETTER_PREFIX, SETTER_PREFIX).generate(entity, null, outputter);
	}

	private void generateForEnum(InformationModel infomodel, Enum en, IGeneratedWriter outputter) {
		new JavaEnumGeneratorTask(JAVA_FILE_EXTENSION, ENTITY_TARGET_PATH, ENTITY_PACKAGE).generate(en,null, outputter);
				
	}

	@Override
	public String getServiceKey() {
		return KEY;
	}

	

}
