define(["../../init/appController"],function(repositoryControllers) {

repositoryControllers.controller("tenantUserManagementController", 
    [ "$rootScope", "$scope", "$http", "$uibModal", "$uibModalInstance", "tenant", "dialogConfirm", 
    function($rootScope, $scope, $http, $uibModal, $uibModalInstance, tenant, dialogConfirm) {
        
    	  $scope.tenant = tenant;
        $scope.isRetrievingTenantUsers = false;
        $scope.userTenantUsers = [];

        $scope.cancel = function() {
            $uibModalInstance.dismiss("Canceled.");
        };

        $scope.$on("USER_CONTEXT_UPDATED", function(evt, data) {
           // $scope.getTenants();
        });

        $scope.getTenantUsers = function(tenantId) {
            $scope.isRetrievingTenantUsers = true;

            $http.get("./rest/tenants/" + tenantId + "/users")
                .then(function(result) {
                    $scope.isRetrievingTenantUsers = false;
                    $scope.userTenantUsers = result.data;
                }, function(reason) {
                    $scope.isRetrievingTenantUsers = false;
                    // TODO : handling of failures
                });
        };

        $scope.getTenantUsers($scope.tenant.tenantId);

        $scope.newUser = function() {
            return {
                edit: false,
                username : "",
                roleModelCreator : false,
                roleModelPromoter : false,
                roleModelReviewer : false,
                roleModelPublisher : false,
                roleUser : true,
                roleAdmin : false
            };
        };

        $scope.editableUser = function(user) {
            return {
                edit: true,
                username : user.username,
                roleModelCreator : user.roles.includes("ROLE_MODEL_CREATOR"),
                roleModelPromoter : user.roles.includes("ROLE_MODEL_PROMOTER"),
                roleModelReviewer : user.roles.includes("ROLE_MODEL_REVIEWER"),
                roleModelPublisher : user.roles.includes("ROLE_MODEL_PUBLISHER"),
                roleUser : user.roles.includes("ROLE_USER"),
                roleAdmin : user.roles.includes("ROLE_TENANT_ADMIN")
            };
        };

        $scope.createOrUpdateUser = function(user, tenant) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "webjars/repository-web/dist/partials/admin/createOrUpdateUser.html",
                size: "md",
                controller: "createOrUpdateUserController",
                resolve: {
                    user: function () {
                        return user;
                    },
                    tenant: function() {
                        return $scope.tenant;
                    }
                }
            });

            modalInstance.result.finally(function(result) {
                $scope.getTenantUsers($scope.tenant.tenantId);
                $rootScope.init();
            });
        };
        
        $scope.deleteUser = function(user) {
        	var dialog = dialogConfirm($scope, "Are you sure you want to remove user '" + user.username + "'?", ["Confirm", "Cancel"]);
        	
        	dialog.setCallback("Confirm", function() {
	            $http.delete("./rest/tenants/" + $scope.tenant.tenantId + "/users/" + user.username)
	                .then(function(result) {
	                    $scope.getTenantUsers($scope.tenant.tenantId);
	                }, function(reason) {
	                    // TODO : Show error on window
	                });
        	});
        	
        	dialog.run();
        };
        
        $scope.hasUserRole = function(role, roles) {
            return roles.includes(role);
        }
    }
]);

