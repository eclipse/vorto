repositoryControllers.controller('MappingBuilderController', ['$rootScope','$uibModal', '$routeParams', '$scope', '$location','$http', '$sce','$timeout',
	function($rootScope,$uibModal,$routeParams, $scope, $location,$http, $sce,$timeout) {
		
		$scope.modelId = $routeParams.modelId;
						 
		$scope.mappingId = $routeParams.mappingId;
								 
		$scope.targetPlatform = $routeParams.targetPlatform;
		
		$scope.testInProgress = false;
		$scope.mappedOutput = null;
		
		$scope.testResponse = {
			valid : true,
			validationError : ""
		};
						
		
		$scope.isLoading = false;

		$scope.errorMessage = null;
				
		$scope.sourceContent = null;
		
		$scope.newFunctionCode = "";
		
		$scope.customFunctions = [];
		
		$scope.editors = [];
		
		$scope.editMode = false;
		
		$scope.htmlPopover = $sce.trustAsHtml(
			'<p>Use your custom converters in your mapping rules, e.g. custom:myfunc()</p>');
		
		$scope.functionEditorLoaded = function(_editor) {
    		_editor.getSession().setMode("ace/mode/javascript");
    		_editor.setTheme("ace/theme/twilight");
    		_editor.getSession().setTabSize(2);
  			_editor.getSession().setUseWrapMode(true);
  			//if ($scope.state !== 'Draft') _editor.setReadOnly(true);
  			
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
						$scope.mappedOutput = null;
						$scope.errorMessage = null;
						var testRequest = {
							specification : {"infoModel":$scope.infomodel,"properties":$scope.properties},
							sourceJson : $scope.sourceContent
						};
						$http.put("./rest/" + $rootScope.tenant + "/mappings/test",testRequest).success(
							function(data, status, headers, config) {
								$scope.testInProgress = false;
								$scope.mappedOutput = JSON.parse(data.mappedOutput);
								$scope.testResponse = data;
							}).error(function(data, status, headers, config) {
								$scope.testInProgress = false;
								$scope.errorMessage = data.message;
							});			
					};
	
		
		$scope.functionEditorChanged = function (e) {
		    $scope.newFunctionCode = e[1].getSession().getDocument().getValue();
		};
		
		
		$scope.addFunction = function(propertyName,editorClear) {
			$scope.editMode = false;
			if ($scope.newFunctionCode === "" || !$scope.newFunctionCode.includes("function")) {
				return;
			}
			
			var stereotypes = $scope.properties[propertyName].stereotypes;
	
			var functionName = $scope.newFunctionCode.substring($scope.newFunctionCode.indexOf("function ")+"function ".length,$scope.newFunctionCode.indexOf("("))
			if (stereotypes.length === 0 || $scope.properties[propertyName].stereotypes.filter(function(stereotype) {return stereotype.name === "functions"}).length === 0) {
				 var attributes = {};
					 attributes[functionName] = $scope.newFunctionCode;
					 $scope.properties[propertyName].stereotypes.push({name : "functions", attributes : attributes});
					 var f = {
					 	property: propertyName,
					 	name : functionName,
					 	body : $scope.newFunctionCode
					 };
					 $scope.customFunctions.push(f);
			} else {
				var functionsStereotype = $scope.properties[propertyName].stereotypes.filter(function(stereotype) {return stereotype.name === "functions"});
				functionsStereotype[0].attributes[functionName] = $scope.newFunctionCode;
				var f = {
					 	property: propertyName,
					 	name : functionName,
					 	body : $scope.newFunctionCode
					 };
				var indexOfEditFunction = -1;
				for (var i = 0; i < $scope.customFunctions.length;i++) {
					if ($scope.customFunctions[i].property === propertyName && $scope.customFunctions[i].name === functionName) {
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
		    		if (e.property === propertyName) {
		    			e.editor.getSession().getDocument().setValue("");
		    			return;
		    		}
	    		});	
			}	
		};
		
		$scope.getCustomFunctionsCount = function(property) {
			var count = 0;
			for (var i = 0; i < $scope.customFunctions.length;i++) {
				if ($scope.customFunctions[i].property === property) {
					++count;
				}
			}
			return count;
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
				delete $scope.properties[property.name].stereotypes[0].attributes[func.name];
	    	}
		};
				
	    $scope.loadMappingSpec = function() {
	    	$scope.isLoading = true;
			$http.get("./rest/" + $rootScope.tenant + "/mappings/"+$scope.modelId+"/"+$scope.targetPlatform).success(
				function(data, status, headers, config) {
					$scope.infomodel = data.infoModel;
					$scope.properties = data.properties;
					$scope.loadCustomFunctions();
					$scope.isLoading = false;
				}).error(function(data, status, headers, config) {
					$scope.errorMessage = "Error loading model from repository!";
					$scope.isLoading = false;
				});		
	    };
	    
	    $scope.loadCustomFunctions = function() {
	    	for (propertyName in $scope.properties) {
	    		var stereotypes = $scope.properties[propertyName].stereotypes;
	  			for (var i = 0; i < stereotypes.length;i++) {
	  				if (stereotypes[i].name === "functions") {
	  					for (attributeKey in stereotypes[i].attributes) {
	  						if (attributeKey != "_namespace") {
	  							$scope.newFunctionCode = stereotypes[i].attributes[attributeKey];
	    						$scope.addFunction(propertyName,false);
	  						}	
	  					}
	  					
	  				}
	  			}
	    	}
	    };
	    
	    
	    $scope.save = function() {
			var specification = {"infoModel":$scope.infomodel,"properties":$scope.properties};
            $http.put('./rest/' + $rootScope.tenant + '/mappings/' + $scope.modelId + '/' + $scope.targetPlatform,specification).success(
    			function(data, status, headers, config) {
    			$scope.success = true;
    				
    			$timeout(function() {
    				$scope.success = false;
    			},2000);
    			}).error(function(data, status, headers, config) {
    				$scope.errorMessage = data.msg;
    				$timeout(function() {
    					$scope.errorMessage = null;
    				},2000);
    				
    			});
		};
	    	    
	    $scope.loadSimpleExample = function(property) {
	    	var simpleTestData = { "t001" : 30};	    
	    		    		 	
	    	$scope.properties[property].statusProperties.some(function(e) {
	    		if (e.mandatory) {
	    			e.stereotypes[0].attributes['xpath'] = "/t001";
	    			return true; 
	    		}
	    	});	    	
	    	
	    	$timeout(function() {
	    		$scope.sourceEditorSession.getDocument().setValue(JSON.stringify(simpleTestData,null,4));
	    	},2000);	   	
	    };
	    
	    $scope.loadGattExample = function(property) {
	    	var gattTestData = { "characteristics" : [{
	    							"uuid" : "23-D1-13-EF-5F-78-23-15-DE-EF-12-12-0D-F0-00-00",
	    							"data" : [0, 0, 0, -48, 7, 0]	
	    						  }]
	    						};	    	
	    	$scope.newFunctionCode = "function convertSensorValue(value) { return value*0.01; }";
	    	$scope.addFunction(property,false);
	    	
	    	$scope.properties[property].statusProperties.some(function(e) {
	    		if (e.mandatory) {
	    			e.stereotypes[0].attributes['xpath'] = property.toLowerCase()+":convertSensorValue(conversion:byteArrayToInt(characteristics[@uuid='23-D1-13-EF-5F-78-23-15-DE-EF-12-12-0D-F0-00-00']/data, 3, 0, 0, 3))";
	    			return true;
	    		}
	    	});	    	
	    	
	    	$timeout(function() {
  				$scope.sourceEditorSession.getDocument().setValue(JSON.stringify(gattTestData,null,4));
  			},2000);	   	
	    };
	   
	    
	    $scope.loadBinaryExample = function(property) {
	    	var binaryTestData = {"data" : [0, 0, 0, -48, 7, 0]};
			$scope.newFunctionCode = "function convertSensorValue(value) { return value*0.01; }";
	    	$scope.addFunction(property,false);
	    	
	    	$scope.properties[property].statusProperties.some(function(e) {
	    		if (e.mandatory) {
	    			e.stereotypes[0].attributes['xpath'] = property.toLowerCase()+":convertSensorValue(conversion:byteArrayToInt(/data, 3, 0, 0, 3))";
	    			return true;
	    		}
	    	});	
	    	
	    	$timeout(function() {
  				$scope.sourceEditorSession.getDocument().setValue(JSON.stringify(binaryTestData,null,4));
  			},2000);
	    };
	    
	    $scope.loadConditionExample = function(property) {
	    	var simpleTestData = { "x" : { "header" : "T", "v" : 10 }};	    
	    		    		 	
	    	$scope.properties[property].statusProperties.some(function(e) {
	    		if (e.mandatory) {
	    			e.stereotypes[0].attributes['condition'] = "x/header == 'T'";
	    			e.stereotypes[0].attributes['xpath'] = "x/v";
	    			return true; 
	    		}
	    	});	    	
	    	
	    	$timeout(function() {
	    		$scope.sourceEditorSession.getDocument().setValue(JSON.stringify(simpleTestData,null,4));
	    	},2000);	  
	    };
	    
	    $scope.getMappingState = function() {
	    	$http.get('./rest/' + $rootScope.tenant + '/mappings/'+$scope.modelId+'/'+$scope.targetPlatform+'/info').success(
				function(data, status, headers, config) {
					if (data.length > 0) {
						$http.get('./rest/' + $rootScope.tenant + '/workflows/'+data[0].id.prettyFormat).success(
							function(result, status, headers, config) {
								$scope.state = result.name;
						});
					} else {
						$scope.state = "Draft";
					}
				});
	    };
	    
	    $scope.getMappingState();
		$scope.loadMappingSpec();

	}]);
