define(["../init/AppController"], function(controllers) {
  controllers.controller("DeleteProjectController", DeleteProjectController);

  DeleteProjectController.$inject = [
    "$rootScope", "$scope", "$uibModalInstance", "ShareDataService"
  ]

  function DeleteProjectController($rootScope, $scope, $uibModalInstance, ShareDataService) {
    $scope.projectName = ShareDataService.getDeleteProjectName();

    $scope.ok = function() {
      $uibModalInstance.dismiss("cancel");
      $rootScope.$broadcast("deleteProject", $scope.projectName);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss("cancel");
    };
  }
});
