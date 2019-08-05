define(["../init/appController"],function(repositoryControllers) {

repositoryControllers.controller("UpdateController", [ "$location", "$rootScope", "$scope", "$http", "$routeParams", 
	function ($location, $rootScope, $scope, $http, $routeParams) {
        $scope.acceptTerms = false;
        $scope.isUpdating = false;
        $scope.accountUpdated = false;

        $scope.doClose = function() {
            $rootScope.init();
            $location.path("/");
        };

        $scope.doUpdate = function() {
            $scope.isUpdating = true;

            var updateSuccess = function(data, status, headers, config) {
                $scope.isUpdating = false;
                $scope.accountUpdated = true;
            };

            var updateError = function(data, status, headers, config) {
                $scope.isUpdating = false;
            };

            $http.post("./rest/accounts/" + $rootScope.user + "/updateTask", { headers: {"Content-Type": "application/json"} })
                .success(updateSuccess)
                .error(updateError);
        };
    }
]);

});