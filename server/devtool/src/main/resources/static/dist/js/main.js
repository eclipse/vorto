require.config({
	paths: {
		"angular": "angular/angular.min",
		"angular-route" : "angular/angular-route.min",
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
		}
	}
});
require(['jquery'], function($) {
	$(document).ready(function() {
		require(['devtoolApp'],function(app) {
			app.bootstrap();
		})
	})
});
