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
    $scope.showLoader = false;

    var gridSize = 6;

    $scope.$on("createProject", function(event, projectName) {
      $scope.createProject(projectName);
    });
    
    $scope.openProject = function(projectName) {
    	$location.path("projects/"+projectName);
        $location.replace();
    };

    $scope.openCreateProjectModal = function() {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: "CreateProjectController",
        templateUrl: "templates/project/create-project-modal-template.html",
        //size: "sm"
      });
    };

	 $scope.openDeleteProjectModal = function(projectName) {
      ShareDataService.setDeleteProjectName(projectName);
      var modalInstance = $uibModal.open({
        animation: true,
        controller: "DeleteProjectController",
        templateUrl: "templates/project/delete-project-modal-template.html",
        //size: "sm"
      });
    };

    $scope.$on("deleteProject", function(event, projectName) {
      $scope.deleteProject(projectName);
    });

    $scope.deleteProject = function(projectName) {
      ProjectDataService.deleteProject({projectName: projectName}).then(function(data){
      	$location.path("projects/");
          $location.replace();
      }).catch(function(error){
        var message = "Failed to delete project " +  projectName ;
        var params = {message: message};
        ToastrService.createErrorToast(params);
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
      $scope.showLoader = true;
      ProjectDataService.getProjects().then(function(data){
        $scope.showLoader = false;
        $scope.topRow = data.splice(0, gridSize-1);
        $scope.projectsMatrix = $scope.listToMatrix(data, gridSize);
      }).catch(function(error){
        $scope.showLoader = false;
        $scope.projects = [];
        var message = "Failed to load projects";
        var params = {message: message};
        ToastrService.createErrorToast(params);
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
