define(["angular"],function(angular) {
var app = angular.module('apps.controller', []);

app.controller('InfomodelEditorController', function($rootScope, $scope, $http, $location) {
	
	$scope.editor = null;
	$scope.error = null;
	$scope.errorMessage = null;
	$scope.validationError = false;
	require(["webjars/ace/1.2.0/src/ace"], function() {
				require(["xtext/xtext-ace"], function(xtext) {
					editor = xtext.createEditor({syntaxDefinition: "mode-infomodel"});
				});
			});
			
	$scope.share = function() {
		if (editor.xtextServices.editorContext._annotations.length != 0) {
			$scope.error = "Your information model contains errors. Please correct and try again.";
			return;
		}
		
		// do upload and checkin REST call to Vorto Information Model Repository
	};
	
});
return app;
});
