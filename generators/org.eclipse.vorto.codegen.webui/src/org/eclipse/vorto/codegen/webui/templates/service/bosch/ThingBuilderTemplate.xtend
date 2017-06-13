package org.eclipse.vorto.codegen.webui.templates.service.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingBuilderTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''ThingBuilder.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch;
		
		import java.util.Date;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.internal.model.FeatureImpl;
		import com.example.iot.«element.name.toLowerCase».service.bosch.internal.model.ThingImpl;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.AclEntry;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Feature;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Thing;
		import com.google.gson.Gson;
		import com.google.gson.GsonBuilder;
		import com.google.gson.reflect.TypeToken;
		
		public class ThingBuilder {
		
			private ThingImpl thing;
		
			public ThingBuilder() {
				this.thing = new ThingImpl();
				this.thing.getAttributes().put(ThingImpl.ATTRIBUTE_CREATEDON, Thing.JSON_DATE_FORMAT.format(new Date()));
			}
			
			
			public ThingBuilder fromThing(com.bosch.cr.model.things.Thing thing) {		
				String json = thing.toJson().toString();
				Gson gson = new GsonBuilder().create();
				this.thing = gson.fromJson(json, new TypeToken<ThingImpl>(){}.getType());
				return this;
			}
		
			public ThingBuilder withThingId(String thingId) {
				this.thing.setThingId(thingId);
				return this;
			}
		
			public ThingBuilder withThingId(String namespace, String deviceId) {
				StringBuilder builder = new StringBuilder();
				builder.append(namespace);
				builder.append(":");
				builder.append(deviceId);
				return withThingId(builder.toString());
			}
		
			public ThingBuilder withCreationBy(String userId) {
				this.thing.getAttributes().put(ThingImpl.ATTRIBUTE_CREATEDBY, userId);
				return this;
			}
		
			public ThingBuilder withThingName(String thingName) {
				this.thing.getAttributes().put(ThingImpl.ATTRIBUTE_NAME, thingName);
				return this;
			}
		
			public ThingBuilder withDeviceId(String deviceId) {
				this.thing.getAttributes().put(ThingImpl.ATTRIBUTE_DEVICEID, deviceId);
				return this;
			}
		
			public ThingBuilder withFeature(Feature feature) {
				this.thing.getFeatures().put(feature.getId(), ((FeatureImpl)feature).getContent());
				return this;
			}
		
			public ThingBuilder withPermission(String authorizationSubject, AclEntry acl) {
				this.thing.getAcl().put(authorizationSubject, acl);
				return this;
			}
			
			public Thing build() {
				return this.thing;
			}
		
		}
		
		'''
	}
	
}