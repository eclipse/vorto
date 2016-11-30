package org.eclipse.vorto.codegen.examples.thingworx

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import java.io.StringWriter
import org.eclipse.emf.common.util.EList
import org.eclipse.vorto.codegen.api.Generated
import org.eclipse.vorto.codegen.api.GenerationResultZip
import org.eclipse.vorto.codegen.api.ICodeGeneratorTask
import org.eclipse.vorto.codegen.api.IGeneratedWriter
import org.eclipse.vorto.codegen.api.IVortoCodeGenProgressMonitor
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.api.VortoCodeGeneratorException
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
import org.eclipse.vorto.core.api.model.functionblock.ReturnType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingWorxCodeGenerator implements IVortoCodeGenerator {

	private enum VortoPropertyType {
		STATUS, CONFIGURATION	
	}
	
	override generate(InformationModel model, InvocationContext invocationContext,
			IVortoCodeGenProgressMonitor monitor) throws VortoCodeGeneratorException {
		var zipOutput = new GenerationResultZip(model,getServiceKey());
		new JSONGeneratorTask().generate(model,invocationContext,zipOutput);
		return zipOutput;
	}
	
	public static class JSONGeneratorTask implements ICodeGeneratorTask<InformationModel> {
		
		override generate(InformationModel model, InvocationContext invocationContext, IGeneratedWriter writer) {
			
			writer.write(new Generated(model.getName() + ".json", null, getContent(model)));
		}

		def String getContent(InformationModel model) {
			// Build JSON with Jackson - @TODO: determine IIC best practice for specifying dependencies.
			var JsonFactory f = new JsonFactory()
			var json = ""
			try {
				var StringWriter writer = new StringWriter()
				var JsonGenerator g = f.createGenerator(writer)
				g.writeStartObject() // Start of JSON object
				// Generate the ThingTemplate
				generateThingTemplate(g, model)
				// Get JSON
				g.writeEndObject() //End of JSON object
				g.flush()
				json = writer.toString()
				// Clean up
				g.close()
				writer.close()

			} catch (Exception e) {
				return e.message
			}

			return json
		}

		def generateThingTemplate(JsonGenerator g, InformationModel model) {
			g.writeObjectFieldStart("thingTemplate")
			// ThingTemplate Attributes
			g.writeStringField("name", model.displayname) // name is also available
			g.writeStringField("baseThingTemplate", "RemoteThing")
			g.writeStringField("description", model.description)
			g.writeStringField("tags", "Applications:Vorto_CodeGen")

			/*
			 * The current Vorto IDL for Information models does not enable property (configuration and 
			 * status), service, event, or alert definition at the infomodel level. The ThingWorx object model, 
			 * however, allows definition of all those things at the ThingTemplate level. We'll write out a 
			 * skeleton for these things to make it easier on the developers on the ThingWorx extension side.
			 * They won't have values in this version, though. 
			 */
			g.writeArrayFieldStart("propertyDefinitions")
			g.writeEndArray // End of Property Definitions
			g.writeArrayFieldStart("serviceDefinitions")
			g.writeEndArray // End of Service Definitions
			g.writeArrayFieldStart("eventDefinitions")
			g.writeEndArray // End of Event Definitions
			g.writeObjectFieldStart("alertConfigurations")
			g.writeEndObject // End of alertConfigurations
			/* Implemented Shapes - include the Function Block models of which this 
			 * Information Block model is composed */
			enumerateImplementedShapes(g, model) // End of implementedShapes
			g.writeEndObject // End of thingTemplate
		}

		protected def enumerateImplementedShapes(JsonGenerator g, InformationModel model) {
			g.writeArrayFieldStart("implementedShapes")
			for (functionBlock : model.properties) {
				var fbModel = functionBlock.type
				g.writeStartObject //Current implementedShape
				  g.writeStringField("name", fbModel.displayname)
				  g.writeStringField("type", "ThingShape")
				  g.writeStringField("description", getDescription(fbModel))
				  g.writeStringField("tags", "Applications:Vorto_CodeGen")
				
				  /**************** Enumerate Properties ****************/
				  g.writeArrayFieldStart("propertyDefinitions")
				    /* 
				     * Vorto has two entities that map to ThingWorx properties, statuses and configurations.
				     * The difference between the two is that a status is reported by a Thing, while a configuration
				     * can be written and sent down to a thing. We're going to map statuses and configurations
				     * to properties. Statuses are read only and configurations are writable.
				     */
				    //Map statuses to properties 
				    if (fbModel.functionblock.status != null) {
					  enumerateProperties(fbModel, g, VortoPropertyType.STATUS) //Statuses are read only
				    }
				    //Map configurations to properties
				    if (fbModel.functionblock.configuration != null) {
					  enumerateProperties(fbModel, g, VortoPropertyType.CONFIGURATION) //Configurations are writeable
				    }
				  g.writeEndArray // End of propertyDefinitions
				
				  /**************** Enumerate Services ****************/
				  g.writeArrayFieldStart("serviceDefinitions")
					  enumerateServices(fbModel, g)
				  g.writeEndArray // End of serviceDefinitions
				
				  /**************** Enumerate Event Definitions ****************/
				  g.writeArrayFieldStart("eventDefinitions")
					//TODO - Event definitions left for post-PoC coding effort
				  g.writeEndArray // End of eventDefinitions
				
				  /**************** Enumerate Alerts ****************/
				  g.writeObjectFieldStart("alertConfigurations")
				    g.writeArrayFieldStart("alertDefinitions")
					  //TODO - Alert definitions left for post-PoC coding effort
				    g.writeEndArray // End of alertDefinitions
				  g.writeEndObject // End of alertConfigurations
				g.writeEndObject // End of current implementedShape
			  }
			g.writeEndArray //End of implementedShapes array
		}
		
		protected def enumerateProperties(FunctionblockModel fbModel, JsonGenerator g, VortoPropertyType propType) {
			var readOnly = false
			var EList<Property> properties = null
			
			if (propType == VortoPropertyType.STATUS){
				readOnly = true
				properties = fbModel.functionblock.status.properties
			}
			else if (propType == VortoPropertyType.CONFIGURATION) {
				properties = fbModel.functionblock.configuration.properties
				readOnly = false
			}
			
			for (currentStatusProperty : properties) {
				/* 
				* We are only handling simple data types for now. Complex Vorto data types don't 
				* map directly to ThingWorx, although ThingWorx has a model that can handle them.
				* We'll handle complex data types in future versions.
				*/
				if (currentStatusProperty.type instanceof PrimitivePropertyType) {
					var currentType = getPrimitivePropertyType(currentStatusProperty)
					g.writeStartObject //Start of current property
					  g.writeStringField("name", currentStatusProperty.name)
					  g.writeStringField("baseType", getThingWorxDataType(currentType))
					  g.writeStringField("description", getDescription(currentStatusProperty))
					  g.writeBooleanField("isLocalOnly", false)
					  g.writeObjectFieldStart("aspects")
					    g.writeNumberField("cacheTime", 0.0)
					    g.writeStringField("dataChangeType", "VALUE")
					    g.writeBooleanField("isLogged", false)
					    g.writeBooleanField("isPersistent", true)
					    g.writeBooleanField("isReadOnly", readOnly)
					  g.writeEndObject // End of aspects
					g.writeEndObject // End of current propertyDefinition
				}
			}
		}
		
		protected def enumerateServices(FunctionblockModel fbModel, JsonGenerator g) {
			var services = fbModel.functionblock.operations
			
			for (currentService : services) {
				g.writeStartObject //Start of current service
				  g.writeStringField("name", currentService.name)
				  g.writeStringField("description", getDescription(currentService))
				  g.writeObjectFieldStart("resultType") //Start of result block
				    g.writeStringField("name", "result") //Result is a convention for ThingWorx
				    g.writeStringField("baseType", getResultBaseType(currentService))
				    g.writeStringField("description", currentService.description)
				  g.writeEndObject //End of ResultType
				  g.writeArrayFieldStart("parameterDefinitions") //Start of parameter definitions
				  for (currentParam : currentService.params) {
					//For this version, we are not supporting complex property types
					if (currentParam instanceof PrimitiveParam) {
					  g.writeStartObject //Start of current parameter
					    g.writeStringField("name", currentParam.name)
						var paramType = (currentParam as PrimitiveParam).type.literal
						g.writeStringField("baseType", getThingWorxDataType(paramType))
						g.writeStringField("description", "")
					  g.writeEndObject // End of current parameterDefinition
					}
				  }
				  g.writeEndArray //End of parameterDefinitions
				g.writeEndObject //End of current service
			}
		}
		
		protected def String getDescription(Property property){
			if (property.description != null){
				return property.description
			} else {
				return ""
			}	
		}
		
		protected def String getDescription(Operation operation){
			if (operation.description != null){
				return operation.description
			} else {
				return ""
			}	
		}
		
		protected def String getDescription(FunctionblockModel fbModel){
			if (fbModel.description != null){
				return fbModel.description
			} else {
				return ""
			}	
		}
		
		protected def String getPrimitivePropertyType(Property property) {
			if (property.type instanceof PrimitivePropertyType) {
				return (property.type as PrimitivePropertyType).getType.toString
			} else {
				return "UNDEFINED"
			}
		}

		protected def String getResultBaseType(Operation operation){
			if (operation.returnType != null && operation.returnType instanceof ReturnPrimitiveType){
				if (operation.returnType.multiplicity == false){
					var ReturnType primitiveType = operation.returnType
					var typeName = (primitiveType as ReturnPrimitiveType).returnType.literal
					return getThingWorxDataType(typeName)
				} else {
					return "COMPLEX_TYPE"	
				}
			} else {
				return "NOTHING"
			}
		}
		
		def String getThingWorxDataType(String vortoType) {
			var String dataType = ""

			val typeMap = newHashMap(
				"base64Binary" -> "BLOB",
				"boolean" -> "BOOLEAN",
				"dateTime" -> "DATETIME",
				"int" -> "INTEGER",
				"short" -> "INTEGER",
				"long" -> "INTEGER",
				"byte" -> "INTEGER",
				"double" -> "NUMBER",
				"float" -> "NUMBER",
				"string" -> "STRING"
			)

			dataType = typeMap.get(vortoType)

			if (dataType == null) {
				dataType = "UNDEFINED"
			}
			return dataType
		}
		
	}
		
	override getServiceKey() {
		return "thingworx"
	}
	
}