repositoryControllers.controller("createOrUpdateUserController", 
    ["$rootScope", "$scope", "$uibModal", "$uibModalInstance", "$http", "user", "tenant",
    function($rootScope, $scope, $uibModal, $uibModalInstance, $http, user, tenant) {
        
        $scope.mode = user.edit ? "Update" : "Add";
        $scope.user = user;
        $scope.tenant = tenant;
        $scope.isCurrentlyAddingOrUpdating = false;
        $scope.errorMessage = "";
        $scope.selectedAuthenticationProviderId = null;
        $scope.userPartial = "";
        $scope.selectedUser = null;
        $scope.retrievedUsers = [];
        $scope.technicalUserSubject = null;

        $scope.selectUser = function(user) {
            if (user) {
                $scope.selectedUser = user;
                document.getElementById('userId').value = $scope.selectedUser.username;
            }
            $scope.retrievedUsers = [];
        }

        $scope.highlightUser = function(user) {
            let element = document.getElementById(user.username);
            if (element) {
                element.style.backgroundColor = '#7fc6e7';
                element.style.color = '#ffffff'
            }
        }

        $scope.unhighlightUser = function(user) {
            let element = document.getElementById(user.username);
            if (element) {
                element.style.backgroundColor = 'initial';
                element.style.color = 'initial';
            }
        }

        $scope.findUsers = function() {
            // only initiates user search if partial name is larger >= 4 characters
            // this is to prevent unmanageably large drop-downs
            if ($scope.userPartial && $scope.userPartial.length >= 4) {
                $http.get("./rest/accounts/search/" + $scope.userPartial)
                .then(function (result) {
                    if (result.data) {
                        $scope.retrievedUsers = result.data;
                        if ($scope.retrievedUsers.length == 1) {
                            $scope.selectedUser = $scope.retrievedUsers[0];
                            $scope.selectUser();
                        }
                    } else {
                        $scope.retrievedUsers = [];
                        $scope.selectedUser = null;
                    }
                });
            }
            else {
                $scope.retrievedUsers = [];
                $scope.selectedUser = null;
            }
            $scope.toggleSubmitButton();
        };

        $scope.cancel = function() {
            $uibModalInstance.dismiss("Canceled.");  
        };

        $scope.promptCreateNewTechnicalUser = function() {
            let modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "webjars/repository-web/dist/partials/admin/createTechnicalUser.html",
                size: "md",
                controller: "createOrUpdateUserController",
                resolve: {
                    user: function () {
                        return user;
                    },
                    tenant: function() {
                        return tenant;
                    }
                }
            });

            modalInstance.result.finally(function(result) {
                $uibModalInstance.close($scope.user);
            });
        };

        $scope.createNewTechnicalUser = function() {
            $scope.isCurrentlyAddingOrUpdating = false;
            $http.post("./rest/tenants/" + $scope.tenant.tenantId + "/users/" + $scope.user.username, {
                "username": $scope.user.username,
                "roles" : $scope.getRoles($scope.user),
                "authenticationProviderId": $scope.selectedAuthenticationProviderId,
                "subject": $scope.technicalUserSubject
            })
            .then(
                function(result) {
                    $uibModalInstance.close($scope.user);
                },
                function(reason) {
                    $scope.errorMessage = "Creation of technical user " +
                        $scope.user.username + " in namespace " +
                        $scope.tenant.defaultNamespace + " failed. ";
                }
            );
        };

        $scope.toggleSubmitButton = function() {
            let button = document.getElementById("submitButton");
            if (button) {
                button.disabled = !(
                    ($scope.user && $scope.user.username)
                    ||
                    $scope.userPartial
                    ||
                    $scope.selectedUser
                );
            }
            return button && !button.disabled;
        }

        $scope.addOrUpdateUser = function() {
            // adds username to scope user by either using selected user from
            // drop-down if any, or using the string in user's input box
            if ($scope.selectedUser) {
                $scope.user = $scope.selectedUser;
            }
            else if ($scope.userPartial) {
                $scope.user.username = $scope.userPartial;
            }
            $scope.validate($scope.user, function(result) {
                if (result.valid) {
                    $scope.isCurrentlyAddingOrUpdating = false;
                    $http.put("./rest/tenants/" + $scope.tenant.tenantId + "/users/" + $scope.user.username, {
                            "username": $scope.user.username,
                            "roles" : $scope.getRoles($scope.user)
                        })
                        .then(function(result) {
                            $uibModalInstance.close($scope.user); 
                        }, function(reason) {
                            $scope.errorMessage = "You cannot change your own permissions.";
                        });
                } else {
                    $scope.promptCreateNewTechnicalUser();
                }
            });
        };
        
        $scope.getRoles = function(user) {
            var roles = [];
            if ($scope.user.roleModelCreator) {
                roles.push("ROLE_MODEL_CREATOR")
            }
            
            if ($scope.user.roleModelPromoter) {
                roles.push("ROLE_MODEL_PROMOTER")
            }
            
            if ($scope.user.roleModelReviewer) {
                roles.push("ROLE_MODEL_REVIEWER")
            }
            
            if ($scope.user.roleModelPublisher) {
                roles.push("ROLE_MODEL_PUBLISHER")
            }
            
            if ($scope.user.roleUser) {
                roles.push("ROLE_USER")
            }
            
            if ($scope.user.roleAdmin) {
                roles.push("ROLE_TENANT_ADMIN")
            }
            
            return roles;
        };
        
        $scope.validate = function(user, callback) {
            if (user.username === undefined || user.username.trim() === '') {
                callback({
                        valid : false,
                        errorMessage : "UserId must not be null."
                    });
                return;
            }
            
            $http.get("./rest/accounts/" + user.username)
                .then(function(result) {
                    callback({ valid: true });
                }, function(reason) {
                    if (reason.status == 404) {
                        callback({
                            valid: false,
                            errorMessage: "User doesn't exist."
                        });
                    } else {
                        callback({
                            valid: false,
                            errorMessage: "Error while accessing the server."
                        });
                    }
                });
        };
    }
]);
});
