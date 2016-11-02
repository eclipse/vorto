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
    }, {
      language: 'type',
      display: 'Entity',
      subType: 'entity'
    }, {
      language: 'type',
      display: 'Enum',
      subType: 'enum'
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

    $scope.uploadProject = function() {
      var resources = [];
      $http.get('./project/' + $scope.projectName + '/resources').success(
		  function(data, status, headers, config) {
          for (i = 0; i < data.length; i++) {
            resources.push(data[i]);
          }
          $http.post('./publish/upload', {
            "projectResourceList": resources
          }).success(
            function(data, status, headers, config) {
              console.log(data);
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
      var url = './project/' + $scope.projectName + '/resources/delete/' + tab.namespace + '/' + tab.name + '/' + tab.version;
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
        resourceId: resource.resourceId,
        subType: resource.subType
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
      } else if (language == 'type') {
        $scope.openEntityEditor(tab);
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

    $scope.openEntityEditor = function(tab) {
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: "type",
            syntaxDefinition: "xtext/mode-type",
            serviceUrl: "/datatype/xtext-service",
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
      $http.get('./project/' + $scope.projectName + '/resources/check/' + model.namespace + '/' + model.name + '/' + model.version).success(
        function(data, status, headers, config) {
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
            subType: model.subType
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
          } else if (model.language == 'type') {
            if (model.subType == 'entity') {
              $scope.addEntityEditor(editorDivId, model);
            } else if (model.subType == 'enum') {
              $scope.addEnumEditor(editorDivId, model);
            }
          }
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

    $scope.generateEntityContent = function(model) {
      var content = 'namespace ' + model.namespace + '\nversion ' + model.version + '\ndisplayname "' + model.displayname + '"\ndescription "' + model.description + '"\ncategory demo';
      content += "\nentity " + model.displayname + " {\n";
      content += "}\n";
      return content;
    }

    $scope.generateEnumContent = function(model) {
      var content = 'namespace ' + model.namespace + '\nversion ' + model.version + '\ndisplayname "' + model.displayname + '"\ndescription "' + model.description + '"\ncategory demo';
      content += "\nenum " + model.displayname + " {\n";
      content += "}\n";
      return content;
    }

    $scope.addInfoModelEditor = function(parentId, model) {
      var content = $scope.generateInfoModelContent(model);
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
          $http.post('./project/' + $scope.projectName + '/resources', {
            "name": model.name,
            "resourceId": resourceId,
            "version": model.version,
            "namespace": model.namespace,
            "subType": model.subType
          }).success(
            function(data, status, headers, config) {}).error(function(data, status, headers, config) {
            window.alert('File already exists')
          });
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
          $http.post('./project/' + $scope.projectName + '/resources', {
            "name": model.name,
            "resourceId": resourceId,
            "version": model.version,
            "namespace": model.namespace,
            "subType": model.subType
          }).success(
            function(data, status, headers, config) {}).error(function(data, status, headers, config) {});
        });
      });
    }

    $scope.addEntityEditor = function(parentId, model) {
      var content = $scope.generateEntityContent(model);
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: "type",
            syntaxDefinition: "xtext/mode-type",
            serviceUrl: "/datatype/xtext-service",
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
          $http.post('./project/' + $scope.projectName + '/resources', {
            "name": model.name,
            "resourceId": resourceId,
            "version": model.version,
            "namespace": model.namespace,
            "subType": model.subType
          }).success(
            function(data, status, headers, config) {}).error(function(data, status, headers, config) {});
        });
      });
    }

    $scope.addEnumEditor = function(parentId, model) {
      var content = $scope.generateEnumContent(model);
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: "type",
            syntaxDefinition: "xtext/mode-type",
            serviceUrl: "/datatype/xtext-service",
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
          $http.post('./project/' + $scope.projectName + '/resources', {
            "name": model.name,
            "resourceId": resourceId,
            "version": model.version,
            "namespace": model.namespace,
            "subType": model.subType
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
          } else if ($scope.tabs[$scope.selectedTabIndex]['language'] == 'type') {
            $http.get('./editor/datatype/link/datatype/' + $scope.selectedEditor.xtextServices.validationService._encodedResourceId + '/' + $scope.selectedModelId['namespace'] + '/' + $scope.selectedModelId['name'] + '/' + $scope.selectedModelId['version']).success(
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
      filter = $scope.queryFilter;
      if ($scope.tabs[$scope.selectedTabIndex]['language'] == 'infomodel') {
        $http.get('./editor/infomodel/search=' + filter).success(
          function(data, status, headers, config) {
            $scope.models = data;
          }).error(function(data, status, headers, config) {
          $scope.models = [];
        });
      } else if ($scope.tabs[$scope.selectedTabIndex]['language'] == 'fbmodel') {
        $http.get('./editor/functionblock/search=' + filter).success(
          function(data, status, headers, config) {
            $scope.models = data;
          }).error(function(data, status, headers, config) {
          $scope.models = [];
        });
      } else if ($scope.tabs[$scope.selectedTabIndex]['language'] == 'type') {
        if ($scope.tabs[$scope.selectedTabIndex]['subType'] == 'entity') {
          $http.get('./editor/datatype/search=' + filter).success(
            function(data, status, headers, config) {
              $scope.models = data;
            }).error(function(data, status, headers, config) {
            $scope.models = [];
          });
        } else {
          $scope.models = [];
        }
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
      $scope.model.subType = $scope.editorType.subType;
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

  app.controller('PublishController', function( $rootScope, $scope, $uibModalInstance, $http, result) {
	  $scope.modelCheckedIn = false;
	  $scope.result = result;
      $scope.modelStats = {};
      var infocount = 0, fbcount  = 0, typecount = 0, mappingcount = 0;	  
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
	      console.log(uploadResults);
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
	      
	      $http.put('./publish/checkInMultiple', validUploadHandles)
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
	      $http.put('./publish/' + handleId)
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
