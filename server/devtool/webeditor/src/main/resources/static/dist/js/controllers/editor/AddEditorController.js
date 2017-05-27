define(["angular"], function(angular) {
  angular
    .module("apps.controller")
    .controller("AddEditorController", AddEditorController);

  AddEditorController.$inject = ["$rootScope", "$scope", "$uibModalInstance", "ShareDataService"]

  function AddEditorController($rootScope, $scope, $uibModalInstance, ShareDataService) {
    $scope.editorTypes = ShareDataService.getAddEditorTypes();
    $scope.selected = $scope.editorTypes[0];

    $scope.ok = function() {
      $uibModalInstance.close($scope.selected.editorType);
      $rootScope.$broadcast("describeEditor", $scope.selected);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss("cancel");
    };
  }
});
