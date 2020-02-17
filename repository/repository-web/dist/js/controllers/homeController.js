define(["../init/appController"],function(repositoryControllers) {

repositoryControllers.controller('RemoveAccountModalController', [ '$location', '$scope', '$rootScope', '$http', '$uibModalInstance','$window',
	function ($location, $scope, $rootScope, $http, $uibModalInstance,$window) {

		$scope.isOnlyAdminForNamespaces = function() {
			$http
			.get("./rest/namespaces/userIsOnlyAdmin")
			.then(
					function(result) {
						// result.data cannot be undefined, i.e. falsey, so either
						// this or error
						$scope.soleNamespaceAdmin = result.data;
					},
					function(error) {
						// no error handling within context but setting soleNamespaceAdmin
						// as false for safety
						$scope.soleNamespaceAdmin = true;
					}
			);
		};

		$scope.isOnlyAdminForNamespaces();

	$scope.deleteAccount = function() {
	   	$http.delete('./rest/accounts/'+$rootScope.user)
	       .then(function(response) {
	    	   console.log(JSON.stringify(response));
	           $scope.user = response.data;
	           if (response.status === 204){
	        	   $rootScope.logout();
	        	   $uibModalInstance.dismiss("cancel");
	           }
	       });
	};
	
	$scope.cancel = function() {
		$uibModalInstance.dismiss("cancel");
};
}]);

/*global repositoryControllers*/
repositoryControllers.controller("LoginController", [ "$location", "$scope", "$rootScope", 
	function ($location, $scope, $rootScope) {
	
	if ($rootScope.authenticated === true) {
        $location.path("/");
    };
	
}]);

});