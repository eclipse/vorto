/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.examples.aws.templates.shadow

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.datatype.Property

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 */
class GetThingShadowLambdaTemplate implements IFileTemplate<Property> {
		
	override getFileName(Property context) {
		return "GetThingShadowLambda_"+context.name+".js"
	}
	
	override getPath(Property context) {
		return "aws/shadow";
	}
	
	override getContent(Property element,InvocationContext context) {
		'''
		var config = {
		    "thingName": "<PUT THING NAME HERE>",
		    "endpointAddress": "<PUT YOUR ENDPOINT URL HERE>"
		}
		
		var AWS = require('aws-sdk');
		var iotdata = new AWS.IotData({endpoint: config.endpointAddress});
		
		exports.handler = function(event, context, callback) {
		     iotdata.getThingShadow({
		        thingName: config.thingName
		    },function(err, data) {
		         if (err) {
		            callback(err);
		        } else {          
		            var jsonPayload = JSON.parse(data.payload);
		            var «element.name» = jsonPayload.state.reported["«element.name»"];
		            callback(null,«element.name»);
		        }
		    });
		};
		'''
	}
	
}
