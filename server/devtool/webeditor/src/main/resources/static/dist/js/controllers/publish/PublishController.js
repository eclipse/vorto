define(["angular"], function(angular) {
  angular
    .module("apps.controller")
    .controller("PublishController", PublishController);

  PublishController.$inject = ["$rootScope", "$scope", "$uibModalInstance", "$http", "ShareDataService"]

  function PublishController($rootScope, $scope, $uibModalInstance, $http, ShareDataService) {
    $scope.modelCheckedIn = false;
    $scope.result = ShareDataService.getPublishResult();
    $scope.infoModelCount = 0;
    $scope.fbModelCount= 0;
    $scope.typeCount = 0;
    $scope.mappingCount = 0;

    $scope.showResultBox = true;

    $scope.displayValidation = function(result) {
      $scope.stateArr = [];
      $scope.uploadResult = result;
      $scope.showCheckin = true;

      if ($scope.uploadResult.obj != null && $scope.uploadResult.obj.length > 0) {
        angular.forEach($scope.uploadResult.obj, function(resultObject, idx) {
          var item = (idx == 0) ? {
            active: false
          } : {
            active: true
          };
          var modelType = resultObject.modelResource.modelType;
          switch (modelType) {
            case "Functionblock":
              $scope.infoModelCount++;
              break;
            case "InformationModel":
              $scope.fbModelCount++;
              break;
            case "Datatype":
              $scope.typeCount++;
              break;
            case "Mapping":
              $scope.mappingCount++;
              break;
          }
          $scope.stateArr.push(item);
          $scope.showCheckin = (resultObject.valid && $scope.showCheckin);
        });
      } else {
        $scope.showCheckin = false;
      }

      $scope.isLoading = false;
      $scope.showResultBox = true;
      $scope.resultMessage = result.message;
    }

    $scope.displayValidation($scope.result);

    $scope.checkin = function(uploadResults) {
      $rootScope.error = "";
      if (uploadResults.length == 1) {
        checkinSingle(uploadResults[0].handleId);
      } else {
        checkInMultipleModels(uploadResults);
      }
    };

    checkInMultipleModels = function(uploadResults) {
      var validUploadHandles = [];
      $scope.isLoading = true;
      $scope.loadMessage = "Checking in... Please wait!";
      $scope.showResultBox = false;
      angular.forEach(uploadResults, function(uploadResult, idx) {
        if (uploadResult.valid) {
          var handle = {
            handleId: uploadResult.handleId,
            id: {
              name: uploadResult.modelResource.id.name,
              namespace: uploadResult.modelResource.id.namespace,
              version: uploadResult.modelResource.id.version
            }
          }
          validUploadHandles.push(handle);
        }
      });

      $http.put("./rest/publish/checkInMultiple", validUploadHandles)
        .success(function(result) {
          $scope.isLoading = false;
          $scope.showResultBox = true;
          $scope.resultMessage = result.message;
          $scope.modelCheckedIn = true;
        }).error(function(data, status, headers, config) {
          $scope.isLoading = false;
          if (status == 403) {
            $rootScope.error = "Operation is Forbidden";
          } else if (status == 401) {
            $rootScope.error = "Unauthorized Operation";
          } else if (status == 400) {
            $rootScope.error = "Bad Request. Server Down";
          } else if (status == 500) {
            $rootScope.error = "Internal Server Error";
          } else {
            $rootScope.error = "Failed Request with response status " + status;
          }
        });
    };

    checkinSingle = function(handleId) {
      $http.put("./rest/publish/" + handleId)
        .success(function(result) {
          $scope.showResultBox = true;
          $scope.resultMessage = result.message;
          $scope.modelCheckedIn = true;
        }).error(function(data, status, headers, config) {
          if (status == 403) {
            $rootScope.error = "Operation is Forbidden";
          } else if (status == 401) {
            $rootScope.error = "Unauthorized Operation";
          } else if (status == 400) {
            $rootScope.error = "Bad Request. Server Down";
          } else if (status == 500) {
            $rootScope.error = "Internal Server Error";
          } else {
            $rootScope.error = "Failed Request with response status " + status;
          }
        });
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss("cancel");
    };
  }
});
