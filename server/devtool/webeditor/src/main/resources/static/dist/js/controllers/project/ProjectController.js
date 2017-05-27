define(["angular"], function(angular) {
  angular
    .module("apps.controller")
    .controller("ProjectController", ProjectController);

  ProjectController.$inject = ["$rootScope", "$scope", "$location", "$http", "$uibModal"]

  function ProjectController($rootScope, $scope, $location, $http, $uibModal) {
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
      $http.post("./rest/project", {
        "name": projectName
      }).success(
        function(data, status, headers, config) {
          if (data.message === "resource already exists") {
            window.alert("Project " + projectName + " already exsits")
          } else {
            $location.path("editor/" + projectName);
            $location.replace();
          }
        }).error(function(data, status, headers, config) {
          window.alert("Failed to create new Project " + projectName)
      });
    }

    $scope.getProjects = function() {
      $http.get("./rest/project").success(
        function(data, status, headers, config) {
          $scope.topRow = data.splice(0, gridSize);
          $scope.projectsMatrix = $scope.listToMatrix(data, gridSize);
        }).error(function(data, status, headers, config) {
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

    $scope.displayedProjects = [].concat($scope.projects);
    $scope.itemsByPage = 10;
    $scope.displayedPages = ($scope.projects.length / 2);

    $scope.predicates = ["Name"]

    $scope.getters = {
      name: function(value) {
        return value.name.sort();
      }
    }
  }
});
