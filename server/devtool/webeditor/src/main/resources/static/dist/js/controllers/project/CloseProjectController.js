define(["../init/AppController"], function(controllers) {
  controllers.controller("CloseProjectController", CloseProjectController);

  CloseProjectController.$inject = [
    "$rootScope", "$scope", "$uibModalInstance", "ShareDataService"
  ]

  function CloseProjectController($rootScope, $scope, $uibModalInstance, ShareDataService) {
    $scope.unsavedFiles = ShareDataService.getUnsavedFiles();

    $scope.yes = function() {
      $rootScope.$broadcast("closeProject", true);
      $uibModalInstance.dismiss("cancel");
    };

    $scope.no = function() {
      $rootScope.$broadcast("closeProject", false);
      $uibModalInstance.dismiss("cancel");
    };
  }
});
