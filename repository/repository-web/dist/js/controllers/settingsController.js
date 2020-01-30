define(["../init/appController"],function(repositoryControllers) {

repositoryControllers.controller('SettingsController', [ '$location', '$rootScope', '$scope', '$http', '$uibModal', '$timeout',
	function ($location, $rootScope, $scope, $http, $uibModal, $timeout) {
		
	$scope.saveSettings = function() {
		 $http.put("./rest/accounts/" + $rootScope.user, $scope.user.email).success(
                function(data, status, headers, config) {
                    $scope.success = true;
                    $timeout(function() {
                        $scope.success = false;
                    },2000);
                }).error(function(data, status, headers, config) {
                    $scope.errorMessage = data.msg;
                    $timeout(function() {
                        $scope.errorMessage = null;
                    },2000);

                });
	};
	
	$scope.getSettings = function() {
		$http.get("./rest/accounts/" + $rootScope.user).success(
                function(data, status, headers, config) {
                    $scope.user = data;
                }).error(function(data, status, headers, config) {
                   // problem getting user info
                });
	};
	
	$scope.getSettings();

	$scope.openRemoveAccount = function() {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: "RemoveAccountModalController",
        templateUrl: "deleteAccount.html",
        size: "medium",
      });
      
      modalInstance.result.then(
				function () {
					$location.path("/login");
				});
    };
    
}]);

});