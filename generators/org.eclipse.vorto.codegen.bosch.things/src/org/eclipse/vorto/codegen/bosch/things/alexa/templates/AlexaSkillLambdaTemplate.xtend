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
package org.eclipse.vorto.codegen.bosch.things.alexa.templates

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class AlexaSkillLambdaTemplate extends AbstractAlexaTemplate {
		
	override getFileName(InformationModel context) {
		return "alexa-skillset-lambda.js"
	}
	override getContent(InformationModel element, InvocationContext context) {
		'''
		'use strict';
		
		 var speechOutput;
		 var reprompt;
		 var welcomeOutput = "Let's ask the device. What do you want to know?";
		
		 var thingsApiToken = "INSERT API TOKEN OF BOSCH IOT THINGS HERE";
		 var thingId = «context.configurationProperties.get("thingId")»
		 var username= "INSERT USERNAME HERE";
		 var password= "INSERT PASSWORD HERE";
		 
		 
		 var http = require('http');
		
		// --------------- Helpers that build all of the responses -----------------------
		
		function buildSpeechletResponse(title, output, repromptText, shouldEndSession) {
		    return {
		        outputSpeech: {
		            type: 'PlainText',
		            text: output,
		        },
		        card: {
		            type: 'Simple',
		            title: `SessionSpeechlet - ${title}`,
		            content: `SessionSpeechlet - ${output}`,
		        },
		        reprompt: {
		            outputSpeech: {
		                type: 'PlainText',
		                text: repromptText,
		            },
		        },
		        shouldEndSession,
		    };
		}
		
		function buildResponse(sessionAttributes, speechletResponse) {
		    console.log("Responding with " + JSON.stringify(speechletResponse));
		    return {
		        version: '1.0',
		        sessionAttributes,
		        response: speechletResponse,
		    };
		}
		
		 function buildSpeechletResponseDelegate(shouldEndSession) {
		      return {
		          outputSpeech:null,
		          directives: [
		                  {
		                      "type": "Dialog.Delegate",
		                      "updatedIntent": null
		                  }
		              ],
		         reprompt: null,
		          shouldEndSession: shouldEndSession
		      }
		  }
		
		
		// --------------- Functions that control the skill's behavior -----------------------
		
		function getWelcomeResponse(callback) {
		    console.log("in welcomeResponse");
		    // If we wanted to initialize the session to have some attributes we could add those here.
		    const sessionAttributes = {};
		    const cardTitle = 'Welcome';
		    const speechOutput = 'what do you want to do?';
		    const repromptText = "";
		    const shouldEndSession = false;
		
		    callback(sessionAttributes,
		        buildSpeechletResponse(cardTitle, speechOutput, repromptText, shouldEndSession));
		
		}
		
		«FOR fbProperty : element.properties» 
			«IF fbProperty.type.functionblock.status != null»
				«FOR statusProperty : fbProperty.type.functionblock.status.properties»
					function fetch«statusProperty.name.toFirstUpper»(request, session, callback){
						 console.log("in fetch«statusProperty.name.toFirstUpper»");
						 
						  var sessionAttributes = {};
						  var httpRequest = {
						 	 host : "things.apps.bosch-iot-cloud.com",
						 	 path: "/api/1/things/"+thingId,
						 	 method: 'GET',
						 	 headers: {
						 	 	'Content-Type': 'application/json',
						 		'x-cr-api-token': thingsApiToken,
						 		'Authorization' : "Basic " + new Buffer(username + ":" + password).toString("base64")
						 	 }
						  };
						 
						 console.log("url: "+JSON.stringify(httpRequest));
						 http.get(httpRequest, function(response) {
						 	response.on('data', function(data) {
						 		var result = JSON.parse(data);
						 		speechOutput = result.features.«fbProperty.name».properties.status.«statusProperty.name»;
						 		//say the results
						 		callback(sessionAttributes, buildSpeechletResponse("«fbProperty.name»", speechOutput, "", true));
						 	});
						 });
					}
				«ENDFOR»
			«ENDIF»
		«ENDFOR»
		
		function handleSessionEndRequest(callback) {
		    const cardTitle = 'Session Ended';
		    const speechOutput = 'Have a nice day!';
		    // Setting this to true ends the session and exits the skill.
		    const shouldEndSession = true;
		
		    callback({}, buildSpeechletResponse(cardTitle, speechOutput, null, shouldEndSession));
		}
		
		// --------------- Events -----------------------
		
		/**
		 * Called when the session starts.
		 */
		function onSessionStarted(sessionStartedRequest, session) {
		    console.log(`onSessionStarted requestId=${sessionStartedRequest.requestId}, sessionId=${session.sessionId}`);
		}
		
		/**
		 * Called when the user launches the skill without specifying what they want.
		 */
		function onLaunch(request, session, callback) {
		    //console.log(`onLaunch requestId=${launchRequest.requestId}, sessionId=${session.sessionId}`);
		    console.log("in launchRequest");
		    console.log("  request: "+JSON.stringify(request));
		    // Dispatch to your skill's launch.
		    getWelcomeResponse(callback);
		}
		
		/**
		 * Called when the user specifies an intent for this skill.
		 */
		function onIntent(request, session, callback) {
		    //console.log(`onIntent requestId=${intentRequest.requestId}, sessionId=${session.sessionId}`);
		    console.log("in onIntent");
		    console.log("  request: "+JSON.stringify(request));
		
		    const intent = request.intent;
		    const intentName = request.intent.name;
		
		    // Dispatch to your skill's intent handlers
		    if (intentName === 'AMAZON.HelpIntent') {
		    	getWelcomeResponse(callback);
		    } else if (intentName === 'AMAZON.StopIntent' || intentName === 'AMAZON.CancelIntent') {
		    	handleSessionEndRequest(callback);
		    } 
		    «FOR fbProperty : element.properties» 
		    	«IF fbProperty.type.functionblock.status != null»
		    		«FOR statusProperty : fbProperty.type.functionblock.status.properties»
		    			else if (intentName === '«statusProperty.name»') {
		    				fetch«statusProperty.name.toFirstUpper»(request, session, callback);
		    			}
		    		«ENDFOR»
		    	«ENDIF»
		   	«ENDFOR»
		   	 else {
		   	 	throw new Error('Invalid intent');
		   	 }
		}
		
		/**
		 * Called when the user ends the session.
		 * Is not called when the skill returns shouldEndSession=true.
		 */
		function onSessionEnded(sessionEndedRequest, session) {
		    console.log(`onSessionEnded requestId=${sessionEndedRequest.requestId}, sessionId=${session.sessionId}`);
		    // Add cleanup logic here
		}
		
		
		// --------------- Main handler -----------------------
		
		// Route the incoming request based on type (LaunchRequest, IntentRequest,
		// etc.) The JSON body of the request is provided in the event parameter.
		exports.handler = (event, context, callback) => {
		    try {
		        // console.log(`event.session.application.applicationId=${event.session.application.applicationId}`);
		        console.log("EVENT=====" + JSON.stringify(event));
		
		        /**
		         * Uncomment this if statement and populate with your skill's application ID to
		         * prevent someone else from configuring a skill that sends requests to this function.
		         */
		        /*
		        if (event.session.application.applicationId !== 'amzn1.echo-sdk-ams.app.[unique-value-here]') {
		             callback('Invalid Application ID');
		        }
		        */
		
		        if (event.session.new) {
		            onSessionStarted({ requestId: event.request.requestId }, event.session);
		        }
		
		        if (event.request.type === 'LaunchRequest') {
		            onLaunch(event.request,
		                event.session,
		                (sessionAttributes, speechletResponse) => {
		                    callback(null, buildResponse(sessionAttributes, speechletResponse));
		                });
		        } else if (event.request.type === 'IntentRequest') {
		            onIntent(event.request,
		                event.session,
		                (sessionAttributes, speechletResponse) => {
		                    callback(null, buildResponse(sessionAttributes, speechletResponse));
		                });
		        } else if (event.request.type === 'SessionEndedRequest') {
		            onSessionEnded(event.request, event.session);
		            callback();
		        }
		    } catch (err) {
		        callback(err);
		    }
		};
		'''		
	}
	
}
