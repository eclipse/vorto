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

repositoryControllers.controller("UpdateController", [ "$location", "$rootScope", "$scope", "$http", "$routeParams", 
	function ($location, $rootScope, $scope, $http, $routeParams) {
        $scope.acceptTerms = false;
        $scope.isUpdating = false;
        $scope.accountUpdated = false;

        $scope.doClose = function() {
            $rootScope.init();
            $location.path("/");
        };

        $scope.doUpdate = function() {
            $scope.isUpdating = true;

            var updateSuccess = function(data, status, headers, config) {
                $scope.isUpdating = false;
                $scope.accountUpdated = true;
            };

            var updateError = function(data, status, headers, config) {
                $scope.isUpdating = false;
            };

            $http.post("./rest/accounts/" + $rootScope.user + "/updateTask", { headers: {"Content-Type": "application/json"} })
                .success(updateSuccess)
                .error(updateError);
        };
    }
]);

});