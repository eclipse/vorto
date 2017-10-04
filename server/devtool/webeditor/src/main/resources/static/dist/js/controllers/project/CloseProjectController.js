define(["../init/AppController"], function(controllers) {
  controllers.controller("CloseProjectController", CloseProjectController);

  CloseProjectController.$inject = [
    "$rootScope", "$scope", "$uibModalInstance", "ShareDataService"
  ]

  function CloseProjectController($rootScope, $scope, $uibModalInstance, ShareDataService) {
    $scope.unsavedFiles = ShareDataService.getUnsavedFiles();
    $scope.isEditorServiceToggled = ShareDataService.getIsEditorServiceToggled();
    $scope.message = "closing";
    if($scope.isEditorServiceToggled){
      $scope.message = "switching modes";
    }

    $scope.yes = function() {
      $scope.broadcast(true);
      $uibModalInstance.dismiss("cancel");
    };

    $scope.no = function() {
      $scope.broadcast(false);
      $uibModalInstance.dismiss("cancel");
    };

    $scope.broadcast = function(save) {
      var broadcastString = "closeProject";
      if($scope.isEditorServiceToggled){
        ShareDataService.setIsEditorServiceToggled(false);
        broadcastString = "closeAllEditors"
      }
      $rootScope.$broadcast(broadcastString, save);
    }
  }
});
