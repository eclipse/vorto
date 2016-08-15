define(["angular"], function(angular) {
  var app = angular.module('apps.controller', ['smart-table', 'apps.directive']);

  app.controller('EditorController', function($rootScope, $scope, $location, $routeParams, $http, $compile, $uibModal) {

    $scope.error = null;
    $scope.errorMessage = null;
    $scope.validationError = false;
    $scope.selectedModel = null;
    $scope.selectedModelId = null;
    $scope.showAddFunctionBlockButton = true;

    $scope.tabs = [];
    $scope.editors = [];
    $scope.counter = 0;
    $scope.selectedTabIndex = 0;
    $scope.selectedTabId = 0;
    $scope.selectedEditor = null;

    $scope.showImportButton = true;

    $scope.showEditorBody = true;
    $scope.projectName;

    $scope.editorTypes = [{
      language: 'infomodel',
      display: 'Info Model'
    }, {
      language: 'fbmodel',
      display: 'Function Block'
    }];
    $scope.models = [];
    $scope.queryFilter = "";

    $scope.openProject = function() {
      $scope.projectName = $routeParams.projectName;
      $http.get('./project/' + $scope.projectName + '/open').success(
        function(data, status, headers, config) {
          $scope.showEditorBody = true;
          $scope.getResources();
        }).error(function(data, status, headers, config) {
        $scope.showEditorBody = false;
      });
    }

    $scope.openProject();

    $scope.getResources = function() {
      $http.get('./project/' + $scope.projectName + '/resources').success(
        function(data, status, headers, config) {
          for (i = 0; i < data.length; i++) {
            $scope.openEditor(data[i]);
          }
        }).error(function(data, status, headers, config) {

      });
    }

    $scope.$on("addTab", function(event, model) {
      $scope.addEditor(model);
    });

    $scope.$on("describeEditor", function(event, editor) {
      $scope.openDescribeEditorModal(editor);
    });

    $scope.$on("deleteEditor", function(event, tab) {
      var url = './project/' + $scope.projectName + '/resources/delete/' + tab.resourceId + '/';
      $http.get(url).success(
        function(data, status, headers, config) {
          $scope.deleteTab(tab.index);
        }).error(function(data, status, headers, config) {
        windoow.alert('Unable to delete the file');
      });
    });

    $scope.$on("closeProject", function(event) {
      $scope.closeProject();
    });

    $scope.closeProject = function() {
      $location.path('/project');
      $location.replace();
    }

    $scope.openEditor = function(resource) {
      $scope.counter++;
      var tabId = $scope.counter;
      var editorParentDivId = "xtext-editor-parent-" + tabId;
      var editorDivId = "xtext-editor-" + tabId;
      var language = $scope.getResourceLanguage(resource);
      var tab = {
        id: tabId,
        editorParentDivId: editorParentDivId,
        editorDivId: editorDivId,
        language: language,
        name: resource.name,
        version: resource.version,
        namespace: resource.namespace,
        resourceId: resource.resourceId
      };
      $scope.tabs.push(tab);
      $scope.selectedTabIndex = $scope.tabs.length - 1;
      $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]['id'];
      var element = angular.element(document).find('#editors');
      element.append('<div id="' + editorParentDivId + '" ng-show="selectedTabId==' + tabId + '"><div id="' + editorDivId + '" class="custom-xtext-editor"></div></div>');
      $compile(element.contents())($scope);
      if (language == 'infomodel') {
        $scope.openInfoModelEditor(tab);
      } else if (language == 'fbmodel') {
        $scope.openFunctionBlockEditor(tab);
      }
    }

    $scope.openInfoModelEditor = function(tab) {
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: "infomodel",
            syntaxDefinition: "xtext/mode-infomodel",
            serviceUrl: "/infomodel/xtext-service",
            enableFormattingAction: "true",
            showErrorDialogs: "true",
            parent: tab.editorDivId,
            resourceId: tab.resourceId
          });
          $scope.editors.push(editor);
          $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
          $scope.search();
        });
      });
    }

    $scope.openFunctionBlockEditor = function(tab) {
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: "fbmodel",
            syntaxDefinition: "xtext/mode-fbmodel",
            serviceUrl: "/functionblock/xtext-service",
            enableFormattingAction: "true",
            showErrorDialogs: "true",
            parent: tab.editorDivId,
            resourceId: tab.resourceId
          });
          $scope.editors.push(editor);
          $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
          $scope.search();
        });
      });
    }

    $scope.addEditor = function(model) {
      $scope.counter++;
      var tabId = $scope.counter;
      var editorParentDivId = "xtext-editor-parent-" + tabId;
      var editorDivId = "xtext-editor-" + tabId;
      var tab = {
        id: tabId,
        editorParentDivId: editorParentDivId,
        editorDivId: editorDivId,
        language: model.language,
        name: model.name,
      };
      $scope.tabs.push(tab);
      $scope.selectedTabIndex = $scope.tabs.length - 1;
      $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]['id'];
      var element = angular.element(document).find('#editors');
      element.append('<div id="' + editorParentDivId + '" ng-show="selectedTabId==' + tabId + '"><div id="' + editorDivId + '" class="custom-xtext-editor"></div></div>');
      $compile(element.contents())($scope);
      if (model.language == 'infomodel') {
        $scope.addInfoModelEditor(editorDivId, model);
      } else if (model.language == 'fbmodel') {
        $scope.addFunctionBlockEditor(editorDivId, model);
      }
    }

