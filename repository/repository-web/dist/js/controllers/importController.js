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

repositoryControllers.controller('ImportController', ['$scope', '$rootScope', '$http', '$location',
    function ($scope, $rootScope, $http, $location) {

        $scope.selectedImporter = { key: 'Vorto' };

        $scope.isLoading = false;
        $scope.fileAdded = false;
        $scope.beingUploaded = false;
        $scope.beingCheckedIn = false;
        $scope.targetNamespace = null;

		$scope.getNamespaces = function() {
		    $scope.userNamespaces = [];
        $http.get("./rest/namespaces/role/model_creator")
          .then(function(result) {
              if (result.data) {
                  $scope.userNamespaces = result.data;
                  if ($scope.userNamespaces.length > 0) {
                      $scope.userNamespaces.sort(function (a, b) {
                          return a.name.localeCompare(b.name);
                      });
                  }
              }
          },
          function(reason) {
            // TODO : handling of failures
          });
		};
                    
		$scope.getNamespaces();

    $scope.uploadModel = function () {
        $scope.showResultBox = false;
        $scope.uploadResult = {};
        $scope.resultMessage = "";
			  $scope.fileImported = false;
        $scope.beingUploaded = true;
        $scope.error = null;

            var fileToUpload = document.getElementById('file-upload').files[0];
            if (fileToUpload != undefined) {
                var filename = document.getElementById('file-upload').files[0].name;
                var extn = filename.split(".").pop();
                upload('./api/v1/importers?targetNamespace='+$scope.targetNamespace.name, fileToUpload);
                
            } else {
                $rootScope.error = "Choose model file(s) and click Upload.";
            }
        };

        $scope.fileNameChanged = function (element) {
            $scope.$apply(function ($scope) {
                $scope.browsedFile = element.files[0].name;
            });
            $scope.fileAdded = true;
            $scope.showCheckin = false;
            $scope.showResultBox = false;
            $scope.$digest();
        };

        upload = function (url, fileToUpload) {
            $scope.isLoading = true;
            $scope.modelStats = {};
            var infocount = 0, fbcount = 0, typecount = 0, mappingcount = 0;
            var fd = new FormData();
            fd.append('file', fileToUpload);
            fd.append('key', $scope.selectedImporter.key);
            $http.post(url, fd, {
                transformRequest: angular.identity,
                headers: { 'Content-Type': undefined }
            })
                .success(function (result) {
                    $scope.isLoading = false;
                    $scope.uploadResult = result;
                    $scope.showResultBox = true;
                    $scope.showCheckin = true;
                    $scope.stateArr = [];
                    $scope.fileAdded = true;
                    $scope.hasWarning = false;

                    for (report of $scope.uploadResult.reports) {
                        if(report.message.severity == "WARNING"){
                            $scope.hasWarning = true;
                        }
                      } 

                    if ($scope.uploadResult.reports.length > 0 && $scope.uploadResult.valid) {
                        angular.forEach($scope.uploadResult.reports, function (resultObject, idx) {
                            var item = (idx == 0) ? { active: false } : { active: true };
                            var modelType = resultObject.model.type;
                            switch (modelType) {
                                case "Functionblock":
                                    fbcount++;
                                    break;
                                case "InformationModel":
                                    infocount++;
                                    break;
                                case "Datatype":
                                    typecount++;
                                    break;
                                case "Mapping":
                                    mappingcount++;
                                    break;
                            }
                            $scope.stateArr.push(item);
                            $scope.showCheckin = (resultObject.valid && $scope.showCheckin);
                            $scope.fileAdded = !(resultObject.valid && $scope.showCheckin);
                        });
                    } else {
                        $scope.showCheckin = false;
                        $scope.fileAdded = true;
                    }

                    $scope.modelStats = { infocount: infocount, fbcount: fbcount, typecount: typecount, mappingcount: mappingcount };
                    $scope.beingUploaded = false;
                    $scope.resultMessage = result.message;
                }).error(function (data, status, headers, config) {
                    $scope.isLoading = false;
                    $scope.beingUploaded = false;
                    if (status == 403) {
                        $scope.error = "Operation is Forbidden";
                    } else if (status == 401) {
                        $scope.error = "Unauthorized Operation";
                    } else if (status == 400) {
                        $scope.error = "Bad Request";
                    } else if (status == 500) {
                        $scope.error = "Internal Server Error";
                    } else {
                        $scope.error = "Failed Request with response status " + status;
                    }
                });
        };

        $scope.isMissing = function (reference, missingReferences) {
            var index;
            for (index = 0; missingReferences != null && index < missingReferences.length; index++) {
                if (missingReferences[index].namespace == reference.namespace
                    && missingReferences[index].name == reference.name
                    && missingReferences[index].version == reference.version) {
                    return true;
                }
            }
            return false;
        };

        $scope.checkin = function () {
            $scope.isLoading = true;
            $scope.showCheckin = false;
            $scope.hasWarning = false;
            $scope.fileAdded = true;
            $scope.fileImported = false;
            $scope.beingCheckedIn = true;
            $scope.loadMessage = "Importing models... Please wait!";
            $rootScope.error = "";
            checkinSingle($scope.uploadResult.handleId);
        };

        checkinSingle = function (handleId) {
        	var importUrl = "./api/v1/importers/" + handleId + "?key=" + $scope.selectedImporter.key + "&targetNamespace="+$scope.targetNamespace.name;

            $http.put(importUrl)
                .success(function (result) {
                    $scope.showResultBox = true;
                    $scope.beingCheckedIn = false;
                    $scope.resultMessage = "Import was successful!";
                    $scope.showCheckin = false;
                    $scope.fileImported = true;
                    $scope.fileAdded = true;
                    $scope.isLoading = false;
                    $rootScope.modelsSaved = null;
                    $scope.importedModels = result;
                }).error(function (data, status, headers, config) {
                    if (status == 403) {
                        $scope.error = "Operation is Forbidden";
                    } else if (status == 401) {
                        $scope.error = "Unauthorized Operation";
                    } else if (status == 400) {
                        $scope.error = "Bad Request. Server Down";
                    } else if (status == 500) {
                        $scope.error = "Internal Server Error";
                    } else {
                        $scope.error = "Failed Request with response status " + status;
                    }
                    $scope.isLoading = false;
                    $scope.beingCheckedIn = false;
                });;
        };

        $scope.getImporters = function () {
            $http.get('./api/v1/importers')
                .success(function (result) {
                    $scope.importers = result;
                });
        };

        $scope.getImporters();


        $scope.getSelectedImporterInfo = function (selectedImporter) {
            if ($scope.importers === undefined) {
                return;
            }
            for (var i = 0; i < $scope.importers.length; i++) {
                if ($scope.importers[i].key == selectedImporter) return $scope.importers[i];
            }
        };

    }]);
    
 });
