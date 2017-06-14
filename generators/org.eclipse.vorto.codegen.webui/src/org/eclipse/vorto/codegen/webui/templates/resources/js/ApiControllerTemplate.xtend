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
package org.eclipse.vorto.codegen.webui.templates.resources.js

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class ApiControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''api-controller.js'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/js'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		var apiController = angular.module('api.controller', []);
		
		apiController.controller('SwaggerController', ['$rootScope', '$scope', '$location', '$http', '$sce',
			function($rootScope, $scope, $location, $http, $sce) {
				
				$scope.isLoading = true;
				$scope.url = $scope.swaggerUrl = 'v2/api-docs';
				$scope.defaultErrorHandler = function(data, status) {
					alert('Error Loading Swagger API!');
				};
				
			}]);
		'''
	}
	
}
