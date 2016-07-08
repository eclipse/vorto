define("devtoolApp",["angular","angular-route","angular-animate","angular-aria","angular-bootstrap","angular-bootstrap-templates","smart-table","controllers","jquery"],function(angular) {

	var app = angular.module('devtoolApp', ['ngRoute','apps.controller','ngAnimate','ui.bootstrap','ui.bootstrap.tpls']);
	
	app.bootstrap = function() {
		angular.bootstrap(document,['devtoolApp']);
	};
	
	app.config([ '$routeProvider', '$httpProvider',
		function($routeProvider, $httpProvider) {
			$routeProvider
			.when('/editor', {
				templateUrl : "templates/editor-template.html",
				controller : 'EditorController'
			})
			.otherwise({
				redirectTo : '/editor'
			});
			
} ]).run(function($location, $http, $rootScope) {
	// add global functions here
});
	
	return app;
});