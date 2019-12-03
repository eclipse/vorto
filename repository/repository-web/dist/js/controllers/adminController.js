define(["../init/appController"],function(repositoryControllers) {

repositoryControllers.controller('AdminController', ['$scope', '$rootScope', '$http', '$location', '$timeout',
    function ($scope, $rootScope, $http, $location,$timeout) {

        $scope.restore = function () {
            $scope.restoreResult = {};
            $scope.resultMessage = "";
            $rootScope.error = "";

            var fileToUpload = document.getElementById('file-upload').files[0];
            if (fileToUpload != undefined) {
                var filename = document.getElementById('file-upload').files[0].name;
                var fd = new FormData();
                fd.append('file', fileToUpload);
                $http.post('./rest/' + $rootScope.tenant + '/backups', fd, {
                        transformRequest: angular.identity,
                        headers: {
                            'Content-Type': undefined
                        }
                    })
                    .success(function (result) {
                        $location.path("/");
                    });
            } else {
                $rootScope.error = "Choose backup to restore and click Restore.";
            }
        };

        $scope.fileNameChanged = function(element) {
            $scope.$apply(function($scope) {
                $scope.browsedFile= element.files[0].name;
            });  
            $scope.$digest();
        };
        
        $scope.diagnostics = [];
        $scope.diagnosticsError = "";
        $scope.isRunningDiagnostics = false;
        $scope.hasDiagnosticsError = false
        
        $scope.diagnose = function() {            
            $scope.isRunningDiagnostics = true;
            $scope.hasDiagnosticsError = false;
            $scope.diagnosticsError = "";
            $http.get('./rest/' + $rootScope.tenant + '/diagnostics')
                .then(function(result) {
                    $scope.hasDiagnosticsError = false;
                    $scope.isRunningDiagnostics = false;
                    $scope.diagnostics = result.data;
                    $timeout(function () {
						$scope.success = result.data.length == 0;
					}, 2000);
                }, function(error) {
                    $scope.hasDiagnosticsError = true;
                    $scope.diagnosticsError = error.data.error + ": " + error.data.message
                    $scope.isRunningDiagnostics = false;
                });
        };

        $scope.isReindexing = false;
        $scope.hasIndexingError = false;
        $scope.hasIndexingResult = false;
        $scope.indexingError = null;
        $scope.indexingResultMessage = null;
        $scope.reindex = function() {
        	$scope.isReindexing = true;
        	$http.post('./rest/reindex')
	        	.then(function(result) {
	        		$scope.indexingResultMessage = 'Indexed ' + result.data.numberOfTenants + ' tenants with ' + result.data.totalNumberOfIndexedModels + ' models.';
	        		$scope.hasIndexingResult = true;
	        		$scope.isReindexing = false;
	        		$scope.hasIndexingError = false;
	            }, function(error) {
	            	$scope.isReindexing = false;
	            	$scope.hasIndexingError = true;
	            	$scope.indexingError = "";
	            });
        };

        $scope.isForceReindexing = false;
        $scope.hasForceReindexingError = false;
        $scope.hasForceReindexingResult = false;
        $scope.forceReindexingError = null;
        $scope.forceReindexingResultMessage = null;
        $scope.forceReindex = function() {
            $scope.isForceReindexing = true;
            $http.post('./rest/forcereindex')
            .then(function(result) {
                $scope.forceReindexingResultMessage = 'Indexed ' + result.data.numberOfTenants + ' tenants with ' + result.data.totalNumberOfIndexedModels + ' models.';
                $scope.hasForceReindexingResult = true;
                $scope.isForceReindexing = false;
                $scope.hasForceReindexingError = false;
            }, function(error) {
                $scope.isForceReindexing = false;
                $scope.hasForceReindexingError = true;
                $scope.forceReindexingError = "";
            });
        };
    }
]);

});