<<<<<<< 29ff6b8d2c5ee91524641f1e474159e7d3f9ea4a
<<<<<<< 681797320fe7535bef2783a0818d98b72d22447b
<<<<<<< 7fae963b67e1e36ca814808f8a6fb281d04d8a0b
  	$scope.getters= {
<<<<<<< b3179dff921b0569bff00f05397e808255908577
        name: function (value) {
	        return value.id.name.sort();
        }
  	 }
  	
  	$scope.predicates = ['Name'];
=======
        namespace: function (value) {
	        return value.id.namespace.sort();
        },
        name: function (value) {
	        return value.id.name.sort();
        },
        version: function (value) {
	        return value.id.version.sort();
        }
  	 }
  	
  	$scope.predicates = ['Name', 'Namespace', 'Version'];
>>>>>>> Devtool (#1)
  	
});

app.controller('FunctionblockEditorController', function($rootScope, $scope, $http, $location) {
	
	$scope.editor = null;
	$scope.error = null;
	$scope.errorMessage = null;
	$scope.validationError = false;
	$scope.selectedModel = null;
	$scope.selectedModelId = null;
	$scope.showAddFunctionBlockButton = true;
	
	$scope.models = [];
  	$scope.queryFilter = "";	
	
	require(["webjars/ace/1.2.0/src/ace"], function() {
				require(["xtext/xtext-ace"], function(xtext) {
					editor = xtext.createEditor({syntaxDefinition: "xtext/mode-fbmodel", serviceUrl: "/functionblock/xtext-service"});
				});
			});
	
	$scope.isValidFunctionblock = function() {
		if (editor.xtextServices.editorContext._annotations.length != 0) {
			$error = "Your function block contains errors. Please correct and try again.";
			return false;
		}else{
			return true;
		}
	}
			
	$scope.share = function() {
		if ($scope.isValidFunctionblock()) {
			// do upload and checkin REST call to Vorto Information Model Repository
		}		
	};
	
  	$scope.searchOnEnter = function(keyEvent) {
  		if (keyEvent.keyCode === 13) {
  			$scope.search();
  		}
  	};

  	$scope.search = function() {
  		var modelType = "Datatype";
  		var filter = null;
  		filter = $scope.queryFilter + " " + modelType ;
  		$http.get('./editor/functionblock/search=' + filter).success(
	      function(data, status, headers, config) {
					$scope.models = data;
	      }).error(function(data, status, headers, config) {
					$scope.models = [];
	      });
		};

	$scope.isDataTypeSelected = function() {
		for (i = 0; i < $scope.displayedModels.length; i++) { 
		    if($scope.displayedModels[i]['isSelected']){
		    	$scope.selectedModel = $scope.displayedModels[i];
		    	$scope.selectedModelId = $scope.selectedModel['id'];
		    	return true;
		    }
		}
    	$scope.selectedModel = null;
    	$scope.selectedModelId = null;
		return false;
	}
		
	$scope.importDatatype = function() {
		if($scope.isDataTypeSelected()){
			if($scope.isValidFunctionblock()){
				$scope.showAddFunctionBlockButton = false
		  		$http.get('./editor/functionblock/link/datatype/' + editor.xtextServices.validationService._encodedResourceId + '/' + $scope.selectedModelId['namespace'] + '/' + $scope.selectedModelId['name'] + '/' + $scope.selectedModelId['version'] ).success(
		  		      function(data, status, headers, config) {
		  				editor.setValue(data);
		  		      }).error(function(data, status, headers, config) {
		  		    	  window.alert('Failed')
		  		      });
				$scope.showAddFunctionBlockButton = true;
			}
			else{
				window.alert('Your function block contains errors. Please correct and try again.');			
			}
		}else{
			window.alert('Please select a function block');			
		}
	}
				
  	$scope.search();
=======
    $scope.addInfoModelEditor = function(parentId) {
=======
    $scope.addInfoModelEditor = function(parentId, filename) {
>>>>>>> Implemented the web-editor with in memory data storage
=======
    $scope.generateInfoModelContent = function(model) {
      var content = 'namespace ' + model.namespace + '\nversion ' + model.version + '\ndisplayname "' + model.displayname + '"\ndescription "' + model.description + '"\ncategory demo';
      content += "\ninfomodel " + model.displayname + " {";
      content += "\n\n}";
      return content;
    }

    $scope.generateFunctionBlockContent = function(model) {
      var content = 'namespace ' + model.namespace + '\nversion ' + model.version + '\ndisplayname "' + model.displayname + '"\ndescription "' + model.description + '"\ncategory demo';
      content += "\nfunctionblock " + model.displayname + " {";
      content += "\n\tconfiguration {\n\t //Please enter functionblock configuration details.\n\t}\n";
      content += "\n\tstatus {\n\t //Please enter functionblock status details.\n\t}\n";
      content += "\n\tfault {\n\t //Please enter functionblock fault configuration.\n\t}\n";
      content += "\n\toperations {\n\t //Please enter functionblock operations.\n\t}\n";
      content += "}";
      return content;
    }

    $scope.addInfoModelEditor = function(parentId, model) {
      var content = $scope.generateInfoModelContent(model);
>>>>>>> Taking namespace as input while creating a new file. Added auto generation of new file content at front end. Displaying loaded while new file content is being generated
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: "infomodel",
            syntaxDefinition: "xtext/mode-infomodel",
            serviceUrl: "/infomodel/xtext-service",
            enableFormattingAction: "true",
            showErrorDialogs: "true",
            parent: parentId
          });
          editor.setValue(content);
          $scope.editors.push(editor);
          $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
          $scope.search();
          var resourceId = $scope.selectedEditor.xtextServices.validationService._encodedResourceId;
          var tab = $scope.tabs[$scope.selectedTabIndex];
          tab['resourceId'] = resourceId;
          tab['name'] = model.name;
          tab['version'] = model.version;
          tab['namespace'] = model.namespace;
          $http.post('./project/' + $scope.projectName + '/resources/create', {
            "name": model.name,
            "resourceId": resourceId,
            "version": model.version,
            "namespace": model.namespace
          }).success(
            function(data, status, headers, config) {}).error(function(data, status, headers, config) {});
        });
      });
    }

    $scope.addFunctionBlockEditor = function(parentId, model) {
      var content = $scope.generateFunctionBlockContent(model);
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: "fbmodel",
            syntaxDefinition: "xtext/mode-fbmodel",
            serviceUrl: "/functionblock/xtext-service",
            enableFormattingAction: "true",
            showErrorDialogs: "true",
            parent: parentId
          });
          editor.setValue(content);
          $scope.editors.push(editor);
          $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
          $scope.search();
          var resourceId = $scope.selectedEditor.xtextServices.validationService._encodedResourceId;
          var tab = $scope.tabs[$scope.selectedTabIndex];
          tab['resourceId'] = resourceId;
          tab['name'] = model.name;
          tab['version'] = model.version;
          tab['namespace'] = model.namespace;          
          $http.post('./project/' + $scope.projectName + '/resources/create', {
            "name": model.name,
            "resourceId": resourceId,
            "version": model.version,
            "namespace": model.namespace
          }).success(
            function(data, status, headers, config) {}).error(function(data, status, headers, config) {});
        });
      });
    }

    $scope.getEditor = function(index) {
      var tab = $scope.tabs[index];
      var tabEditorId = '#' + tab['editorParentDivId'];
      return angular.element(document.querySelector(tabEditorId));
    }

    $scope.deleteTab = function(index) {
      $scope.getEditor(index).remove();
      $scope.tabs.splice(index, 1);
      $scope.editors.splice(index, 1);
    }

    $scope.selectTab = function(index) {
      $scope.selectedTabIndex = index;
      $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]['id']
      $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
      $scope.search();
    }

    $scope.openAddEditorModal = function() {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: 'AddEditorModalController',
        templateUrl: 'templates/add-editor-modal-template.html',
        size: 'sm',
        resolve: {
          editorTypes: function() {
            return $scope.editorTypes;
          }
        }
      });
