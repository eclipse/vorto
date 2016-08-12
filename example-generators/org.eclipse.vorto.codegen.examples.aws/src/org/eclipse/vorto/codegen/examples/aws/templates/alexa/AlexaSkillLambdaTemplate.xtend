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
package org.eclipse.vorto.codegen.examples.aws.templates.alexa

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann (Robert Bosch (SEA) Pte. Ltd)
 */
class AlexaSkillLambdaTemplate extends AbstractAlexaTemplate {
		
	override getFileName(InformationModel context) {
		return "index.js"
	}
	override getContent(InformationModel element, InvocationContext context) {
		'''
		«element.name».prototype.intentHandlers = {
		    // register custom intent handlers
		    «FOR fbProperty : element.properties»
		    	«FOR operation : fbProperty.type.functionblock.operations SEPARATOR ','»
		    		"«operation.name»": function (intent, session, response) {
		    			 var params = {
		    			 	FunctionName: '<ADD ARN OF LAMBDA FUNCTION HERE>', /* should be ARN */
		    			 	ClientContext: null,
		    			 	InvocationType: 'RequestResponse',
		    			 	LogType: 'Tail',
		    			 	Payload: JSON.stringify({«FOR param : operation.params SEPARATOR ','»"«param.name»":intent.slots.«param.name».value«ENDFOR»}),
		    			 	Qualifier: '$LATEST'
		    			 };
		    			 lambda.invoke(params, function(err, data) {
		    			 	if (err) console.log(err, err.stack); // an error occurred
		    			 	else {
		    			 		console.log(data); // successful response
		    			 		response.tell("Okay I invoked «operation.name» on «element.name» for you.");
		    			 	}
		    			 });
		    			 
		    		}
		    	«ENDFOR»
		    	«IF fbProperty.type.functionblock.status != null»
		    	«FOR statusProperty : fbProperty.type.functionblock.status.properties SEPARATOR ','»
		    		"«statusProperty.name»Status": function (intent, session, response) {
		    			var params = {
		    				FunctionName: '<ADD ARN OF LAMBDA FUNCTION HERE>', /* should be ARN */
		    			    ClientContext: null,
		    			    InvocationType: 'RequestResponse',
		    			    LogType: 'Tail',
		    			    Qualifier: '$LATEST'
		    			  };
		    			  lambda.invoke(params, function(err, data) {
		    			  	if (err) console.log(err, err.stack); // an error occurred
		    			    else {
		    			    	console.log(data); // successful response
		    			    	response.tell("The current «statusProperty.name» status of the «element.name» is "+data.«statusProperty.name»+".");
		    			    }
		    			 });
		    			 
		    		}
		    	«ENDFOR»
		    	«ENDIF»
		    «ENDFOR»
		   };
		'''
	}
	
}
