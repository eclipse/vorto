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
define(["../init/appService"], function(repository) {
  var timeout = 30000;
  repository.factory('SessionTimeoutService',['$rootScope', '$http','$q','$location', '$timeout', function($rootScope, $http, $q, $location, $timeout) {
    return {
      initSessionTimeoutWarning: initSessionTimeoutWarning,
      extendSession: extendSession,
      checkSessionTimeout: checkSessionTimeout
    };

    function initSessionTimeoutWarning($scope) {
      $scope.logout = $rootScope.logout;
      $scope.extendSession = function() {
        extendSession($scope);
      };
      checkSessionTimeout($scope);
    }

    function extendSession($scope) {
      $http.get('./rest/accounts/' + $rootScope.user)
        .error(function (result) {
          $rootScope.logout();
        });
      checkSessionTimeout($scope);
    }

    function checkSessionTimeout($scope) {
      $http.get('rest/config/session/timeout')
        .then(function(result) {
          var timeoutOffset;
          if ((result.data * 1000) > 300000) {
            timeoutOffset = 300000;
          } else {
            timeoutOffset = (result.data * 1000) / 2;
          }
          timeout = result.data * 1000 - timeoutOffset; // seconds to milliseconds
          $scope.sessionExpirationWarning = false;
          $scope.sessionExpirationPromise = $timeout(function() {
            return $rootScope.authenticated; // only display warning if the user is authenticated
          }, timeout);
          $scope.sessionExpirationPromise.then(function(value) {
            $scope.sessionExpirationWarning = value;
            countDown($scope, timeoutOffset)
          });
        });
    }

    function countDown($scope, timeoutOffset) {
      $scope.sessionTimeoutCountdown = timeoutOffset / 1000;
      $scope.sessionTimeoutCountdownStep = function() {
        $scope.sessionTimeoutCountdown = $scope.sessionTimeoutCountdown - 1;
        $scope.sessionTimeoutDisplayValue = buildDisplayString($scope);
        countdown = $timeout($scope.sessionTimeoutCountdownStep, 1000);
        if ($scope.sessionTimeoutCountdown <= 0) {
          $timeout.cancel(countdown);
        }
      };
      var countdown = $timeout($scope.sessionTimeoutCountdownStep, 1000);
    }

    function buildDisplayString($scope) {
      var minutes = Math.floor($scope.sessionTimeoutCountdown / 60);
      var seconds = ($scope.sessionTimeoutCountdown % 60).toFixed(0);
      var minuteLabel;
      if (minutes == 1) {
        minuteLabel = " minute ";
      } else {
        minuteLabel = " minutes ";
      }
      var secondLabel;
      if (seconds == 1) {
        secondLabel = " second";
      } else {
        secondLabel = " seconds";
      }
      return minutes + minuteLabel + seconds + secondLabel;
    }
  }]);
});
