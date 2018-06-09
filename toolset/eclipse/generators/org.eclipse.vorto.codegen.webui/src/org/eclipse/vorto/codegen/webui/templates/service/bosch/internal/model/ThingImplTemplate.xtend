package org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.model

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingImplTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''ThingImpl.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/internal/model'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.internal.model;
		
		import java.text.ParseException;
		import java.util.Date;
		import java.util.HashMap;
		import java.util.Map;
		
		import org.apache.commons.lang3.builder.ToStringBuilder;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Feature;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Thing;
		import com.fasterxml.jackson.annotation.JsonIgnore;
		
		public class ThingImpl implements Thing {
		
			private String thingId;
		
			private Map<String, Object> attributes = new HashMap<String, Object>();
		
			private Map<String, Object> features = new HashMap<String, Object>();
		
			public static final String ATTRIBUTE_MODELID = "_modelId";
			public static final String ATTRIBUTE_CREATEDON = "createdOn";
			public static final String ATTRIBUTE_CREATEDBY = "createdBy";
		
			public static final String ATTRIBUTE_NAME = "thingName";
		
			public static final String ATTRIBUTE_DEVICEID = "deviceId";
		
			public ThingImpl() {
		
			}
		
			public String getThingId() {
				return thingId;
			}
		
			public void setThingId(String thingId) {
				this.thingId = thingId;
			}
		
			public Map<String, Object> getAttributes() {
				return attributes;
			}
		
			public void setAttributes(Map<String, Object> attributes) {
				this.attributes = attributes;
			}
		
			public Map<String, Object> getFeatures() {
				return features;
			}
		
			public void setFeatures(Map<String, Object> features) {
				this.features = features;
			}
		
			@Override
			public String toString() {
				return ToStringBuilder.reflectionToString(this);
			}
		
			@Override
			@JsonIgnore
			public String getThingTypeId() {
				return (String) this.attributes.get(ATTRIBUTE_MODELID);
			}
		
			@Override
			@JsonIgnore
			public boolean hasThingType() {
				return this.attributes.containsKey(ATTRIBUTE_MODELID);
			}
		
			@Override
			public Map<String, Object> listAttributes() {
				return this.attributes;
			}
		
			@SuppressWarnings("unchecked")
			@Override
			public Map<String, Feature> listFeatures() {
				Map<String, Feature> features = new HashMap<String, Feature>(this.features.size());
				for (String featureKey : this.features.keySet()) {
					FeatureImpl feature = new FeatureImpl(featureKey, (Map<String, Object>) this.features.get(featureKey));
					features.put(featureKey, feature);
				}
				return features;
			}
		
			@Override
			@JsonIgnore
			public String getId() {
				return this.thingId;
			}
		
			@Override
			@JsonIgnore
			public String getName() {
				return (String) this.attributes.get(ATTRIBUTE_NAME);
			}
		
			@Override
			@JsonIgnore
			public Date getCreatedOn() {
				String createdOn = (String) this.attributes.get(ATTRIBUTE_CREATEDON);
				if (createdOn != null) {
					try {
						return JSON_DATE_FORMAT.parse(createdOn);
					} catch (ParseException e) {
						throw new RuntimeException("Cannot parse createdOn attribute. Seems to have unexpected format");
					}
				} else {
					return null;
				}
			}
		
			@Override
			@JsonIgnore
			public String getDeviceId() {
				return (String) this.attributes.get(ATTRIBUTE_DEVICEID);
			}
		
			@Override
			@JsonIgnore
			public String getCreator() {
				return (String) this.attributes.get(ATTRIBUTE_CREATEDBY);
			}
		
		}
		
		'''
	}
	
}
