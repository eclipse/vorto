/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
define(["../init/appController"],function(repositoryControllers) {

repositoryControllers.controller('GeneratorController', [ '$rootScope', '$scope','$http', 
	function ($rootScope, $scope, $http) {

    $scope.generators = [];
    $scope.mostUsedGenerators = [];
    $scope.isLoading = false;

    $scope.listGenerators = function() {
    	$scope.isLoading = true;
        $http.get('./api/v1/generators').then(
            function(result) {
            	$scope.isLoading = false;
							$scope.generators = result.data;
            },
						function(error){}
				);
    };

    $scope.listTopUsed = function() {
        $http.get('./rest/generators/rankings/3').then(
            function(result) {
                $scope.mostUsedGenerators = result.data;
            },
						function(error){}
				);
    };

    $scope.isFilled = function(rating, value) {
        if (rating === value) {
            return "filled"
        }
    };

    $scope.listGenerators();
$scope.listTopUsed();

} ]);

repositoryControllers.controller('GeneratorConfigController', [ '$rootScope', '$scope','$http','generator','model','$uibModalInstance', 
	function ($rootScope, $scope, $http, generator, model, $uibModalInstance) {

	$scope.model = model;
	$scope.generator = generator;
	$scope.configParams = {};
	
	$scope.enableGeneratorButton = function() {
		if ($scope.generator.configKeys && $scope.generator.configKeys.length > 0) {
			for (var i = 0;i < $scope.generator.configKeys.length;i++) {
				var key = $scope.generator.configKeys[i];
				if ($scope.configParams[key]) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	};
	
	$scope.loadConfiguration = function() {
		$http.get('./api/v1/generators/'+$scope.generator.key)
			.then(
					function(result) {
						$scope.generator = result.data;

						if ($scope.generator.configKeys && $scope.generator.configTemplate) {
							for (var i = 0;i < $scope.generator.configKeys.length;i++) {
								var key = result.data.configKeys[i];
								$scope.configParams[key] = "";
							}
							$scope.configTemplate = $scope.generator.configTemplate;
						} else {
							$scope.configTemplate = "";
						}
					},
					function(error){}
			);
	};
	
	$scope.loadConfiguration();
	
	$scope.generate = function() {
		var requestParams = "";
		for (var i = 0;i < ($scope.generator.configKeys && $scope.generator.configKeys.length);i++) {
			var key = $scope.generator.configKeys[i];
			var concat = "&";
			if (i == 0) {
				concat = "?";
			}
			requestParams += concat + key + "=" + $scope.configParams[key];
		}
     	window.location.assign('./api/v1/generators/'+$scope.generator.key+'/models/'+$scope.model.id.prettyFormat+'/'+requestParams);
 	 	$uibModalInstance.dismiss("cancel");
    };

    $scope.cancel = function() {
     	$uibModalInstance.dismiss("cancel");
};
}]);

});