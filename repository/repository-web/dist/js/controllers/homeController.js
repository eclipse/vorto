define(["../init/appController"],function(repositoryControllers) {

repositoryControllers.controller('RemoveAccountModalController', [ '$location', '$scope', '$rootScope', '$http', '$uibModalInstance','$window', 'TenantService',
	function ($location, $scope, $rootScope, $http, $uibModalInstance,$window, TenantService) {

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
	
	$scope.ownsNamespaces = function() {
		var promise = TenantService.getNamespacesForRole('ROLE_TENANT_ADMIN');
		promise.then(
			function(namespaces) {
				$scope.ownsNamespaces = namespaces.length > 0;
			}
		);
	};
	
	$scope.ownsNamespaces();

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