var repositoryControllers = angular.module('repositoryControllers', ['swaggerUi']);

repositoryControllers.controller('SearchController', [ '$scope', '$rootScope', '$http', '$location', 
	function ($scope,$rootScope,$http,$location) {

    $scope.models = [];
    $scope.modelType = 'all';
    $scope.queryFilter = "";
    $scope.fileToUpload = null;
    
    $scope.clearInput = function() {
        $scope.queryFilter = "";
    };

    $scope.searchOnEnter = function(keyEvent) {
        if (keyEvent.keyCode === 13) {
            $scope.search();
        }
    };

    $scope.search = function() {
        var filter = null;
        if ($scope.modelType === 'all') {
            filter = $scope.queryFilter;
        } else {
            filter = $scope.queryFilter + " "+$scope.modelType;
        }
        if ($rootScope.modelsSaved && $rootScope.modelsSaved.filter === filter) {
        	// models are already fetched
        	$scope.models = $rootScope.modelsSaved.models;
        	return;
        }
        $http.get('./rest/model/query=' + filter).success(
            function(data, status, headers, config) {
            	$rootScope.modelsSaved = {'filter': filter, 'models': data};
            	$scope.models = data;
            }).error(function(data, status, headers, config) {
                $scope.models = [];
            });
    };

    $scope.search();

    $scope.displayedModels = [].concat($scope.models);
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
} ]);

