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
        $location.path("/details/"+model.id.prettyFormat);
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
        $scope.filteredModels.sort(function(a, b){
        	var result = 0;
        	
        	if((a.state === "Released") && (b.state !== "Released")) {
        		result = -1;
        	} else if((a.state !== "Released") && (b.state === "Released")) {
        		result =  1;
        	} else {
        		result = 0;
        	}
            
            if (result == 0) {
            	if ((a.type === "InformationModel") && (b.type !== "InformationModel")) return -1;
            	if ((a.type !== "InformationModel") && (b.type === "InformationModel")) return 1;
            	if ((a.type === "Functionblock") && (b.type !== "Functionblock")) return -1;
            	if ((a.type !== "Functionblock") && (b.type === "Functionblock")) return 1;
            	if ((a.type === "Datatype") && (b.type !== "Datatype")) return -1;
            	if ((a.type !== "Datatype") && (b.type === "Datatype")) return 1;
            }         
            
            return result;
        });
    };

    $scope.openCreateModelDialog = function(action) {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: function($scope) {
        	$scope.errorMessage = null;
        	$scope.modelType = "InformationModel";
        	$scope.currentStep = "step1";
        	$scope.modelName = "";
        	$scope.modelNamespace = "";
        	$scope.modelVersion = "1.0.0";
        	$scope.fbType = "org.eclipse.vorto";
        	
        	$scope.selected = {};
			$scope.selected.properties = [];
		
			$scope.properties = [];
			$scope.rePropertyName = /^[A-Za-z][A-Za-z0-9_]{1,30}$/;
			
            $scope.selectedProperty = {};
            $scope.selectedProperty["propertyName"] = "";
            $scope.selectedProperty["selectedFb"] = null;
			
			$scope.selectFunctionBlock = function(fb){
                $scope.selectedProperty["selectedFb"] = fb;
			};
        	
        	$scope.loadFunctionblocks = function(namespace) {
				$http.get("./rest/search/releases?type=Functionblock&namespace="+namespace).success(
					function(data, status, headers, config) {
						$scope.functionblocks = data;
						$scope.isLoading = false;
					}).error(function(data, status, headers, config) {
						$scope.functionblocks = [];
						$scope.errorMessage  = "No Functionblocks found!";
						$scope.isLoading = false;
					});			
	   		 };
	   		 
	   		 $scope.loadFunctionblocks('org.eclipse.vorto');
	    
		    $scope.generatePropertyName = function(selectedFb) {
                $scope.selectedProperty["selectedFb"] = selectedFb;
                $scope.selectedProperty["propertyName"] = generateVariableName(selectedFb);
		    	
		    	function generateVariableName(fb) {
		    		var variableName = fb.name.toLowerCase();
		    		var i = 0;
		    		while (contains(variableName,$scope.selected.properties)) {
		    			variableName += ++i;
		    		}
		    		return variableName;
		    	};
		    	
		    	function contains(variableName,properties) {
		    		for (var i = 0; i < properties.length;i++) {
			    		if (properties[i].name === variableName) {
			    			return true;
			    		}
			    	}
			    	return false;
		    	};
	
		    };
			
			$scope.addProperty = function() {
		    	var property = {};
                property["name"] = $scope.selectedProperty["propertyName"];
                property["type"] = $scope.selectedProperty["selectedFb"];
			    $scope.selected.properties.push(property);
                console.log($scope.selectedProperty["propertyName"]);
                console.log($scope.selectedProperty["selectedFb"]);
                $scope.selectedProperty["propertyName"] = "";
                $scope.selectedProperty["selectedFb"] = null;	
	    	};
	    
		    $scope.removeProperty = function(item,model) {
		    	for(var i = 0; i < $scope.selected.properties.length; i++){
					if ($scope.selected.properties[i].name === item.name) $scope.selected.properties.splice(i, 1);
				}
			};
			
			$scope.next = function(page,modelType, modelNamespace, modelName, modelVersion) {
        		$scope.currentStep = page;
        		$scope.modelType = modelType;
        		$scope.modelNamespace = modelNamespace;
        		$scope.modelName = modelName;
        		$scope.modelVersion = modelVersion;
        	};
        	
        	
			$scope.create = function(modelType,modelNamespace, modelName, modelVersion) {
				$scope.isLoading = true;
		    	$http.post('./rest/' + $rootScope.tenant + '/models/'+$rootScope.modelId(modelNamespace,modelName,modelVersion)+'/'+modelType,$scope.selected.properties)
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
        templateUrl: "webjars/repository-web/dist/partials/createmodel-template.html",
        size: "lg",
        resolve: {
        	model: function() {
    			return $scope.model;
    		}
    	}
      });
      
      modalInstance.result.then(
        function(model) {
            $location.path("/details/"+model.id.prettyFormat);
        });
};
} ]);
