define(["../init/AppController"], function(controllers) {
  controllers.controller("CloseEditorController", CloseEditorController);

  CloseEditorController.$inject = [
    "$rootScope", "$scope", "$uibModalInstance", "ShareDataService"
  ]

  function CloseEditorController($rootScope, $scope, $uibModalInstance, ShareDataService) {
    $scope.index = ShareDataService.getCloseEditorIndex();

    $scope.no = function() {
      $rootScope.$broadcast("closeEditor", $scope.index, false);
      $uibModalInstance.dismiss("cancel");
    };

    $scope.yes = function() {
      $rootScope.$broadcast("closeEditor", $scope.index, true);
      $uibModalInstance.dismiss("cancel");
    };
    
  }
});
