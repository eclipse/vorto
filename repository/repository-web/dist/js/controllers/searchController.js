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
  
  repositoryControllers.controller('SearchController', 
    ['$scope', '$filter', '$rootScope', '$http', '$location', '$uibModal', 'openCreateModelDialog', '$timeout',
    function ($scope,$filter,$rootScope,$http,$location,$uibModal, openCreateModelDialog, $timeout) {

    $scope.models = [];
    $scope.filteredModels = [];
    $scope.modelType = 'all';
    $scope.modelState = $rootScope.authenticated === true ? 'all' :'Released';
    $scope.onlyMyModels = "false";
    $scope.onlyPublicModels = "false";
    $scope.queryFilter = "";
    $scope.fileToUpload = null;
    $scope.isLoading = false;
    $scope.onlyYourModels = false;
    
    $scope.clearInput = function() {
        $scope.queryFilter = "";
    };
 
    $scope.searchOnEnter = function(keyEvent) {
        if (keyEvent.keyCode === 13) {
        	$scope.search();  
        }
    };

    $scope.search = function() {
    	$scope.isLoading = true;
    	var filter = "";
    	       
        if ($scope.modelState === 'all' && $scope.modelType === 'all' && $scope.onlyMyModels === false && $scope.onlyPublicModels === false) {
        	filter = $scope.queryFilter;
        } else {
        	if ($scope.modelType !== 'all') {
        		filter += "type:"+$scope.modelType+" ";
        	}
        	if ($scope.modelState !== 'all') {
        		filter += "state:"+$scope.modelState+" ";
        	}
        	
        	if ($scope.onlyMyModels === true) {
        		filter += "author:"+$rootScope.user+" ";
        	}
        	
        	if ($scope.onlyPublicModels === true) {
        		filter += "visibility:public ";
        	}
        	
        	if ($scope.queryFilter !== "") {
        		filter += $scope.queryFilter
        	}
        }

        $http.get('./api/v1/search/models?expression=' + filter).success(
            function(data, status, headers, config) {
            	$scope.models = data;
            	$scope.modelsTotal = data.length;
                $scope.isLoading = false;
                filterModels();
            }).error(function(data, status, headers, config) {
                $scope.models = [];
                $scope.filteredModels = [];
                $scope.isLoading = false;
            });
    };

    $scope.search();

    $scope.itemsByPage 		= 15;
    $scope.displayedPages 	= ($scope.models.length / 2);

    $scope.getters= {
        modelType: function (value) {
            return value.id.modelType.sort();
        },
        namespace: function (value) {
            return value.id.namespace.sort();
        },
        name: function (value) {
            return value.id.name.sort();
        },
        version: function (value) {
            return value.id.version.sort();
        }
    }

    $scope.go = function(model){
        $location.path("/details/"+model.id.prettyFormat);
    };

    $scope.showYourModels = function() {
        $scope.onlyYourModels = !$scope.onlyYourModels;
        filterModels();
    };

    function filterModels() {    
        $scope.filteredModels = $scope.models;
        if($scope.onlyYourModels) {
            $scope.filteredModels = $filter('filter')($scope.filteredModels, {author: $rootScope.user });        
        }
        $scope.filteredModels.sort(function(a, b){
        	var result = 0;
        	
        	if((a.state === "Released") && (b.state !== "Released")) {
        		result = -1;
        	} else if((a.state !== "Released") && (b.state === "Released")) {
        		result =  1;
        	} else {
        		result = 0;
        	}
            
            if (result == 0) {
            	if ((a.type === "InformationModel") && (b.type !== "InformationModel")) return -1;
            	if ((a.type !== "InformationModel") && (b.type === "InformationModel")) return 1;
            	if ((a.type === "Functionblock") && (b.type !== "Functionblock")) return -1;
            	if ((a.type !== "Functionblock") && (b.type === "Functionblock")) return 1;
            	if ((a.type === "Datatype") && (b.type !== "Datatype")) return -1;
            	if ((a.type !== "Datatype") && (b.type === "Datatype")) return 1;
            }         
            
            return result;
        });
    };

    $scope.openCreateModelDialog = openCreateModelDialog($scope);
} ]);

});
