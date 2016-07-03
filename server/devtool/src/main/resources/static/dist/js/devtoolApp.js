define("devtoolApp",["angular","angular-route","smart-table","controllers"],function(angular) {

	var app = angular.module('devtoolApp', ['ngRoute','apps.controller']);
	
	app.bootstrap = function() {
		angular.bootstrap(document,['devtoolApp']);
	};
	
	app.config([ '$routeProvider', '$httpProvider',
		function($routeProvider, $httpProvider) {
			$routeProvider
			.when('/infomodel-editor', {
				templateUrl : "templates/infomodel-editor-template.html",
				controller : 'InfomodelEditorController'
			})
			.when('/functionblock-editor', {
				templateUrl : "templates/functionblock-editor-template.html",
				controller : 'FunctionblockEditorController'
			})
			.otherwise({
				redirectTo : '/infomodel-editor'
			});
			
} ]).run(function($location, $http, $rootScope) {
	// add global functions here
});
	
	return app;
});