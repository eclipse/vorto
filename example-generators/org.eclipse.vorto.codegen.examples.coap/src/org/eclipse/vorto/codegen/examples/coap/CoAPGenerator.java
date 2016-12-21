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
package org.eclipse.vorto.codegen.examples.coap;

import org.eclipse.vorto.codegen.api.ChainedCodeGeneratorTask;
import org.eclipse.vorto.codegen.api.GenerationResultZip;
import org.eclipse.vorto.codegen.api.GeneratorTaskFromFileTemplate;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException;
import org.eclipse.vorto.codegen.examples.coap.client.tasks.CoAPClientFunctionblockImplGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.client.tasks.CoAPClientFunctionblockInterfaceGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.client.tasks.CoAPClientInformationModelGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.client.templates.ClientDemoAppTemplate;
import org.eclipse.vorto.codegen.examples.coap.client.templates.ClientTemplate;
import org.eclipse.vorto.codegen.examples.coap.client.templates.CoAPMethodTemplate;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaClassGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaEnumGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaFBOperationParamSetGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaFBOperationReturnPrimitiveTypeWrapperGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaFBPropertyPrimitiveParamWrapperGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaFunctionblockImplGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaFunctionblockInterfaceGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaInformationModelGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.tasks.JavaInformationModelInterfaceGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.common.templates.PomFileTemplate;
import org.eclipse.vorto.codegen.examples.coap.server.tasks.CoAPServerGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.server.tasks.CoAPServerIMRequestHandlerGeneratorTask;
import org.eclipse.vorto.codegen.examples.coap.server.templates.CoAPServerDemoAppTemplate;
import org.eclipse.vorto.codegen.examples.coap.server.templates.ICoAPRequestHandlerTemplate;
import org.eclipse.vorto.codegen.examples.coap.server.templates.JsonTransformerTemplate;
import org.eclipse.vorto.codegen.examples.coap.server.templates.LinkTemplate;
import org.eclipse.vorto.codegen.examples.coap.server.templates.ResourceTemplate;
import org.eclipse.vorto.codegen.examples.coap.server.templates.URIAnalyzerTemplate;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.Operation;
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;


public class CoAPGenerator implements IVortoCodeGenerator {
	
	public static final String COAP_CLIENT_NAME 					= "Client";
	public static final String COAP_SERVER_NAME 					= "Server";
	public static final String COAP_REQUEST_HANDLER_NAME 			= "CoAPRequestHandler";
	public static final String COAP_PARAM_SET_SUFFIX				= "ParamSet";
	public static final String COAP_PRIM_TYPE_WRAPPER_SUFFIX		= "PrimTypeWrapper";
	
	public static final String COAP_SERVER_PROJECT_SUFFIX 			= "_CoAP_Server";
	public static final String COAP_CLIENT_PROJECT_SUFFIX			= "_CoAP_Client";
	public static final String JAVA_FILE_EXTENSION 					= ".java";
	public static final String JAVA_INTERFACE_PREFIX 				= "I";
	public static final String JAVA_IMPL_SUFFIX 					= "Impl";
	public static final String GETTER_PREFIX 						= "get";
	public static final String SETTER_PREFIX 						= "set";
	
	// Packages
	public static final String BASE_PACKAGE							= "org.eclipse.vorto.";
	
	public static final String COAP_SERVER_PACKAGE 					= BASE_PACKAGE 		+ "coap.server";
	public static final String COAP_CLIENT_PACKAGE 					= BASE_PACKAGE 		+ "coap.client";
	public static final String COAP_REQUEST_HANDLER_PACKAGE			= BASE_PACKAGE 		+ "coap";
	public static final String COAP_DEMO_PACKAGE					= BASE_PACKAGE 		+ "demo";
	
	public static final String MODEL_PACKAGE 						= BASE_PACKAGE 		+ "model.";
	public static final String DT_PACKAGE 							= MODEL_PACKAGE 	+ "datatypes";
	public static final String FB_PACKAGE 							= MODEL_PACKAGE 	+ "functionblocks";
	public static final String FB_IMPL_PACKAGE						= FB_PACKAGE		+ ".implementation";
	public static final String FB_INTERFACE_PACKAGE					= FB_PACKAGE		+ ".interfaces";
	public static final String IM_PACKAGE 							= MODEL_PACKAGE		+ "informationmodels";
	public static final String OP_PARAM_SET_PACKAGE 				= MODEL_PACKAGE 	+ "operationparameterset";
	public static final String PRIMITIVE_TYPE_WRAPPER_PACKAGE		= MODEL_PACKAGE 	+ "primitivetypewrapper";
	
