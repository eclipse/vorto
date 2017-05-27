define(["angular"], function(angular) {
  angular
    .module("apps.controller")
    .controller("EditorController", EditorController);

  EditorController.$inject = ["$rootScope", "$scope", "$location", "$routeParams", "$http", "$compile", "$uibModal", "toastr", "ShareDataService"]

  function EditorController($rootScope, $scope, $location, $routeParams, $http, $compile, $uibModal, toastr, ShareDataService) {
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
        serviceUrl: "/infomodel/xtext-service",
        searchUrl: "./rest/editor/infomodel/search=",
        importModelUrl: "./rest/editor/infomodel/link/model",
        referenceModelUrl: "./rest/editor/infomodel/link/resource",
        updateUrl: "/infomodel/xtext-service/update?resource=",
        validateUrl: "./infomodel/xtext-service/validate?resource=",
        loadUrl: "./infomodel/xtext-service/load?resource="
      },
      fbmodel: {
        syntaxDefinition: "xtext/mode-fbmodel",
        serviceUrl: "/functionblock/xtext-service",
        searchUrl: "./rest/editor/functionblock/search=",
        importModelUrl: "./rest/editor/functionblock/link/model",
        referenceModelUrl: "./rest/editor/functionblock/link/resource",
        updateUrl: "./functionblock/xtext-service/update?resource=",
        validateUrl: "./functionblock/xtext-service/validate?resource=",
        loadUrl: "./functionblock/xtext-service/load?resource="
      },
      type: {
        syntaxDefinition: "xtext/mode-type",
        serviceUrl: "/datatype/xtext-service",
        searchUrl: "./rest/editor/datatype/search=",
        importModelUrl: "./rest/editor/datatype/link/model",
        referenceModelUrl: "./rest/editor/datatype/link/resource",
        updateUrl: "/datatype/xtext-service/update?resource=",
        validateUrl: "./datatype/xtext-service/validate?resource=",
        loadUrl: "./datatype/xtext-service/load?resource="
      }
    }

    $scope.projectResources = [];

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
          label: "New Resource",
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
              if ($scope.projectResources[i]["id"] == parentId) {
                temp = $scope.projectResources[i];
              }
            }
            var parent = $scope.tabs[$scope.selectedTabIndex];
            if (parent.name != temp.name || parent.version != temp.version || parent.namespace != temp.namespace) {
              return false;
            }
            if (node.type == "infomodel") {
              return false;
            } else if (node.type == "fbmodel" && (par.type == "infomodel" || par.type == "fbmodel")) {
              more.origin.settings.dnd.always_copy = true;
              return true;
            } else if (node.type == "entity" && par.type == "fbmodel") {
              more.origin.settings.dnd.always_copy = true;
              return true;
            } else if (node.type == "enum" && (par.type == "fbmodel" || par.type == "entity")) {
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
          icon: "/images/im.png",
          valid_children: ["fbmodel"]
        },
        fbmodel: {
          icon: "/images/fb.png",
          valid_children: ["fbmodel", "enum", "entity"]
        },
        enum: {
          icon: "/images/enu.png",
          valid_children: []
        },
        entity: {
          icon: "/images/ent.png",
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
      $http.get("./rest/project/" + $scope.projectName + "/open").success(
        function(data, status, headers, config) {
          $scope.showEditorBody = true;
          $scope.getResources();
          $scope.getReferences();
        }).error(function(data, status, headers, config) {
        $scope.showEditorBody = false;
      });
    }

    $scope.openProject();

    $scope.uploadProject = function() {
      var unsavedFiles = $scope.getUnsavedFiles();
      if (unsavedFiles.length < 1) {
        $http.get("./rest/publish/" + $scope.projectName + "/validate").success(
          function(data, status, headers, config) {
            $scope.openPublishModal(data);
          }).error(function(data, status, headers, config) {
            window.alert("Failed to upload resources")
        });
      } else {
        ShareDataService.setUnsavedFiles(unsavedFiles);
        $uibModal.open({
          animation: true,
          controller: "UnsavedEditorFilesController",
          templateUrl: "templates/editor/unsaved-files-modal-template.html",
          size: "sm"
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

    $scope.getReferences = function() {
      var url = "./rest/project/" + $rootScope.globalContext.referenceRepository + "/resources";
      $http.get(url).success(
        function(data, status, headers, config) {
          for (i = 0; i < data.length; i++) {
            var resource = $scope.getResourceObject(data[i]);
            $scope.loadResourceAtServer(resource);
          }
        }).error(function(data, status, headers, config) {
          windoow.alert("Unable to delete the file");
      });
    }

    $scope.getResources = function() {
      $http.get("./rest/project/" + $scope.projectName + "/resources").success(
        function(data, status, headers, config) {
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
        }).error(function(data, status, headers, config) {

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
      var url = "./rest/project/" + $scope.projectName + "/delete";
      $http.post(url, {
        "projectName": $scope.projectName,
        "resourceId": tab["resourceId"]
      }).success(
        function(data, status, headers, config) {
          if (tab.index > -1) {
            $scope.closeTab(tab.index);
          }
          $scope.removeResource(tab);
          $scope.editors.forEach(function(editor){
            var content = editor.getValue();
            editor.setValue(content);
          });

        }).error(function(data, status, headers, config) {
          windoow.alert("Unable to delete the file");
      });
    });

    $scope.$on("closeProject", function(event, save) {
      if(save){
        $scope.editors.forEach(function(editor){
          editor.xtextServices.saveResource();
        });
      }
      $scope.closeProject();
    });

    $scope.$on("closeEditor", function(event, index, save) {
      if (save) {
        var editor = $scope.editors[index];
        editor.xtextServices.saveResource();
      }
      $scope.closeEditor(index);
    });

    $scope.closeProject = function() {
      $location.path("/project");
      $location.replace();
    }

    $scope.loadResourceAtServer = function(resource) {
      $http.get($scope.editorConfig[resource.language]["loadUrl"] + resource["resourceId"]).success(
        function(data, status, headers, config) {}).error(function(data, status, headers, config) {});
    }

    $scope.saveResource = function() {
      var editor = $scope.editors[$scope.selectedTabIndex];
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
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            xtextLang: tab.language,
            syntaxDefinition: $scope.editorConfig[tab.language].syntaxDefinition,
            serviceUrl: $scope.editorConfig[tab.language].serviceUrl,
            enableFormattingAction: "true",
            enableSaveAction: "true",
            showErrorDialogs: "true",
            parent: tab.editorDivId,
            resourceId: tab.resourceId
          });
          $scope.editors.push(editor);
          $scope.selectedEditor = $scope.editors[$scope.selectedTabIndex];
          $scope.clearSearch();
          $scope.isEditorOpen = true;
          $scope.$apply();
        });
      });
    }

    $scope.addEditor = function(model) {
      $http.post("./rest/project/" + $scope.projectName + "/resources", {
        "name": model.name,
        "version": model.version,
        "namespace": model.namespace,
        "filename": model.filename,
        "modelSubType": model.modelSubType,
        "modelType": model.modelType,
        "description": model.description
      }).success(
        function(data, status, headers, config) {
          if (data.message == "resource already exists") {
            window.alert("File already exists")
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
            $scope.addXtextEditor(editorDivId, model);
          }
        }).error(function(data, status, headers, config) {
        window.alert("File already exists")
      });
    }

    $scope.addXtextEditor = function(parentId, model) {
      require(["webjars/ace/1.2.0/src/ace"], function() {
        require(["xtext/xtext-ace"], function(xtext) {
          editor = xtext.createEditor({
            enableFormattingAction: "true",
            enableSaveAction: "true",
            showErrorDialogs: "true",
            parent: parentId,
            xtextLang: model.language,
            resourceId: model.resourceId,
            syntaxDefinition: $scope.editorConfig[model.language].syntaxDefinition,
            serviceUrl: $scope.editorConfig[model.language].serviceUrl
          });
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
          $scope.$apply();
        });
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
        $scope.models = []
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
        size: "sm"
      });
    };

    $scope.openAddEditorModal = function() {
      ShareDataService.setAddEditorTypes($scope.editorTypes);
      $uibModal.open({
        animation: true,
        controller: "AddEditorController",
        templateUrl: "templates/editor/add-editor-modal-template.html",
        size: "sm"
      });
    };

    $scope.openDescribeEditorModal = function(editorType) {
      ShareDataService.setDescribeEditorType(editorType);
      $uibModal.open({
        animation: true,
        controller: "DescribeEditorController",
        templateUrl: "templates/editor/describe-editor-modal-template.html",
        size: "sm"
      });
    };

    $scope.openDeleteEditorModal = function(tab) {
      ShareDataService.setDeleteEditorTab(tab);
      $uibModal.open({
        animation: true,
        controller: "DeleteEditorController",
        templateUrl: "templates/editor/delete-editor-modal-template.html",
        size: "m"
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
          size: "sm"
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
          var language = $scope.tabs[$scope.selectedTabIndex]["language"];
          var editor = $scope.selectedEditor;
          var modelId = $scope.selectedModelId;
          $http.post($scope.editorConfig[language].importModelUrl, {
            "modelName": modelId["name"],
            "modelNamespace": modelId["namespace"],
            "modelVersion": modelId["version"],
            "projectName": $scope.projectName,
            "resourceId": editor.xtextServices.validationService._encodedResourceId
          }).success(
            function(data, status, headers, config) {
              editor.setValue(data);
            }).error(function(data, status, headers, config) {
            window.alert("Failed")
          }).finally(function() {
            $scope.showImportButton = true;
          });
        } else {
          window.alert("Please select a model to import");
        }
      } else {
        window.alert("Your Vorto Model contains errors. Please correct and try again.");
      }
    }

    $scope.referenceResource = function(targetResourceId, referenceResourceId) {
      if ($scope.isValidModel($scope.selectedEditor)) {
        $scope.showImportButton = false;
        var language = $scope.tabs[$scope.selectedTabIndex]["language"];
        var editor = $scope.selectedEditor;
        var modelId = $scope.selectedModelId;
        $http.post($scope.editorConfig[language].referenceModelUrl, {
          "targetResourceId": targetResourceId,
          "referenceResourceId": referenceResourceId
        }).success(
          function(data, status, headers, config) {
            editor.setValue(data);
          }).error(function(data, status, headers, config) {
            window.alert("Failed")
        }).finally(function() {
            $scope.showImportButton = true;
        });
      } else {
          window.alert("Your Vorto Model contains errors. Please correct and try again.");
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
      var filter = null;
      var modelType = null;
      filter = $scope.queryFilter;
      if ($scope.tabs[$scope.selectedTabIndex] == undefined) {
        return;
      }
      var language = $scope.tabs[$scope.selectedTabIndex]["language"];
      if ($scope.tabs[$scope.selectedTabIndex]["modelSubType"] === "enum") {
        $scope.models = [];
        return;
      }
      $scope.showSearchButton = false;
      $http.get($scope.editorConfig[language].searchUrl + filter).success(
        function(data, status, headers, config) {
          $scope.models = data;
        }).error(function(data, status, headers, config) {
        $scope.models = [];
      }).finally(function() {
        $scope.showSearchButton = true;
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
  }
});
