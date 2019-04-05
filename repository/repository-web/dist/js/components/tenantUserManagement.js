
repositoryControllers.controller("tenantUserManagementController", 
    [ "$rootScope", "$scope", "$http", "$uibModal", 
    function($rootScope, $scope, $http, $uibModal) {
        
        $scope.isRetrievingTenants = false;
        $scope.isRetrievingTenantUsers = false;
        $scope.userTenants = [];
        $scope.chosenTenant = null;
        $scope.userTenantUsers = [];
        
        $scope.getTenants = function() {
            $scope.isRetrievingTenants = true;
            $http.get("./rest/tenants?role=ROLE_TENANT_ADMIN")
                .then(function(result) {
                    $scope.isRetrievingTenants = false;
                    console.log(JSON.stringify(result));
                    $scope.userTenants = result.data;
                    if ($scope.userTenants.length > 0) {
                        $scope.getTenantUsers($scope.userTenants[0].tenantId);
                    }
                }, function(reason) {
                    $scope.isRetrievingTenants = false;
                    // TODO : handling of failures
                });
        };
        
        $scope.getTenants();
        
        $scope.$on("USER_CONTEXT_UPDATED", function(evt, data) {
            $scope.getTenants();
        });
        
        $scope.getTenantUsers = function(tenantId) {
            $scope.isRetrievingTenantUsers = true;
            $scope.chosenTenant = tenantId;
            
            $http.get("./rest/tenants/" + tenantId + "/users")
                .then(function(result) {
                    $scope.isRetrievingTenantUsers = false;
                    console.log(JSON.stringify(result));
                    $scope.userTenantUsers = result.data;
                }, function(reason) {
                    $scope.isRetrievingTenantUsers = false;
                    // TODO : handling of failures
                });
        };
        
        $scope.newUser = function() {
            return {
                edit: false,
                username : "",
                roleModelCreator : true,
                roleModelPromoter : true,
                roleModelReviewer : true,
                roleUser : true
            };
        };
        
        $scope.editableUser = function(user) {
            return {
                edit: true,
                username : user.username,
                roleModelCreator : user.roles.includes("ROLE_MODEL_CREATOR"),
                roleModelPromoter : user.roles.includes("ROLE_MODEL_PROMOTER"),
                roleModelReviewer : user.roles.includes("ROLE_MODEL_REVIEWER"),
                roleUser : user.roles.includes("ROLE_USER")
            };
        };
        
        $scope.createOrUpdateUser = function(user) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: "webjars/repository-web/dist/partials/admin/createOrUpdateUser.html",
                size: "md",
                controller: "createOrUpdateUserController",
                resolve: {
                    user: function () {
                        return user;
                    },
                    tenantId: function() {
                        return $scope.chosenTenant;
                    }
                }
            });
            
            modalInstance.result.finally(function(result) {
                $scope.getTenantUsers($scope.chosenTenant);
                $rootScope.init();
            });
        };
        
        $scope.deleteUser = function(user) {
            $http.delete("./rest/tenants/" + $scope.chosenTenant + "/users/" + user.username)
                .then(function(result) {
                    console.log("SUCCESS:" + JSON.stringify(result));
                    $scope.getTenantUsers($scope.chosenTenant);
                }, function(reason) {
                    console.log("ERROR:" + JSON.stringify(reason));
                    // TODO : Show error on window
                });
        };
        
        $scope.hasUserRole = function(role, roles) {
            return roles.includes(role);
        }
    }
]);

repositoryControllers.directive("tenantUser", function() {
    return {
        templateUrl: "webjars/repository-web/dist/partials/admin/tenantUserManagement.html"
    };
});

repositoryControllers.controller("createOrUpdateUserController", 
    ["$rootScope", "$scope", "$uibModalInstance", "$http", "user", "tenantId",
    function($rootScope, $scope, $uibModalInstance, $http, user, tenantId) {
        
        $scope.mode = user.edit ? "Update" : "Create";
        $scope.user = user;
        $scope.tenantId = tenantId;
        $scope.isCurrentlyAddingOrUpdating = false;
        $scope.errorMessage = "";
        
        $scope.cancel = function() {
            $uibModalInstance.dismiss("Canceled.");  
        };
        
        $scope.addOrUpdateUser = function() {
            $scope.validate($scope.user, function(result) {
                if (result.valid) {
                    $scope.isCurrentlyAddingOrUpdating = false;
                    console.log(JSON.stringify($scope.getRoles($scope.user)));
                    $http.put("./rest/tenants/" + $scope.tenantId + "/users/" + $scope.user.username, {
                            "username": $scope.user.username,
                            "roles" : $scope.getRoles($scope.user)
                        })
                        .then(function(result) {
                            console.log("SUCCESS:" + JSON.stringify(result));
                            $uibModalInstance.close($scope.user); 
                        }, function(reason) {
                            console.log("ERROR:" + JSON.stringify(result));
                            // TODO : do proper error handling
                        });
                } else {
                    $scope.errorMessage = result.errorMessage;
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
            
            if ($scope.user.roleUser) {
                roles.push("ROLE_USER")
            }
            return roles;
        };
        
        $scope.validate = function(user, callback) {
            if (user.username === '') {
                callback({
                        valid : false,
                        errorMessage : "UserId must not be null."
                    });
            }
            
            $http.get("./rest/accounts/" + user.username)
                .then(function(result) {
                    callback({ valid: true });
                }, function(reason) {
                    console.log("ERROR:" + JSON.stringify(reason));
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

