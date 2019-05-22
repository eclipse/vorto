repositoryControllers.controller("tenantManagementController", 
    [ "$rootScope", "$scope", "$http", "$uibModal", "dialogConfirm", "dialogPrompt",
    function($rootScope, $scope, $http, $uibModal, dialogConfirm, dialogPrompt) {
        $scope.tenants = [];
        $scope.isRetrievingTenants = false;
        $scope.errorMessage = "";
        
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
                controller: "createOrUpdateTenantController",
                resolve: {
                    tenant: function () {
                        return tenant;
                    },
                    tenants: function() {
                    	return $scope.tenants;
                    }
                }
            });
            
            modalInstance.result.finally(function(result) {
                $scope.getTenants();
                $rootScope.init();
            });
        };
	$scope.createTenant = function(tenant) {
            var modalInstance = $uibModal.open({
                animation: true,
                title: "Add Namespace",
                label: "Namespace",
                prefix: "vorto.private.",
                templateUrl: "webjars/repository-web/dist/partials/admin/createTenant.html",
                controller: "createOrUpdateTenantController",
                resolve: {
                    tenant: function () {
                    	tenant.prefixText = "vorto.private.";
                    	tenant.label = "Please specify a namespace for your repository";
                    	tenant.title = "Create Repository";
                    	tenant.createNameSpaceId = $rootScope.displayName;
                        return tenant;
                    },
                    tenants: function() {
                    	return $scope.tenants;
                    }
                }
            });
            
            modalInstance.result.finally(function(result) {
                $scope.getTenants();
                $rootScope.init();
            });
        };
        
        $scope.manageUsers = function(tenant) {
        	var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "webjars/repository-web/dist/partials/admin/tenantUserManagement.html",
                size: "lg",
                controller: "tenantUserManagementController",
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
        }
        
        $scope.requestOfficialNamespace = function(tenant) {
        	var dialog = dialogPrompt($scope, "Official Namespace Request", "Namespace (e.g com.abc.xyz)", ["Confirm", "Cancel"]);
        	
        	dialog.setCallback("Confirm", function(promptReply) {
        		$http.post("./rest/tenants/" + tenant.tenantId + "/namespaces/" + promptReply + "/requestOfficial")
	                .then(function(result) {
	                	var emailSent = result.data;
	                	if (!emailSent) {
	                		$scope.errorMessage = "Email was not sent. Please try contacting the administrators of Vorto manually.";
	                	}
	                    console.log("SUCCESS:" + JSON.stringify(result));
	                }, function(reason) {
	                    console.log("ERROR:" + JSON.stringify(reason));
	                });
        	});
        	
        	dialog.run();
        }
        
        $scope.deleteTenant = function(tenant) {
        	var dialog = dialogConfirm($scope, "Are you sure you want to remove this repository " + "?", ["Yes, Delete", "Cancel"]);
        	
        	dialog.setCallback("Yes, Delete", function() {
        		$http.delete("./rest/tenants/" + tenant.tenantId)
	                .then(function(result) {
	                    console.log("SUCCESS:" + JSON.stringify(result));
	                    $scope.getTenants();
	                }, function(reason) {
	                    console.log("ERROR:" + JSON.stringify(reason));
	                    $scope.getTenants();
	                });
        	});
        	
        	dialog.run();
        };
    }
]);

repositoryControllers.directive("tenantManagement", function() {
    return {
        templateUrl: "webjars/repository-web/dist/partials/admin/tenantManagement.html"
    };
});

repositoryControllers.controller("createOrUpdateTenantController", 
    ["$rootScope", "$scope", "$uibModal", "$uibModalInstance","dialogConfirm", "$http", "tenant", "tenants",
    function($rootScope, $scope, $uibModal, $uibModalInstance, dialogConfirm, $http, tenant, tenants) {
	
	
        
        $scope.tenant = tenant;
        $scope.mode = tenant.edit ? "Update" : "Create";
        $scope.originalNamespaces = tenant.namespaces.slice();
        $scope.errorMessage = "";
		
        $scope.isCreatingOrUpdating = false;
        
        $scope.cancel = function() {
            $uibModalInstance.dismiss("Canceled.");
        };
        
        $scope.createOrUpdateTenant = function() {
        	$scope.isCreatingOrUpdating = true;
        	var defaultValue = 'vorto.private.';
			if($scope.mode == "Create"){
				$scope.tenant.tenantId = Math.random();
				$scope.tenant.createNameSpaceId = defaultValue + $scope.tenant.createNameSpaceId;
				$scope.tenant.namespaces.push($scope.tenant.createNameSpaceId);
                    if ($scope.tenant.namespaces.length == 1) {
                    	$scope.tenant.defaultNamespace = $scope.tenant.namespaces[0]; 
                    }
				$scope.tenant.admins.push($rootScope.displayName);
			}
			$scope.tenant.authenticationProvider="GITHUB";
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
                $scope.errorMessage = reason.data.errorMessage;
                $scope.isCreatingOrUpdating = false;
            });
        };
        
        $scope.isInvalid = function() {
        	return 
				//$scope.tenant.tenantId === '' || 
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
        
        /*$scope.removeNamespace = function(namespace) {
            $scope.removeFromArray($scope.tenant.namespaces, namespace);
            if (namespace == $scope.tenant.defaultNamespace) {
                $scope.tenant.defaultNamespace = "";
            }
        };*/
        
        $scope.removeNamespace = function(namespace) {
        	var dialog = dialogConfirm($scope, "Are you sure you want to remove this namespace " + "?", ["Yes, Delete", "Cancel"]);
        	
        	dialog.setCallback("Yes, Delete", function() {
        		$scope.removeFromArray($scope.tenant.namespaces, namespace);
        		if (namespace == $scope.tenant.defaultNamespace) {
                $scope.tenant.defaultNamespace = "";
        		}
        	});
        	dialog.run();
        }
        
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
                prefix: "vorto.private.",
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
                    if ($scope.tenant.namespaces.length == 1) {
                    	$scope.tenant.defaultNamespace = $scope.tenant.namespaces[0]; 
                    }
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
 

