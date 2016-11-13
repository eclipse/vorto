define(["angular"], function(angular) {
  var app = angular.module('apps.controller', ['smart-table', 'apps.directive']);

  app.controller('LoginController', ['$scope', '$rootScope', '$location', '$http',

    function($scope, $rootScope, $location, $http) {

      var authenticate = function(credentials, callback) {
        $http.get('rest/context/user').success(function(data) {
          if (data.name) {
            $rootScope.authenticated = true;
            $rootScope.user = data.name;
            $rootScope.error = false;
            $location.path("/project");
          } else {
            $rootScope.authenticated = false;
          }
          callback && callback();
        }).error(function(data) {
          $rootScope.authenticated = false;
          callback && callback();
        });
      }

      authenticate();
      $scope.credentials = {};
      $scope.login = function() {
        $http.post('/j_spring_security_check', $.param($scope.credentials), {
          headers: {
            "content-type": "application/x-www-form-urlencoded"
          }
        }).success(function(data) {
          authenticate(function() {
            if ($rootScope.authenticated) {
              $scope.error = false;
              console.log("authenticated, redirecting to project overview");
              $location.path("project");
            } else {
              $scope.error = true;
            }
          });
        }).error(function(data) {
          $scope.error = true;
          $rootScope.authenticated = false;
        })
      };

    }
  ]);

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
    
    $scope.rootParentId = "root";

    $scope.editorTypes = [{
      language: 'infomodel',
      display: 'Info Model',
      displayname: 'InformationModel',
      desc: 'NewInfoModel'
    }, {
      language: 'fbmodel',
      display: 'Function Block',
      modelType: 'Functionblock',
      displayname: 'NewFunctionblock'
    }, {
      language: 'type',
      display: 'Entity',
      subType: 'entity',
      modelType: 'Datatype',
      displayname: 'NewEntity'      
    }, {
      language: 'type',
      display: 'Enum',
      subType: 'enum',
      modelType: 'Datatype',
      displayname: 'NewEnum'      
    }];
    $scope.models = [];
    $scope.queryFilter = "";

    $scope.editorConfig = {
      infomodel: {
        syntaxDefinition: "xtext/mode-infomodel",
        serviceUrl: "/infomodel/xtext-service",
        searchUrl: "./rest/editor/infomodel/search=",
        importModelUrl: "./rest/editor/infomodel/link/"
      },
      fbmodel: {
        syntaxDefinition: "xtext/mode-fbmodel",
        serviceUrl: "/functionblock/xtext-service",
        searchUrl: "./rest/editor/functionblock/search=",
        importModelUrl: "./rest/editor/functionblock/link/"
      },
      type: {
        syntaxDefinition: "xtext/mode-type",
        serviceUrl: "/datatype/xtext-service",
        searchUrl: "./rest/editor/datatype/search=",
        importModelUrl: "./rest/editor/datatype/link/"
      }
    }

    $scope.projectResources = [];

    $scope.contextMenu = function customMenu(node){
        var items = {
                openResource: {
                	label: "Open",
                	action: function (){
                		var index = $scope.getTabIndex(node.original);
                		if(index < 0){
                			$scope.openEditor(node.original);                			
                		}else{
                			$scope.selectTab(index);
                		}
                	}
                },        		
                deleteResource: {
                    label: "Delete",
                    action: function (){
                    	$scope.openDeleteEditorModal(node.original);
                    }
                },
                addResource: {
                	label: "New",
                	action: function (){
                		$scope.openAddEditorModal();
                	}
                }
            };
        if(node.type === 'project'){
        	delete items.deleteResource;        	
        	delete items.openResource;        	        	
        }else{
        	delete items.addResource;
        }
        return items;
    }

    $scope.treeConfig = {
      core: {
        multiple: false,
        animation: true,
        check_callback: true,
        worker: true
      },
      types: {
    	project:{
    		
    	},
        infomodel: {
          icon: '/images/im.png'
        },
        fbmodel: {
          icon: "/images/fb.png"
        },
        enum: {
          icon: "/images/enu.png"
        },
        entity: {
            icon: "/images/ent.png"
          }
      },
      contextmenu: {
    	  items: $scope.contextMenu
      },    	        
      version: 1,
      plugins: ['types', 'contextmenu', 'wholerow', 'changed']
    };

      
    $scope.openProject = function() {
      $scope.projectName = $routeParams.projectName;
      $http.get('./rest/project/' + $scope.projectName + '/open').success(
        function(data, status, headers, config) {
          $scope.showEditorBody = true;
          $scope.getResources();
        }).error(function(data, status, headers, config) {
        $scope.showEditorBody = false;
      });
    }

    $scope.openProject();

    $scope.uploadProject = function() {
      var resources = [];
      $http.get('./rest/project/' + $scope.projectName + '/resources').success(
        function(data, status, headers, config) {
          for (i = 0; i < data.length; i++) {
            resources.push(data[i]);
          }
          $http.post('./rest/publish/upload', {
            "projectResourceList": resources
          }).success(
            function(data, status, headers, config) {
              $scope.openPublishModal(data);
            }).error(function(data, status, headers, config) {
            window.alert("Failed to upload resources")
          });
        }).error(function(data, status, headers, config) {
        window.alert("Failed to upload resources")
      });
    }

    $scope.openPublishModal = function(result) {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: 'PublishController',
        templateUrl: 'templates/publish-template.html',
        size: 'lg',
        resolve: {
          result: function() {
            return result;
          }
        }
      });
    };

    $scope.getResources = function() {
      $http.get('./rest/project/' + $scope.projectName + '/resources').success(
        function(data, status, headers, config) {
          var resources = [];
          var rootNode = {id: $scope.rootParentId, parent: "#", text: $scope.projectName, state: {opened: true}, type: "project"};
          resources.push(rootNode);
          for (i = 0; i < data.length; i++) {
            data[i]['language'] = $scope.getResourceLanguage(data[i]);
            if(data[i]['subType'] != null){
                data[i]['type'] = data[i]['subType'];            	
            }else{
                data[i]['type'] = data[i]['language'];            	
            }
            data[i]['parent'] = $scope.rootParentId;
            data[i]['text'] = data[i]['name'];
            data[i]['filename'] = data[i]['name'] + '.' + data[i]['language'];;
            $scope.openEditor(data[i]);
            resources.push(data[i]);
          }
          $scope.openResourceTree(resources);
        }).error(function(data, status, headers, config) {

      });
    }

    $scope.openResourceTree = function(resources) {
    	$scope.projectResources = [];
    	for(var i = 0 ; i < resources.length ; i++){
    		$scope.projectResources.push(resources[i]);
    	}
    	$scope.treeConfig.version++;
    }
    
    $scope.addResource = function(resource) {
		$scope.projectResources.push(resource);    	
    	$scope.treeConfig.version++;
		console.log(resource);    	
    }
    
    $scope.removeResource = function(resource) {
    	$scope.treeConfig.version++;
    	for(var i = 0 ; i < $scope.projectResources.length ; i++){
    		var temp = $scope.projectResources[i];
    		if(temp.name === resource.name && temp.namespace === resource.namespace && temp.version === resource.version){
    		    $scope.projectResources.splice(i, 1);
    			$scope.treeConfig.version++;    
    			return
    		}    		
    	}
    }
    
    $scope.$on("addTab", function(event, model) {
      $scope.addEditor(model);
    });

    $scope.$on("describeEditor", function(event, editor) {
      $scope.openDescribeEditorModal(editor);
    });

    $scope.$on("deleteEditor", function(event, tab) {
      var url = './rest/project/' + $scope.projectName + '/resources/delete/' + tab.namespace + '/' + tab.name + '/' + tab.version;
      $http.get(url).success(
        function(data, status, headers, config) {
          $scope.closeTab(tab.index);
          $scope.removeResource(tab);
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
      var tab = resource;
      tab.id = tabId;
      tab.editorParentDivId = editorParentDivId;
      tab.editorDivId = editorDivId;
      $scope.tabs.push(tab);
      $scope.selectedTabIndex = $scope.tabs.length - 1;
      $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]['id'];
      var element = angular.element(document).find('#editors');
      element.append('<div id="' + editorParentDivId + '" ng-show="selectedTabId==' + tabId + '"><div id="' + editorDivId + '" class="custom-xtext-editor"></div></div>');
      $compile(element.contents())($scope);
      $scope.openXtextEditor(tab);
    }

    $scope.openXtextEditor = function(tab) {
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: tab.language,
            syntaxDefinition: $scope.editorConfig[tab.language].syntaxDefinition,
            serviceUrl: $scope.editorConfig[tab.language].serviceUrl,
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
      $http.get('./rest/project/' + $scope.projectName + '/resources/check/' + model.namespace + '/' + model.name + '/' + model.version).success(
        function(data, status, headers, config) {
          $scope.counter++;
          var tabId = $scope.counter;
          var editorParentDivId = "xtext-editor-parent-" + tabId;
          var editorDivId = "xtext-editor-" + tabId;
          var tab = model;
          tab.id = tabId;
          tab.editorParentDivId = editorParentDivId;
          tab.editorDivId = editorDivId;
          if(tab.subType != null){
        	  tab.type = tab.subType;
          }else{
        	  tab.type = tab.language;
          }
          $scope.tabs.push(tab);
          $scope.selectedTabIndex = $scope.tabs.length - 1;
          $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]['id'];
          var element = angular.element(document).find('#editors');
          element.append('<div id="' + editorParentDivId + '" ng-show="selectedTabId==' + tabId + '"><div id="' + editorDivId + '" class="custom-xtext-editor"></div></div>');
          $compile(element.contents())($scope);
          $scope.addXtextEditor(editorDivId, model);
        }).error(function(data, status, headers, config) {
        window.alert('File already exists')
      });
    }

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

    $scope.generateDataTypeContent = function(model) {
      var content = 'namespace ' + model.namespace + '\nversion ' + model.version + '\ndisplayname "' + model.displayname + '"\ndescription "' + model.description + '"\ncategory demo';
      content += "\n" + model.subType + " " + model.displayname + " {\n";
      content += "}\n";
      return content;
    }

    $scope.addXtextEditor = function(parentId, model) {
      var content;
      if (model.language == 'infomodel') {
        content = $scope.generateInfoModelContent(model);
      } else if (model.language == 'fbmodel') {
        content = $scope.generateFunctionBlockContent(model);
      } else if (model.language == 'type') {
        content = $scope.generateDataTypeContent(model);
      }

      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            enableFormattingAction: "true",
            showErrorDialogs: "true",
            parent: parentId,
            xtextLang: model.language,
            syntaxDefinition: $scope.editorConfig[model.language].syntaxDefinition,
            serviceUrl: $scope.editorConfig[model.language].serviceUrl
          });
          editor.setValue(content);
          $scope.editors.push(editor);
          $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
          $scope.search();
          var resourceId = $scope.selectedEditor.xtextServices.validationService._encodedResourceId;
          var tab = $scope.tabs[$scope.selectedTabIndex];
          tab['resourceId'] = resourceId;
          tab['filename'] = model.filename;
          tab['text'] = model.name;
          tab['parent'] = $scope.rootParentId; 
          $http.post('./rest/project/' + $scope.projectName + '/resources', {
            "name": model.name,
            "filename": model.filename,
            "resourceId": resourceId,
            "version": model.version,
            "namespace": model.namespace,
            "subType": model.subType,
            "modelType": model.modelType
          }).success(
            function(data, status, headers, config) {
            	$scope.addResource(tab);
            	console.log($scope.projectResources);
            }).error(function(data, status, headers, config) {
            	window.alert('Failed to create file')
          });
        });
      });
    }

    $scope.getEditor = function(index) {
      var tab = $scope.tabs[index];
      var tabEditorId = '#' + tab['editorParentDivId'];
      return angular.element(document.querySelector(tabEditorId));
    }

    $scope.closeTab = function(index) {
      $scope.getEditor(index).remove();
      $scope.tabs.splice(index, 1);
      $scope.editors.splice(index, 1);
      if($scope.editors.length > 0){
    	  $scope.selectTab($scope.editors.length - 1);
      }
    }

    $scope.selectTab = function(index) {
      $scope.selectedTabIndex = index;
      $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]['id']
      $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
      $scope.search();
    }

    $scope.getTabIndex = function(tab) {
    	for(var i = 0 ; i < $scope.tabs.length ; i++){
    		var temp = $scope.tabs[i];
    		if(temp.name === tab.name && temp.namespace === tab.namespace && temp.version === tab.version){
    			return i;
    		}    		
    	}
    	return -1;
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

    $scope.openDeleteEditorModal = function(tab) {
      var modalInstance = $uibModal.open({
        animation: true,
        controller: 'DeleteEditorModalController',
        templateUrl: 'templates/delete-editor-modal-template.html',
        size: 'm',
        resolve: {
          tab: function() {
        	var index = $scope.getTabIndex(tab);
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
      $http.get('./rest/project/' + $scope.projectName + '/resources').success(
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
          var language = $scope.tabs[$scope.selectedTabIndex]['language'];
          $http.get($scope.editorConfig[language].importModelUrl + $scope.selectedEditor.xtextServices.validationService._encodedResourceId + '/' + $scope.selectedModelId['namespace'] + '/' + $scope.selectedModelId['name'] + '/' + $scope.selectedModelId['version']).success(
            function(data, status, headers, config) {
              $scope.selectedEditor.setValue(data);
            }).error(function(data, status, headers, config) {
            window.alert('Failed')
          }).finally(function() {
            $scope.showImportButton = true;
          });
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
      filter = $scope.queryFilter;
      var language = $scope.tabs[$scope.selectedTabIndex]['language'];
      if($scope.tabs[$scope.selectedTabIndex]['subType'] === 'enum'){
          $scope.models = [];
          return;
      }
      $http.get($scope.editorConfig[language].searchUrl + filter).success(
        function(data, status, headers, config) {
          $scope.models = data;
        }).error(function(data, status, headers, config) {
        $scope.models = [];
      });
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
      $http.post('./rest/project', {
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
      $http.get('./rest/project').success(
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
      name: editorType.displayname,
      displayname: editorType.displayname,
      namespace: "com.company",
      version: "1.0.0",
      description: "Model description for " + editorType.displayname,
      category: "demo"
    };

    $scope.ok = function() {
      $uibModalInstance.close($scope.model);
      $scope.model.displayname = $scope.model.name;
      $scope.model.name = $scope.model.name;
      $scope.model.filename = $scope.model.name + "." + $scope.model.language;
      $scope.model.subType = $scope.editorType.subType;
      $scope.model.modelType = $scope.editorType.modelType;
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

  app.controller('PublishController', function($rootScope, $scope, $uibModalInstance, $http, result) {
    $scope.modelCheckedIn = false;
    $scope.result = result;
    $scope.modelStats = {};
    var infocount = 0,
      fbcount = 0,
      typecount = 0,
      mappingcount = 0;
    $scope.showResultBox = true;

    $scope.displayValidation = function(result) {
      $scope.stateArr = [];
      $scope.uploadResult = result;
      $scope.showCheckin = true;

      if ($scope.uploadResult.obj != null && $scope.uploadResult.obj.length > 0) {
        angular.forEach($scope.uploadResult.obj, function(resultObject, idx) {
          var item = (idx == 0) ? {
            active: false
          } : {
            active: true
          };
          var modelType = resultObject.modelResource.modelType;
          switch (modelType) {
            case "Functionblock":
              fbcount++;
              break;
            case "InformationModel":
              infocount++;
              break;
            case "Datatype":
              typecount++;
              break;
            case "Mapping":
              mappingcount++;
              break;
          }
          $scope.stateArr.push(item);
          $scope.showCheckin = (resultObject.valid && $scope.showCheckin);
        });
      } else {
        $scope.showCheckin = false;
      }

      $scope.modelStats = {
        infocount: infocount,
        fbcount: fbcount,
        typecount: typecount,
        mappingcount: mappingcount
      };
      $scope.isLoading = false;
      $scope.showResultBox = true;
      $scope.resultMessage = result.message;
    }

    $scope.displayValidation($scope.result);

    $scope.checkin = function(uploadResults) {
      $rootScope.error = "";
      if (uploadResults.length == 1) {
        checkinSingle(uploadResults[0].handleId);
      } else {
        checkInMultipleModels(uploadResults);
      }
    };

    checkInMultipleModels = function(uploadResults) {
      var validUploadHandles = [];
      $scope.isLoading = true;
      $scope.loadMessage = "Checking in... Please wait!";
      $scope.showResultBox = false;
      angular.forEach(uploadResults, function(uploadResult, idx) {
        if (uploadResult.valid) {
          var handle = {
            handleId: uploadResult.handleId,
            id: {
              name: uploadResult.modelResource.id.name,
              namespace: uploadResult.modelResource.id.namespace,
              version: uploadResult.modelResource.id.version
            }
          }
          validUploadHandles.push(handle);
        }
      });

      $http.put('./rest/publish/checkInMultiple', validUploadHandles)
        .success(function(result) {
          $scope.isLoading = false;
          $scope.showResultBox = true;
          $scope.resultMessage = result.message;
          $scope.modelCheckedIn = true;
        }).error(function(data, status, headers, config) {
          $scope.isLoading = false;
          if (status == 403) {
            $rootScope.error = "Operation is Forbidden";
          } else if (status == 401) {
            $rootScope.error = "Unauthorized Operation";
          } else if (status == 400) {
            $rootScope.error = "Bad Request. Server Down";
          } else if (status == 500) {
            $rootScope.error = "Internal Server Error";
          } else {
            $rootScope.error = "Failed Request with response status " + status;
          }
        });
    };

    checkinSingle = function(handleId) {
      $http.put('./rest/publish/' + handleId)
        .success(function(result) {
          $scope.showResultBox = true;
          $scope.resultMessage = result.message;
          $scope.modelCheckedIn = true;
        }).error(function(data, status, headers, config) {
          if (status == 403) {
            $rootScope.error = "Operation is Forbidden";
          } else if (status == 401) {
            $rootScope.error = "Unauthorized Operation";
          } else if (status == 400) {
            $rootScope.error = "Bad Request. Server Down";
          } else if (status == 500) {
            $rootScope.error = "Internal Server Error";
          } else {
            $rootScope.error = "Failed Request with response status " + status;
          }
        });
    };

    $scope.cancel = function() {
      $uibModalInstance.dismiss('cancel');
    };

  });

  return app;
});
