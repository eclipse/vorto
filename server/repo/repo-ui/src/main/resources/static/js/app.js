var repository = angular.module('repository', ['ngRoute', 'repositoryControllers', 'repositoryDirectives', 'swaggerUi', 'smart-table', 'ngAnimate']);

repository.config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {

	$routeProvider
        .when('/', {
            templateUrl: "partials/search-template.html",
            controller: 'SearchController'
        })
        .when('/upload', {
            templateUrl: "partials/upload-template.html",
            controller: 'UploadController'
        })
        .when('/details/:namespace/:name/:version', {
            templateUrl: "partials/details-template.html",
            controller: 'DetailsController'
        })
        .when('/generators', {
            templateUrl: "partials/generators-template.html",
            controller: 'GeneratorController'
        })
        .when('/api', {
        	templateUrl: "partials/rest-api-template.html",
            controller: 'SwaggerController'
        })
        .when('/settings', {
            templateUrl: "partials/settings-template.html",
            controller: 'SettingsController'
        })
        .when('/login', {
            templateUrl: "partials/login-template.html",
            controller: 'AuthenticateController'
        })
        .when('/signup', {
            templateUrl: "partials/signup-template.html",
            controller: 'SignUpController'
        })
        .otherwise({redirectTo: '/'});

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

}]).run(function($location, $http, $rootScope) {

		// register listener to watch route changes
	 	$rootScope.$on("$locationChangeStart", function(event, next, current) {

	 		if($location.path() === "/upload" && $rootScope.authenticated === false) {
	 			$location.path('/login');
	 		}

	 		if($location.path() === "/settings" && $rootScope.authenticated === false) {
	 			$location.path('/login');
	 		}
	    });

		$rootScope.user = [];
		$rootScope.getUser = function() {
			$http.get('./user').success(
	      		function(data, status, headers, config) {
	      			if (data.name !== null) {
                    	$rootScope.user = data.name;
                    	$rootScope.authenticated = true;
                    	$rootScope.authority = data.role;
                    }
	      		}).error(function(data, status, headers, config) {
					$rootScope.authenticated = false;
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
