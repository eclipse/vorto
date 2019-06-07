repositoryControllers.controller('SignUpController', [ '$location', '$rootScope', '$scope', '$http', '$routeParams', 
	function ($location, $rootScope, $scope, $http, $routeParams) {

	$scope.acceptTerms = false;
	
	var isAcceptingTermsAndCondition = false;
	
    $scope.acceptTermsAndConditions = function() {
    	isAcceptingTermsAndCondition = true;
    	
        $http.post('./rest/accounts', {
            headers: {'Content-Type': "application/json"}
        })
        .success( function(data, status, headers, config) {
            isAcceptingTermsAndCondition = false;
            $rootScope.init();
            $location.path('/');
        }).error(function(data, status, headers, config) {
            isAcceptingTermsAndCondition = false;
        });
}
}]);