	// Paths
	private String javaSrcLoc;
	private String basePath;
	
	private String coapServerPath;
	private String coapClientPath;
	private String coapRequestHandlerPath;
	private String coapDemoPath;
	
	private String modelPath;
	private String primitiveTypeWrapperTargetPath;
	private String opParameterSetTargetPath;
	private String entityTargetPath;
	private String fbTargetPath;
	private String fbImplTargetPath;
	private String fbInterfaceTargetPath;
	private String imTargetPath;
		
	private Boolean opParamSetRequired = false;
	private Boolean primTypeWrapperRequired = false;
	
	public IGenerationResult generate(InformationModel infomodel, InvocationContext mappingContext,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		
		GenerationResultZip zipOutputter = new GenerationResultZip(infomodel,getServiceKey());
		
		ChainedCodeGeneratorTask<InformationModel> generator = new ChainedCodeGeneratorTask<InformationModel>();
		
		/*
		/ Generate the client part...
		*/
		String CLIENT_PROJ = infomodel.getName() + COAP_CLIENT_PROJECT_SUFFIX;
		initPaths(CLIENT_PROJ);
		
		generator.addTask(
			new CoAPClientInformationModelGeneratorTask(
				JAVA_FILE_EXTENSION, 
				imTargetPath, 
				IM_PACKAGE,
				JAVA_INTERFACE_PREFIX, 
				JAVA_IMPL_SUFFIX, 
				GETTER_PREFIX, 
				SETTER_PREFIX,
				FB_INTERFACE_PACKAGE, 
				FB_IMPL_PACKAGE)
			);
		generator.addTask(
			new JavaInformationModelInterfaceGeneratorTask(
				JAVA_FILE_EXTENSION,  
				imTargetPath,
				IM_PACKAGE,
				JAVA_INTERFACE_PREFIX, 
				GETTER_PREFIX, 
				SETTER_PREFIX, 
				FB_INTERFACE_PACKAGE)
			);
		generator.addTask(
				new GeneratorTaskFromFileTemplate<InformationModel>(
					new ClientTemplate(
							coapClientPath, 
							COAP_CLIENT_PACKAGE)
					)
				);
		generator.addTask(
				new GeneratorTaskFromFileTemplate<InformationModel>(
					new CoAPMethodTemplate(
							coapClientPath, 
							COAP_CLIENT_PACKAGE)
					)
				);
		generator.addTask(
				new GeneratorTaskFromFileTemplate<InformationModel>(
					new ClientDemoAppTemplate(
							coapDemoPath, 
							COAP_DEMO_PACKAGE)
					)
				);
		generator.addTask(
				new GeneratorTaskFromFileTemplate<InformationModel>(
					new PomFileTemplate("artifact",
							COAP_DEMO_PACKAGE + ".ClientDemoApp", 
							CLIENT_PROJ)
					)
				);
		generator.generate(infomodel ,mappingContext, zipOutputter);
		
		for (FunctionblockProperty fbp : infomodel.getProperties()) {
			
			FunctionBlock fb = fbp.getType().getFunctionblock();
			for (Entity entity : Utils.getReferencedEntities(fb)) {
				generateForEntity(entity, zipOutputter);
			}
			for (Enum en : Utils.getReferencedEnums(fb)) {
				generateForEnum(en, zipOutputter);
			}
			
			for (Operation op : fb.getOperations()) {
				generateForOperation(op, zipOutputter);
			}
			
			if (fb.getStatus() != null){
				for (Property property : fb.getStatus().getProperties()){
					generateForProperty(property, zipOutputter);
				}
			}
			
			this.generateForClientFunctionBlock(fbp.getType(), zipOutputter);
		}

		/*
		/ Generate the server part...
		*/
		String SERVER_PROJ = infomodel.getName() + COAP_SERVER_PROJECT_SUFFIX;
		initPaths(SERVER_PROJ);
		
		
		generator.addTask(
			new CoAPServerGeneratorTask(
				COAP_SERVER_NAME, 
				COAP_SERVER_PACKAGE, 
				JAVA_FILE_EXTENSION, 
				coapServerPath, 
				JAVA_INTERFACE_PREFIX + COAP_REQUEST_HANDLER_NAME,
				COAP_REQUEST_HANDLER_PACKAGE)
			);
		
		
		generator.addTask(
			new CoAPServerIMRequestHandlerGeneratorTask(
				COAP_REQUEST_HANDLER_NAME,
				JAVA_FILE_EXTENSION,
				coapRequestHandlerPath,
				COAP_REQUEST_HANDLER_PACKAGE,
				JAVA_INTERFACE_PREFIX,
				COAP_PRIM_TYPE_WRAPPER_SUFFIX,
				IM_PACKAGE,
				DT_PACKAGE,
				getOpParamSetPackage(),
				getPrimitiveParamWrapperPackage())
			);
		generator.addTask(
			new JavaInformationModelGeneratorTask(
				JAVA_FILE_EXTENSION,  
				imTargetPath,
				IM_PACKAGE,
				JAVA_INTERFACE_PREFIX, 
				JAVA_IMPL_SUFFIX, 
				GETTER_PREFIX, 
				SETTER_PREFIX, 
				FB_INTERFACE_PACKAGE, FB_IMPL_PACKAGE)
			);
		generator.addTask(
			new JavaInformationModelInterfaceGeneratorTask(
				JAVA_FILE_EXTENSION,  
				imTargetPath,
				IM_PACKAGE,
				JAVA_INTERFACE_PREFIX, 
				GETTER_PREFIX, 
				SETTER_PREFIX, 
				FB_INTERFACE_PACKAGE, FB_IMPL_PACKAGE)
			);
		generator.addTask(
			new GeneratorTaskFromFileTemplate<InformationModel>(
				new ICoAPRequestHandlerTemplate(
						coapRequestHandlerPath, 
						COAP_REQUEST_HANDLER_PACKAGE)
				)
			);
		generator.addTask(
			new GeneratorTaskFromFileTemplate<InformationModel>(
				new JsonTransformerTemplate(
						coapRequestHandlerPath, 
						COAP_REQUEST_HANDLER_PACKAGE)
				)
			);
		generator.addTask(
			new GeneratorTaskFromFileTemplate<InformationModel>(
				new URIAnalyzerTemplate(
						coapRequestHandlerPath, 
						COAP_REQUEST_HANDLER_PACKAGE)
				)
			);
		generator.addTask(
			new GeneratorTaskFromFileTemplate<InformationModel>(
				new ResourceTemplate(
						coapServerPath, 
						COAP_SERVER_PACKAGE, 
						COAP_REQUEST_HANDLER_PACKAGE)
				)
			);
		generator.addTask(
			new GeneratorTaskFromFileTemplate<InformationModel>(
				new LinkTemplate(
						coapServerPath, 
						COAP_SERVER_PACKAGE)
				)
			);
		generator.addTask(
			new GeneratorTaskFromFileTemplate<InformationModel>(
				new CoAPServerDemoAppTemplate(
						coapDemoPath, 
						COAP_DEMO_PACKAGE, 
						COAP_REQUEST_HANDLER_PACKAGE, 
						COAP_SERVER_PACKAGE)
				)
			);
		generator.addTask(
			new GeneratorTaskFromFileTemplate<InformationModel>(
				new PomFileTemplate(
						"artifact",
						COAP_DEMO_PACKAGE + ".ServerDemoApp", 
						SERVER_PROJ)
				)
			);
		generator.generate(infomodel ,mappingContext, zipOutputter);
		
		for (FunctionblockProperty fbp : infomodel.getProperties()) {
			
			this.generateForFunctionBlock(fbp.getType(), zipOutputter);

			FunctionBlock fb = fbp.getType().getFunctionblock();
			for (Entity entity : Utils.getReferencedEntities(fb)) {
				generateForEntity(entity, zipOutputter);
			}
			for (Enum en : Utils.getReferencedEnums(fb)) {
				generateForEnum(en, zipOutputter);
			}
			
			for (Operation op : fb.getOperations()) {
				generateForOperation(op, zipOutputter);
			}
		}
		
		
		return zipOutputter;
	}

