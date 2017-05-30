define(["../init/AppController"], function(controllers) {
  controllers.controller("UnsavedEditorFilesController", UnsavedEditorFilesController);

  UnsavedEditorFilesController.$inject = [
    "$rootScope", "$scope", "$uibModalInstance", "ShareDataService"
  ]

  function UnsavedEditorFilesController($rootScope, $scope, $uibModalInstance, ShareDataService) {
    $scope.unsavedFiles = ShareDataService.getUnsavedFiles();
    $scope.showUnsavedFiles = $scope.unsavedFiles.length > 0;

    $scope.close = function() {
      $uibModalInstance.dismiss("cancel");
    };
  }
});
