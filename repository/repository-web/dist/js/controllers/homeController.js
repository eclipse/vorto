define(["../init/appController"],function(repositoryControllers) {

repositoryControllers.controller('RemoveAccountModalController', [ '$location', '$scope', '$rootScope', '$http', '$uibModalInstance','$window',
	function ($location, $scope, $rootScope, $http, $uibModalInstance,$window) {

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
	
	$scope.soleNamespaceAdmin = true;
	
	$scope.ownsNamespaces = function() {
		var promise = TenantService.getNamespacesForRole('ROLE_TENANT_ADMIN');
		promise.then(
			function(namespaces) {
				if (namespaces != null && namespaces.length > 0) {
					for(var i=0; i < namespaces.length; i++) {
						if (namespaces[i].admins != null && namespaces[i].admins.length < 2) {
							$scope.soleNamespaceAdmin = true;
							return;
						}
					}
				}
				
				$scope.soleNamespaceAdmin = false;
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