/*global repositoryControllers*/
repositoryControllers.controller("UpdateController", [ "$location", "$rootScope", "$scope", "$http", "$routeParams", 
	function ($location, $rootScope, $scope, $http, $routeParams) {
        $scope.acceptTerms = false;
        $scope.isUpdating = false;

        $scope.doUpdate = function() {
            $scope.isUpdating = true;

            var updateSuccess = function(data, status, headers, config) {
                $scope.isUpdating = false;
                $rootScope.init();
                $location.path('/');
        	};

            var updateError = function(data, status, headers, config) {
                $scope.isUpdating = false;
            };

            $http.post("./rest/users/" + $rootScope.user + "/updateTask", { headers: {"Content-Type": "application/json"}  })
                .success(updateSuccess)
                .error(updateError);
        }
    }
]);