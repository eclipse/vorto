/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class ControllersScriptFileTemplate implements IFileTemplate<InformationModel> {

	override getFileName(InformationModel context) {
		return "controllers.js";
	}
	
	override getPath(InformationModel context) {
		return "webdevice.example/src/main/resources/static/script";
	}

	override getContent(InformationModel infoModel,InvocationContext invocationContext) {
		return '''
		var deviceAppControllers = angular.module('deviceAppControllers', []);
		
		«FOR fbProperty : infoModel.properties»
			«var fbm = fbProperty.type»
			deviceAppControllers.controller('«fbm.name»Controller', ['$scope', '$http', '$rootScope', function($scope,$http, $rootScope) {
				$scope.formData = {};
				 $scope.modelinfo = [];
				 
				 $scope.saveConfig = function(fbName) {
				 	var response = $http({method: 'PUT', data: formData, url:'rest/«fbm.name.toLowerCase»/saveConfiguration', headers: {'Content-Type':'application/json'}});
						response.success(function(data, status, headers, config) {
						   	$rootScope.responseMessage = "Configuration for «fbm.name» saved";
					    });
					};
					
					«FOR operation : fbm.functionblock.operations»
					$scope.«operation.name» = function() {
						$http.put("rest/«fbm.name.toLowerCase»/«operation.name»").success(function(data){
							$rootScope.responseMessage = "«operation.name» invoked";
						});	 
					};
					«ENDFOR»
			}]);
		«ENDFOR»
		'''
	}
}
