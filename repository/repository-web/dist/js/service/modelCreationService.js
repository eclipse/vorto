repository.factory('openCreateModelDialog',
    ['$rootScope', '$http', '$location', '$uibModal',
    function($rootScope, $http, $location, $uibModal) {
   
    return function($scope) {
        return function(action) {
            var modalInstance = $uibModal.open({
                animation: true,
                controller: function($scope) {
                    $scope.errorMessage = null;
                    $scope.modelType = "InformationModel";
                    $scope.currentStep = "step1";
                    $scope.modelName = "";
                    $scope.modelNamespace = "";
                    $scope.modelVersion = "1.0.0";
                    $scope.fbType = "org.eclipse.vorto";
                    
                    $scope.selected = {};
                    $scope.selected.properties = [];
                
                    $scope.properties = [];
                    $scope.rePropertyName = /^[A-Za-z][A-Za-z0-9_]{1,30}$/;
                    
                    $scope.selectedProperty = {};
                    $scope.selectedProperty["propertyName"] = "";
                    $scope.selectedProperty["selectedFb"] = null;
                    
                    $scope.namespaceRoot = "";
                    $scope.namespaceAppend = "";
                    $scope.userNamespaces = [];
                    
                    $scope.selectFunctionBlock = function(fb){
                        $scope.selectedProperty["selectedFb"] = fb;
                    };
                    
                    $scope.loadFunctionblocks = function(namespace) {
                        $http.get("./rest/search/releases?type=Functionblock&namespace="+namespace).success(
                            function(data, status, headers, config) {
                                $scope.functionblocks = data;
                                $scope.isLoading = false;
                            }).error(function(data, status, headers, config) {
                                $scope.functionblocks = [];
                                $scope.errorMessage  = "No Functionblocks found!";
                                $scope.isLoading = false;
                            });         
                    };
                     
                    $scope.loadFunctionblocks('org.eclipse.vorto');
                    
                    $scope.getNamespaces = function() {
                        $scope.userNamespaces = [];
                        $http.get("./rest/tenants?role=ROLE_MODEL_CREATOR")
                            .then(function(result) {
                                var tenants = result.data;
                                if (tenants != null) {
                                    for(var i=0; i < tenants.length; i++) {
                                        if (tenants[i].namespaces != null) {
                                            for(var k=0; k < tenants[i].namespaces.length; k++) {
                                                $scope.userNamespaces.push({
                                                    tenant: tenants[i].tenantId,
                                                    namespace: tenants[i].namespaces[k]
                                                }); 
                                            }
                                        }
                                    }
                                }
                                if ($scope.userNamespaces.length > 0) {
                                    $scope.userNamespaces.sort(function (a, b) {
                                        return a.namespace.localeCompare(b.namespace);
                                    });
                                    $scope.namespaceRoot = $scope.userNamespaces[0].namespace; 
                                }
                            }, function(reason) {
                                // TODO : handling of failures
                            });
                    };
                    
                    $scope.getNamespaces();
                
                    $scope.generatePropertyName = function(selectedFb) {
                        $scope.selectedProperty["selectedFb"] = selectedFb;
                        $scope.selectedProperty["propertyName"] = generateVariableName(selectedFb);
                        
                        function generateVariableName(fb) {
                            var variableName = fb.name.toLowerCase();
                            var i = 0;
                            while (contains(variableName,$scope.selected.properties)) {
                                variableName += ++i;
                            }
                            return variableName;
                        };
                        
                        function contains(variableName,properties) {
                            for (var i = 0; i < properties.length;i++) {
                                if (properties[i].name === variableName) {
                                    return true;
                                }
                            }
                            return false;
                        };
            
                    };
                    
                    $scope.createNamespace = function(namespaceRoot, namespaceAppend) {
                        if (namespaceAppend == null || namespaceAppend === '') {
                            return namespaceRoot.namespace;
                        } else {
                            return namespaceRoot.namespace + "." + namespaceAppend;
                        }
                    };
                    
                    $scope.addProperty = function() {
                        var property = {};
                        property["name"] = $scope.selectedProperty["propertyName"];
                        property["type"] = $scope.selectedProperty["selectedFb"];
                        $scope.selected.properties.push(property);
                        console.log($scope.selectedProperty["propertyName"]);
                        console.log($scope.selectedProperty["selectedFb"]);
                        $scope.selectedProperty["propertyName"] = "";
                        $scope.selectedProperty["selectedFb"] = null;   
                    };
                
                    $scope.removeProperty = function(item,model) {
                        for(var i = 0; i < $scope.selected.properties.length; i++){
                            if ($scope.selected.properties[i].name === item.name) $scope.selected.properties.splice(i, 1);
                        }
                    };
                    
                    $scope.next = function(page, modelType, namespaceRoot, namespaceAppend, modelName, modelVersion) {
                        $scope.currentStep = page;
                        $scope.modelType = modelType;
                        $scope.namespaceRoot = namespaceRoot;
                        $scope.namespaceAppend = namespaceAppend;
                        $scope.modelName = modelName;
                        $scope.modelVersion = modelVersion;
                    };
                    
                    $scope.getTenant = function(namespaceRoot) {
                        for(var i=0; i < $scope.userNamespaces.length; i++) {
                            if ($scope.userNamespaces[i].namespace === namespaceRoot.namespace) {
                                return $scope.userNamespaces[i].tenant; 
                            }
                        }
                        
                        return null;
                    }
                    
                    $scope.create = function(namespaceRoot, modelType, modelNamespace, modelName, modelVersion) {
                        $scope.isLoading = true;
                        var tenantId = $scope.getTenant(namespaceRoot);
						  $http.post('./rest/models/'+$rootScope.modelId(modelNamespace,modelName,modelVersion)+'/'+modelType, $scope.selected.properties)
						.success(function(result){
                                $scope.isLoading = false;
                                if (result.status === 409) {
                                    $scope.errorMessage = "Model with this name and namespace already exists.";
                                } else {
                                    modalInstance.close({
                                        model: result
                                    });
                                }
                            }).error(function(data, status, header, config) {
                                $scope.isLoading = false;
                                if (status === 409) {
                                    $scope.errorMessage = "Model with this name and namespace already exists.";
                                }
                            });
                    };
                        
                    $scope.cancel = function() {
                        modalInstance.dismiss();
                    };
                },
                templateUrl: "webjars/repository-web/dist/partials/createmodel-template.html",
                size: "lg",
                resolve: {
                    model: function() {
                        return $scope.model;
                    }
                }
              });
              
              modalInstance.result.then(function(result) {
				  $location.path("/details/" + result.model.id.prettyFormat);
              });
        };
    }
}]);