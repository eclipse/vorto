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
        '$window',
        function ($location, $rootScope, $scope, $http, $uibModal, $timeout,
            $window) {

          $scope.user = null;

          $scope.initialize = function() {
            if ($rootScope.userInfo) {
              $scope.user = {
                "username" : $rootScope.userInfo.name,
                "authenticationProvider" : $rootScope.userInfo.provider.id
              };
              $scope.getEmail();
            }
            else {
              $http.get("./user")
              .then(
                  function(result) {
                    $scope.user =
                        {
                          "username" : result.data.name,
                          "authenticationProvider": result.data.provider.id
                        };
                    $scope.getEmail();
                  },
                  function(error) {
                    $scope.success = false;
                    $scope.errorMessage = "There was an issue while retrieving user data. Please reload this page and try again.";
                    angular.element(document).ready(function () {
                      $scope.toggleSaveButtonAvailability(false);
                    });
                  }
              )
            }
          }

          $scope.getEmail = function() {
            if ($scope.user) {
              /*
                Once we have the username + oauth provider id, we need
                an additional call to the account settings to get the
                e-mail address.
                I expect this is in order to minimize the exposition of that
                field all across the front-end.
                */
              $http.get("./rest/accounts", {
                params: {
                  "username": $scope.user.username,
                  "authenticationProvider": $scope.user.authenticationProvider
                }
              })
              .then(
                  function (result) {
                    $scope.user.email = result.data.email;
                  },
                  function (error) {
                    $scope.success = false;
                    $scope.errorMessage = "There was an issue while retrieving user data. Please reload this page and try again.";
                    angular.element(document).ready(function () {
                      $scope.toggleSaveButtonAvailability(false);
                    });
                  }
              )
            }
          }

          $scope.initialize();

          // HTML autofocus does not work well with angularJS it seems
          angular.element(document).ready(function () {
            let element = document.getElementById("email");
            if (element) {
              element.focus();
            }
          });

          $scope.reload = function () {
            $window.location.reload();
          }

          $scope.toggleSaveButtonAvailability = function (value) {
            let saveButton = document.getElementById("submit");
            if (saveButton) {
              saveButton.disabled = !value;
            }
          }

          $scope.toggleCancelButtonAvailability = function (value) {
            let cancelButton = document.getElementById("cancel");
            if (cancelButton) {
              cancelButton.disabled = !value;
            }
          }

          $scope.saveSettings = function () {
            $http.put("./rest/accounts/", $scope.user)
            .then(
                function (result) {
                  $scope.success = true;
                  $scope.errorMessage = null;
                  $scope.toggleSaveButtonAvailability(false);
                  $scope.toggleCancelButtonAvailability(false);
                },
                function (error) {
                  $scope.success = false;
                  $scope.errorMessage = error.data.msg;
                  $scope.toggleSaveButtonAvailability(true);
                  $scope.toggleCancelButtonAvailability(false);
                  $timeout(
                      function () {
                        $scope.errorMessage = "Saving the settings timed out. Please try again.";
                      },
                      2000
                  );
                }
            );
          };

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
