define(["../init/AppController"], function(controllers) {
  controllers.controller("LoginController", LoginController);

  LoginController.$inject = ["$rootScope", "$scope", "$location", "$http", "LoginDataService"]

  function LoginController($rootScope, $scope, $location, LoginDataService) {

    $scope.credentials = {};

    var authenticate = function(credentials, callback) {
      LoginDataService.getUser().then(function(data){
        if (data.name != null) {
          $rootScope.user = data.name;
          $rootScope.authenticated = true;
          $rootScope.getContext();
        } else {
          $location.path("/login");
        }
        callback && callback();
      }).catch(function(error){
        $rootScope.authenticated = false;
        $location.path("/login");
        callback && callback();
      });
    }
  }
});
