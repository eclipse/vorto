define(["../init/AppController"], function(controllers) {
  controllers.controller("ProjectController", ProjectController);

  ProjectController.$inject = [
    "$rootScope", "$scope", "$location", "$http", "$uibModal",
    "ProjectDataService", "ToastrService", "ShareDataService"
  ]

  function ProjectController($rootScope, $scope, $location, $http, $uibModal, ProjectDataService, ToastrService, ShareDataService) {
    $scope.selectedProject = null;
    $scope.projects = [];
    $scope.topRow = [];
    var gridSize = 6;

    $scope.$on("createProject", function(event, projectName) {
      $scope.createProject(projectName);
    });

    $scope.openCreateProjectModal = function() {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: "CreateProjectController",
        templateUrl: "templates/project/create-project-modal-template.html",
        //size: "sm"
      });
    };

    $scope.createProject = function(projectName) {
      var project = {name: projectName};
      var params = {project: project};
      ProjectDataService.createProject(params).then(function(data){
        if (data.message === "resource already exists") {
          var message = "Project " + projectName + " already exists";
          var params = {message: message};
          ToastrService.createErrorToast(params);
        } else {
          $location.path("projects/" + projectName);
          $location.replace();
        }
      }).catch(function(error){
          var message = "Failed to create Project " + projectName;
          var params = {message: message};
          ToastrService.createErrorToast(params);
      });
    }

    $scope.showProjects = function() {
      ProjectDataService.getProjects().then(function(data){
        $scope.topRow = data.splice(0, gridSize-1);
        $scope.projectsMatrix = $scope.listToMatrix(data, gridSize);
      }).catch(function(error){
          $scope.projects = [];
      });
    }

    $scope.listToMatrix = function(list, gridsize) {
        var grid = [];
        var row = -1;
        for (var i = 0; i < list.length; i++) {
            var col = i % gridsize;
            if (col === 0) {
                grid[++row] = [];
            }
            grid[row][col] = list[i];
        }
        return grid;
    };

    $scope.showProjects();
  }
});
