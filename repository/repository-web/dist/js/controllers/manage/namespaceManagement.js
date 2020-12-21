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
define(["../../init/appController"], function (repositoryControllers) {

  repositoryControllers.controller("namespaceManagementController",
      ["$rootScope", "$scope", "$http", "$uibModal", "dialogConfirm",
        "dialogPrompt",
        function ($rootScope, $scope, $http, $uibModal, dialogConfirm,
            dialogPrompt) {
          $scope.namespaces = [];
          $scope.filteredNamespaces = [];
          $scope.isRetrievingNamespaces = false;
          $scope.errorMessage = "";
          $scope.requestEmailTemplate = "Dear%20Vorto%20Team%2C%20%0A%0AI%20would%20like%20to%20request%20for%20an%20official%20namespace.%20%0A%0ANamespace%20Owner%20%28user%20ID%29%20%3A%20%0ANamespace%3A%0A%0AThank%20you.%20%0A%0ABest%20regards%2C%20";
          $scope.namespaceSearchTerm = "";

          $scope.focusOnFilter = function () {
            // html auto-focus doesn't work, likely due to loading overlay div
            let element = document.getElementById("namespaceFilter");
            if (element) {
              element.focus();
            }
          }

          $scope.focusOnFilter();

          $scope.getNamespaces = function () {
            $scope.isRetrievingNamespaces = true;
            $http
            .get("./rest/namespaces/all")
            .then(
                function (result) {
                  $scope.isRetrievingNamespaces = false;
                  $scope.namespaces = result.data;
                  // disabling filter here if 0 or 1 namespaces (typical user)
                  // cannot use ng-disabled as it is executed before retrieving the namespaces
                  if (!$scope.namespaces || $scope.namespaces.length <= 1) {
                    document.getElementById("namespaceFilter").disabled = true;
                  }
                  $scope.filterNamespaces();
                },
                function (reason) {
                  $scope.isRetrievingNamespaces = false;
                  // TODO : handling of failures
                }
            );
            $scope.focusOnFilter();
          }

          $scope.getNamespaces();

          $scope.filterNamespaces = function () {
            if (!$scope.namespaceSearchTerm) {
              $scope.filteredNamespaces = $scope.namespaces;
            } else {
              $scope.filteredNamespaces = $scope.namespaces.filter(
                  namespace => namespace.name.toLowerCase().includes(
                      $scope.namespaceSearchTerm.toLowerCase())
              );
            }
          }

          $scope.newNamespace = function () {
            return {
              edit: false,
              admins: [],
              name: ""
            };
          };

          $scope.editableNamespace = function (namespace) {
            namespace.edit = true;
            return namespace;
          };

          $scope.suggestNamespaceName = function () {
            // sanitizes user ID by only leaving namespace-compatible characters
            let sanitizedUserID = $rootScope.user.replace(/[^a-zA-Z0-9_]/g, "");
            let namespaceNames = $scope.namespaces.map(n => n.name);
            // sanitized user ID not present in available namespace names
            if (!namespaceNames.includes(sanitizedUserID)) {
              return sanitizedUserID;
            }
                // namespace already exists with sanitized user ID - suggesting
            // name prepended with integer
            else {
              let index = 0;
              // hard-capping at 99 - not expecting anybody to have more than
              // 99 namespaces with their name + integer
              while (index < 100) {
                let appended = sanitizedUserID + index;
                if (!namespaceNames.includes(appended)) {
                  return appended;
                } else {
                  index++;
                }
              }
              // giving up and returning existing namespace name suggestion
              // of sanitized user ID
              return sanitizedUserID;
            }
          }

          $scope.openRequestAccessToNamespace = function () {
            var modalInstance = $uibModal.open(
                {
                  animation: true,
                  title: "Request access to a namespace",
                  label: "Request access to a namespace",
                  templateUrl: "webjars/repository-web/dist/partials/admin/requestAccessToNamespace.html",
                  controller: "requestAccessToNamespaceController",
                  size: "lg",
                  scope: $scope,
                  backdrop: 'static',
                  resolve: {
                    modal: function() {
                      return true;
                    }
                  }
                }
            );
            modalInstance.result.finally(function (result) {
              $scope.getNamespaces();
            });
          }

          $scope.createNamespace = function (namespace, namespaces) {
            var modalInstance = $uibModal.open({
              animation: true,
              title: "Add Namespace",
              label: "Namespace",
              prefix: "vorto.private.",
              templateUrl: "webjars/repository-web/dist/partials/admin/createNamespace.html",
              controller: "createNamespaceController",
              resolve: {
                namespace: function () {
                  namespace.prefixText = "vorto.private.";
                  namespace.label = "Please specify a namespace";
                  namespace.title = "Create Namespace";
                  namespace.createNameSpaceId = $rootScope.displayName;
                  namespace.sysAdmin = $rootScope.hasAuthority("sysadmin");
                  // suggests namespace name based on user ID, sanitized
                  namespace.name = $scope.suggestNamespaceName();
                  return namespace;
                },
                namespaces: function () {
                  return $scope.namespaces;
                }
              },
              backdrop: 'static'
            });

            modalInstance.result.finally(function (result) {
              $scope.getNamespaces();
              $rootScope.init();
            });
          };

          $scope.restoreNamespace = function (namespace) {
            var modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "webjars/repository-web/dist/partials/dialog/restoration_prompt_and_confirm.html",
              size: "lg",
              controller: function ($scope) {
                $scope.allowRestore = false;
                $scope.errorMessage = null;
                $scope.namespaceName = namespace.name;

                $scope.fileNameChanged = function (element) {
                  $scope.$apply(function ($scope) {
                    if (element != null && element.files != null
                        && element.files.length > 0) {
                      $scope.allowRestore = true;
                    } else {
                      $scope.allowRestore = false;
                    }
                  });
                };

                $scope.restore = function () {
                  var element = document.getElementById('backupFile');
                  if (element != null && element.files != null
                      && element.files.length > 0) {
                    var fd = new FormData();
                    fd.append('file', element.files[0]);
                    $http.post(
                        './rest/namespaces/' + $scope.namespaceName
                        + '/restore',
                        fd, {
                          transformRequest: angular.identity,
                          headers: {
                            'Content-Type': undefined
                          }
                        })
                    .then(
                        function (result) {
                          var updatedNamespaces = result;
                          if (updatedNamespaces && updatedNamespaces.length
                              && updatedNamespaces.length < 1) {
                            $scope.errorMessage = "No namespaces were restored. Maybe you used the wrong backup file?";
                          } else {
                            $scope.errorMessage = null;
                            modalInstance.dismiss();
                          }
                        },
                        function (error) {
                          // TODO : better error message
                          $scope.errorMessage = "Unkown server error.";
                        }
                    );
                  }
                };

                $scope.cancel = function () {
                  modalInstance.dismiss();
                };
              },
              resolve: {
                namespace: function () {
                  return $scope.namespace;
                }
              },
              backdrop: 'static'
            });
          };

          $scope.manageUsers = function (namespace) {
            var modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "webjars/repository-web/dist/partials/admin/namespaceUserManagement.html",
              size: "lg",
              controller: "namespaceUserManagementController",
              resolve: {
                namespace: function () {
                  return namespace;
                },
                modal: function() {
                  return true;
                }
              },
              backdrop: 'static'
            });

            modalInstance.result.finally(function (result) {
              $scope.getNamespaces();
              $rootScope.init();
            });
          }

          $scope.openDeleteDialog = function (namespace) {
            var modalInstance = $uibModal.open({
              animation: true,
              controller: function ($scope) {
                $scope.hasPublicModels = false;

                $scope.delete = function () {
                  $http
                  .delete("./rest/namespaces/" + namespace.name)
                  .then(
                      function (result) {
                        modalInstance.close();
                      },
                      function (reason) {
                        if (reason.data) {
                          $scope.errorMessage = reason.data.errorMessage;
                        }
                        $scope.isCreatingOrUpdating = false;
                        modalInstance.close();
                      }
                  );
                };

                $scope.getPublicModelsForNamespace = function () {
                  $http
                  .get(
                      './api/v1/search/models?expression=namespace:'
                      + namespace.name + ' visibility:Public'
                  )
                  .then(
                      function (result) {
                        $scope.hasPublicModels = result.data.length > 0;
                      },
                      function (error) {
                        console.log("Problem getting data from repository");
                      }
                  );
                };

                $scope.getPublicModelsForNamespace();

                $scope.cancel = function () {
                  modalInstance.dismiss();
                };
              },
              templateUrl: "deleteNamespace.html",
              size: "lg",
              resolve: {
                namespace: function () {
                  return namespace;
                }
              },
              backdrop: 'static'
            });

            modalInstance.result.finally(function (result) {
              $scope.getNamespaces();
            });
          };
        }
      ]);

  repositoryControllers.directive("namespaceManagement", function () {
    return {
      templateUrl: "webjars/repository-web/dist/partials/admin/namespaceManagement.html"
    };
  });

  repositoryControllers.controller("createNamespaceController",
      ["$rootScope", "$scope", "$uibModal", "$uibModalInstance",
        "dialogConfirm", "$http", "namespace", "namespaces",
        function ($rootScope, $scope, $uibModal, $uibModalInstance,
            dialogConfirm, $http, namespace, namespaces) {

          $scope.namespace = namespace;
          $scope.namespaces = namespaces;
          $scope.mode = namespace.edit ? "Update" : "Create";
          $scope.errorMessage = null;
          $scope.requestEmailTemplate = "Dear%20Vorto%20Team%2C%20%0A%0AI%20would%20like%20to%20request%20for%20an%20official%20namespace.%20%0A%0ANamespace%20Owner%20%28user%20ID%29%20%3A%20%0ANamespace%3A%0A%0AThank%20you.%20%0A%0ABest%20regards%2C%20";

          $scope.isCreatingOrUpdating = false;

          $scope.cancel = function () {
            $uibModalInstance.dismiss("Canceled.");
          };

          /*
          There seems to have been an intent to allow users to manipulate
          namespaces other than creating, deleting or adding users - i.e.
          basically renaming them, but it was never implemented (and for good
          reason: it would require modifying all the model inside the given namespace).
          Therefore, this PUT operation only sends the new namespace's name as
          a path variable, with an empty body.
          The back-end will figure out which user to set as admin, i.e. the
          logged on user.
          The back-end will also refuse to create an additional namespace for
          non-sysadmin users who already have one private namespace.
           */
          $scope.createOrUpdateNamespace = function () {

            $scope.isCreatingOrUpdating = true;

            var indexOfNewNamespace = 0;
            if ($scope.mode == "Create") {
              // Defines whether namespace name will be preceded by vorto.private
              // based on user privileges. This will also be checked in the
              // back-end.
              var defaultValue = 'vorto.private.';
              if ($rootScope.hasAuthority("sysadmin")) {
                defaultValue = "";
              }
              $scope.namespace.name = defaultValue + $scope.namespace.name;
            }
            $http
            .put("./rest/namespaces/" + $scope.namespace.name, {})
            .then(
                function (result) {
                  // add user as admin
                  $scope.namespace.admins.push($rootScope.user);
                  // adds to known namespaces list
                  indexOfNewNamespace = $scope.namespaces.push($scope.namespace)
                      - 1;
                  $scope.isCreatingOrUpdating = false;
                  $uibModalInstance.close($scope.namespace);
                },
                function (reason) {
                  $scope.errorMessage = reason.data.errorMessage;
                  $scope.isCreatingOrUpdating = false;
                  $scope.namespaces.splice(indexOfNewNamespace, 1);
                }
            );
          };

          $scope.isInvalid = function () {
            return $scope.namespace.admins.length <= 0;
          };

          $scope.removeAdmin = function (admin) {
            $scope.removeFromArray($scope.namespace.admins, admin);
          };

          $scope.readonlyNamespace = function (namespace) {
            return $scope.namespace.edit;
          };

          $scope.removeNamespace = function (namespace) {
            var dialog = dialogConfirm($scope,
                "Are you sure you want to remove this namespace " + "?",
                ["Yes, Delete", "Cancel"]);

            dialog.setCallback("Yes, Delete", function () {
              $scope.namespaces = $scope.namespaces.filter((e) => {
                return e != namespace
              });
            });
            dialog.run();
          }

          $scope.addNamespace = function () {
            var prefix = $rootScope.privateNamespacePrefix;
            if ($rootScope.hasAuthority("sysadmin")) {
              var prefix = "";
            }
            $scope.addItem({
              title: "Add Namespace",
              label: "Namespace",
              prefix: prefix,
              validate: function (value, resultFn) {
                if ($scope.namespace.namespaces.includes(prefix + value)) {
                  resultFn({
                    valid: false,
                    errorMessage: "You already have this namespace."
                  });
                  return;
                }

                $http.get("./rest/namespaces/" + prefix + value + "/valid")
                .then(
                    function (result) {
                      if (result.data) {
                        resultFn({valid: true});
                      } else {
                        resultFn({
                          valid: false,
                          errorMessage: "This namespace has been taken up already."
                        });
                      }
                    },
                    function (reason) {
                      resultFn({
                        valid: false,
                        errorMessage: "Error while accessing the server."
                      });
                    }
                );
              },
              successFn: function (value) {
                $scope.namespace.namespaces.push(prefix + value);
                if ($scope.namespace.namespaces.length == 1) {
                  $scope.namespace.defaultNamespace = $scope.namespace.namespaces[0];
                }
              }
            });
          };

          $scope.addItem = function (dialogSettings) {
            var namespace = $scope.namespace;
            var modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "addItem.html",
              size: "sm",
              controller: function ($scope) {

                $scope.dialog = dialogSettings;
                $scope.value = "";
                $scope.errorMessage = "";
                $scope.isAdding = false;

                $scope.cancel = function () {
                  modalInstance.dismiss("Canceled.");
                };

                $scope.add = function () {
                  $scope.isAdding = true;
                  dialogSettings.validate($scope.value,
                      function (validationResult) {
                        $scope.isAdding = false;
                        if (!validationResult.valid) {
                          $scope.errorMessage = validationResult.errorMessage;
                        } else {
                          modalInstance.close($scope.value);
                        }
                      });
                };
              }
            });

            modalInstance.result.then(dialogSettings.successFn);
          };
        }
      ]);

});
