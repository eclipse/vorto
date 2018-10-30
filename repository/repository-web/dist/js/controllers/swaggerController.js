repositoryControllers.controller('SwaggerController', [ '$location', '$scope','$http', 
	function ($location,$scope,$http) {
	
    $scope.isLoading = true;
    $scope.url = $scope.swaggerUrl = 'v2/api-docs';
    $scope.defaultErrorHandler = function(data, status) {
        alert('Error Loading Swagger API!');
    };
    
}]);