	private void generateForFunctionBlock(FunctionblockModel fbm, IGeneratedWriter outputter) {
		new JavaFunctionblockImplGeneratorTask(
			JAVA_FILE_EXTENSION, 
			fbImplTargetPath, 
			FB_IMPL_PACKAGE,
			JAVA_INTERFACE_PREFIX, 
			JAVA_IMPL_SUFFIX, 
			DT_PACKAGE, FB_INTERFACE_PACKAGE).generate(fbm, null, outputter);
		
		new JavaFunctionblockInterfaceGeneratorTask(
			JAVA_FILE_EXTENSION, 
			fbInterfaceTargetPath, 
			FB_INTERFACE_PACKAGE,
			JAVA_INTERFACE_PREFIX, 
			DT_PACKAGE).generate(fbm, null, outputter);
	}

	private void generateForEntity(Entity entity, IGeneratedWriter outputter) {
		new JavaClassGeneratorTask(
			JAVA_FILE_EXTENSION, 
			entityTargetPath, 
			DT_PACKAGE,
			GETTER_PREFIX, 
			SETTER_PREFIX).generate(entity, null, outputter);
	}

	private void generateForEnum(Enum en, IGeneratedWriter outputter) {
		new JavaEnumGeneratorTask(
			JAVA_FILE_EXTENSION, 
			entityTargetPath, 
			DT_PACKAGE).generate(en, null, outputter);
	}
	
