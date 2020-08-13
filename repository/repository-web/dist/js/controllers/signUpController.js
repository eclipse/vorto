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

  repositoryControllers.controller('SignUpController',
      ['$location', '$rootScope', '$scope', '$http', '$routeParams',
        "$uibModal",
        function ($location, $rootScope, $scope, $http, $routeParams,
            $uibModal) {

          $scope.acceptTerms = false;
          $scope.acceptPrivacyPolicy = false;
          $scope.isAcceptingTermsAndCondition = false;

          $scope.runTermsAndConditions = function () {
            var modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "signup.html",
              size: "lg",
              controller: function ($scope) {
                $scope.isAcceptingTermsAndCondition = false;

                $scope.acceptTermsAndConditions = function () {
                  $scope.isAcceptingTermsAndCondition = true;

                  $http.post('./rest/accounts', {
                    headers: {'Content-Type': "application/json"}
                  })
                  .then(
                      function (result) {
                        $scope.isAcceptingTermsAndCondition = false;
                        $rootScope.init();
                        $location.path('/');
                        modalInstance.dismiss();
                      },
                      function (error) {
                        $scope.isAcceptingTermsAndCondition = false;
                      }
                  );
                };

              },
              backdrop: 'static'
            });
          };

          $scope.runTermsAndConditions();
        }]);

});