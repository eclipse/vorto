define(["../init/AppController"], function(controllers) {
  controllers.controller("DeleteEditorController", DeleteEditorController);

  DeleteEditorController.$inject = [
    "$rootScope", "$scope", "$uibModalInstance", "ShareDataService"
  ]

  function DeleteEditorController($rootScope, $scope, $uibModalInstance, ShareDataService) {
    $scope.tab = ShareDataService.getDeleteEditorTab();

    $scope.ok = function() {
      $uibModalInstance.dismiss("cancel");
      $rootScope.$broadcast("deleteEditor", $scope.tab);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss("cancel");
    };
  }
});
