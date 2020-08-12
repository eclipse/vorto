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

  repositoryControllers.controller("createTechnicalUserController",
      ["$rootScope", "$scope", "$http", "$uibModal", "$uibModalInstance",
        "namespace", "dialogConfirm", "user", "namespace", "context",
        function ($rootScope, $scope, $http, $uibModal, $uibModalInstance,
            namespace, dialogConfirm, user, namespace, context) {

          $scope.user = user;
          $scope.namespace = namespace;
          $scope.context = context;
          $scope.selectedAuthenticationProviderId = null;
          $scope.technicalUserSubject = null;
          $scope.errorMessage = null;
          $scope.ackCreateTechnicalUser = false;
          $scope.isCurrentlyAddingOrUpdating = false;

          $scope.createNewTechnicalUser = function () {
            $scope.isCurrentlyAddingOrUpdating = true;
            $http.post("./rest/namespaces/" + $scope.namespace.name + "/users",
                {
                  "userId": $scope.user.userId,
                  "roles": $scope.getRoles($scope.user),
                  "authenticationProviderId": $scope.selectedAuthenticationProviderId,
                  "subject": $scope.technicalUserSubject,
                  "isTechnicalUser": true
                })
            .then(
                function (result) {
                  $scope.isCurrentlyAddingOrUpdating = false;
                  $uibModalInstance.close($scope.user);
                },
                function (reason) {
                  $scope.isCurrentlyAddingOrUpdating = false;
                  $scope.errorMessage = "Creation of technical user " +
                      $scope.user.userId + " in namespace " +
                      $scope.namespace.name + " failed. ";
                }
            );
          };

          $scope.cancel = function () {
            $uibModalInstance.dismiss("Canceled.");
          };

          // duplicate from namespaceUserManagement - could not find a way to
          // inject from caller
          $scope.getRoles = function (user) {
            var roles = [];
            if ($scope.user.roleModelCreator) {
              roles.push("model_creator")
            }

            if ($scope.user.roleModelPromoter) {
              roles.push("model_promoter")
            }

            if ($scope.user.roleModelReviewer) {
              roles.push("model_reviewer")
            }

            if ($scope.user.roleModelPublisher) {
              roles.push("model_publisher")
            }

            if ($scope.user.roleUser) {
              roles.push("model_viewer")
            }

            if ($scope.user.roleAdmin) {
              roles.push("namespace_admin")
            }

            return roles;
          };
        }
      ]);
});