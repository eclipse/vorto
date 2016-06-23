require.config({
	paths: {
		"angular": "/webjars/angularjs/1.3.13/angular.min",
		"angular-route" : "/webjars/angular-route/1.3.13/angular-route.min",
		"smart-table" : "/webjars/angular-smart-table/2.1.6/dist/smart-table.min",
		"jquery" : "/webjars/jquery/2.1.1/jquery.min",
		"mode-pwl" : "xtext-resources/generated/mode-infomodel",
		"ace/ext/language_tools" : "/webjars/ace/1.2.0/src/ext-language_tools",
		"webjars/ace/1.2.0/src/ace" : "/webjars/ace/1.2.0/src/ace",
		"xtext/xtext-ace" : "/xtext/2.9.2/xtext-ace"
	},
	shim: {
		'angular': {
			exports: "angular"
		},
		'jquery': {
			exports: "jQuery"
		},
		'angular-route' : {
			deps: ["angular"]
		},
		'smart-table' : {
			deps: ["angular"]
		}
	}
});
require(['jquery'], function($) {
	$(document).ready(function() {
		console.log('Ready');
		require(['devtoolApp'],function(app) {
			app.bootstrap();
		})
	})
});