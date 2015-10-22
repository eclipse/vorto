/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.java;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator;
import org.eclipse.vorto.codegen.examples.java.tasks.JavaClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.java.tasks.JavaEnumGeneratorTask;
import org.eclipse.vorto.codegen.examples.java.tasks.JavaFunctionblockImplGeneratorTask;
import org.eclipse.vorto.codegen.examples.java.tasks.JavaFunctionblockInterfaceGeneratorTask;
import org.eclipse.vorto.codegen.examples.java.tasks.JavaInformationModelGeneratorTask;
import org.eclipse.vorto.codegen.templates.java.utils.ModelHelper;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

public class JavaGenerator implements ICodeGenerator<InformationModel> {

	public static final String JAVA_PROJECT_SUFFIX = "_Java";
	public static final String JAVA_FILE_EXTENSION = ".java";
	public static final String JAVA_INTERFACE_PREFIX = "I";
	public static final String JAVA_IMPL_SUFFIX = "Impl";

	public static final String ENTITY_TARGET_PATH = "src/entities";
	public static final String FB_TARGET_PATH = "src/functionblocks";
	public static final String IM_TARGET_PATH = "src/informationmodels";

	public static final String ENTITY_PACKAGE = "entities";
	public static final String FB_PACKAGE = "functionblocks";
	public static final String IM_PACKAGE = "informationmodels";

	public static final String GETTER_PREFIX = "get";
	public static final String SETTER_PREFIX = "set";

	@Override
	public void generate(InformationModel infomodel, IProgressMonitor monitor) {
		new EclipseProjectGenerator<InformationModel>(infomodel.getName() + JAVA_PROJECT_SUFFIX)
				.addTask(new JavaInformationModelGeneratorTask(JAVA_FILE_EXTENSION, IM_TARGET_PATH, IM_PACKAGE,
						JAVA_INTERFACE_PREFIX, JAVA_IMPL_SUFFIX, GETTER_PREFIX, SETTER_PREFIX, FB_PACKAGE))
				.javaNature("src").generate(infomodel, monitor);

		for (FunctionblockProperty fbp : infomodel.getProperties()) {
			this.generateForFunctionBlock(infomodel, fbp.getType(), monitor);

			FunctionBlock fb = fbp.getType().getFunctionblock();
			for (Entity entity : ModelHelper.getReferencedEntities(fb)) {
				generateForEntity(infomodel, entity, monitor);
			}
			for (Enum en : ModelHelper.getReferencedEnums(fb)) {
				generateForEnum(infomodel, en, monitor);
			}
		}
	}

	private void generateForFunctionBlock(InformationModel infomodel, FunctionblockModel fbm,
			IProgressMonitor monitor) {
		new EclipseProjectGenerator<FunctionblockModel>(infomodel.getName() + JAVA_PROJECT_SUFFIX)
				.addTask(new JavaFunctionblockImplGeneratorTask(JAVA_FILE_EXTENSION, FB_TARGET_PATH, FB_PACKAGE,
						JAVA_INTERFACE_PREFIX, JAVA_IMPL_SUFFIX, ENTITY_PACKAGE))
				.addTask(new JavaFunctionblockInterfaceGeneratorTask(JAVA_FILE_EXTENSION, FB_TARGET_PATH, FB_PACKAGE,
						JAVA_INTERFACE_PREFIX, ENTITY_PACKAGE))
				.generate(fbm, monitor);
	}

	private void generateForEntity(InformationModel infomodel, Entity entity, IProgressMonitor monitor) {
		new EclipseProjectGenerator<Entity>(infomodel.getName() + JAVA_PROJECT_SUFFIX)
				.addTask(new JavaClassGeneratorTask(JAVA_FILE_EXTENSION, ENTITY_TARGET_PATH, ENTITY_PACKAGE,
						GETTER_PREFIX, SETTER_PREFIX))
				.generate(entity, monitor);
	}

	private void generateForEnum(InformationModel infomodel, Enum en, IProgressMonitor monitor) {
		new EclipseProjectGenerator<Enum>(infomodel.getName() + JAVA_PROJECT_SUFFIX)
				.addTask(new JavaEnumGeneratorTask(JAVA_FILE_EXTENSION, ENTITY_TARGET_PATH, ENTITY_PACKAGE))
				.generate(en, monitor);
	}

	@Override
	public String getName() {
		return "" + "Java Generator";
	}
}
