define(["../../init/appController"], function (repositoryControllers) {

  repositoryControllers.controller("namespaceManagementController",
      ["$rootScope", "$scope", "$http", "$uibModal", "dialogConfirm",
        "dialogPrompt",
        function ($rootScope, $scope, $http, $uibModal, dialogConfirm,
            dialogPrompt) {
          $scope.namespaces = [];
          $scope.isRetrievingNamespaces = false;
          $scope.errorMessage = "";
          $scope.requestEmailTemplate = "Dear%20Vorto%20Team%2C%20%0A%0AI%20would%20like%20to%20request%20for%20an%20official%20namespace.%20%0A%0ANamespace%20Owner%20%28user%20ID%29%20%3A%20%0ANamespace%3A%0A%0AThank%20you.%20%0A%0ABest%20regards%2C%20";

          $scope.getNamespaces = function () {
            $scope.isRetrievingNamespaces = true;
            $http.get("./api/v1/namespaces/all")
            .then(function (result) {
              $scope.isRetrievingNamespaces = false;
              $scope.namespaces = result.data;
            }, function (reason) {
              $scope.isRetrievingNamespaces = false;
              // TODO : handling of failures
            });
          }

          $scope.getNamespaces();

          $scope.newNamespace = function () {
            return {
              edit: false,
              admins: [],
              name: ""
            };
          };

          // TODO seems unused
          $scope.editableNamespace = function (namespace) {
            namespace.edit = true;
            return namespace;
          };

          $scope.createOrUpdateTenant = function (tenant) {
            var modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "webjars/repository-web/dist/partials/admin/createOrUpdateTenant.html",
              controller: "createOrUpdateTenantController",
              resolve: {
                tenant: function () {
                  return tenant;
                },
                tenants: function () {
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

          $scope.createNamespace = function (namespace) {
            var modalInstance = $uibModal.open({
              animation: true,
              title: "Add Namespace",
              label: "Namespace",
              prefix: "vorto.private.",
              templateUrl: "webjars/repository-web/dist/partials/admin/createNamespace.html",
              controller: "createOrUpdateNamespaceController",
              resolve: {
                namespace: function () {
                  namespace.prefixText = "vorto.private.";
                  namespace.label = "Please specify a namespace";
                  namespace.title = "Create Namespace";
                  namespace.createNameSpaceId = $rootScope.displayName;
                  namespace.sysAdmin = $rootScope.hasAuthority("ROLE_SYS_ADMIN");
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

          $scope.restoreTenant = function (tenant) {
            var modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "webjars/repository-web/dist/partials/dialog/restoration_prompt_and_confirm.html",
              size: "lg",
              controller: function ($scope) {
                $scope.allowRestore = false;
                $scope.errorMessage = null;

                $scope.getNamespace = function () {
                  if (tenant != null) {
                    return "'" + tenant.namespaces[0] + "' ";
                  } else {
                    return "";
                  }
                };

                $scope.getNamespaceParameter = function () {
                  if (tenant != null) {
                    return "namespaces/" + tenant.namespaces[0];
                  } else {
                    return "tenants";
                  }
                };

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
                        './rest/' + $scope.getNamespaceParameter() + '/restore',
                        fd, {
                          transformRequest: angular.identity,
                          headers: {
                            'Content-Type': undefined
                          }
                        })
                    .success(function (result) {
                      console.log(JSON.stringify(result));
                      var updatedTenants = result;
                      if (updatedTenants.length < 1) {
                        $scope.errorMessage = "No tenants were restored. Maybe you used the wrong backup file?";
                      } else {
                        $scope.errorMessage = null;
                        modalInstance.dismiss();
                      }
                    })
                    .error(function (result) {
                      console.log(JSON.stringify(result));
                      // TODO : better error message
                      $scope.errorMessage = "Error on server.";
                    });
                  }
                };

                $scope.cancel = function () {
                  modalInstance.dismiss();
                };
              },
              resolve: {
                tenant: function () {
                  return tenant;
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
                  .delete("./api/v1/namespaces/" + namespace.name)
                  .then(function (result) {
                    modalInstance.close();
                  },
                  function (reason) {
                    if (reason.data) {
                      $scope.errorMessage = reason.data.errorMessage;
                    }
                    $scope.isCreatingOrUpdating = false;
                    modalInstance.close();
                  });
                };

                $scope.getPublicModelsForNamespace = function () {
                  $http
                  .get(
                      './api/v1/search/models?expression=namespace:'
                      + namespace.name + ' visibility:Public'
                  )
                  .success(
                      function (data, status, headers, config) {
                        $scope.hasPublicModels = data.length > 0;
                      }
                    )
                  .error(
                      function (data, status, headers, config) {
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

  repositoryControllers.controller("createOrUpdateTenantController",
      ["$rootScope", "$scope", "$uibModal", "$uibModalInstance",
        "dialogConfirm", "$http", "tenant", "tenants",
        function ($rootScope, $scope, $uibModal, $uibModalInstance,
            dialogConfirm, $http, tenant, tenants) {

          $scope.namespace = tenant;
          $scope.mode = tenant.edit ? "Update" : "Create";
          $scope.originalNamespaces = tenant.namespaces.slice();
          $scope.errorMessage = "";
          $scope.requestEmailTemplate = "Dear%20Vorto%20Team%2C%20%0A%0AI%20would%20like%20to%20request%20for%20an%20official%20namespace.%20%0A%0ANamespace%20Owner%20%28user%20ID%29%20%3A%20%0ANamespace%3A%0A%0AThank%20you.%20%0A%0ABest%20regards%2C%20";

          $scope.isCreatingOrUpdating = false;

          $scope.cancel = function () {
            $uibModalInstance.dismiss("Canceled.");
          };

          $scope.createOrUpdateTenant = function () {
            $scope.isCreatingOrUpdating = true;
            var defaultValue = 'vorto.private.';
            if ($rootScope.hasAuthority("ROLE_SYS_ADMIN")) {
              defaultValue = "";
            }

            var indexOfNewNamespace = 0;
            if ($scope.mode == "Create") {
              $scope.namespaceToRegister = defaultValue
                  + $scope.namespace.createNameSpaceId;

              indexOfNewNamespace = $scope.namespace.namespaces.push(
                  $scope.namespaceToRegister) - 1;
              if ($scope.namespace.namespaces.length == 1) {
                $scope.namespace.defaultNamespace = $scope.namespace.namespaces[0];
              }
              $scope.namespace.admins.push($rootScope.user);
            }
            $http
            .put("./api/v1/namespaces/" + $scope.namespaceToRegister, {})
            .then(function (result) {
                  $scope.isCreatingOrUpdating = false;
                  $uibModalInstance.close($scope.namespace);
                },
                function (reason) {
                  $scope.errorMessage = reason.data.errorMessage;
                  $scope.isCreatingOrUpdating = false;
                  $scope.namespace.namespaces.splice(indexOfNewNamespace, 1);
                });
          };

          $scope.isInvalid = function () {
            return
            $scope.namespace.admins.length <= 0 ||
            $scope.namespace.namespaces.length <= 0 ||
            $scope.namespace.defaultNamespace === '';
          };

          $scope.setDefault = function (namespace) {
            $scope.namespace.defaultNamespace = namespace;
          };

          $scope.removeAdmin = function (admin) {
            $scope.removeFromArray($scope.namespace.admins, admin);
          };

          $scope.readonlyNamespace = function (namespace) {
            return $scope.namespace.edit && $scope.originalNamespaces.includes(
                namespace);
          };

          $scope.removeNamespace = function (namespace) {
            var dialog = dialogConfirm($scope,
                "Are you sure you want to remove this namespace " + "?",
                ["Yes, Delete", "Cancel"]);

            dialog.setCallback("Yes, Delete", function () {
              $scope.removeFromArray($scope.namespace.namespaces, namespace);
              if (namespace == $scope.namespace.defaultNamespace) {
                $scope.namespace.defaultNamespace = "";
              }
            });
            dialog.run();
          }

          $scope.removeFromArray = function (theArray, theValue) {
            for (var i = 0; i < theArray.length; i++) {
              if (theArray[i] === theValue) {
                theArray.splice(i, 1);
              }
            }
          };

          $scope.addAdmin = function () {
            $scope.addItem({
              title: "Add Admin",
              label: "User ID",
              validate: function (value, resultFn) {
                if ($scope.namespace.admins.includes(value)) {
                  resultFn({
                    valid: false,
                    errorMessage: "Collaborator already exists."
                  });
                  return;
                }

                $http.get("./rest/accounts/" + value)
                .then(function (result) {
                  resultFn({valid: true});
                }, function (reason) {
                  if (reason.status == 404) {
                    resultFn({
                      valid: false,
                      errorMessage: "User does not have a Vorto Repository account."
                    });
                  } else {
                    resultFn({
                      valid: false,
                      errorMessage: "Error while accessing the server."
                    });
                  }
                });
              },
              successFn: function (value) {
                $scope.namespace.admins.push(value);
              }
            });
          };

          $scope.addNamespace = function () {
            var prefix = $rootScope.privateNamespacePrefix;
            if ($rootScope.hasAuthority("ROLE_SYS_ADMIN")) {
              var prefix = "";
            }
            $scope.addItem({
              title: "Add Namespace",
              label: "Namespace",
              prefix: prefix,
              validate: function (value, resultFn) {
                //if ($scope.namespace.namespaces.includes($rootScope.privateNamespacePrefix + value)) {
                if ($scope.namespace.namespaces.includes(prefix + value)) {
                  resultFn({
                    valid: false,
                    errorMessage: "You already have this namespace."
                  });
                  return;
                }

                //$http.get("./rest/namespaces/" + $rootScope.privateNamespacePrefix + value + "/valid")
                $http.get("./rest/namespaces/" + prefix + value + "/valid")
                .then(function (result) {
                  if (result.data) {
                    resultFn({valid: true});
                  } else {
                    resultFn({
                      valid: false,
                      errorMessage: "This namespace has been taken up already."
                    });
                  }
                }, function (reason) {
                  resultFn({
                    valid: false,
                    errorMessage: "Error while accessing the server."
                  });
                });
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
            var tenant = $scope.namespace;
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
