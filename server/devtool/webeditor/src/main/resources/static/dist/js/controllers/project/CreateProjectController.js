define(["../init/AppController"], function(controllers) {
  controllers.controller("CreateProjectController", CreateProjectController);

  CreateProjectController.$inject = ["$rootScope", "$scope", "$uibModalInstance"]

  function CreateProjectController($rootScope, $scope, $uibModalInstance) {
    $scope.projectName;

    $scope.ok = function() {
      $uibModalInstance.close($scope.model);
      $rootScope.$broadcast("createProject", $scope.projectName);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss("cancel");
    };
  }
});