<<<<<<< b6c9b094e2d16aa9068c156341ebfedd562b2548
>>>>>>> Created tabbed editor in devtool

      modalInstance.result.then(function(selectedItem) {
        $scope.selected = selectedItem;
      });
=======
>>>>>>> Created modal to describe model before creating editor
    };

    $scope.openDescribeEditorModal = function(editorType) {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: 'DescribeEditorModalController',
        templateUrl: 'templates/describe-editor-modal-template.html',
        size: 'sm',
        resolve: {
          editorType: editorType
        }
      });
    };

    $scope.openDeleteEditorModal = function(index) {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: 'DeleteEditorModalController',
        templateUrl: 'templates/delete-editor-modal-template.html',
        size: 'm',
        resolve: {
          tab: function() {
            var tab = $scope.tabs[$scope.selectedTabIndex]
            tab['index'] = index;
            return tab
          }
        }
      });
    };

    $scope.openCloseProjectModal = function() {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: 'CloseProjectModalController',
        templateUrl: 'templates/close-project-modal-template.html',
        size: 'sm'
      });
    };

    $scope.openOpenResourceModal = function() {
      var resources = [];
      $http.get('./project/' + $scope.projectName + '/resources').success(
        function(data, status, headers, config) {
          var modalInstance = $uibModal.open({
            animation: true,
            controller: 'OpenResourceModalController',
            templateUrl: 'templates/open-resource-modal-template.html',
            size: 'sm',
            resolve: {
              resources: function() {
                return data;
              }
            }
          });
        }).error(function(data, status, headers, config) {
        window.alert('Failed to open resource');
      });
    };

    $scope.isModelSelected = function() {
      for (i = 0; i < $scope.displayedModels.length; i++) {
        if ($scope.displayedModels[i]['isSelected']) {
          $scope.selectedModel = $scope.displayedModels[i];
          $scope.selectedModelId = $scope.selectedModel['id'];
          return true;
        }
      }
      $scope.selectedModel = null;
      $scope.selectedModelId = null;
      return false;
    }

    $scope.isValidEditorTab = function(index) {
      return $scope.isValidModel($scope.editors[index]);
    }

    $scope.isValidModel = function(editor) {
      if (editor == null) {
        return false;
      }
      if (editor.xtextServices.editorContext._annotations.length !== 0) {
        return false;
      } else {
        return true;
      }
    }

    $scope.importModel = function() {
      if ($scope.isValidModel($scope.selectedEditor)) {
        if ($scope.isModelSelected()) {
          $scope.showImportButton = false;
          if ($scope.tabs[$scope.selectedTabIndex]['language'] == 'infomodel') {
            $http.get('./editor/infomodel/link/functionblock/' + $scope.selectedEditor.xtextServices.validationService._encodedResourceId + '/' + $scope.selectedModelId['namespace'] + '/' + $scope.selectedModelId['name'] + '/' + $scope.selectedModelId['version']).success(
              function(data, status, headers, config) {
                $scope.selectedEditor.setValue(data);
              }).error(function(data, status, headers, config) {
              window.alert('Failed')
            }).finally(function() {
              $scope.showImportButton = true;
            });
          } else if ($scope.tabs[$scope.selectedTabIndex]['language'] == 'fbmodel') {
            $http.get('./editor/functionblock/link/datatype/' + $scope.selectedEditor.xtextServices.validationService._encodedResourceId + '/' + $scope.selectedModelId['namespace'] + '/' + $scope.selectedModelId['name'] + '/' + $scope.selectedModelId['version']).success(
              function(data, status, headers, config) {
                editor.setValue(data);
              }).error(function(data, status, headers, config) {
              window.alert('Failed')
            }).finally(function() {
              $scope.showImportButton = true;
            });
          }
        } else {
          window.alert('Please select a model to import');
        }
      } else {
        window.alert('Your Vorto Model contains errors. Please correct and try again.');
      }
    }

    $scope.searchOnEnter = function(keyEvent) {
      if (keyEvent.keyCode === 13) {
        $scope.search();
      }
    };

    $scope.search = function() {
      var filter = null;
      var modelType = null;
      if ($scope.tabs[$scope.selectedTabIndex]['language'] == 'infomodel') {
        modelType = "fbmodel";
        filter = $scope.queryFilter + " " + modelType;
        $http.get('./editor/infomodel/search=' + filter).success(
          function(data, status, headers, config) {
            $scope.models = data;
          }).error(function(data, status, headers, config) {
          $scope.models = [];
        });
      } else if ($scope.tabs[$scope.selectedTabIndex]['language'] == 'fbmodel') {
        modelType = "Datatype";
        filter = $scope.queryFilter + " " + modelType;
        $http.get('./editor/functionblock/search=' + filter).success(
          function(data, status, headers, config) {
            $scope.models = data;
          }).error(function(data, status, headers, config) {
          $scope.models = [];
        });
      }
    }

    $scope.displayedModels = [].concat($scope.models);
    $scope.itemsByPage = 6;
    $scope.displayedPages = ($scope.models.length / 2);

    $scope.getters = {
      name: function(value) {
        return value.id.name.sort();
      }
    }

    $scope.predicates = ['Name'];

    $scope.getResourceLanguage = function(resource) {
      return resource.resourceId.split('.')[1];
    }

  });

  app.controller('ProjectController', function($rootScope, $scope, $location, $http, $uibModal) {
    $scope.selectedProject = null;
    $scope.projects = [];

    $scope.$on("createProject", function(event, projectName) {
      $scope.createProject(projectName);
    });

    $scope.openCreateProjectModal = function() {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: 'CreateProjectModalController',
        templateUrl: 'templates/create-project-modal-template.html',
        size: 'sm'
      });
    };

    $scope.createProject = function(projectName) {
      $http.post('./project', {
        'projectName': projectName
      }).success(
        function(data, status, headers, config) {
          $location.path('editor/' + projectName);
          $location.replace();
        }).error(function(data, status, headers, config) {
        window.alert('Failed to create new Project ' + projectName)
      });
    }

    $scope.getProjects = function() {
      $http.get('./project').success(
        function(data, status, headers, config) {
          $scope.projects = data;
        }).error(function(data, status, headers, config) {
        $scope.projects = [];
      });
    }

    $scope.getProjects();

    $scope.displayedProjects = [].concat($scope.projects);
    $scope.itemsByPage = 10;
    $scope.displayedPages = ($scope.projects.length / 2);

    $scope.predicates = ['Name']

    $scope.getters = {
      name: function(value) {
        return value.projectName.sort();
      }
    }

  });

  app.controller('AddEditorModalController', function($rootScope, $scope, $uibModalInstance, editorTypes) {
    $scope.editorTypes = editorTypes;
    $scope.selected = $scope.editorTypes[0];

    $scope.ok = function() {
      $uibModalInstance.close($scope.selected.editorType);
      $rootScope.$broadcast("describeEditor", $scope.selected);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  });

  app.controller('DescribeEditorModalController', function($rootScope, $scope, $uibModalInstance, editorType) {

    $scope.editorType = editorType;
    $scope.model = {
      language: editorType.language,
      name: "NewModel",
      displayname: "NewModel",
      namespace: "com.company",
      version: "1.0.0",
      description: "Model description for NewModel",
      category: "demo"
    };

    $scope.ok = function() {
      $uibModalInstance.close($scope.model);
      $scope.model.displayname = $scope.model.name;
      $scope.model.name = $scope.model.name + "." + $scope.model.language;
      $rootScope.$broadcast("addTab", $scope.model);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  });

  app.controller('DeleteEditorModalController', function($rootScope, $scope, $uibModalInstance, tab) {
    $scope.tab = tab;

    $scope.ok = function() {
      $uibModalInstance.dismiss('cancel');
      $rootScope.$broadcast("deleteEditor", tab);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  });

  app.controller('CreateProjectModalController', function($rootScope, $scope, $uibModalInstance) {

    $scope.projectName;

    $scope.ok = function() {
      $uibModalInstance.close($scope.model);
      $rootScope.$broadcast("createProject", $scope.projectName);
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  });

  app.controller('CloseProjectModalController', function($rootScope, $scope, $uibModalInstance) {
    $scope.ok = function() {
      $rootScope.$broadcast("closeProject");
      $uibModalInstance.dismiss('cancel');
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };
  });

  return app;
});
