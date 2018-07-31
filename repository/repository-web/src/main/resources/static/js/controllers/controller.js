repositoryControllers.controller('RemoveAccountModalController', [ '$location', '$scope', '$rootScope', '$http', '$uibModalInstance', 
	function ($location, $scope, $rootScope, $http, $uibModalInstance) {

	$scope.deleteAccount = function() {
	   	$http.delete('./rest/users/'+$rootScope.user)
	       .then(function(response) {
	           $scope.user = response.data;
	           if (response.status === 200){
	        	   $rootScope.logout();
	        	   $uibModalInstance.dismiss("cancel");
	           }
	       });
	};

	$scope.cancel = function() {
		$uibModalInstance.dismiss("cancel");
};
}]);

repositoryControllers.controller('LoginController', [ '$location', '$scope', '$rootScope', 
	function ($location, $scope, $rootScope) {

}]);