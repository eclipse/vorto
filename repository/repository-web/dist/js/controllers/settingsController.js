repositoryControllers.controller('SettingsController', [ '$location', '$rootScope', '$scope', '$http', '$uibModal', '$timeout',
	function ($location, $rootScope, $scope, $http, $uibModal, $timeout ) {

	$scope.email = "";
	
	$scope.saveSettings = function() {
		 $http.put("./rest/" + $rootScope.tenant + "/accounts/" + $rootScope.user, $scope.email).success(
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

	$scope.openRemoveAccount = function() {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: "RemoveAccountModalController",
        templateUrl: "deleteAccount.html",
        size: "medium",
      });
    };
    
}]);