repositoryControllers.controller('AdminController', ['$scope', '$rootScope', '$http','$location', 
	function ($scope, $rootScope, $http, $location) {
    
    $scope.removeImages = function() {
    	$http.delete('./rest/admin/content/images')
        .success(function(result){
            $location.path('/');
        }).error(function(data, status, headers, config) {
            $location.path('/');
        });
    };
    
    $scope.restore = function () {
        $scope.restoreResult = {};
        $scope.resultMessage = "";
        $rootScope.error = "";

        var fileToUpload = document.getElementById('file').files[0];
        if(fileToUpload != undefined) {
            var filename = document.getElementById('file').files[0].name;
            var fd = new FormData();
            fd.append('file', fileToUpload);
            $http.post('./rest/admin/content',fd, {
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .success(function(result){
                $location.path("/");
            });
        } else {
            $rootScope.error = "Choose backup to restore and click Restore.";
        }
    };
    
} ]);

repositoryControllers.controller('UploadController', ['$scope', '$rootScope', '$http','$location', 
	function ($scope, $rootScope, $http, $location) {

    $scope.uploadModel = function () {
        $scope.uploadResult = {};
        $scope.resultMessage = "";
        $scope.showResultBox = false;
        $scope.error = null;

        var fileToUpload = document.getElementById('file').files[0];
        if(fileToUpload != undefined) {
            var filename = document.getElementById('file').files[0].name;
            var extn = filename.split(".").pop();
            var modelFiles = ['type', 'fbmodel', 'infomodel', 'mapping'];
            if(modelFiles.indexOf(extn) !== -1)
                upload('./rest/secure/', fileToUpload);
            else
                upload('./rest/secure/multiple', fileToUpload);
        } else {
            $rootScope.error = "Choose model file(s) and click Upload.";
        }
    };

    upload = function(url, fileToUpload) {
        $scope.isLoading = true;
        $scope.loadMessage = "Uploading... Please wait!";
        $scope.modelStats = {};
        var infocount = 0, fbcount  = 0, typecount = 0, mappingcount = 0;
        var fd = new FormData();
        fd.append('file', fileToUpload);
        $http.post(url,fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(result){
            $scope.stateArr = [];
            $scope.uploadResult = result;
            $scope.showCheckin = true;

            if($scope.uploadResult.result != null && $scope.uploadResult.result.length > 0) {
                angular.forEach($scope.uploadResult.result, function (resultObject, idx) {
                    var item =  (idx == 0) ? {active: false} : {active: true} ;
                    var modelType = resultObject.stagingDetails.modelResource.type;
                    switch (modelType) {
                    case "Functionblock":
                        fbcount++;
                        break;
                    case "InformationModel":
                        infocount++;
                        break;
                    case "Datatype":
                        typecount++;
                        break;
                    case "Mapping":
                        mappingcount++;
                        break;
                    }
                    $scope.stateArr.push(item);
                    $scope.showCheckin = (resultObject.valid && $scope.showCheckin);
                });
            } else {
                $scope.showCheckin = false;
            }

            $scope.modelStats = {infocount:infocount, fbcount:fbcount, typecount:typecount, mappingcount:mappingcount };
            $scope.isLoading = false;
            $scope.showResultBox = true;
            $scope.resultMessage = result.message;
        }).error(function(data, status, headers, config) {
            $scope.isLoading = false;
            if(status == 403){
                $scope.error = "Operation is Forbidden";
            }else if(status == 401){
                $scope.error= "Unauthorized Operation";
            }else if(status == 400){
                $scope.error = "Bad Request. Server Down";
            }else if(status == 500){
                $scope.error = "Internal Server Error";
            }else{
                $scope.error = "Failed Request with response status "+status;
            }
        });
    };

    $scope.isMissing = function(reference, missingReferences) {
        var index;
        for (index = 0; missingReferences != null && index < missingReferences.length; index++) {
            if (missingReferences[index].namespace == reference.namespace
                && missingReferences[index].name == reference.name
                && missingReferences[index].version == reference.version) {
                return true;
            }
        }
        return false;
    };


    $scope.checkin = function (uploadResults) {
        $rootScope.error = "";
        if(uploadResults.length == 1) {
            checkinSingle(uploadResults[0].stagingId);
        } else {
            checkInMultipleModels(uploadResults);
        }
    };

    checkInMultipleModels = function(uploadResults) {
        var validUploadHandles = [];
        $scope.isLoading = true;
        $scope.loadMessage = "Checking in... Please wait!";
        $scope.showResultBox = false;
        angular.forEach(uploadResults, function (uploadResult, idx) {
            if(uploadResult.valid) {
                var handle = {
                    handleId : uploadResult.handleId,
                    id : {
                        name : uploadResult.modelResource.id.name,
                        namespace : uploadResult.modelResource.id.namespace,
                        version : uploadResult.modelResource.id.version
                    }
                }
                validUploadHandles.push(handle);
            }
        });

        $http.put('./rest/secure/checkInMultiple', validUploadHandles)
        .success(function(result) {
            $scope.isLoading = false;
            $scope.showResultBox = true;
            $scope.resultMessage = "Checkin was successful!";
            $scope.showCheckin = false;
        }).error(function(data, status, headers, config) {
            $scope.isLoading = false;
            if(status == 403){
                $scope.error = "Operation is Forbidden";
            }else if(status == 401){
                $scope.error = "Unauthorized Operation";
            }else if(status == 400){
                $scope.error = "Bad Request. Server Down";
            }else if(status == 500){
                $scope.error = "Internal Server Error";
            }else{
                $scope.error = "Failed Request with response status "+status;
            }
        });
    };

    checkinSingle = function (handleId) {
        $http.put('./rest/secure/'+handleId)
        .success(function(result){
            // $location.path("/details/"+$scope.uploadResult.modelResource.id.namespace+"/"+$scope.uploadResult.modelResource.id.name+"/"+$scope.uploadResult.modelResource.id.version);
            $scope.showResultBox = true;
            $scope.resultMessage = "Checkin was successful!";
            $scope.showCheckin = false;
        }).error(function(data, status, headers, config) {
            if(status == 403){
                $scope.error = "Operation is Forbidden";
            }else if(status == 401){
                $scope.error= "Unauthorized Operation";
            }else if(status == 400){
                $scope.error = "Bad Request. Server Down";
            }else if(status == 500){
                $scope.error = "Internal Server Error";
            }else{
                $scope.error = "Failed Request with response status "+status;
            }
        });;
    };

}]);

repositoryControllers.controller('DetailsController', ['$rootScope', '$scope', '$http','$routeParams','$location', '$route','$uibModal','$timeout','$window',
	function ($rootScope,$scope, $http,$routeParams,$location,$route,$uibModal,$timeout,$window) {
	
    $scope.model = null;
    $scope.platformGeneratorMatrix = null;
    $scope.platformDemoGeneratorMatrix = null;
    $scope.workflowActions = [];
    $scope.chosenFile = false;

    $scope.uploadImage = function () {
        var fd = new FormData();
        fd.append('file', document.getElementById('imageFile').files[0]);
        fd.append('namespace',$scope.model.id.namespace);
        fd.append('name',$scope.model.id.name);
        fd.append('version',$scope.model.id.version);
        $http.post('./rest/model/image/',fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(result){
        	$timeout(function() {
        		$window.location.reload();
        	},500);
        }).error(function(data, status, headers, config) {
            if(status == 403){
                $rootScope.error = "Operation is Forbidden";
            }else if(status == 401){
                $rootScope.error = "Unauthorized Operation";
            }else if(status == 400){
                $rootScope.error = "Bad Request. Server Down";
            }else if(status == 500){
                $rootScope.error = "Internal Server Error";
            }else{
                $rootScope.error = "Failed Request with response status "+status;
            }
        });
    };

    $scope.hasImage = function() {
        if (model.hasImage) {
            return "image"
        } else {
            return "placeHolder"
        }
    };

    $scope.chooseImageFile = function() {
        document.getElementById("imageFile").click();
    };

    $scope.getDetails = function (namespace,name,version) {
        $http.get('./rest/model/'+namespace+'/'+name+'/'+version)
        .success(function(result){
            $scope.model = result;
        });
    };

    $scope.getContent = function (namespace,name,version) {
        $http.get('./rest/model/file/'+namespace+'/'+name+'/'+version)
        .success(function(result) {
            $scope.content = result;
        }).error(function(data, status, headers, config) {
            $scope.error = data.message;
        });
    };

     $scope.getPlatformGenerators = function () {
        $http.get('./rest/generation-router/platform')
        .success(function(result){
        	var productionGenerators = $scope.filterByTag(result,"production");
        	var demoGenerators = $scope.filterByTag(result,"demo");
            $scope.platformGeneratorMatrix = $scope.listToMatrix(productionGenerators, 2);
            $scope.platformDemoGeneratorMatrix = $scope.listToMatrix(demoGenerators, 2);
        });
    };
    
    $scope.filterByTag = function(result, tag) {
    	var filteredList = [];
    	result.forEach(function(e) {
    	   if (e.tags.includes(tag)) {
    	   	filteredList.push(e);
    	   }
    	});
    	return filteredList;
    };

    $scope.isFilled = function(rating, value) {
        if (rating === value) {
            return "filled"
        }
    };

    $scope.listToMatrix = function(list, n) {
        var grid = [], i = 0, x = list.length, col, row = -1;
        for (var i = 0; i < x; i++) {
            col = i % n;
            if (col === 0) {
                grid[++row] = [];
            }
            grid[row][col] = list[i];
        }
        return grid;
    }


    $scope.getDetails($routeParams.namespace,$routeParams.name,$routeParams.version);
    $scope.getContent($routeParams.namespace,$routeParams.name,$routeParams.version);
    $scope.getPlatformGenerators();

    $scope.remove = function(){
        $http.delete('./rest/admin/'+$routeParams.namespace+'/'+$routeParams.name+'/'+$routeParams.version )
        .success(function(result){
            $location.path('/');
        }).error(function(data, status, headers, config) {
            $location.path('/');
        });
    }

    /*
	 * Start - Handling Comments
	 */
    $scope.comments = null;
    $authority		= $rootScope.authority;

    $scope.getCommentsForModelId = function(){

        $http.get('./rest/comments/model/'+$routeParams.namespace+'/'+$routeParams.name+'/'+$routeParams.version)
        .success(function(result){
            $scope.comments = result;
            $scope.comments.reverse();
        }).error(function(data, status, headers, config) {

            if(status == 403){
                $rootScope.error = "Operation is Forbidden";
            }else if(status == 401){
                $rootScope.error = "Unauthorized Operation";
            }else if(status == 400){
                $rootScope.error = "Could not be removed because it references other models "+JSON.stringify(data);
            }else if(status == 500){
                $rootScope.error = "Internal Server Error";
            }else{
                $rootScope.error = "Failed Request with response status "+status;
            }
        });
    }

    $scope.getCommentsForModelId();

    $scope.createComment = function(commentContent){

        $scope.date = new Date();

        var comment = {
            "modelId"	: $routeParams.namespace + '.' + $routeParams.name + ':' + $routeParams.version,
            "author"	: $scope.user,
            "date"		: $scope.date,
            "content"	: commentContent
        }

        $http.post('./rest/comments', comment)
        .success(function(result){
            $scope.comments.reverse();
            $scope.comments.push(comment);
            $scope.comments.reverse();

        }).error(function(data, status, headers, config) {
            if(status == 403){
                $rootScope.error = "Operation is Forbidden";
            }else if(status == 401){
                $rootScope.error = "Unauthorized Operation";
            }else if(status == 400){
                $rootScope.error = "Could not be removed because it references other models "+JSON.stringify(data);
            }else if(status == 500){
                $rootScope.error = "Internal Server Error";
            }else{
                $rootScope.error = "Failed Request with response status "+status;
            }
        });

        $scope.commentContent = "";
        $scope.getCommentsForModelId();
    }

    /*
	 * Stop - Handling Comments
	 */

	$scope.openGeneratorConfig = function(generator) {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: "GeneratorConfigController",
        templateUrl: "partials/generator-config-template.html",
        size: "lg",
        resolve: {
    		generator: function () {
      			return generator;
    		},
    		model: function() {
    			return $scope.model;
    		}
  		}
      });
    };
    
    $scope.getWorkflowActions = function() {
    	$http.get('./rest/workflows/actions/'+$routeParams.namespace+'/'+$routeParams.name+'/'+$routeParams.version)
	        .success(function(result){
	            $scope.workflowActions = result;
	        });
    };
    
   $scope.getWorkflowActions();
   
   $scope.openWorkflowActionDialog = function(action) {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: function($scope, model) {
        	$scope.action = action;
        	$scope.actionModel = null;
        	$scope.model = model;
			 $scope.takeWorkflowAction = function() {
		    	$http.put('./rest/workflows/actions/'+$scope.model.id.namespace+'/'+$scope.model.id.name+'/'+$scope.model.id.version+'/'+$scope.action)
			        .success(function(result){
			            modalInstance.close();
			        });
		    };
		    
		    $scope.getModel = function() {
		    	if ($scope.action != 'claim') {
			    	$http.get('./rest/workflows/model/'+$routeParams.namespace+'/'+$routeParams.name+'/'+$routeParams.version)
				        .success(function(result){
				        	for(var i = 0; i < result.actions.length;i++) {
				        		if (result.actions[i].name === $scope.action) {
				        			$scope.actionModel = result.actions[i];
				        			return;
				        		}
				        	}
		        	});
	        	}
		    };
		    
		    $scope.getModel();
		    
		    $scope.cancel = function() {
				modalInstance.dismiss();
			};
        },
        templateUrl: "workflowActionDialog.html",
        size: "lg",
        resolve: {
    		action: function () {
      			return action;
    		},
    		model: function() {
    			return $scope.model;
    		}
  		}
      });
      
      modalInstance.result.then(
				function() {
				    $window.location.reload();
				});
    };

}]);

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
		$http.get('./rest/generation-router/info/'+$scope.generator.key)
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
     	window.location.assign('./rest/generation-router/'+$scope.model.id.namespace+'/'+$scope.model.id.name+'/'+$scope.model.id.version+'/'+$scope.generator.key+requestParams);
 	 	$uibModalInstance.dismiss("cancel");
    };

    $scope.cancel = function() {
     	$uibModalInstance.dismiss("cancel");
    };
}]);

