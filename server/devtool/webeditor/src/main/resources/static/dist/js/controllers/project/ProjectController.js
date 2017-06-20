define(["../init/AppController"], function(controllers) {
  controllers.controller("ProjectController", ProjectController);

  ProjectController.$inject = [
    "$rootScope", "$scope", "$location", "$http", "$uibModal",
    "ProjectDataService", "ToastrService"
  ]

  function ProjectController($rootScope, $scope, $location, $http, $uibModal, ProjectDataService, ToastrService) {
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
        size: "sm"
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
          window.alert("Failed to create new Project " + projectName)
      });
    }

    $scope.getProjects = function() {
      ProjectDataService.getProjects().then(function(data){
        $scope.topRow = data.splice(0, gridSize);
        $scope.projectsMatrix = $scope.listToMatrix(data, gridSize);
      }).catch(function(error){
          $scope.projects = [];
      });
    }

    $scope.listToMatrix = function(list, n) {
        var grid = [], i = 0, x = list.length, col, row = -1;
        for (var i = 0; i < x; i++) {
            col = i % n;
            if (col === 0) {
                grid[++row] = [];
            }
            grid[row][col] = list[i];
        }
        return grid;
    };

    $scope.getProjects();
  }
});
