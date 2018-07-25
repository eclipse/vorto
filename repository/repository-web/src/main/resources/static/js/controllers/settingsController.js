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