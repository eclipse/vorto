define("devtoolApp", [
  "angular",
  "angular-aria",
  "angular-route",
  "angular-toastr",
  "angular-animate",
  "angular-bootstrap",
  "angular-bootstrap-templates",
  "jquery",
  "ngJsTree",
  "smart-table",
  "config/init/AppConfigLoader",
  "controllers/init/AppControllerLoader",
  "directives/init/AppDirectiveLoader",
  "services/init/AppServiceLoader"
], function(angular) {

  var app = angular.module("devtoolApp", [
    "ngRoute",
    "ngAnimate",
    "ui.bootstrap",
    "ui.bootstrap.tpls",
    "ngJsTree",
    "toastr",
    "apps.config",
    "apps.controller",
    "apps.directive",
    "apps.service",
  ]);

  app.bootstrap = function() {
    angular.bootstrap(document, ["devtoolApp"]);
  };

  app.config(["$routeProvider", "$httpProvider",
    function($routeProvider, $httpProvider) {
      $routeProvider
        .when("/publish", {
          templateUrl: "templates/publish/publish-template.html",
          controller: "PublishController"
        })
        .when("/editor/:projectName", {
          templateUrl: "templates/editor/editor-template.html",
          controller: "EditorController"
        })
        .when("/project", {
          templateUrl: "templates/project/project-template.html",
          controller: "ProjectController"
        })
        .when("/login", {
          templateUrl: "templates/login/login-template.html",
          controller: "LoginController"
        })
        .otherwise({
          redirectTo: "/project"
        });
    }
  ]).run(function($location, $rootScope, LoginDataService) {

    $rootScope.$on("$locationChangeStart", function(event, next, current) {
      $rootScope.error = false;
      if ($location.path() !== "/login" && $rootScope.authenticated === false) {
        $location.path("/login");
      }
    });

    $rootScope.user = [];

    $rootScope.getUser = function() {
      LoginDataService.getUser().then(function(data){
        if (data.name != null) {
          $rootScope.user = data.name;
          $rootScope.authenticated = true;
          $rootScope.getContext();
        } else {
          $location.path("/login");
        }
      }).catch(function(error){
        $rootScope.authenticated = false;
        $location.path("/login");
      });
    };

    $rootScope.getContext = function() {
      LoginDataService.getContext().then(function(data){
        $rootScope.globalContext = data;
        $location.path("/project");
      }).catch(function(error){
        $rootScope.globalContext = {};
      });
    }

    $rootScope.logout = function() {
      LoginDataService.logout().then(function(data){
        $rootScope.authenticated = false;
        $location.path("/login");
      }).catch(function(error) {
        $location.path("/login");
        $rootScope.authenticated = false;
      });
    };

    $rootScope.getUser();

  });
  return app;
});
