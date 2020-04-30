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

repositoryControllers.controller('SettingsController', [ '$location', '$rootScope', '$scope', '$http', '$uibModal', '$timeout',
	function ($location, $rootScope, $scope, $http, $uibModal, $timeout) {

  $scope.userId = $rootScope.user;

	$scope.saveSettings = function() {
		 $http.put("./rest/accounts/" + $rootScope.user, $scope.user.email).success(
                function(data, status, headers, config) {
                    $scope.success = true;
                    $timeout(function() {
                        $scope.success = false;
                    },2000);
                }).error(function(data, status, headers, config) {
                    $scope.errorMessage = data.msg;
                    $timeout(function() {
                        $scope.errorMessage = null;
                    },2000);

                });
	};

	$scope.getSettings = function() {
		$http.get("./rest/accounts/" + $rootScope.user).success(
                function(data, status, headers, config) {
                    $scope.user = data;
                }).error(function(data, status, headers, config) {
                   // problem getting user info
                });
	};
	
	$scope.getSettings();

	$scope.openRemoveAccount = function() {
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