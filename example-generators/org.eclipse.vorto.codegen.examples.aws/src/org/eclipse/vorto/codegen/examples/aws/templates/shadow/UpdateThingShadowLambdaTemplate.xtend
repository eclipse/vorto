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

import org.eclipse.emf.common.util.EList
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.IMapped
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.functionblock.Operation
import org.eclipse.vorto.core.api.model.functionblock.Param

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 */
class UpdateThingShadowLambdaTemplate implements IFileTemplate<Operation> {
		
	private IMapped<Operation> mappedElement;
	
	new (IMapped<Operation> mappedElement) {
		this.mappedElement = mappedElement;
	}
		
	override getFileName(Operation context) {
		return "UpdateThingShadowLambda_"+context.name+".js"
	}
	
	override getPath(Operation context) {
		return "aws/shadow";
	}
	
	override getContent(Operation element, InvocationContext context) {
		'''
		var config = {
		    "thingName": "<PUT THING NAME HERE>",
		    "endpointAddress": "<PUT YOUR ENDPOINT URL HERE>"
		}
		
		var AWS = require('aws-sdk');
		var iotdata = new AWS.IotData({endpoint: config.endpointAddress});
		
		exports.handler = function(event, context, callback) {
		    var update = {
		            "state": {
		                "desired" : {
		                	«IF element.params.isEmpty»
		                	"«mappedElement.getAttributeValue("field",element.name)»" : "true"
		                	«ELSE»
		                	"«mappedElement.getAttributeValue("field",element.name)»" : «operationParamsToJSON(element.params)»
		                	«ENDIF»
		                }
		            }
		        };
		     iotdata.updateThingShadow({
		        payload: JSON.stringify(update),
		        thingName: config.thingName
		    },function(err, data) {
		         if (err) {
		            callback(err);
		        } else {          
		            callback(null,"success");
		        }
		    });
		};
		'''
	}
	
	def operationParamsToJSON(EList<Param> params) {
		'''
		{
			«FOR param : params SEPARATOR ','»
			"«param.name»": event.«param.name»
			«ENDFOR»
		
		}
		'''
	}
	
}
