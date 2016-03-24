var repositoryControllers = angular.module('repositoryControllers', ['swaggerUi']);

repositoryControllers.controller('SearchController', [ '$scope','$http', '$location', function ($scope,$http,$location) {

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
  		$http.get('./rest/model/query=' + filter).success(
	      function(data, status, headers, config) {
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


repositoryControllers.controller('UploadController', ['$scope', '$rootScope', '$http','$location', function ($scope, $rootScope, $http, $location) {

    $scope.modelFile = null;
    $scope.uploadResult = {};

    $scope.upload = function () {
    	var fd = new FormData();
        fd.append('file', document.getElementById('file').files[0]);
        $http.post('./rest/secure/',fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(result){
        	$scope.uploadResult = result;
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

    $scope.checkin = function (handleId) {
        $http.put('./rest/secure/'+handleId)
        .success(function(result){
          $location.path("/details/"+$scope.uploadResult.modelResource.id.namespace+"/"+$scope.uploadResult.modelResource.id.name+"/"+$scope.uploadResult.modelResource.id.version);
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
	    });;
    };

}]);

repositoryControllers.controller('DetailsController', ['$rootScope', '$scope', '$http','$routeParams','$location', '$route',function ($rootScope,$scope, $http,$routeParams,$location,$route) {
    $scope.model = null;
	$scope.platformGeneratorMatrix = null;
	$scope.documentationGenerators = null;
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
        	  $location.path("/details/"+$scope.model.id.namespace+"/"+$scope.model.id.name+"/"+$scope.model.id.version);
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
        });
    };

    $scope.getPlatformGenerators = function () {
        $http.get('./rest/generation-router/platform')
        .success(function(result){
        	$scope.platformGeneratorMatrix = $scope.listToMatrix(result, 2);
        });
    };

    $scope.getDocumentationGenerators = function () {
        $http.get('./rest/generation-router/documentation')
        .success(function(result){
        	$scope.documentationGenerators = result;
        });
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
    $scope.getDocumentationGenerators();

	$scope.remove = function(){
		$http.delete('./rest/model/'+$routeParams.namespace+'/'+$routeParams.name+'/'+$routeParams.version )
		.success(function(result){
        	$location.path('/');
        }).error(function(data, status, headers, config) {
	    	$location.path('/');
	    });
	}

	/*
		Start - Handling Comments
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
			"modelId"	: '/'+$routeParams.namespace+'/'+$routeParams.name+'/'+$routeParams.version,
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
		Stop - Handling Comments
	*/


}]);

repositoryControllers.controller('GeneratorController', [ '$scope','$http', function ($scope,$http) {

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

repositoryControllers.controller('AuthenticateController', ['$scope', '$rootScope', '$location', '$http',

    function($scope, $rootScope, $location, $http) {

    var authenticate = function(credentials, callback) {

        var headers = credentials ? {authorization : "Basic " + btoa(credentials.username + ":" + credentials.password) } : {};

        $http.get('user', {headers : headers}).success(function(data) {
            if (data.name) {
                $rootScope.authenticated = true;
                $rootScope.getUser();
                $rootScope.auth = btoa(credentials.username + ":" + credentials.password);
            } else {
                $rootScope.authenticated = false;
            }
            callback && callback();
        }).error(function(data) {
            $rootScope.authenticated = false;
            callback && callback();
        });
    }

    authenticate();
    $scope.credentials = {};
    $scope.login = function() {
        authenticate($scope.credentials, function() {
            if ($rootScope.authenticated) {
                $location.path("/");
                $scope.error = false;
            } else {
                $location.path("/login");
                $scope.error = true;
            }
        });
    };

}]);

/*
 * TODO -
 */
repositoryControllers.controller('SignUpController', [ '$location', '$scope','$http', function ($location,$scope,$http) {

	$scope.emailAddressExists = false;
	$scope.usernameExists = false;

	$scope.go = function (path, user) {

		user.firstName = "";
        user.lastName = "";
        user.email = "";
        user.emailCon = "";
        user.username = "";
        user.password = "";
        user.passwordCon = "";

        $location.path("/login");
    };

    $scope.clear = function (user) {

    	user.firstName = "";
        user.lastName = "";
        user.email = "";
        user.emailCon = "";
        user.username = "";
        user.password = "";
        user.passwordCon = "";
    };


    /*
     * checking uniqueness of username and email
     */
	$scope.checkEmailAlreadyExists = function(user){

		$http.post('./rest/users/unique/email', user.email , {
            headers: {'Content-Type': "application/json"}
        }).success(
  		      function(data, status, headers, config) {
  		    	  $scope.emailAddressExists = data;
  		      }).error(function(data, status, headers, config) {
  		      	 });
	}

	$scope.usernameAlreadyExists = function(user){

		$http.post('./rest/users/unique/username', user.username , {
            headers: {'Content-Type': "application/json"}
        }).success(
  		      function(data, status, headers, config) {
  		    	  $scope.usernameExists = data;
  		      }).error(function(data, status, headers, config) {
  		      	 });
	}

	/*
     * create new user
     */
	$scope.register = function(user) {

		var userData = {
                "firstName":user.firstName,
                "lastName":user.lastName,
                "email":user.email,
                "username":user.username,
                "password":user.password
            };

            $http.post('./rest/users', userData, {
                headers: {'Content-Type': "application/json"}
            })
            .success( function(data, status, headers, config) {

            }).error(function(data, status, headers, config) {

    	    });
    }
}]);

/*
 * TODO -
 */
repositoryControllers.controller('SettingsController', [ '$scope','$http','$rootScope', '$location', function ($scope,$http,$rootScope,$location) {

	var currentEmailAddress = "";

	$http.get('./rest/users/'+$rootScope.user).success(
      function(data, status, headers, config) {

			if(data === ""){
				$scope.userExists = false;
			} else {
				$scope.user = data;

				currentEmailAddress = data.email;
				currentUsername = data.username;

				$scope.userExists = true;
			}

      }).error(function(data, status, headers, config) {
      	 });

	$scope.updateProfil = function(user){

		$scope.put = null;

		$http.put('./rest/users/'+$rootScope.user, user)
			.then(function(response) {
				$scope.user = response.data;
				if (response.status === 200){
					$scope.update = true;
				} else if (response.status !== 200){
					$scope.update = false;
				}
				$scope.put=true;
	      });
	}

	$scope.checkEmailAlreadyExists = function(user){

		if (currentEmailAddress === user.email) {
			$scope.emailAddressExists = false;
		} else {
			$http.post('./rest/users/unique/email', user.email)
				.success(
	  		      function(data, status, headers, config) {
	  		    	  $scope.emailAddressExists = data;
	  		      }).error(function(data, status, headers, config) {
	  		      	 });
		}
	}

}]);

repositoryControllers.controller('SwaggerController', [ '$location', '$scope','$http', function ($location,$scope,$http) {
	$scope.isLoading = true;
	$scope.url = $scope.swaggerUrl = 'v2/api-docs';
	$scope.defaultErrorHandler = function(data, status) {
		alert('Error Loading Swagger API!');
	};
}]);
