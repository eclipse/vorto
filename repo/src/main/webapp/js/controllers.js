var repositoryControllers = angular.module('repositoryControllers', []);

repositoryControllers.controller('BrowserController', [ '$scope','$http', function ($scope,$http) {

    $scope.models = [];
  	$scope.modelType = 'all';
  	$scope.queryFilter = "*";
  	$scope.fileToUpload = null;
  	
  	$scope.search = function() {	
  		var filter = null;
  		if ($scope.modelType === 'all') {
  			filter = $scope.queryFilter;
  		} else {
  			filter = $scope.queryFilter + " "+$scope.modelType;
  		}
  		$http.get('./rest/model/query=' + filter).success(
	      function(data, status, headers, config) {
					console.log("success : " + JSON.stringify(data));
					$scope.models = data;
	      }).error(function(data, status, headers, config) {
					console.log("error : " + status);
					$scope.models = [];
	    	}); 	
		};
  	
  	$scope.search();

} ]);


repositoryControllers.controller('UploadController', ['$scope', '$rootScope', '$http','$location', function ($scope, $rootScope, $http, $location) {
	
    $scope.modelFile = null;
    $scope.uploadResult = {};

    $scope.upload = function () {
    	var fd = new FormData();
        fd.append('file', document.getElementById('file').files[0]);
        $http.post('./rest/model/',fd, {
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
        $http.put('./rest/model/'+handleId)
        .success(function(result){
          window.location.href="./#/details/"+$scope.uploadResult.modelResource.id.namespace+"/"+$scope.uploadResult.modelResource.id.name+"/"+$scope.uploadResult.modelResource.id.version;
          window.reload();
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

repositoryControllers.controller('DetailsController', ['$rootScope', '$scope', '$http','$routeParams','$location', function ($rootScope,$scope, $http,$routeParams,$location) {
    $scope.model = null;

    $scope.getDetails = function (namespace,name,version) {
        $http.get('./rest/model/'+namespace+'/'+name+'/'+version)
        .success(function(result){
        	$scope.model = result;
        });
    };
    
    $scope.getContent = function (namespace,name,version) {
        $http.get('./rest/model/file/'+namespace+'/'+name+'/'+version)
        .success(function(result){
        	$scope.content = result;
        });
    };
    
    $scope.getDetails($routeParams.namespace,$routeParams.name,$routeParams.version);
    $scope.getContent($routeParams.namespace,$routeParams.name,$routeParams.version);
	
	$scope.remove = function(){
		$http.delete('./rest/model/'+$routeParams.namespace+'/'+$routeParams.name+'/'+$routeParams.version)
        .success(function(result){
          $location.path('/');
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
	    });;
		
	}	
	
}]);

repositoryControllers.controller('AuthenticateController', ['$scope', '$rootScope', '$cookieStore', '$location', '$http', 
		function($scope, $rootScope, $cookieStore, $location, $http) {
	
	$scope.login = function() {	
		console.log("Login Page to be shown ..... ");
		var authentication = $http({
                    method: "POST",
                    url: "./rest/user/authenticate",
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
				    transformRequest: function(obj) {
				        var str = [];
				        for(var p in obj)
				        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
				        return str.join("&");
				    },
				    data: {username: $scope.username, password: $scope.password}
                });
		
		authentication.success(
	      function(data, status, headers, config) {
			if (status==200 && typeof data == 'object') {
				$rootScope.basicAuthHeader = data.httpAuthHeader;
				$rootScope.user = data;
				$rootScope.authenticated = true;
				$cookieStore.put("vorto_auth",data);
				if($rootScope.targetpath && $rootScope.targetpath != "/authentication"){
					console.log("Authenticated. Back to flow ");
					$location.path($rootScope.targetpath);
				}else{
					console.log("Authenticated. Back to home ");
					$location.path("/");
				}
			} else {
				console.log("Authentication failed ");
				$rootScope.authenticated = false;
				$rootScope.error = "Username or Password not correct.";
 			}			
	    });
	    
	    authentication.error(function(data, status, headers, config) {
	    	$rootScope.authenticated = false;
	    	$rootScope.error = "Username or Password not correct.";
	    	console.log("Authentication failed ");
	    });
	};	
	
}]);

