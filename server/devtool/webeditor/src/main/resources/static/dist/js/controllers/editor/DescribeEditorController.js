define(["../init/AppController"], function(controllers) {
  controllers.controller("DescribeEditorController", DescribeEditorController);

  DescribeEditorController.$inject = [
    "$rootScope", "$scope", "$uibModalInstance", "ShareDataService"
  ]

  function DescribeEditorController($rootScope, $scope, $uibModalInstance, ShareDataService) {
    $scope.editorType = ShareDataService.getDescribeEditorType();
    $scope.model = {
      modelType: $scope.editorType.modelType,
      language: $scope.editorType.language,
      name: $scope.editorType.displayname,
      displayname: $scope.editorType.displayname,
      namespace: "com.company",
      version: "1.0.0",
      description: "Model description for " + $scope.editorType.displayname,
      category: "demo"
    };

    $scope.ok = function() {
      $uibModalInstance.close($scope.model);
      $scope.model.displayname = $scope.model.name;
      $scope.model.name = $scope.model.name;
      $scope.model.filename = $scope.model.name + "." + $scope.model.language;
      $scope.model.modelSubType = $scope.editorType.modelSubType;
      $scope.model.modelType = $scope.editorType.modelType;
      $rootScope.$broadcast("addTab", $scope.model);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss("cancel");
    };
  }
});
