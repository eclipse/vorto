package org.eclipse.vorto.codegen.webui.templates.service.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class BoschThingsDataServiceTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''BoschThingsDataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch;
		
		import java.util.Collections;
		import java.util.List;
		import java.util.Map;
		import java.util.concurrent.CompletableFuture;
		import java.util.function.Function;
		import java.util.stream.Collectors;
		
		import org.eclipse.vorto.repository.api.ModelId;
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		
		import com.bosch.cr.integration.things.ThingIntegration;
		import com.example.iot.«element.name.toLowerCase».model.*;
		import com.example.iot.«element.name.toLowerCase».service.DataService;
		import com.example.iot.«element.name.toLowerCase».service.Query;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Thing;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.ThingSearchResult;
		import com.fasterxml.jackson.databind.ObjectMapper;
		
		public class BoschThingsDataService implements DataService {
		
			private static Logger log = LoggerFactory.getLogger(BoschThingsDataService.class);
		
			private ThingIntegration thingsIntegration;
		
			private ThingClient thingClient;
			
			private static final String SUBSCRIBER_PREFIX = "/«element.name.toLowerCase»/";
		
			public BoschThingsDataService(ThingClient thingClient, ThingIntegration integrationClient) {
				this.thingClient = thingClient;
				this.thingsIntegration = integrationClient;
			}
			
			@Override
			public Query newQuery() {
				return new ThingsQuery();
			}
		
			@Override
			public List<«element.name»> queryThings(Query query) {
				CompletableFuture<ThingSearchResult> result = thingClient.searchThings(query.getValue());
				try {
					return result.get().getThings().stream().map(thingTo«element.name»).collect(Collectors.toList());
				} catch (Exception e) {
					log.error("Problem when searching things",e);
					return Collections.emptyList();
				} 
			}
		
			@Override
			public «element.name» getThing(String thingId) {
				
				try {
					return thingTo«element.name».apply(thingClient.getThing(thingId).get());
				} catch (Exception e) {
					log.error("Problem when getting thing details",e);
				}
				return null;
			}
		
			@Override
			public void registerAsyncCallback(String thingId, DataCallback callback) {
				this.thingsIntegration.forId(thingId).registerForFeatureChanges(getSubscriberId(thingId), featureChange -> {
					try {
						com.bosch.cr.model.things.Thing crThing = thingsIntegration.forId(thingId).retrieve().get();
						Thing thing = Thing.newBuilder().fromThing(crThing).build();
						callback.onChange(thingTo«element.name».apply(thing));
					} catch (Exception e) {
						log.error("Error while retrieving thing", e);
					}
				});
		
			}
		
			@Override
			public void deregisterAsyncCallback(String thingId) {
				this.thingsIntegration.forId(thingId).deregister(getSubscriberId(thingId));
			}
		
			private String getSubscriberId(String thingId) {
				return SUBSCRIBER_PREFIX + thingId;
			}
		
			public static Function<Thing, «element.name»> thingTo«element.name» = (thing) -> {
				«element.name» «element.name.toLowerCase» = new «element.name»(thing.getId(), thing.getName(),
						ModelId.fromPrettyFormat(thing.getThingTypeId()));
				«FOR fbProperty : element.properties»
				«element.name.toLowerCase».set«fbProperty.type.name»(convert(thing, "«fbProperty.name»", «fbProperty.type.name».class));
				«ENDFOR»
				return «element.name.toLowerCase»;
			};
		
			private static <T> T convert(Thing thing, String propertyName, Class<T> expectedClass) {
				Map<String, Object> statusProperty = thing.listFeatures().get(propertyName).getProperties();
				statusProperty.remove("_modelId");
				final ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(Thing.JSON_DATE_FORMAT);
				return mapper.convertValue(statusProperty, expectedClass);
			}
			
			
		}
		
		'''
	}
	
}