package org.eclipse.vorto.mapping.engine.serializer

import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.stream.Collectors
import org.apache.commons.text.StringEscapeUtils
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification
import org.eclipse.vorto.model.EntityModel
import org.eclipse.vorto.model.IModel
import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.model.ModelProperty
import org.eclipse.vorto.model.Stereotype

class EntityMappingSerializer extends AbstractSerializer {
	
	IModel parent
	String propertyName
	EntityModel entity;
	
	new(IMappingSpecification spec, String targetPlatform, String propertyName, EntityModel entity,  IModel parent) {
		super(spec, targetPlatform)
		this.parent = parent
		this.propertyName = propertyName
		this.entity = entity
	}
	
	override getModelId() {
		return new ModelId(propertyName.toFirstUpper+"PayloadMapping",specification.infoModel.id.namespace+".mapping."+specification.infoModel.id.name.toLowerCase+"."+parent.id.name.toLowerCase+".entities",specification.infoModel.id.version);
	}
	
	override String serialize() {
		'''
		vortolang 1.0
		
		namespace «specification.infoModel.id.namespace».mapping.«specification.infoModel.id.name.toLowerCase».«parent.id.name.toLowerCase».entities
		version «specification.infoModel.id.version»
		displayname "«propertyName.toFirstUpper» Entity Payload Mapping"
		description "Maps the «propertyName.toFirstUpper» payload of the «specification.infoModel.id.prettyFormat»"
		category payloadmapping
		
		using «entity.id.namespace».«entity.id.name»;«entity.id.version»
		«var imports = new HashSet »
		«FOR property : entity.properties»
		«IF isEntityProperty(property)»
		«var x = imports.add("using " + specification.infoModel.id.namespace+".mapping."+specification.infoModel.id.name.toLowerCase+"."+parent.id.name.toLowerCase+".entities"+"."+property.name.toFirstUpper+"PayloadMapping;"+specification.infoModel.id.version)»
		«ENDIF»
		«ENDFOR»
		«FOR using : imports»
		«using»
		«ENDFOR»
		
		entitymapping «propertyName.toFirstUpper»PayloadMapping {
			targetplatform «targetPlatform»
			«FOR property : entity.properties»
				«IF isEntityProperty(property)»
				from «entity.id.name».«property.name» to reference «property.name.toFirstUpper+"PayloadMapping"»
				«ELSE»
				«FOR stereotype : filterEmptyStereotypes(property.stereotypes)»
				from «entity.id.name».«property.name» to «stereotype.name» with {«createContent(stereotype.attributes)»}
				«ENDFOR»
				«ENDIF»		
			«ENDFOR»
		}
		'''
	}
	
	private def filterEmptyStereotypes(List<Stereotype> stereotypes) {
		return stereotypes.stream.filter[!attributes.isEmpty].filter[!getNonEmptyAttributes(attributes).isEmpty].collect(Collectors.toList);
	}
	
	private def getNonEmptyAttributes(Map<String,String> attributes) {
		var newMap = new HashMap<String,String>()
		for (String key : attributes.keySet) {
			if (!attributes.get(key).empty) {
				newMap.put(key,attributes.get(key))
			}
		}
	    return newMap;
		
	}
	
	private def String createContent(Map<String,String> attributes) {
		var content = new StringBuilder();
		for (var iter = attributes.keySet.iterator;iter.hasNext;) {
			var key = iter.next;
			if ((key.equalsIgnoreCase("xpath") || key.equalsIgnoreCase("condition")) && attributes.get(key).equals("")) {
			} else {
				content.append(key).append(":").append("\""+escapeQuotes(attributes.get(key))+"\"");
				if (iter.hasNext) {
					content.append(",");
				}
			}
			
		}
		return content.toString;
	}
	
	def escapeQuotes(String value) {
		return StringEscapeUtils.escapeJava(value)
	}
	
	def boolean isEntityProperty(ModelProperty property) {
		return property.type instanceof ModelId && specification.getReferencedModel(entity.id,property.name).isPresent &&  specification.getReferencedModel(entity.id,property.name).get instanceof EntityModel
	}
	
}