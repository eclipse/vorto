require.config({
    paths: {
        "jquery" : "webjars/jquery/jquery.min",
        "bootstrap" : "webjars/bootstrap/js/bootstrap.min",
		"angular": "webjars/angular/angular.min",
		"angular-route" : "webjars/angular-route/angular-route.min",
		"angular-animate" : "webjars/angular-animate/angular-animate.min",
		"angular-bootstrap" : "webjars/angular-ui-bootstrap/ui-bootstrap.min",
		"angular-bootstrap-templates" : "webjars/angular-ui-bootstrap/ui-bootstrap-tpls.min",
		"angular-ui-select" : "webjars/angular-ui-select/select.min",
		"smart-table" : "webjars/angular-smart-table/dist/smart-table.min",
		"pretty-json" : "webjars/ng-prettyjson/dist/ng-prettyjson.min",
		"mode-vorto" : "webjars/repository-web/dist/js/ace-modes/mode-vorto",
		"mode-type" : "webjars/repository-web/dist/js/ace-modes/mode-type",
		"ace/ext/language_tools" : "webjars/ace/src/ext-language_tools",
		"webjars/ace/src/ace" : "webjars/ace/1.2.0/src/ace",
		"xtext/xtext-ace" : "xtext/2.16.0/xtext-ace"
    },
	waitSeconds: 0,
	shim: {
		'angular': {
			exports: "angular"
		},
		'jquery': {
			exports: "jquery"
		},
		'angular-route' : {
			deps: ["angular"]
		},
		'angular-animate' : {
			deps: ["angular"]
		},
		'angular-bootstrap' : {
			deps: ["angular"]
		},
		'angular-bootstrap-templates' : {
			deps: ["angular"]
		},
		'smart-table' : {
			deps: ["angular"]
		},
		'pretty-json' : {
			deps: ["angular"]
		},
		'angular-ui-select' : {
			deps: ["angular"]
		}
	}
});
require(['jquery'], function($) {
	$(document).ready(function() {
		require(['repository'],function(app) {
			app.bootstrap();
		})
	})
});