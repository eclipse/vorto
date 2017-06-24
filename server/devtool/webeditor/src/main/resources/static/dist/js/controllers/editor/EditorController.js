define(["../init/AppController"], function(controllers) {
  controllers.controller("EditorController", EditorController);

  EditorController.$inject = [
    "$q", "$rootScope", "$scope", "$location", "$window", "$routeParams", "$compile",
    "$uibModal", "toastr", "ShareDataService", "ProjectDataService",
    "EditorDataService", "PublishDataService", "ToastrService"
  ]

  function EditorController($q, $rootScope, $scope, $location, $window, $routeParams, $compile, $uibModal, toastr, ShareDataService, ProjectDataService, EditorDataService, PublishDataService, ToastrService) {
    $scope.error = null;
    $scope.errorMessage = null;
    $scope.validationError = false;
    $scope.selectedModel = null;
    $scope.selectedModelId = null;
    $scope.showAddFunctionBlockButton = true;
    $scope.isValidationInProcess = false;

    $scope.tabs = [];
    $scope.editors = [];
    $scope.counter = 0;
    $scope.selectedTabIndex = 0;
    $scope.selectedTabId = 0;
    $scope.selectedEditor = null;
    $scope.references = [];

    $scope.showImportButton = true;
    $scope.showSearchButton = true;
    $scope.isEditorOpen = false;

    $scope.showEditorBody = true;
    $scope.projectName;

    $scope.rootParentId = "root";

    $scope.valid = "valid";
    $scope.invalid = "invalid";

    $scope.editorTypes = [{
      language: "infomodel",
      display: "Info Model",
      modelType: "InformationModel",
      modelSubType: "",
      displayname: "NewInfoModel"
    }, {
      language: "fbmodel",
      display: "Function Block",
      modelType: "Functionblock",
      modelSubType: "",
      displayname: "NewFunctionblock"
    }, {
      language: "type",
      display: "Entity",
      modelSubType: "entity",
      modelType: "Datatype",
      displayname: "NewEntity"
    }, {
      language: "type",
      display: "Enum",
      modelSubType: "enum",
      modelType: "Datatype",
      displayname: "NewEnum"
    }];
    $scope.models = [];
    $scope.queryFilter = "";

    $scope.editorConfig = {
      infomodel: {
        syntaxDefinition: "xtext/mode-infomodel",
        serviceUrl: "infomodel/xtext-service"
      },
      fbmodel: {
        syntaxDefinition: "xtext/mode-fbmodel",
        serviceUrl: "functionblock/xtext-service"
      },
      type: {
        syntaxDefinition: "xtext/mode-type",
        serviceUrl: "datatype/xtext-service"
      }
    }

    $scope.projectResources = [];

    $scope.EditorHints = [
      "Hint: Press Ctrl + Space for Content Assist",
      "Hint: Press Cmd + S / Ctrl + S to Save",
      "Hint: Press Cmd + Shift + F / Ctrl + Shift + F to Auto format",
      "Hint: Right Click on the models in the File Explorer for more options"
    ];

    $scope.getRandomHint = function() {
      var index = Math.floor(Math.random() * $scope.EditorHints.length);
      return $scope.EditorHints[index];
    }

    $scope.editorHint = $scope.getRandomHint();

    $scope.closeHint = function() {
      $scope.editorHint = null;
    };

    $scope.contextMenu = function customMenu(node) {
      var items = {
        openResource: {
          label: "Open",
          action: function() {
            var index = $scope.getTabIndex(node.original);
            if (index < 0) {
              $scope.openEditor(node.original);
            } else {
              $scope.selectTab(index);
              $scope.$apply();
            }
          }
        },
        deleteResource: {
          label: "Delete",
          action: function() {
            var tab = node.original;
            tab.index = $scope.getTabIndex(tab);
            $scope.openDeleteEditorModal(tab);
          }
        },
        addResource: {
          label: "New Model",
          action: function() {
            $scope.openAddEditorModal();
          }
        }
      };
      if (node.type === "project") {
        delete items.deleteResource;
        delete items.openResource;
      } else {
        delete items.addResource;
      }
      return items;
    }

    $scope.treeConfig = {
      core: {
        multiple: false,
        animation: true,
        worker: true,
        check_callback: function(op, node, par, pos, more) {
          if (!$scope.isEditorOpen) {
            return false;
          }
          if (op === "move_node" || op === "copy_node") {
            var parentId = parseInt(par.id);
            var temp = {};
            for (var i = 0; i < $scope.projectResources.length; i++) {
              if ($scope.projectResources[i]["id"] === parentId) {
                temp = $scope.projectResources[i];
              }
            }
            var parent = $scope.tabs[$scope.selectedTabIndex];
            if (parent.name != temp.name || parent.version != temp.version || parent.namespace != temp.namespace) {
              return false;
            }
            if (node.type == "infomodel") {
              return false;
            } else if (node.type === "fbmodel" && (par.type === "infomodel" || par.type === "fbmodel")) {
              more.origin.settings.dnd.always_copy = true;
              return true;
            } else if (node.type === "entity" && par.type === "fbmodel") {
              more.origin.settings.dnd.always_copy = true;
              return true;
            } else if (node.type === "enum" && (par.type === "fbmodel" || par.type === "entity")) {
              more.origin.settings.dnd.always_copy = true;
              return true;
            } else {
              return false;
            }
          }
          return true;
        }
      },
      types: {
        project: {
          valid_children: ["infomodel", "fbmodel", "enum", "entity"]
        },
        infomodel: {
          icon: "images/im.png",
          valid_children: ["fbmodel"]
        },
        fbmodel: {
          icon: "images/fb.png",
          valid_children: ["fbmodel", "enum", "entity"]
        },
        enum: {
          icon: "images/enu.png",
          valid_children: []
        },
        entity: {
          icon: "images/ent.png",
          valid_children: ["enum"]
        }
      },
      contextmenu: {
        items: $scope.contextMenu
      },
      version: 1,
      plugins: ["types", "contextmenu", "wholerow", "changed", "dnd"]
    };

    $scope.copyNodeCallback = function(op, node) {
      $scope.openResourceTree($scope.projectResources);
      $scope.$apply();
      var arr = $scope.projectResources.filter(function(resource) {
        return resource["id"] == node["original"]["id"];
      });
      var referenceResourceId = arr[0]["resourceId"];
      arr = $scope.projectResources.filter(function(resource) {
        return resource["id"] == node["parent"];
      });
      var targetResourceId = arr[0]["resourceId"];
      $scope.referenceResource(targetResourceId, referenceResourceId);
    };

    $scope.openProject = function() {
      $scope.projectName = $routeParams.projectName;
      var params = {projectName: $scope.projectName};
      ProjectDataService.openProject(params).then(function(data){
        $scope.showEditorBody = true;
        $scope.getResources();
      }).catch(function(error){
        $scope.showEditorBody = false;
      });
    }

    $scope.openProject();

    $scope.uploadProject = function() {
      var unsavedFiles = $scope.getUnsavedFiles();
      if (unsavedFiles.length < 1) {
        $scope.isValidationInProcess = true;
        var params = {projectName: $scope.projectName};
        PublishDataService.validateProject(params).then(function(data){
          $scope.isValidationInProcess = false;
          $scope.openPublishModal(data);
        }).catch(function(error){
          $scope.isValidationInProcess = false;
          var message = "Failed to upload resources";
          var params = {message: message};
          ToastrService.createErrorToast(params);
        });
      } else {
        ShareDataService.setUnsavedFiles(unsavedFiles);
        $uibModal.open({
          animation: true,
          controller: "UnsavedEditorFilesController",
          templateUrl: "templates/editor/unsaved-files-modal-template.html",
          //size: "sm"
        });
      }
    }

    $scope.openPublishModal = function(result) {
      ShareDataService.setPublishResult(result);
      $uibModal.open({
        animation: true,
        controller: "PublishController",
        templateUrl: "templates/publish/publish-template.html",
        size: "lg"
      });
    };

    $scope.getResources = function() {
      var params = {projectName: $scope.projectName};
      ProjectDataService.getProjectResources(params).then(function(data){
        var resources = [];
        var rootNode = {
          id: $scope.rootParentId,
          parent: "#",
          text: $scope.projectName,
          state: {
            opened: true
          },
          type: "project"
        };
        var temp = {};
        resources.push(rootNode);
        for (i = 0; i < data.length; i++) {
          var resource = $scope.getResourceObject(data[i]);
          $scope.loadResource(resource);
          resources.push(resource);
        }
        $scope.openResourceTree(resources);
      });
    }

    $scope.getResourceObject = function(data) {
      var resource = {};
      resource["language"] = $scope.getResourceLanguage(data["name"]);
      var properties = data["properties"];
      if (properties["modelSubType"] != "") {
        resource["type"] = properties["modelSubType"];
      } else {
        resource["type"] = resource["language"];
      }
      resource["modelType"] = properties["modelType"];
      resource["modelSubType"] = properties["modelSubType"];
      resource["parent"] = $scope.rootParentId;
      resource["resourceId"] = properties["resourceId"];
      resource["name"] = properties["name"];
      resource["namespace"] = properties["namespace"];
      resource["version"] = properties["version"];
      resource["text"] = properties["name"];
      resource["filename"] = data["name"];
      return resource;
    }

    $scope.openResourceTree = function(resources) {
      $scope.projectResources = [];
      for (var i = 0; i < resources.length; i++) {
        $scope.projectResources.push(resources[i]);
      }
      $scope.treeConfig.version++;
    }

    $scope.addResource = function(resource) {
      $scope.projectResources.push(resource);
      $scope.treeConfig.version++;
    }

    $scope.removeResource = function(resource) {
      $scope.treeConfig.version++;
      for (var i = 0; i < $scope.projectResources.length; i++) {
        var temp = $scope.projectResources[i];
        if (temp.name === resource.name && temp.namespace === resource.namespace && temp.version === resource.version) {
          $scope.projectResources.splice(i, 1);
          $scope.treeConfig.version++;
          return;
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
      var params = {projectName: $scope.projectName, resourceId: tab.resourceId};
      ProjectDataService.deleteProjectResource(params).then(function(data){
        if (tab.index > -1) {
          $scope.closeTab(tab.index);
        }
        $scope.removeResource(tab);
        $scope.editors.forEach(function(editor){
          var content = editor.getValue();
          editor.setValue(content);
        });
      }).catch(function(error){
        var message = "Unable to delete file";
        var params = {message: message};
        ToastrService.createErrorToast(params);
      });
    });

    $scope.$on("closeProject", function(event, save) {
      $scope.editors.forEach(function(editor){
        if(!save){
          editor.setValue(editor.lastSavedValue);
        }
        editor.xtextServices.saveResource();
      });
      $scope.closeProject();
    });

    $window.onbeforeunload = function() {
      var unsavedFiles = $scope.getUnsavedFiles();
      if(unsavedFiles.length > 0){
        return "Unsaved changes";
      }
    }

    $scope.$on("closeEditor", function(event, index, save) {
      var editor = $scope.editors[index];
      if (save) {
        editor.xtextServices.saveResource();
        var tab = $scope.tabs[index];
        var message = "Resource " + tab.filename + " saved";
        var params = {message: message};
        ToastrService.createSuccessToast(params);
      }else{
        editor.setValue(editor.lastSavedValue);
        editor.xtextServices.saveResource();
      }
      $scope.closeEditor(index);
    });

    $scope.closeProject = function() {
      $location.path("/projects");
      $location.replace();
    }

    $scope.loadResourceAtServer = function(resource) {
      EditorDataService.loadResource(resource);
    }

    $scope.saveResource = function() {
      var editor = $scope.editors[$scope.selectedTabIndex];
      var tab = $scope.tabs[$scope.selectedTabIndex];
      var message = "Resource " + tab.filename + " saved";
      var params = {message: message};
      ToastrService.createSuccessToast(params);
      editor.xtextServices.saveResource();
    }

    $scope.loadResource = function(resource) {
      $scope.counter++;
      var tabId = $scope.counter;
      var editorParentDivId = "xtext-editor-parent-" + tabId;
      var editorDivId = "xtext-editor-" + tabId;
      var tab = resource;
      tab.id = tabId;
      tab.editorParentDivId = editorParentDivId;
      tab.editorDivId = editorDivId;
      tab.status = $scope.valid;
      $scope.loadResourceAtServer(resource);
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
      tab.status = $scope.valid;
      $scope.tabs.push(tab);
      $scope.selectedTabIndex = $scope.tabs.length - 1;
      $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]['id'];
      var element = angular.element(document).find('#editors');
      element.append('<div id="' + editorParentDivId + '" ng-show="selectedTabId==' + tabId + '"><div id="' + editorDivId + '" class="custom-xtext-editor"></div></div>');
      $compile(element.contents())($scope);
      $scope.openXtextEditor(tab);
    }

    $scope.openXtextEditor = function(tab) {
      $scope.createEditor(tab).then(function(editor){
        $scope.editors.push(editor);
        $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
        $scope.clearSearch();
        $scope.isEditorOpen = true;
      });
    }

    $scope.addEditor = function(model) {
      var resource = {
        name: model.name,
        version: model.version,
        namespace: model.namespace,
        filename: model.filename,
        modelSubType: model.modelSubType,
        modelType: model.modelType,
        description: model.description
      };
      var params = {projectName: $scope.projectName, resource: resource};
      ProjectDataService.createProjectResource(params).then(function(data){
        if (data.message === "resource already exists") {
          var message = resource.filename + " already exists";
          var params = {message: message};
          ToastrService.createErrorToast(params);
        } else {
          $scope.counter++;
          var tabId = $scope.counter;
          var editorParentDivId = "xtext-editor-parent-" + tabId;
          var editorDivId = "xtext-editor-" + tabId;
          model.resourceId = data.resource.properties["resourceId"];
          var tab = model;
          tab.id = tabId;
          tab.editorParentDivId = editorParentDivId;
          tab.editorDivId = editorDivId;
          if (tab.modelSubType != "") {
            tab.type = tab.modelSubType;
          } else {
            tab.type = tab.language;
          }
          $scope.tabs.push(tab);
          $scope.selectedTabIndex = $scope.tabs.length - 1;
          $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]['id'];
          var element = angular.element(document).find('#editors');
          element.append('<div id="' + editorParentDivId + '" ng-show="selectedTabId==' + tabId + '"><div id="' + editorDivId + '" class="custom-xtext-editor"></div></div>');
          $compile(element.contents())($scope);
          model.editorDivId = editorDivId;
          $scope.addXtextEditor(model);
        }
      }).catch(function(error){
        var message = resource.filename + " already exists";
        var params = {message: message};
        ToastrService.createErrorToast(params);
      });
    }

    $scope.addXtextEditor = function(model) {
      $scope.createEditor(model).then(function(editor){
        $scope.editors.push(editor);
        $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
        $scope.clearSearch();
        var resourceId = $scope.selectedEditor.xtextServices.validationService._encodedResourceId;
        var tab = $scope.tabs[$scope.selectedTabIndex];
        tab["text"] = model.name;
        tab["parent"] = $scope.rootParentId;
        tab["status"] = $scope.valid;
        $scope.addResource(tab);
        $scope.selectTab($scope.selectedTabIndex);
        $scope.isEditorOpen = true;
      });
    }

    $scope.getEditor = function(index) {
      var tab = $scope.tabs[index];
      var tabEditorId = "#" + tab["editorParentDivId"];
      return angular.element(document.querySelector(tabEditorId));
    }

    $scope.closeTab = function(index) {
      var editor = $scope.editors[index];
      if (editor.xtextServices.editorContext._dirty) {
        $scope.openCloseEditorModal(index);
      } else {
        $scope.closeEditor(index);
      }
    }

    $scope.closeEditor = function(index) {
      $scope.getEditor(index).remove();
      $scope.tabs.splice(index, 1);
      $scope.editors.splice(index, 1);
      if ($scope.editors.length > 0) {
        $scope.selectTab($scope.editors.length - 1);
      } else {
        $scope.models = [];
        $scope.isEditorOpen = false;
      }
    }

    $scope.selectTab = function(index) {
      $scope.selectedTabIndex = index;
      $scope.selectedTabId = $scope.tabs[$scope.selectedTabIndex]["id"]
      $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
      $scope.clearSearch();
    }

    $scope.getTabIndex = function(tab) {
      for (var i = 0; i < $scope.tabs.length; i++) {
        var temp = $scope.tabs[i];
        if (temp.name === tab.name && temp.namespace === tab.namespace && temp.version === tab.version) {
          return i;
        }
      }
      return -1;
    }

    $scope.openCloseEditorModal = function(index) {
      ShareDataService.setCloseEditorIndex(index);
      $uibModal.open({
        animation: true,
        controller: "CloseEditorController",
        templateUrl: "templates/editor/close-editor-modal-template.html",
        //size: "sm"
      });
    };

    $scope.openAddEditorModal = function() {
      ShareDataService.setAddEditorTypes($scope.editorTypes);
      $uibModal.open({
        animation: true,
        controller: "AddEditorController",
        templateUrl: "templates/editor/add-editor-modal-template.html",
        //size: "sm"
      });
    };

    $scope.openDescribeEditorModal = function(editorType) {
      ShareDataService.setDescribeEditorType(editorType);
      $uibModal.open({
        animation: true,
        controller: "DescribeEditorController",
        templateUrl: "templates/editor/describe-editor-modal-template.html",
        //size: "sm"
      });
    };

    $scope.openDeleteEditorModal = function(tab) {
      ShareDataService.setDeleteEditorTab(tab);
      $uibModal.open({
        animation: true,
        controller: "DeleteEditorController",
        templateUrl: "templates/editor/delete-editor-modal-template.html",
        //size: "m"
      });
    };

    $scope.openCloseProjectModal = function() {
      var unsavedFiles = $scope.getUnsavedFiles();
      if (unsavedFiles.length < 1) {
        $rootScope.$broadcast("closeProject");
      } else {
        ShareDataService.setUnsavedFiles(unsavedFiles);
        $uibModal.open({
          animation: true,
          controller: "CloseProjectController",
          templateUrl: "templates/project/close-project-modal-template.html",
          //size: "sm"
        });
      }
    };

    $scope.isModelSelected = function() {
      for (i = 0; i < $scope.displayedModels.length; i++) {
        if ($scope.displayedModels[i]["isSelected"]) {
          $scope.selectedModel = $scope.displayedModels[i];
          $scope.selectedModelId = $scope.selectedModel["id"];
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
      if (editor === null) {
        return false;
      }
      if (editor.xtextServices.editorContext._annotations.length !== 0) {
        return false;
      } else {
        return true;
      }
    }

    $scope.importModel = function(model) {
      if ($scope.isValidModel($scope.selectedEditor)) {
        if (model) {
          $scope.showImportButton = false;
          var language = $scope.tabs[$scope.selectedTabIndex]["language"];
          var editor = $scope.selectedEditor;
          var modelId = model.id;
          var importModelRequest = {
            modelName: modelId["name"],
            modelNamespace: modelId["namespace"],
            modelVersion: modelId["version"],
            projectName: $scope.projectName,
            resourceId: editor.xtextServices.validationService._encodedResourceId
          }
          var params = {language: language, importModelRequest: importModelRequest};
          EditorDataService.importModel(params).then(function(data){
              editor.setValue(data);
              $scope.showImportButton = true;
          }).catch(function(error){
              var message = "Failed to import model";
              var params = {message: message};
              ToastrService.createErrorToast(params);
              $scope.showImportButton = true;
          });
        } else {
            var message = "Please select a model to import";
            var params = {message: message};
            ToastrService.createWarningToast(params);
            $scope.showImportButton = true;
        }
      } else {
          var message = "Your Vorto Model contains errors. Please correct and try again.";
          var params = {message: message};
          ToastrService.createWarningToast(params);
          $scope.showImportButton = true;
      }
    }

    $scope.referenceResource = function(targetResourceId, referenceResourceId) {
      if ($scope.isValidModel($scope.selectedEditor)) {
        var language = $scope.tabs[$scope.selectedTabIndex]["language"];
        var editor = $scope.selectedEditor;
        var modelId = $scope.selectedModelId;
        var referenceResourceRequest = {
          targetResourceId: targetResourceId,
          referenceResourceId: referenceResourceId
        };
        var params = {language: language, referenceResourceRequest: referenceResourceRequest};
        EditorDataService.referenceResource(params).then(function(data){
          editor.setValue(data);
        }).catch(function(error){
          var message = "Failed to reference resource";
          var params = {message: message};
          ToastrService.createErrorToast(params);
        });
      } else {
        var message = "Your Vorto Model contains errors. Please correct and try again.";
        var params = {message: message};
        ToastrService.createWarningToast(params);
      }
    }

    $scope.searchOnEnter = function(keyEvent) {
      if (keyEvent.keyCode === 13) {
        $scope.search();
      }
    };

    $scope.clearSearch = function() {
      $scope.queryFilter = "";
      $scope.models = [];
    }

    $scope.search = function() {
      var filter = $scope.queryFilter;
      if ($scope.tabs[$scope.selectedTabIndex] === undefined) {
        return;
      }
      var language = $scope.tabs[$scope.selectedTabIndex]["language"];
      if ($scope.tabs[$scope.selectedTabIndex]["modelSubType"] === "enum") {
        $scope.models = [];
        return;
      }
      var params = {language: language, filter: filter};
      $scope.showSearchButton = false;
      EditorDataService.searchRepository(params).then(function(data){
        $scope.models = data;
        $scope.showSearchButton = true;
        if(data.length == 0) {
          var message = "No models found for filter " + filter + " in the Vorto repository";
          var params = {message: message};
          ToastrService.createWarningToast(params);
        }
      }).catch(function(error){
        $scope.models = [];
        $scope.showSearchButton = true;
        var message = "Failed to get search for models with filter " + filter;
        var params = {message: message};
        ToastrService.createErrorToast(params);
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

    $scope.predicates = ["Name"];

    $scope.getResourceLanguage = function(filename) {
      return filename.split(".")[1];
    }

    $scope.getUnsavedFiles = function() {
      var unsavedFiles = [];
      for (var i = 0; i < $scope.editors.length; i++) {
        if ($scope.editors[i].xtextServices.editorContext._dirty) {
          unsavedFiles.push($scope.tabs[i]["filename"]);
        }
      }
      return unsavedFiles;
    }

    $scope.createEditor = function(model) {
      var defer = $q.defer();
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          var editor = xtext.createEditor({
            enableFormattingAction: "true",
            enableSaveAction: "true",
            showErrorDialogs: "true",
            parent: model.editorDivId,
            xtextLang: model.language,
            resourceId: model.resourceId,
            syntaxDefinition: $scope.editorConfig[model.language].syntaxDefinition,
            serviceUrl: $scope.editorConfig[model.language].serviceUrl
          });
          editor.commands.addCommand({
              name: 'customSaveCommand',
              bindKey: {win: 'Ctrl-S',  mac: 'Command-S'},
              exec: function(editor) {
                var tab = $scope.tabs[$scope.selectedTabIndex];
                editor.xtextServices.saveResource().then(function(data){
                  var message = "Resource " + tab.filename + " saved";
                  var params = {message: message};
                  ToastrService.createSuccessToast(params);
                }, function(err){
                  var message = "Failed to save " + tab.filename;
                  var params = {message: message};
                  ToastrService.createErrorToast(params);
                });
                editor.lastSavedValue = editor.getValue();
              }
          });
          editor.xtextServices.loadResource().then(function(data){
            editor.lastSavedValue = data.fullText;
            defer.resolve(editor);
          }, function(err){
            defer.resolve(editor);
          });
        });
      });
      return defer.promise;
    }
  }
});
