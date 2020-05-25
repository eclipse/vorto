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

  repositoryControllers.controller('SettingsController',
      ['$location', '$rootScope', '$scope', '$http', '$uibModal', '$timeout',
        function ($location, $rootScope, $scope, $http, $uibModal, $timeout) {

          let email = "";
          let username = "";

          /*
            Retrieves username from root scope initially, in order to fetch
            settings (so far only e-mail) from back-end.
            Persists username to session storage.
           */
          if ($rootScope.user) {
            $scope.username = $rootScope.user;
            if (sessionStorage) {
              sessionStorage.username = JSON.stringify($scope.username);
            }
          } else if (sessionStorage && sessionStorage.username) {
            $scope.username = JSON.parse(sessionStorage.username);
          }

          $scope.toggleSaveButtonAvailability = function (value) {
            let saveButton = document.getElementById("submit");
            if (saveButton) {
              saveButton.disabled = !value;
            }
          }

          $scope.saveSettings = function () {
            $http.put("./rest/accounts/" + $scope.username, $scope.email)
            .success(
                function (result) {
                  $scope.success = true;
                  $scope.errorMessage = null;
                  if (sessionStorage) {
                    sessionStorage.username = JSON.stringify($scope.username);
                  }
                  $scope.toggleSaveButtonAvailability(false);
                }
            )
            .error(
                function (data, status, headers, config) {
                  $scope.success = false;
                  $scope.errorMessage = data.msg;
                  $scope.toggleSaveButtonAvailability(true);
                  $timeout(
                      function () {
                        $scope.errorMessage = "Saving the settings timed out. Please try again.";
                      },
                      2000
                  );
                });
          };

          $scope.getSettings = function () {
            $http.get("./rest/accounts/" + $scope.username)
            .success(
                function (data, status, headers, config) {
                  $scope.errorMessage = null;
                  $scope.username = data.username;
                  $scope.email = data.email;
                  if (sessionStorage) {
                    sessionStorage.username = JSON.stringify($scope.username);
                  }
                  // will likely fail as DOM not loaded but that's ok, it's
                  // enabled by default
                  $scope.toggleSaveButtonAvailability(true);
                }
            )
            .error(
                function (reason) {
                  $scope.success = false;
                  $scope.errorMessage = "There was an issue while retrieving user data. Please reload this page and try again.";
                  angular.element(document).ready(function () {
                    $scope.toggleSaveButtonAvailability(false);
                  });
                });
          };

          $scope.getSettings();

          $scope.openRemoveAccount = function () {
            var modalInstance = $uibModal.open({
              animation: true,
              controller: "RemoveAccountModalController",
              templateUrl: "deleteAccount.html",
              size: "medium"
            });

            modalInstance.result.then(
                function () {
                  $location.path("/login");
                }
            );
          };

        }]);

});