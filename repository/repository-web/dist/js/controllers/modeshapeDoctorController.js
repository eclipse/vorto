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
define(["../init/appController"], function (repositoryControllers) {

    repositoryControllers.controller("modeshapeDoctorController",
        ["$rootScope", "$scope", "$http", "$uibModal", "dialogConfirm",
            "dialogPrompt",
            function ($rootScope, $scope, $http, $uibModal, dialogConfirm,
                      dialogPrompt) {

                // $scope.modeshapePath = "";
                // $scope.updateModeshapePath = function (path) {
                //     $scope.modeshapePath = path;
                // }
                // $scope.readModeshapeNode = function() {
                //     var workspace = "295938ec91084d78bae3a4eacb033abb";
                //     $http.get("/rest/namespaces/diagnostics/modeshape/node/" + workspace + "?path=" + $scope.modeshapePath)
                //         .then(response => {
                //             alert(response.data);
                //         });
                // };

            }]);

    repositoryControllers.directive("modeshapeDoctor", function () {
        return {
            templateUrl: "webjars/repository-web/dist/partials/admin/modeshapeDoctor.html"
        };
    });

});
