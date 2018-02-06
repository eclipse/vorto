require.config({
	paths: {
		"jquery" : "../../webjars/jquery/3.2.1/jquery.min",
		"angular": "../../webjars/angularjs/1.3.13/angular.min",
		"angular-route" : "../../webjars/angular-route/1.3.13/angular-route.min",
		"angular-animate" : "../../webjars/angular-animate/1.3.13/angular-animate.min",
		"angular-aria" : "../../webjars/angular-aria/1.3.15/angular-aria.min",
		"angular-bootstrap" : "../../webjars/angular-ui-bootstrap/0.14.3/ui-bootstrap.min",
		"angular-bootstrap-templates" : "../../webjars/angular-ui-bootstrap/0.14.3/ui-bootstrap-tpls.min",
		"smart-table" : "../../webjars/angular-smart-table/2.1.6/dist/smart-table.min",
		"mode-pwl" : "xtext-resources/generated/mode-infomodel",
		"ace/ext/language_tools" : "../../webjars/ace/1.2.0/src/ext-language_tools",
		"webjars/ace/1.2.0/src/ace" : "../../webjars/ace/1.2.0/src/ace",
		"xtext/xtext-ace" : "../../xtext/2.9.0/xtext-ace",
		"jstree" : "../../webjars/jstree/3.2.1/jstree.min",
		"ngJsTree": "../../webjars/ng-js-tree/0.0.6/dist/ngJsTree.min",
		"angular-toastr" : "../../webjars/angular-toastr/2.1.1/angular-toastr.tpls.min"
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
		'angular-aria' : {
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
		'ngJsTree' : {
			deps: ["jstree", "angular"]
		},
		'angular-toastr' : {
			deps: ["angular", "angular-animate"]
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
