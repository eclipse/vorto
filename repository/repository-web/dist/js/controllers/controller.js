repositoryControllers.controller('RemoveAccountModalController', [ '$location', '$scope', '$rootScope', '$http', '$uibModalInstance', 
	function ($location, $scope, $rootScope, $http, $uibModalInstance) {

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

}]);