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
define(["../../init/appController"],function(repositoryControllers) {

repositoryControllers.controller("MappingBuilderController", ["$rootScope","$uibModal", "$routeParams", "$scope", "$location","$http", "$sce","$timeout", "SessionTimeoutService",
    function($rootScope,$uibModal,$routeParams, $scope, $location,$http, $sce, $timeout, sessionTimeoutService) {

    $scope.modelId = $routeParams.modelId;

    $scope.testInProgress = false;
    $scope.mappedOutput = null;

	$scope.testResponse = {
		report : {
			valid : true
		}
	};

    $scope.isLoading = false;

    $scope.errorMessage = null;

    $scope.sourceContent = null;

    $scope.newFunctionCode = "";

    $scope.customFunctions = [];

    $scope.editors = [];

    $scope.editMode = false;
    
    $scope.conditions = [];
    
    $scope.contentType = "json";

    $scope.htmlPopover = $sce.trustAsHtml(
    "<p>Use your custom converters in your mapping rules, e.g. custom:myfunc()</p>");

    sessionTimeoutService.initSessionTimeoutWarning($scope);

    $scope.setAceLang = function() {
    	if ($scope.contentType === 'json') {
    		$scope.sourceEditorSession.setMode("ace/mode/json");
    	} else {
    		$scope.sourceEditorSession.setMode(null);
    	}
    };

    $scope.functionEditorLoaded = function(_editor) {
        _editor.getSession().setMode("ace/mode/javascript");
        _editor.setTheme("ace/theme/twilight");
        _editor.getSession().setTabSize(2);
        _editor.getSession().setUseWrapMode(true);
        //if ($scope.state !== "Draft") _editor.setReadOnly(true);

        $timeout(function() {
            var e = {
                    property : _editor.container.attributes[0].value,
                    editor : _editor
            };
            $scope.editors.push(e);
        },3000);

    };

    $scope.sourceEditorLoaded = function(_editor) {
        $scope.sourceEditorSession = _editor.getSession();
        _editor.getSession().setMode("ace/mode/json");
        _editor.setTheme("ace/theme/twilight");
        _editor.getSession().setTabSize(2);
        _editor.getSession().setUseWrapMode(true);
    };


    $scope.aceChanged = function () {
        $scope.sourceContent = $scope.sourceEditorSession.getDocument().getValue();
    };

    $scope.executeTest = function() {
        $scope.testInProgress = true;
        
        $scope.canonical = null;
        $scope.ditto = null;
        $scope.awsiot = null;
        
        $scope.errorMessage = null;
        var testRequest = {
                specification : {"infoModel":$scope.infomodel},
                content : $scope.sourceContent,
                contentType : $scope.contentType             
        };
        
        $http.put("./rest/mappings/specifications/test",testRequest).success(
                function(data, status, headers, config) {
                    $scope.testInProgress = false;
                    
                    $scope.canonical = JSON.parse(data.canonical);
                    $scope.ditto = JSON.parse(data.ditto);
                    $scope.awsiot = JSON.parse(data.awsiot);
                    
                    $scope.testResponse = data;
                }).error(function(data, status, headers, config) {
                    $scope.testInProgress = false;
                    $scope.errorMessage = data.message;
                });			
    };


    $scope.functionEditorChanged = function (e) {
        $scope.newFunctionCode = e[1].getSession().getDocument().getValue();
    };
    
	$scope.applyCondition = function(property) {
	   var stereotypes = property.type.stereotypes;
	   if (stereotypes.length === 0 || property.type.stereotypes.filter(function(stereotype) {return stereotype.name === "condition"}).length === 0) {
            var attributes = {};
            attributes['value'] = $scope.conditions[property.name];
            property.type.stereotypes.push({name : "condition", attributes : attributes});
        } else {
            var conditionStereotype = property.type.stereotypes.filter(function(stereotype) {return stereotype.name === "condition"});
            conditionStereotype[0].attributes['value'] = $scope.conditions[property.name];
        }
	};

    $scope.addFunction = function(property,editorClear) {
        $scope.editMode = false;
        if ($scope.newFunctionCode === "" || !$scope.newFunctionCode.includes("function")) {
            return;
        }

        var stereotypes = property.type.stereotypes;

        var functionName = $scope.newFunctionCode.substring($scope.newFunctionCode.indexOf("function ")+"function ".length,$scope.newFunctionCode.indexOf("("))
        if (stereotypes.length === 0 || property.type.stereotypes.filter(function(stereotype) {return stereotype.name === "functions"}).length === 0) {
            var attributes = {};
            attributes[functionName] = $scope.newFunctionCode;
            property.type.stereotypes.push({name : "functions", attributes : attributes});
            var f = {
                    property: property.name,
                    name : functionName,
                    body : $scope.newFunctionCode
            };
            $scope.customFunctions.push(f);
        } else {
            var functionsStereotype = property.type.stereotypes.filter(function(stereotype) {return stereotype.name === "functions"});
            functionsStereotype[0].attributes[functionName] = $scope.newFunctionCode;
            var f = {
                    property: property.name,
                    name : functionName,
                    body : $scope.newFunctionCode
            };
            var indexOfEditFunction = -1;
            for (var i = 0; i < $scope.customFunctions.length;i++) {
                if ($scope.customFunctions[i].property === property.name && $scope.customFunctions[i].name === functionName) {
                    indexOfEditFunction = i;
                    break;
                }
            }
            if (indexOfEditFunction > -1) {
                $scope.customFunctions[indexOfEditFunction] = f;
            } else {
                $scope.customFunctions.push(f);
            }
        }
        if (editorClear) {
            $scope.editors.forEach(function(e) {
                if (e.property === property.name) {
                    e.editor.getSession().getDocument().setValue("");
                    return;
                }
            });	
        }	
    };

    $scope.getCustomFunctionsCount = function(property) {
        var count = 0;
        for (var i = 0; i < $scope.customFunctions.length;i++) {
            if ($scope.customFunctions[i].property === property.name) {
                ++count;
            }
        }
        return count;
    };
    
    $scope.getCustomFunctionsForProperty = function(property) {
    	var functions = [];
   
        for (var i = 0; i < $scope.customFunctions.length;i++) {
            if ($scope.customFunctions[i].property === property.name) {
                functions.push($scope.customFunctions[i]);
            }
        }
        return functions;
    };

    $scope.editFunction = function(func) {
        $scope.editMode = true;
        $scope.editors.forEach(function(e) {
            if (e.property === func.property) {
                e.editor.getSession().getDocument().setValue(func.body);
                return;
            }
        });
    };

    $scope.deleteFunction = function(func,property) {
        for (var i = 0; i < $scope.customFunctions.length;i++) {
            if ($scope.customFunctions[i].name === func.name) {
                indexOfFunctionToRemove = i;
                break;
            }
        }

        if (indexOfFunctionToRemove > -1) {
            $scope.customFunctions.splice(indexOfFunctionToRemove,1);
            delete property.type.stereotypes[0].attributes[func.name];
        }
    };

    $scope.loadMappingSpec = function() {
        $scope.isLoading = true;
        $http.get("./rest/mappings/specifications/"+$scope.modelId).success(
                function(data, status, headers, config) {
                    $scope.infomodel = data.infoModel;
                    $scope.state = "Draft";
                    $scope.loadCustomFunctions();
                    $scope.loadFbConditions();
                    $scope.isLoading = false;
                }).error(function(data, status, headers, config) {
                    $scope.validationError = data;
                    $scope.isLoading = false;
                });		
    };
    
    $scope.loadFbConditions = function() {
        $scope.infomodel.functionblocks.forEach(function(property) {
           var stereotypes = property.type.stereotypes;
           if (stereotypes != null) {
           	$scope.loadFbConditionFromStereotype(property, stereotypes);
           }
        }); 
    };
    
    $scope.isDatatype = function(property) {
    	return property.type.hasOwnProperty("modelType") && property.type.modelType === 'EntityModel';
    };
    
    $scope.isEnumModel = function(property) {
    	return property.type.hasOwnProperty("modelType") && property.type.modelType === 'EnumModel';
    };
    
    $scope.loadFbConditionFromStereotype = function(property, stereotypes) {
    	$scope.conditions[property.name] = "";
        for (var i = 0; i < stereotypes.length;i++) {     	
            if (stereotypes[i].name === "condition") {
              $scope.conditions[property.name] = stereotypes[i].attributes['value'];
            }  
        }
    };

    $scope.loadCustomFunctions = function() {
    	$scope.infomodel.functionblocks.forEach(function(property) {
    		var stereotypes = property.type.stereotypes;
            if (stereotypes != null) {
           		$scope.loadCustomFunctionsFromStereotype(property, stereotypes);
           	}
    	});
    };

    $scope.loadCustomFunctionsFromStereotype = function(property, stereotypes) {
        for (var i = 0; i < stereotypes.length;i++) {
            if (stereotypes[i].name === "functions") {
                $scope.loadCustomFunctionsFromStereotypeAttributes(property, stereotypes, stereotypes[i].attributes);
            }
        }
    };

    $scope.loadCustomFunctionsFromStereotypeAttributes = function(property, stereotypes, stereotypeAttributes) {
        for (var attributeKey in stereotypeAttributes) {
            if (stereotypeAttributes.hasOwnProperty(attributeKey) && attributeKey !== "_namespace") {
                $scope.newFunctionCode = stereotypeAttributes[attributeKey];
                $scope.addFunction(property,false);
            }   
        }
    };
    
    $scope.save = function() {
    	$scope.isSaving = true;
        var specification = {"infoModel":$scope.infomodel};
        $http.put("./rest/mappings/specifications/" + $scope.modelId,specification).success(
                function(data, status, headers, config) {
                    $scope.success = true;
					$scope.isSaving = false;
					if ($scope.mappingId == null) {
						$scope.getMappingId();
					}
					
                    $timeout(function() {
                        $scope.success = false;
                    },2000);
                }).error(function(data, status, headers, config) {
                    $scope.errorMessage = data.msg;
                    $scope.isSaving = false;
                    $timeout(function() {
                        $scope.errorMessage = null;
                    },2000);

                });
    };

    $scope.loadMappingSpec();
    
    
    $scope.getMappingId = function() {
    	$http.get("./rest/mappings/specifications/"+$scope.modelId+"/mappingId").success(
                function(data, status, headers, config) {
                    $scope.mappingId = data.mappingId;
                }).error(function(data, status, headers, config) {
                    $scope.mappingId = null;
                });		
    };
    
    $scope.getMappingId();

}]);

});
