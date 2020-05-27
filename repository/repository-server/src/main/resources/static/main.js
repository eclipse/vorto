/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
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
				"angular-ace-noconflict" : "webjars/ace/src-min-noconflict/ace",
				"angular-ace": "webjars/angular-ui-ace/ui-ace.min",
				"smart-table" : "webjars/angular-smart-table/dist/smart-table.min",
				"pretty-json" : "webjars/ng-prettyjson/dist/ng-prettyjson.min",
				"mode-type" : "webjars/repository-web/dist/js/ace-modes/mode-type",
				"mode-fbmodel" : "webjars/repository-web/dist/js/ace-modes/mode-fbmodel",
				"mode-infomodel" : "webjars/repository-web/dist/js/ace-modes/mode-infomodel",
				"mode-mapping" : "webjars/repository-web/dist/js/ace-modes/mode-mapping",
				"ace/ext/language_tools" : "webjars/ace/src/ext-language_tools",
				"webjars/ace/src/ace" : "webjars/ace/1.2.0/src/ace",
				"xtext/xtext-ace" : "xtext/2.20.0/xtext-ace"
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
		},
		'angular-ace-noconflict' : {
			deps: ["angular"]
		},
		'angular-ace' : {
			deps: ["angular","angular-ace-noconflict"]
		}
	}
});
define(['jquery'], function($) {
	$(document).ready(function() {
		require(['repository'],function(app) {
			app.bootstrap();
		})
	})
});