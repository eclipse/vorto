/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.bosch.things.javaclient.templates

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.bosch.things.javaclient.TypeMapper
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingsClientTemplate implements ITemplate<InformationModel> {

	override getContent(InformationModel model, InvocationContext context) {
	    
	    var properties = context.configurationProperties
	    var thingid = properties.get("thingId") ?: 'Please add Things Id'
 
'''
package com.example.things;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.cr.integration.IntegrationClient;
import com.bosch.cr.integration.things.FeatureHandle;
import com.bosch.cr.integration.things.ThingHandle;
import com.bosch.cr.integration.things.ThingIntegration;
import com.bosch.cr.json.JsonObject;
import com.bosch.cr.model.things.Feature;
import com.example.things.functionblocks.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * This example shows how to create and use the Java Integration Client for
 * managing your Thing.
 */
public class «model.name»ThingsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(«model.name»ThingsClient.class);

    private static final String THING_ID = "«thingid»";
    
    private static final int TIMEOUT = 5;
    
    static final DateFormat JSON_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

    public static void main(final String... args) {
        new «model.name»ThingsClient().execute();
    }

    /**
     * Periodically update a thing with the java client.
     */
    public void execute() {
        IntegrationClient thingClient = ThingClientFactory.getThingIntegrationClient();
        ThingIntegration thingIntegration = thingClient.things();
        
        ThingHandle thingHandle = thingIntegration.forId(THING_ID);
        
        try {
            // Test if the thing exist, exceptions will be thrown if it doesn't
            thingHandle.retrieve().get(TIMEOUT, TimeUnit.SECONDS);
            
            // Loop to update the attributes of the Thing
            for(int i = 0; i <= 100; i++) {
                «FOR fbProperty : model.properties»
                update«fbProperty.name.toFirstUpper»(thingHandle, get«fbProperty.name.toFirstUpper»());
                «ENDFOR»       
                Thread.sleep(2000);
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.info(e.getMessage());
        } finally {
            // Terminate the Java client
            thingClient.destroy();
        }
    }
    
    «FOR fbProperty : model.properties»
    /**
     * Modify this code to retrieve actual sensor reading
     */
    private «fbProperty.type.name» get«fbProperty.name.toFirstUpper»()  {
    	«fbProperty.type.name» «fbProperty.name» = new «fbProperty.type.name»();
    	«IF fbProperty.type.namespace == "com.ipso.smartobjects" && fbProperty.type.name == "Location" && fbProperty.type.version == "0.0.1"»
    		«fbProperty.name».setLatitude("«1.0»");
    		«fbProperty.name».setLongitude("«1.0»");
    		«fbProperty.name».setAltitude("");
    		«fbProperty.name».setUncertainty("");
    		«fbProperty.name».setVelocity((byte) new java.util.Random().nextInt(100));
    		«fbProperty.name».setTimestamp(new java.util.Date());
    	«ELSE»
    		«FOR statusProperty : fbProperty.type.functionblock.status.properties»
    		«fbProperty.name».set«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»(«TypeMapper.getRandomValue(statusProperty.type)»);
    		«ENDFOR»
    	«ENDIF»
        return «fbProperty.name»; 
    }
    
    /**
     * Update «fbProperty.type.name» Feature
     */
    private void update«fbProperty.name.toFirstUpper»(ThingHandle thingHandle, «fbProperty.type.name» fb) {
        String featureId = "«fbProperty.name»";
        FeatureHandle featureHandle = thingHandle.forFeature(featureId);
        
        try {
            featureHandle.retrieve()
                .exceptionally(addMissingFeature(thingHandle, featureId))
                .thenCompose(feature -> {
                    return featureHandle.putProperty("status", JsonObject.newBuilder()
                        «FOR statusProperty : fbProperty.type.functionblock.status.properties»
                        «IF statusProperty.type instanceof PrimitivePropertyType && (statusProperty.type as PrimitivePropertyType).type == PrimitiveType.DATETIME»
                        .set("«statusProperty.name»", JSON_DATE_FORMAT.format(fb.get«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»()))
                        «ELSE»
                        .set("«statusProperty.name»", fb.get«TypeMapper.checkKeyword(statusProperty.name).toFirstUpper»())
                        «ENDIF»
                        «ENDFOR»
                        .build());
                 }).whenComplete((aVoid, throwable) -> {
                     if (null == throwable) {
                         LOGGER.info("Thing with ID '{}' feature «fbProperty.type.name» was updated.", THING_ID);
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
