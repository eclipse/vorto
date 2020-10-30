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

                $scope.modeshapeData = null;
                $scope.modeshapePath = "";
                $scope.modeshapeWorkspaceId = "";
                $scope.error = null;

                $scope.readModeshapeData = function (modeshapeWorkspaceId, modeshapePath) {
                    $http.get("/rest/namespaces/diagnostics/modeshape/node/" + modeshapeWorkspaceId + "?path=" + modeshapePath)
                        .then(response => {
                            $scope.modeshapeData = response.data;
                            $scope.modeshapePath = modeshapePath;
                            $scope.error = null;
                        }).catch(error => {
                            $scope.error = {message: "Could not load node.", stack: error};
                    });
                };

                $scope.deleteModeshapeNode = function (modeshapeWorkspaceId, modeshapePath) {
                    if (confirm("Are you sure you want to delete " + modeshapePath + "?")) {
                        $http.delete("/rest/namespaces/diagnostics/modeshape/node/" + modeshapeWorkspaceId + "?path=" + modeshapePath)
                            .then(response => {
                                if (response.status !== 200) {
                                    alert('Could not delete node. Status: ' + response.status);
                                }
                                $scope.modeshapeData = null;
                                $scope.error = null;
                            }, error => {
                                $scope.modeshapeData = null;
                                $scope.error = {message: "Could not delete node.", stack: error};
                            });
                    }
                };

                $scope.deleteModeshapeNodeProperty = function (modeshapeWorkspaceId, modeshapePath, propertyName) {
                    if (confirm("Are you sure you want to delete property: " + propertyName + " on node: " + modeshapePath + "?")) {
                        let body = {
                            data: {
                                name: propertyName
                            },
                            headers: {
                                "Content-Type": "application/json;charset=utf-8"
                            }
                        };

                        $http.delete("/rest/namespaces/diagnostics/modeshape/node/" + modeshapeWorkspaceId + "/property?path=" + modeshapePath, body)
                            .then(response => {
                                $scope.readModeshapeData(modeshapeWorkspaceId, modeshapePath);
                                $scope.error = null;
                            }, error => {
                                $scope.modeshapeData = null;
                                $scope.error = {message: "Could not load property.", stack: error};
                            });
                    }
                };

                $scope.deleteModeshapeNodeACL = function (modeshapeWorkspaceId, modeshapePath, principalName) {
                    if (confirm("Are you sure you want to delete the ACL entry for: " + principalName + " on node: " + modeshapePath + "?")) {
                        let body = {
                            data: {
                                principal: principalName
                            },
                            headers: {
                                "Content-Type": "application/json;charset=utf-8"
                            }
                        };

                        $http.delete("/rest/namespaces/diagnostics/modeshape/node/" + modeshapeWorkspaceId + "/acl?path=" + modeshapePath, body)
                            .then(response => {
                                $scope.readModeshapeData(modeshapeWorkspaceId, modeshapePath);
                                $scope.error = null;
                            }, error => {
                                $scope.modeshapeData = null;
                                $scope.error = {message: "Could not load ACL entry.", stack: error};
                            });
                    }
                };

                $scope.openEditModeshapePropertyDialog = function (modeshapeWorkspaceId, modeshapePath, propertyName, propertyValue) {
                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: "webjars/repository-web/dist/partials/admin/editModeshapeProperty.html",
                        size: "lg",
                        controller: function ($scope) {
                            $scope.propertyName = propertyName;
                            $scope.propertyValue = propertyValue;
                            $scope.setModeshapeProperty = function () {
                                let body = {
                                    "name" : $scope.propertyName,
                                    "value" : $scope.propertyValue
                                };
                                $http.put("/rest/namespaces/diagnostics/modeshape/node/" + modeshapeWorkspaceId + "/property?path=" + modeshapePath, body)
                                    .then(response => {
                                        $scope.modeshapeData = response.data;
                                        $scope.modeshapePath = modeshapePath;
                                        $scope.error = null;
                                    }).catch(error => {
                                        $scope.error = {message: "Error updating modeshape node.", stack: error};
                                });
                            }
                        },
                        resolve: {
                            modal: function () {
                                return true;
                            }
                        },
                        backdrop: 'static'
                    });
                };

                $scope.openEditModeshapeACLDialog = function (modeshapeWorkspaceId, modeshapePath, aclEntry) {
                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: "webjars/repository-web/dist/partials/admin/editModeshapeACL.html",
                        size: "lg",
                        controller: function ($scope) {
                            $scope.aclPrincipal = aclEntry.principal;
                            $scope.aclPrivileges = aclEntry.privileges;

                            $scope.addPrivilege = function (privilegeToAdd) {
                                if (privilegeToAdd) {
                                    $scope.aclPrivileges.push(privilegeToAdd);
                                }
                            };

                            $scope.removePrivilege = function (privilegeToRemove) {
                                if (privilegeToRemove) {
                                    $scope.aclPrivileges = $scope.aclPrivileges.filter(function (value, index, arr) {
                                        return value !== privilegeToRemove;
                                    });
                                }
                            };

                            $scope.setModeshapeAclEntry = function () {
                                let body = {
                                    principal : $scope.aclPrincipal,
                                    privileges : $scope.aclPrivileges
                                };
                                $http.put("/rest/namespaces/diagnostics/modeshape/node/" + modeshapeWorkspaceId + "/acl?path=" + modeshapePath, body)
                                    .then(response => {
                                        $scope.modeshapeData = response.data;
                                        $scope.modeshapePath = modeshapePath;
                                        $scope.error = null;
                                    }).catch(error => {
                                    $scope.error = {message: "Error updating ACL.", stack: error};
                                });
                            }
                        },
                        resolve: {
                            modal: function () {
                                return true;
                            }
                        },
                        backdrop: 'static'
                    });
                };
            }]);

    repositoryControllers.directive("modeshapeDoctor", function () {
        return {
            templateUrl: "webjars/repository-web/dist/partials/admin/modeshapeDoctor.html"
        };
    });

});
