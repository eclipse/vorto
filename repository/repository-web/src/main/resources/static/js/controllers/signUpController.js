repositoryControllers.controller('SignUpController', [ '$location', '$rootScope', '$scope', '$http', '$routeParams', 
	function ($location, $rootScope, $scope, $http, $routeParams) {

	$scope.acceptTerms = false;
	
	var isAcceptingTermsAndCondition = false;
	
    $scope.acceptTermsAndConditions = function() {
    	isAcceptingTermsAndCondition = true;
    	
        $http.post('./rest/users', {
            headers: {'Content-Type': "application/json"}
        })
        .success( function(data, status, headers, config) {
        	isAcceptingTermsAndCondition = false;
        	$rootScope.getUser();
            $location.path('/');
        }).error(function(data, status, headers, config) {
        	isAcceptingTermsAndCondition = false;
        });
}
}]);