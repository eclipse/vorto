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
package org.eclipse.vorto.codegen.kura.templates.cloud.bosch

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.codegen.kura.templates.cloud.TypeMapper
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType

/**
 * @author Alexander Edelmann
 */
class BoschDataServiceTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'''BoschDataService.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.javaPackageBasePath»/cloud/bosch'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package «Utils.javaPackage».cloud.bosch;
		
		import java.util.concurrent.ExecutionException;
		import java.util.concurrent.TimeUnit;
		import java.util.concurrent.TimeoutException;
		import java.util.function.Function;
		
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		
		import com.bosch.cr.integration.IntegrationClient;
		import com.bosch.cr.integration.things.FeatureHandle;
		import com.bosch.cr.integration.things.ThingHandle;
		import com.bosch.cr.json.JsonObject;
		import com.bosch.cr.model.things.Feature;
		import «Utils.javaPackage».cloud.*;
		
		public class BoschDataService implements IDataService {
		
			private IntegrationClient integrationClient;
			
		    private static final Logger LOGGER = LoggerFactory.getLogger(BoschDataService.class);
		
		    private static final int TIMEOUT = 5;
		    
		    private String solutionId;
		    
		    private String endpointUrl;
			
			public BoschDataService(String solutionId, String endpointUrl) {
				this.solutionId = solutionId;
				this.endpointUrl = endpointUrl;
			}
			
			«FOR fbProperty : element.properties»
			@Override
			public void publish«fbProperty.name.toFirstUpper»(String thingId, «fbProperty.type.name» data) {
				 
				 ThingHandle thingHandle = getIntegrationClient().things().forId(thingId);
				 String featureId = "«fbProperty.name»"; //feature id according to Information model function block property
			        FeatureHandle featureHandle = thingHandle.forFeature(featureId);
			        
			        try {
			            featureHandle.retrieve()
			                .exceptionally(addMissingFeature(thingHandle, featureId))
			                .thenCompose(feature -> {
			                     return featureHandle.setProperties(JsonObject.newBuilder()
			                     «FOR statusProperty : fbProperty.type.functionblock.status.properties»
			                        	«IF statusProperty.type instanceof PrimitivePropertyType && (statusProperty.type as PrimitivePropertyType).type == PrimitiveType.DATETIME»
			                        	.set("«statusProperty.name»", JSON_DATE_FORMAT.format(data.get«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»()))
			                            «ELSE»
			                        	.set("«statusProperty.name»", data.get«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»())
			                            «ENDIF»
			                        «ENDFOR»
			                     .build());
			                 }).whenComplete((aVoid, throwable) -> {
			                     if (null == throwable) {
			                         LOGGER.info("Thing with ID '{}' feature «fbProperty.name» was updated with values {}", thingId,data);
			                     }
			                     else {
			                         LOGGER.error(throwable.getMessage());
			                     }
			                 }).get(TIMEOUT, TimeUnit.SECONDS);
			        } catch (InterruptedException | ExecutionException | TimeoutException e) {
			            e.printStackTrace();
			            throw new RuntimeException(e);
			        }
			}
			«ENDFOR»
			
			private IntegrationClient getIntegrationClient() {
				if (this.integrationClient == null) {
					return integrationClient = ThingClientFactory.getThingIntegrationClient(solutionId,endpointUrl);
				} else {
					return this.integrationClient;
				}
			}
		
			private Function<Throwable, Feature> addMissingFeature(ThingHandle thingHandle, String featureId) {
		        return throwable -> {
		            LOGGER.info("Creating the feature first because it's missing");
		            Feature feature = Feature.newBuilder().withId(featureId).build();
		            try {
		                return thingHandle.putFeature(feature)
		                    .thenCompose(aVoid -> thingHandle.forFeature(featureId).retrieve())
		                    .get(TIMEOUT, TimeUnit.SECONDS);
		            } catch (Exception e) {
		                e.printStackTrace();
		                throw new RuntimeException(e);
		            }
		        };
		    }
		
		}
		
		
		'''
	}
	
}
