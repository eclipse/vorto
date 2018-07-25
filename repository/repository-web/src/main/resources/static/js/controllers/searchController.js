repositoryControllers.controller('SearchController', [ '$scope', '$filter', '$rootScope', '$http', '$location', '$uibModal',
	function ($scope,$filter,$rootScope,$http,$location,$uibModal) {

    $scope.models = [];
    $scope.filteredModels = [];
    $scope.modelType = 'all';
    $scope.queryFilter = "";
    $scope.fileToUpload = null;
    $scope.isLoading = false;
    $scope.onlyYourModels = false;
    
    $scope.clearInput = function() {
        $scope.queryFilter = "";
    };
 
    $scope.searchOnEnter = function(keyEvent) {
        if (keyEvent.keyCode === 13) {
            $scope.search();
        }
    };

    $scope.search = function() {
    	$scope.isLoading = true;
        var filter = null;
        if ($scope.modelType === 'all') {
            filter = $scope.queryFilter;
        } else {
            filter = $scope.queryFilter + " "+$scope.modelType;
        }
        $http.get('./api/v1/search/models?expression=' + filter).success(
            function(data, status, headers, config) {            	
            	$scope.models = data;
                $scope.isLoading = false;
                filterModels();
            }).error(function(data, status, headers, config) {
                $scope.models = [];
                $scope.isLoading = false;
            });
    };

    $scope.search();

    $scope.itemsByPage 		= 15;
    $scope.displayedPages 	= ($scope.models.length / 2);

    $scope.getters= {
        modelType: function (value) {
            return value.id.modelType.sort();
        },
        namespace: function (value) {
            return value.id.namespace.sort();
        },
        name: function (value) {
            return value.id.name.sort();
        },
        version: function (value) {
            return value.id.version.sort();
        }
    }

    $scope.go = function(model){
        $location.path("/details/"+model.id.namespace+"/"+model.id.name+"/"+model.id.version);
    };

    $scope.showYourModels = function() {
        $scope.onlyYourModels = !$scope.onlyYourModels;
        filterModels();
    };

    function filterModels() {    
        $scope.filteredModels = $scope.models;    
        if($scope.onlyYourModels) {
            $scope.filteredModels = $filter('filter')($scope.filteredModels, {author: $rootScope.user });        
        }
    };
    
    $scope.openCreateModelDialog = function(action) {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: function($scope) {
        	$scope.errorMessage = null;
        	$scope.modelType = "InformationModel";
        	$scope.modelName = "";
        	$scope.modelNamespace = "";
        	$scope.modelVersion = "1.0.0";
        	
			$scope.create = function() {
				$scope.isLoading = true;
		    	$http.post('./rest/models/'+$rootScope.modelId($scope.modelNamespace,$scope.modelName,$scope.modelVersion)+'/'+$scope.modelType,null)
			        .success(function(result){
			        	$scope.isLoading = false;
			        	if (result.status === 409) {
			        		$scope.errorMessage = "Model with this name and namespace already exists.";
			        	} else {
			        		modalInstance.close(result);
			        	}
			        }).error(function(data, status, header, config) {
			        	$scope.isLoading = false;
			        	if (status === 409) {
			        		$scope.errorMessage = "Model with this name and namespace already exists.";
			        	}
			        });
		    };
		 	    
		    $scope.cancel = function() {
				modalInstance.dismiss();
			};
        },
        templateUrl: "partials/createmodel-template.html",
        size: "lg",
        resolve: {
        	model: function() {
    			return $scope.model;
    		}
    	}
      });
      
      modalInstance.result.then(
        function(model) {
            $location.path("/details/"+model.id.namespace+"/"+model.id.name+"/"+model.id.version);
        });
};
} ]);
