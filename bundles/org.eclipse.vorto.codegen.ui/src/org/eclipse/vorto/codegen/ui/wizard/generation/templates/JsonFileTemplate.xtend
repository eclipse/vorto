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
 
 package org.eclipse.vorto.codegen.ui.wizard.generation.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.ui.context.IGeneratorProjectContext

class JsonFileTemplate implements ITemplate<IGeneratorProjectContext> {
	
	public def String printTribleQuotationMarks() {
		return "'''";
	}
	
	public def String printEntryArrows() {
		return "«";
	}
	
	public def String printExitArrows() {
		return "»";
	}

	public override String getContent(IGeneratorProjectContext metaData) {
		return '''
		package «metaData.getPackageName»
		
		import org.eclipse.core.runtime.IProgressMonitor
		import org.eclipse.emf.common.util.EList
		import org.eclipse.vorto.codegen.api.ICodeGenerator
		import org.eclipse.vorto.codegen.api.tasks.Generated
		import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask
		import org.eclipse.vorto.codegen.api.tasks.IOutputter
		import org.eclipse.vorto.codegen.api.tasks.eclipse.EclipseProjectGenerator
		import org.eclipse.vorto.core.api.model.datatype.Entity
		import org.eclipse.vorto.core.api.model.datatype.Enum
		import org.eclipse.vorto.core.api.model.datatype.EnumLiteral
		import org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType
		import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
		import org.eclipse.vorto.core.api.model.datatype.Property
		import org.eclipse.vorto.core.api.model.datatype.PropertyType
		import org.eclipse.vorto.core.api.model.functionblock.Event
		import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
		import org.eclipse.vorto.core.api.model.functionblock.Operation
		import org.eclipse.vorto.core.api.model.functionblock.Param
		import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
		import org.eclipse.vorto.core.api.model.functionblock.RefParam
		import org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
		import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
		import org.eclipse.vorto.core.api.model.functionblock.ReturnType
		import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
		import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
		
		/* 
		 * For a realistic business scenario we would recommend to use a json processor framework
		 * like jackson to create json code out of a model. However, as we wanted to have an easily 
		 * understandable example at this place we decided to illustrate it using xtend without
		 * having any dependencies to third party frameworks.
		 */
		class JsonGenerator implements ICodeGenerator<InformationModel> {
		    override generate(InformationModel model, IProgressMonitor monitor) {
				new EclipseProjectGenerator("org.eclipse.vorto."+model.getName().toLowerCase()+".json")
					.addTask(new CreateJsonRepresentation()).generate(model, monitor);
		    }
			    static class CreateJsonRepresentation implements ICodeGeneratorTask<InformationModel> {
			        override generate(InformationModel model, IOutputter outputter) {
			        	for (FunctionblockProperty functionblockProperty : model.properties) {
			        		var functionBlock = functionblockProperty.type;
				            outputter.output(
				                new Generated(functionBlock.name + ".json", null,
				                    getJsonRepresentation(functionBlock)))
			            }
			        }
			        def String getJsonRepresentation(FunctionblockModel model) {
			            «printTribleQuotationMarks()»
			                {
			                    "functionblock model": {
			                        "namespace": "«printEntryArrows()»model.namespace«printExitArrows()»",
			                        "version": "«printEntryArrows()»model.version«printExitArrows()»",
			                        "name": "«printEntryArrows()»model.name«printExitArrows()»",
			                        "functionblock": {
			                            "displayname": "«printEntryArrows()»model.functionblock.displayname«printExitArrows()»",
			                            "category": "«printEntryArrows()»model.functionblock.category«printExitArrows()»",
			                            "configuration": [«printEntryArrows()»printConfigurationBlock(model)«printExitArrows()»],
			                            "fault": [«printEntryArrows()»printFaultBlock(model)«printExitArrows()»],
			                            "status": [«printEntryArrows()»printStatusBlock(model)«printExitArrows()»],
			                            "events": [«printEntryArrows()»printEvents(model)«printExitArrows()»],
			                            "operations": [«printEntryArrows()»printOperations(model)«printExitArrows()»]
			                        }«printEntryArrows()»printEntityBlockIfNecessary(model)«printExitArrows()»«printEntryArrows()»printEnumBlockIfNecessary(model)«printExitArrows()»
			                    }
			                }
			            «printTribleQuotationMarks()»
			        }
			        def printConfigurationBlock(FunctionblockModel model) {
			            if(model.functionblock.configuration != null) printProperties(model.functionblock.configuration.properties)
			        }
			        def printFaultBlock(FunctionblockModel model) {
			            if(model.functionblock.fault != null) printProperties(model.functionblock.fault.properties)
			        }
			        def printStatusBlock(FunctionblockModel model) {
			            if(model.functionblock.status != null) printProperties(model.functionblock.status.properties)
			        }
			        def printEnumBlockIfNecessary(FunctionblockModel model) {
			            if (model.enums.nullOrEmpty == false) «printTribleQuotationMarks()»
			                ,
			                "enums": [«printEntryArrows()»printEnums(model)«printExitArrows()»]
			            «printTribleQuotationMarks()»
			        }
			        def printEntityBlockIfNecessary(FunctionblockModel model) {
			            if (model.entities.nullOrEmpty == false) «printTribleQuotationMarks()»
			                ,
			                "entities": [«printEntryArrows()»printEntities(model)«printExitArrows()»]
			            «printTribleQuotationMarks()»
			        }
			        def printEntities(FunctionblockModel model) «printTribleQuotationMarks()»
			             
			                «printEntryArrows()»FOR Entity entity : model.entities«printExitArrows()»
			                    {
			                        "name": "«printEntryArrows()»entity.name«printExitArrows()»",
			                        "superType": "«printEntryArrows()»getSuperType(entity)«printExitArrows()»",
			                        "properties": [«printEntryArrows()»printProperties(entity.properties)«printExitArrows()»]
			                    }«printEntryArrows()»getComma(model.entities, entity)«printExitArrows()»
			                «printEntryArrows()»ENDFOR«printExitArrows()»
			        «printTribleQuotationMarks()»
			        def printEnums(FunctionblockModel model) «printTribleQuotationMarks()»
			             
			                «printEntryArrows()»FOR Enum enumVar : model.enums«printExitArrows()»
			                    {
			                        "name": "«printEntryArrows()»enumVar.name«printExitArrows()»",
			                        "literals ": [«printEntryArrows()»getLiterals(enumVar.enums)«printExitArrows()»]
			                    }«printEntryArrows()»getComma(model.enums, enumVar)«printExitArrows()»
			                «printEntryArrows()»ENDFOR«printExitArrows()»
			        «printTribleQuotationMarks()»
			        def getSuperType(Entity entity) {
			            if (entity.superType == null) {
			                «printTribleQuotationMarks()»not existing«printTribleQuotationMarks()»
			            } else {
			                «printTribleQuotationMarks()»«printEntryArrows()»entity.superType.name«printExitArrows()»«printTribleQuotationMarks()»
			            }
			        }
			        def printEvents(FunctionblockModel model) {
			            if (model.functionblock.events.nullOrEmpty == false) {
			                «printTribleQuotationMarks()»
			                     
			                        «printEntryArrows()»FOR Event event : model.functionblock.events«printExitArrows()»
			                            {
			                                "name": "«printEntryArrows()»event.name«printExitArrows()»",
			                                "properties ": [«printEntryArrows()»printProperties(model.functionblock.status.properties)«printExitArrows()»]
			                            }«printEntryArrows()»getComma(model.functionblock.events, event)«printExitArrows()»
			                        «printEntryArrows()»ENDFOR«printExitArrows()»
			                «printTribleQuotationMarks()»
			            }
			        }
			        def printOperations(FunctionblockModel model) {
			            if (model.functionblock.operations.nullOrEmpty == false) {
			                «printTribleQuotationMarks()»
			                     
			                        «printEntryArrows()»FOR Operation operation : model.functionblock.operations«printExitArrows()»
			                            {
			                                "method name": "«printEntryArrows()»operation.name«printExitArrows()»",
			                                "parameter": [«printEntryArrows()»printParameter(operation.params)«printExitArrows()»],
			                                "return type": "«printEntryArrows()»getReturnType(operation.returnType)«printExitArrows()»",
			                                "description": "«printEntryArrows()»getOperationDescription(operation)«printExitArrows()»"
			                            }«printEntryArrows()»getComma(model.functionblock.operations, operation)«printExitArrows()»
			                        «printEntryArrows()»ENDFOR«printExitArrows()»
			                «printTribleQuotationMarks()»
			            }
			        }
			        def getLiterals(EList<EnumLiteral> enumList) {
			            «printTribleQuotationMarks()»
			                    «printEntryArrows()»FOR EnumLiteral literal : enumList«printExitArrows()»
			                        "«printEntryArrows()»literal.name«printExitArrows()»"«printEntryArrows()»getComma(enumList, literal)«printExitArrows()»
			                    «printEntryArrows()»ENDFOR«printExitArrows()»
			            «printTribleQuotationMarks()»
			        }
			        def printParameter(EList<Param> params) {
			            if (params.nullOrEmpty == false) {
			                «printTribleQuotationMarks()»
			                     
			                        «printEntryArrows()»FOR Param param : params«printExitArrows()»
			                            {
			                                "multiple": "«printEntryArrows()»getMultipleAsBoolean(param)«printExitArrows()»",
			                                "name": "«printEntryArrows()»param.name«printExitArrows()»",
			                                "parameter type": "«printEntryArrows()»getParamType(param)«printExitArrows()»"
			                            }«printEntryArrows()»getComma(params, param)«printExitArrows()»
			                        «printEntryArrows()»ENDFOR«printExitArrows()»
			                «printTribleQuotationMarks()»
			            }
			        }
			        def printProperties(EList<Property> properties) {
			            if (properties.isEmpty == false) {
			                «printTribleQuotationMarks()»
			                     
			                        «printEntryArrows()»FOR Property property : properties«printExitArrows()»
			                            {
			                                "presence": "«printEntryArrows()»IF property.presence.mandatory«printExitArrows()»mandatory«printEntryArrows()»ELSE«printExitArrows()»optional«printEntryArrows()»ENDIF«printExitArrows()»",
			                                "multiple": "«printEntryArrows()»getMultipleAsBoolean(property)«printExitArrows()»",
			                                "property name": "«printEntryArrows()»property.name«printExitArrows()»",
			                                "type": "«printEntryArrows()»getPropertyType(property)«printExitArrows()»",
			                                "description": "«printEntryArrows()»getPropertyDescription(property)«printExitArrows()»"
			                            }«printEntryArrows()»getComma(properties, property)«printExitArrows()»
			                        «printEntryArrows()»ENDFOR«printExitArrows()»
			                «printTribleQuotationMarks()»
			            }
			        }
			        def <T extends Object> getComma(EList<T> list, T actualObject) {
			            if(list.indexOf(actualObject) == (list.size - 1)) '' else ','
			        }
			        def getOperationDescription(Operation operation) {
			            if(operation.description.nullOrEmpty == false) operation.description
			        }
			        def getPropertyDescription(Property property) {
			            if(property.description.nullOrEmpty == false) property.description
			        }
					def getPropertyType(Property property) {
		    			val PropertyType type = property.type;
		       				if (type instanceof PrimitivePropertyType)
		       				(type as PrimitivePropertyType).type.getName()
		        			else if (type instanceof ObjectPropertyType)
		            			(type as ObjectPropertyType).type.getName()
					}
			        def getReturnType(ReturnType returnType) {
			            if (returnType instanceof ReturnPrimitiveType)
			                (returnType as ReturnPrimitiveType).returnType.getName()
			            else if (returnType instanceof ReturnObjectType)
			                (returnType as ReturnObjectType).returnType.getName()
			        }
			        def getParamType(Param parameter) {
			            if (parameter instanceof PrimitiveParam)
			                (parameter as PrimitiveParam).type.getName()
			            else if (parameter instanceof RefParam)
			                (parameter as RefParam).type.getName()
			        }
			        def getMultipleAsBoolean(Param param) {
			            if(param.multiplicity) 'false' else 'true'
			        }
			        def getMultipleAsBoolean(Property property) {
			            if(property.multiplicity) 'false' else 'true'
			        }
			    }
			    override getName() {
					return "«metaData.generatorName»";
				}
			}
		'''
	}		
}

