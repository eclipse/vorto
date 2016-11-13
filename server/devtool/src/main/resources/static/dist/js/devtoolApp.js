define("devtoolApp", ["angular", "angular-route", "angular-animate", "angular-aria", "angular-bootstrap", "angular-bootstrap-templates", "smart-table", "controllers", "directives", "jquery", "ngJsTree"], function(angular) {

  var app = angular.module('devtoolApp', ['ngRoute', 'apps.controller', 'apps.directive', 'ngAnimate', 'ui.bootstrap', 'ui.bootstrap.tpls', 'ngJsTree']);

  app.bootstrap = function() {
    angular.bootstrap(document, ['devtoolApp']);
  };

  app.config(['$routeProvider', '$httpProvider',
    function($routeProvider, $httpProvider) {
      $routeProvider
		.when('/publish', {
		   templateUrl: "templates/publish-template.html",
		   controller: 'PublishController'
		  })
        .when('/editor/:projectName', {
          templateUrl: "templates/editor-template.html",
          controller: 'EditorController'
        })
        .when('/project', {
          templateUrl: "templates/project-template.html",
          controller: 'ProjectController'
        })
        .when('/login', {
          templateUrl: "templates/login-template.html",
          controller: 'LoginController'
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
    
	 	$rootScope.$on("$locationChangeStart", function(event, next, current) {
			$rootScope.error = false;
	 		if($location.path() !== "/login" && $rootScope.authenticated === false) {
	 			$location.path('/login');
	 		}
	    });

		$rootScope.user = [];
		$rootScope.getUser = function() {
			$http.get('rest/context/user').success(
	      		function(data, status, headers, config) {
	      			if (data.name != null) {
                    	$rootScope.user = data.name;
                    	$rootScope.authenticated = true;
                    	$rootScope.authority = data.role;
                    	$location.path("/project");
                    } else {
                    	$location.path("/login");
                    }
	      		}).error(function(data, status, headers, config) {
					$rootScope.authenticated = false;
					$location.path("login");
	    		});
		};

		$rootScope.getUser();

		$rootScope.logout = function() {
			$http.post('logout', {}).success(function() {
				$rootScope.authenticated = false;
				$location.path("/login");
			}).error(function(data) {
				$location.path("/login");
				$rootScope.authenticated = false;
			});
		};
  });

  return app;
});
