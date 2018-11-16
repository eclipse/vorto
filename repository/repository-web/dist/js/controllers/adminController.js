var repositoryControllers = angular.module('repositoryControllers', ['swaggerUi']);

repositoryControllers.controller('AdminController', ['$scope', '$rootScope', '$http', '$location',
    function ($scope, $rootScope, $http, $location) {

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
        $scope.isRunningDiagnostics = false;
        
        $scope.diagnose = function() {            
            $scope.isRunningDiagnostics = true;
            $http.get('./rest/' + $rootScope.tenant + '/diagnostics')
                .then(function(result) {
                    console.log(JSON.stringify(result));
                    $scope.isRunningDiagnostics = false;
                    $scope.diagnostics = result.data;
                });
        };

    }
]);