repositoryControllers.controller('GeneratorController', [ '$scope','$http', 
	function ($scope,$http) {

    $scope.generators = [];
    $scope.mostUsedGenerators = [];

    $scope.listGenerators = function() {
        $http.get('./rest/generation-router/platform').success(
            function(data, status, headers, config) {
                $scope.generators = data;
            });
    };

    $scope.listTopUsed = function() {
        $http.get('./rest/generation-router/topused/3').success(
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

repositoryControllers.controller('AuthenticateController',['$scope', '$rootScope', '$location', '$http',
     function($scope, $rootScope, $location, $http) {
    	
}]);

repositoryControllers.controller('SettingsController', [ '$location', '$rootScope', '$scope', '$http', '$uibModal', 
	function ($location, $rootScope, $scope, $http, $uibModal, ) {

	$scope.openRemoveAccount = function() {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: "RemoveAccountModalController",
        templateUrl: "deleteAccount.html",
        size: "medium",
      });
    };
    
}]);

repositoryControllers.controller('RemoveAccountModalController', [ '$location', '$scope', '$rootScope', '$http', '$uibModalInstance', 
	function ($location, $scope, $rootScope, $http, $uibModalInstance) {

	$scope.deleteAccount = function() {
	   	$http.delete('./rest/users/'+$rootScope.user)
	       .then(function(response) {
	           $scope.user = response.data;
	           if (response.status === 200){
	        	   $rootScope.logout();
	        	   $uibModalInstance.dismiss("cancel");
	           }
	       });
	};

	$scope.cancel = function() {
		$uibModalInstance.dismiss("cancel");
	};
}]);

repositoryControllers.controller('SignUpController', [ '$location', '$rootScope', '$scope', '$http', '$routeParams', 
	function ($location, $rootScope, $scope, $http, $routeParams) {

	$scope.acceptTerms = false;
	
	var isAcceptingTermsAndCondition = false;
	
    $scope.acceptTermsAndConditions = function() {
    	isAcceptingTermsAndCondition = true;
    	
        $http.post('./rest/user/acceptTermsAndCondition', {
            headers: {'Content-Type': "application/json"}
        })
        .success( function(data, status, headers, config) {
        	isAcceptingTermsAndCondition = false;
        	$rootScope.getUser();
            $location.path('/');
        }).error(function(data, status, headers, config) {
        	isAcceptingTermsAndCondition = false;
        });
    }
}]);

repositoryControllers.controller('SwaggerController', [ '$location', '$scope','$http', 
	function ($location,$scope,$http) {
	
    $scope.isLoading = true;
    $scope.url = $scope.swaggerUrl = 'v2/api-docs';
    $scope.defaultErrorHandler = function(data, status) {
        alert('Error Loading Swagger API!');
    };
    
}]);
