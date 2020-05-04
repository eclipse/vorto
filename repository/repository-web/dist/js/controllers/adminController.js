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
define(["../init/appController"],function(repositoryControllers) {

repositoryControllers.controller('AdminController', ['$scope', '$rootScope', '$http', '$location', '$timeout',
    function ($scope, $rootScope, $http, $location,$timeout) {

        $scope.diagnostics = [];
        $scope.diagnosticsError = "";
        $scope.isRunningDiagnostics = false;
        $scope.hasDiagnosticsError = false;

        $scope.diagnose = function() {
            $scope.isRunningDiagnostics = true;
            $scope.hasDiagnosticsError = false;
            $scope.diagnosticsError = "";
            $http.get('./rest/namespaces/diagnostics')
                .then(function(result) {
                    $scope.hasDiagnosticsError = false;
                    $scope.isRunningDiagnostics = false;
                    $scope.diagnostics = result.data;
                    $timeout(
                        function () {
						              $scope.success = result.data.length == 0;
					              }, 2000
                    );
                },
                function(error) {
                    $scope.hasDiagnosticsError = true;
                    $scope.diagnosticsError = error.data.error + ": " + error.data.message
                    $scope.isRunningDiagnostics = false;
                });
        };

        $scope.isReindexing = false;
        $scope.hasIndexingError = false;
        $scope.hasIndexingResult = false;
        $scope.indexingError = null;
        $scope.indexingResultMessage = null;
        $scope.reindex = function() {
        	$scope.isReindexing = true;
        	$http.post('./rest/reindex')
	        	.then(function(result) {
	        		$scope.indexingResultMessage = 'Indexed ' + result.data.numberOfNamespaces + ' namespaces with ' + result.data.totalNumberOfIndexedModels + ' models.';
	        		$scope.hasIndexingResult = true;
	        		$scope.isReindexing = false;
	        		$scope.hasIndexingError = false;
	            }, function(error) {
	            	$scope.isReindexing = false;
	            	$scope.hasIndexingError = true;
	            	$scope.indexingError = "";
	            });
        };

        $scope.isForceReindexing = false;
        $scope.forceReindexingResultMessage = null;
        $scope.forceReindex = function() {
            $scope.isForceReindexing = true;
            $http.post('./rest/forcereindex')
            .then(function(result) {
                $scope.forceReindexingResultMessage = 'Indexed ' + result.data.numberOfNamespaces + ' namespaces with ' + result.data.totalNumberOfIndexedModels + ' models.';
                $scope.hasIndexingResult = true;
                $scope.isForceReindexing = false;
                $scope.hasForceReindexingError = false;
            }, function(error) {
                $scope.isForceReindexing = false;
                $scope.hasForceReindexingError = true;
                $scope.indexingError = "";
            });
        };
    }
]);

});