	private void generateForOperation(Operation op, IGeneratedWriter outputter) {
		if(op.getReturnType() != null && op.getReturnType() instanceof ReturnPrimitiveType){
			primTypeWrapperRequired = true;
			new JavaFBOperationReturnPrimitiveTypeWrapperGeneratorTask(
				op.getName() + COAP_PRIM_TYPE_WRAPPER_SUFFIX, 
				JAVA_FILE_EXTENSION, 
				primitiveTypeWrapperTargetPath, 
				PRIMITIVE_TYPE_WRAPPER_PACKAGE).generate(op, null, outputter);
		}
		if(!op.getParams().isEmpty()){
			opParamSetRequired = true;
			new JavaFBOperationParamSetGeneratorTask(
				op.getName() + COAP_PARAM_SET_SUFFIX, 
				JAVA_FILE_EXTENSION, 
				opParameterSetTargetPath, 
				OP_PARAM_SET_PACKAGE,
				DT_PACKAGE).generate(op, null, outputter);
		}
	}
	
	private void generateForProperty(Property property, IGeneratedWriter outputter) {
		if(property.getType() != null && property.getType() instanceof PrimitivePropertyType){
			new JavaFBPropertyPrimitiveParamWrapperGeneratorTask(
				property.getName() + COAP_PRIM_TYPE_WRAPPER_SUFFIX, 
				JAVA_FILE_EXTENSION, 
				primitiveTypeWrapperTargetPath, 
				PRIMITIVE_TYPE_WRAPPER_PACKAGE)
			.generate(property, null, outputter);
		}
	}
	
	private void generateForClientFunctionBlock(FunctionblockModel fbm, IGeneratedWriter outputter) {
		
		new CoAPClientFunctionblockImplGeneratorTask(
			JAVA_FILE_EXTENSION, 
			fbImplTargetPath, 
			FB_IMPL_PACKAGE,
			JAVA_INTERFACE_PREFIX, 
			JAVA_IMPL_SUFFIX, 
			COAP_PARAM_SET_SUFFIX, 
			COAP_PRIM_TYPE_WRAPPER_SUFFIX,
			DT_PACKAGE, FB_INTERFACE_PACKAGE, 
			getOpParamSetPackage(), 
			getPrimitiveParamWrapperPackage(), 
			COAP_CLIENT_PACKAGE).generate(fbm, null, outputter);
		new CoAPClientFunctionblockInterfaceGeneratorTask(
			JAVA_FILE_EXTENSION, 
			fbInterfaceTargetPath, 
			FB_INTERFACE_PACKAGE,
			JAVA_INTERFACE_PREFIX, 
			DT_PACKAGE).generate(fbm, null, outputter);
	}
	
	private void initPaths(String root){
		javaSrcLoc							= root 			+ "/src/main/java/";
		basePath							= javaSrcLoc 	+ "org/eclipse/vorto/";
		
		coapServerPath 						= basePath 		+ "coap/server";
		coapClientPath 						= basePath 		+ "coap/client";
		coapRequestHandlerPath 				= basePath 		+ "coap";
		coapDemoPath						= basePath 		+ "demo";
		
		modelPath							= basePath 		+ "model/";
		primitiveTypeWrapperTargetPath		= modelPath 	+ "primitivetypewrapper";
		opParameterSetTargetPath			= modelPath 	+ "operationparameterset";
		entityTargetPath 					= modelPath 	+ "datatypes";
		fbTargetPath 						= modelPath 	+ "functionblocks";
		fbImplTargetPath					= fbTargetPath 	+ "/implementation";
		fbInterfaceTargetPath				= fbTargetPath 	+ "/interfaces";
		imTargetPath 						= modelPath 	+ "informationmodels";
	}
	
	private String getOpParamSetPackage(){
		return opParamSetRequired? OP_PARAM_SET_PACKAGE : null;
	}
	
	private String getPrimitiveParamWrapperPackage() {
		return primTypeWrapperRequired? PRIMITIVE_TYPE_WRAPPER_PACKAGE : null;
	}
	
	@Override
	public String getServiceKey() {
		return "coap";
	}
}
