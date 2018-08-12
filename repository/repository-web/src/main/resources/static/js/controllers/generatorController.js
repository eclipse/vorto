repositoryControllers.controller('GeneratorController', [ '$scope','$http', 
	function ($scope,$http) {

    $scope.generators = [];
    $scope.mostUsedGenerators = [];
    $scope.isLoading = false;

    $scope.listGenerators = function() {
    	$scope.isLoading = true;
        $http.get('./api/v1/generators').success(
            function(data, status, headers, config) {
            	$scope.isLoading = false;
                $scope.generators = data;
            });
    };

    $scope.listTopUsed = function() {
        $http.get('./rest/generators/rankings/3').success(
            function(data, status, headers, config) {
                $scope.mostUsedGenerators = data;
            });
    };

    $scope.isFilled = function(rating, value) {
        if (rating === value) {
            return "filled"
        }
    };

    $scope.listGenerators();
$scope.listTopUsed();

} ]);

repositoryControllers.controller('GeneratorConfigController', [ '$scope','$http','generator','model','$uibModalInstance', 
	function ($scope,$http,generator,model,$uibModalInstance) {

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
			.success(function(result) {
				$scope.generator = result;
				
				if ($scope.generator.configKeys && $scope.generator.configTemplate) {
					for (var i = 0;i < $scope.generator.configKeys.length;i++) {
						var key = result.configKeys[i];
						$scope.configParams[key] = "";
					}
					$scope.configTemplate = $scope.generator.configTemplate;
				} else {
					$scope.configTemplate = "";
				}

		});
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