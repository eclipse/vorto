define("devtoolApp", ["angular", "angular-route", "angular-animate", "angular-aria", "angular-bootstrap", "angular-bootstrap-templates", "smart-table", "controllers", "directives", "jquery"], function(angular) {

  var app = angular.module('devtoolApp', ['ngRoute', 'apps.controller', 'apps.directive', 'ngAnimate', 'ui.bootstrap', 'ui.bootstrap.tpls']);

  app.bootstrap = function() {
    angular.bootstrap(document, ['devtoolApp']);
  };

  app.config(['$routeProvider', '$httpProvider',
    function($routeProvider, $httpProvider) {
      $routeProvider
        .when('/editor/:projectName', {
          templateUrl: "templates/editor-template.html",
          controller: 'EditorController'
        })
        .when('/project', {
          templateUrl: "templates/project-template.html",
          controller: 'ProjectController'
        })
        .otherwise({
          redirectTo: '/project'
        });

    }
  ]).run(function($location, $http, $rootScope) {
    
    $rootScope.getContext = function() {
      $http.get('./rest/context').success(function(data, status, headers, config) {
		$rootScope.globalContext = data;
      });
    }
    
    $rootScope.getContext();
  });

  return app;
});
