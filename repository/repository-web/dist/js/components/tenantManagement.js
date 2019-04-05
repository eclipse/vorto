
repositoryControllers.controller("tenantManagementController", 
    [ "$rootScope", "$scope", "$http", "$uibModal", 
    function($rootScope, $scope, $http, $uibModal) {
        $scope.tenants = [];
        $scope.isRetrievingTenants = false;
        
        $scope.getTenants = function() {
            $scope.isRetrievingTenants = true;
            $http.get("./rest/tenants")
                .then(function(result) {
                    $scope.isRetrievingTenants = false;
                    console.log(JSON.stringify(result));
                    $scope.tenants = result.data;
                }, function(reason) {
                    $scope.isRetrievingTenants = false;
                    // TODO : handling of failures
                });
        }
        
        $scope.getTenants();
        
        $scope.newTenant = function() {
            return {
                edit: false,
                tenantId: "",
                admins: [],
                authenticationProvider: "GITHUB",
                authorizationProvider: "DB",
                defaultNamespace: "",
                namespaces: []
            };
        };
        
        $scope.editableTenant = function(tenant) {
            tenant.edit = true;
            return tenant;
        };
        
        $scope.createOrUpdateTenant = function(tenant) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "webjars/repository-web/dist/partials/admin/createOrUpdateTenant.html",
                size: "lg",
                controller: "createOrUpdateTenantController",
                resolve: {
                    tenant: function () {
                        return tenant;
                    }
                }
            });
            
            modalInstance.result.finally(function(result) {
                $scope.getTenants();
                $rootScope.init();
            });
        };
        
        $scope.deleteTenant = function(tenant) {
            $http.delete("./rest/tenants/" + tenant.tenantId)
                .then(function(result) {
                    console.log("SUCCESS:" + JSON.stringify(result));
                    $scope.getTenants();
                }, function(reason) {
                    console.log("ERROR:" + JSON.stringify(reason));
                    $scope.getTenants();
                });
        };
    }
]);

repositoryControllers.directive("tenantManagement", function() {
    return {
        templateUrl: "webjars/repository-web/dist/partials/admin/tenantManagement.html"
    };
});

repositoryControllers.controller("createOrUpdateTenantController", 
    ["$rootScope", "$scope", "$uibModal", "$uibModalInstance", "$http", "tenant",
    function($rootScope, $scope, $uibModal, $uibModalInstance, $http, tenant) {
        
        $scope.tenant = tenant;
        $scope.mode = tenant.edit ? "Update" : "Create";
        $scope.originalNamespaces = tenant.namespaces.slice();
        
        $scope.isCreatingOrUpdating = false;
        
        $scope.cancel = function() {
            $uibModalInstance.dismiss("Canceled.");
        };
        
        $scope.createOrUpdateTenant = function() {
            $scope.isCreatingOrUpdating = true;
            $http.put("./rest/tenants/" + $scope.tenant.tenantId, {
                "tenantAdmins" : $scope.tenant.admins,
                "authenticationProvider" : $scope.tenant.authenticationProvider,
                "authorizationProvider" : $scope.tenant.authorizationProvider,
                "defaultNamespace" : $scope.tenant.defaultNamespace,
                "namespaces" : $scope.tenant.namespaces
            }).then(function(result) {
                console.log("SUCCESS:" + JSON.stringify(result));
                $scope.isCreatingOrUpdating = false;
                $uibModalInstance.close($scope.tenant);
            }, function(reason) {
                console.log("ERROR:" + JSON.stringify(reason));
                $scope.isCreatingOrUpdating = false;
            });
        };
        
        $scope.isInvalid = function() {
            return $scope.tenant.tenantId === '' || 
                $scope.tenant.admins.length <= 0 || 
                $scope.tenant.namespaces.length <= 0 ||
                $scope.tenant.defaultNamespace === '';
        };
        
        $scope.setDefault = function(namespace) {
            $scope.tenant.defaultNamespace = namespace;
        };
        
        $scope.removeAdmin = function(admin) {
            $scope.removeFromArray($scope.tenant.admins, admin);
        };
        
        $scope.readonlyNamespace = function(namespace) {
            return $scope.tenant.edit && $scope.originalNamespaces.includes(namespace);
        };
        
        $scope.removeNamespace = function(namespace) {
            $scope.removeFromArray($scope.tenant.namespaces, namespace);
            if (namespace == $scope.tenant.defaultNamespace) {
                $scope.tenant.defaultNamespace = "";
            }
        };
        
        $scope.removeFromArray = function(theArray, theValue) {
            for(var i = 0; i < theArray.length; i++){ 
                if (theArray[i] === theValue) {
                    theArray.splice(i, 1); 
                }
            }
        };
        
        $scope.addAdmin = function() {
            $scope.addItem({
                title: "Add Admin",
                label: "User ID",
                validate: function(value, resultFn) {
                    if ($scope.tenant.admins.includes(value)) {
                        resultFn({
                            valid: false,
                            errorMessage: "You already have this user."
                        });
                        return;
                    }
                    
                    $http.get("./rest/accounts/" + value)
                        .then(function(result) {
                            resultFn({ valid: true });
                        }, function(reason) {
                            console.log("ERROR:" + JSON.stringify(reason));
                            if (reason.status == 404) {
                                resultFn({
                                    valid: false,
                                    errorMessage: "User doesn't exist."
                                });
                            } else {
                                resultFn({
                                    valid: false,
                                    errorMessage: "Error while accessing the server."
                                });
                            }
                        });
                },
                successFn: function(value) {
                    $scope.tenant.admins.push(value);
                }
            });
        };
        
        $scope.addNamespace = function() {
            $scope.addItem({
                title: "Add Namespace",
                label: "Namespace",
                info: "this namespace will be appended with '" + $rootScope.privateNamespacePrefix + "'. An official namespace (without the '" + $rootScope.privateNamespacePrefix + "' prefix) can be requested after the tenant is created.",
                validate: function(value, resultFn) {
                    if ($scope.tenant.namespaces.includes($rootScope.privateNamespacePrefix + value)) {
                        resultFn({
                            valid: false,
                            errorMessage: "You already have this namespace."
                        });
                        return;
                    }
                    
                    $http.get("./rest/namespaces/" + $rootScope.privateNamespacePrefix + value + "/valid")
                        .then(function(result) {
                            if (result.data) {
                                resultFn({ valid: true });
                            } else {
                                resultFn({
                                    valid: false,
                                    errorMessage: "This namespace has been taken up already."
                                });
                            }
                        }, function(reason) {
                            console.log("ERROR : " + JSON.stringify(reason))
                            resultFn({
                                valid: false,
                                errorMessage: "Error while accessing the server."
                            });
                        });
                },
                successFn: function(value) {
                    $scope.tenant.namespaces.push($rootScope.privateNamespacePrefix + value);
                }
            });
        };
        
        $scope.addItem = function(dialogSettings) {
            var tenant = $scope.tenant;
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "addItem.html",
                size: "sm",
                controller: function($scope) {
                    
                    $scope.dialog = dialogSettings;
                    $scope.value = "";
                    $scope.errorMessage = "";
                    $scope.isAdding = false;
                    
                    $scope.cancel = function() {
                        modalInstance.dismiss("Canceled.");  
                    };
                    
                    $scope.add = function() {
                        $scope.isAdding = true;
                        dialogSettings.validate($scope.value, function(validationResult) {
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