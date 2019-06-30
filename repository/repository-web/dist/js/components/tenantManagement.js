repositoryControllers.controller("tenantManagementController", 
    [ "$rootScope", "$scope", "$http", "$uibModal", "dialogConfirm", "dialogPrompt",
    function($rootScope, $scope, $http, $uibModal, dialogConfirm, dialogPrompt) {
        $scope.tenants = [];
        $scope.isRetrievingTenants = false;
        $scope.errorMessage = "";
        $scope.requestEmailTemplate = "Dear%20Vorto%20Team%2C%20%0A%0AI%20would%20like%20to%20request%20for%20an%20official%20namespace.%20%0A%0ANamespace%20Owner%20%28user%20ID%29%20%3A%20%0ANamespace%3A%0A%0AThank%20you.%20%0A%0ABest%20regards%2C%20";
        
        $scope.getTenants = function() {
            $scope.isRetrievingTenants = true;
            $http.get("./rest/tenants")
                .then(function(result) {
                    $scope.isRetrievingTenants = false;
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
                    	tenant.label = "Please specify a namespace";
                    	tenant.title = "Create Namespace";
                    	tenant.createNameSpaceId = $rootScope.displayName;
                    	tenant.sysAdmin = $rootScope.hasAuthority("ROLE_SYS_ADMIN");
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
        
        $scope.openDeleteDialog = function (tenant) {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: function ($scope) {
					$scope.hasPublicModels = false;
					
					$scope.delete = function() {
						$http.delete("./rest/tenants/" + tenant.tenantId)
	                	.then(function(result) {
	                    	modalInstance.close();
	                	}, function(reason) {
	                    	modalInstance.close();
	                	});
					};
					
					$scope.getPublicModelsForTenant = function() {
						$http.get("./rest/search/public?tenantId="+tenant.tenantId).success(
                            function(data, status, headers, config) {
                            	console.log(data);
                                $scope.hasPublicModels = data.length > 0;
                            }).error(function(data, status, headers, config) {
                                console.log("Problem getting data from repository");
                            });
					};
					
					$scope.getPublicModelsForTenant();

					$scope.cancel = function () {
						modalInstance.dismiss();
					};
				},
				templateUrl: "deleteNamespace.html",
				size: "lg",
				resolve: {
					tenant: function () {
                        return tenant;
                    }
				}
			});

			modalInstance.result.finally(function(result) {
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
    ["$rootScope", "$scope", "$uibModal", "$uibModalInstance","dialogConfirm", "$http", "tenant", "tenants",
    function($rootScope, $scope, $uibModal, $uibModalInstance, dialogConfirm, $http, tenant, tenants) {
	
	
        
        $scope.tenant = tenant;
        $scope.mode = tenant.edit ? "Update" : "Create";
        $scope.originalNamespaces = tenant.namespaces.slice();
        $scope.errorMessage = "";
        $scope.requestEmailTemplate = "Dear%20Vorto%20Team%2C%20%0A%0AI%20would%20like%20to%20request%20for%20an%20official%20namespace.%20%0A%0ANamespace%20Owner%20%28user%20ID%29%20%3A%20%0ANamespace%3A%0A%0AThank%20you.%20%0A%0ABest%20regards%2C%20";
        
		
        $scope.isCreatingOrUpdating = false;
        
        $scope.cancel = function() {
            $uibModalInstance.dismiss("Canceled.");
        };
        
        $scope.createOrUpdateTenant = function() {
        	$scope.isCreatingOrUpdating = true;
        	var defaultValue = 'vorto.private.';
        	if ($rootScope.hasAuthority("ROLE_SYS_ADMIN")) {
            	defaultValue = "";
        	}
        	
        	var indexOfNewNamespace = 0;
			if($scope.mode == "Create"){
				$scope.tenant.tenantId = $scope.generateTenantId();
				$scope.namespaceToRegister = defaultValue + $scope.tenant.createNameSpaceId;
				
				indexOfNewNamespace = $scope.tenant.namespaces.push($scope.namespaceToRegister) - 1;
                if ($scope.tenant.namespaces.length == 1) {
                	$scope.tenant.defaultNamespace = $scope.tenant.namespaces[0]; 
                }
				$scope.tenant.admins.push($rootScope.user);
			}
			$scope.tenant.authenticationProvider=$rootScope.context.loginType;
            $http.put("./rest/tenants/" + $scope.tenant.tenantId, {
                "tenantAdmins" : $scope.tenant.admins,
                "authenticationProvider" : $scope.tenant.authenticationProvider,
                "authorizationProvider" : $scope.tenant.authorizationProvider,
                "defaultNamespace" : $scope.tenant.defaultNamespace,
                "namespaces" : $scope.tenant.namespaces
            }).then(function(result) {
                $scope.isCreatingOrUpdating = false;
                $uibModalInstance.close($scope.tenant);
            }, function(reason) {
                $scope.errorMessage = reason.data.errorMessage;
                $scope.isCreatingOrUpdating = false;
                $scope.tenant.namespaces.splice(indexOfNewNamespace,1);
            });
            
            
        };
        
        $scope.generateTenantId = function() {
   			var result           = '';
   			var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
			var charactersLength = characters.length;
			for ( var i = 0; i < 20; i++ ) {
				result += characters.charAt(Math.floor(Math.random() * charactersLength));
			}
   			return result;
		}
        
        $scope.isInvalid = function() {
        	return 
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
                            errorMessage: "Collaborator already exists."
                        });
                        return;
                    }
                    
                    $http.get("./rest/accounts/" + value)
                        .then(function(result) {
                            resultFn({ valid: true });
                        }, function(reason) {
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
                successFn: function(value) {
                    $scope.tenant.admins.push(value);
                }
            });
        };
        
        $scope.addNamespace = function() {
        	var prefix = $rootScope.privateNamespacePrefix;
        	if ($rootScope.hasAuthority("ROLE_SYS_ADMIN")) {
            	var prefix = "";
        	}
            $scope.addItem({
                title: "Add Namespace",
                label: "Namespace",
                prefix: prefix,
                validate: function(value, resultFn) {
                    //if ($scope.tenant.namespaces.includes($rootScope.privateNamespacePrefix + value)) {
                	if ($scope.tenant.namespaces.includes(prefix + value)) {
                        resultFn({
                            valid: false,
                            errorMessage: "You already have this namespace."
                        });
                        return;
                    }
                    
                    //$http.get("./rest/namespaces/" + $rootScope.privateNamespacePrefix + value + "/valid")
                	$http.get("./rest/namespaces/" + prefix + value + "/valid")
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
                            resultFn({
                                valid: false,
                                errorMessage: "Error while accessing the server."
                            });
                        });
                },
                successFn: function(value) {
                	$scope.tenant.namespaces.push(prefix + value);
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
 

