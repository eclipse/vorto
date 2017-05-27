define(["angular"], function(angular) {
  angular
    .module("apps.controller")
    .controller("LoginController", LoginController);

  LoginController.$inject = ["$rootScope", "$scope", "$location", "$http"]

  function LoginController($rootScope, $scope, $location, $http) {

    $scope.credentials = {};

    var authenticate = function(credentials, callback) {
      $http.get("rest/context/user").success(function(data) {
        if (data.name) {
          $rootScope.authenticated = true;
          $rootScope.user = data.name;
          $rootScope.error = false;
          $location.path("/project");
        } else {
          $rootScope.authenticated = false;
        }
        callback && callback();
      }).error(function(data) {
        $rootScope.authenticated = false;
        callback && callback();
      });
    }

    authenticate();

    $scope.login = function() {
      $http.post("/j_spring_security_check", $.param($scope.credentials), {
        headers: {
          "content-type": "application/x-www-form-urlencoded"
        }
      }).success(function(data) {
        authenticate(function() {
          if ($rootScope.authenticated) {
            $scope.error = false;
            $location.path("project");
          } else {
            $scope.error = true;
          }
        });
      }).error(function(data) {
          $scope.error = true;
          $rootScope.authenticated = false;
      })
    };
  }
});
