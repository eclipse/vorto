repositoryControllers.controller('DetailsController', 
    ['$rootScope', '$scope', '$http', '$routeParams', '$location', '$route', 
     '$uibModal', '$timeout', '$window', '$timeout', 'openCreateModelDialog',
    function ($rootScope, $scope, $http, $routeParams, $location, $route, $uibModal, 
        $timeout, $window, $timeout, openCreateModelDialog) {

		$scope.model = [];
		$scope.aclEntries = [];
		$scope.platformGeneratorMatrix = null;
		$scope.platformDemoGeneratorMatrix = null;
		$scope.workflowActions = [];
		$scope.chosenFile = false;
		$scope.editMode = false;
		$scope.modelIsLoading = false;
		$scope.isLoading = false;
		$scope.isLoadingGenerators = false;
		$scope.showReferences = false;
		$scope.showUsages = false;
		$scope.showMappings = true;
		$scope.modelFileNames = [];
		$scope.modelEditor = null;
		$scope.attachments = [];
		$scope.permission = "READ";
		$scope.encodeURIComponent = encodeURIComponent;
		$scope.newComment = {value: ""}
		$scope.canGenerate = true;

		$scope.modelEditorLoaded = function (_editor) {
			$scope.modelEditor = _editor;
			$scope.modelEditorSession = _editor.getSession();
			_editor.getSession().setMode("ace/mode/vorto");
			_editor.setTheme("ace/theme/chrome");
			_editor.getSession().setTabSize(2);
			_editor.setShowPrintMargin(false);
			_editor.getSession().setUseWrapMode(true);
		};

		$scope.modelEditorChanged = function (e) {
			$scope.newModelCode = $scope.modelEditorSession.getDocument().getValue();
		};

		$scope.saveModel = function () {
			$scope.isLoading = true;
			$scope.error = false;
			var newContent = {
				contentDsl: $scope.newModelCode,
				type: $scope.model.type

			};

			$http.put('./rest/tenants/' + $scope.tenantId + '/models/' + $scope.model.id.prettyFormat, newContent)
				.success(function (result) {
					$scope.isLoading = false;
					$scope.message = result.message;
					if (result.valid) {
						$scope.success = "Changes saved successfully. Reloading page ...";
						$timeout(function () {
							$window.location.reload();
						}, 1000);
					} else {
						$scope.validationIssues = result.validationIssues; 
					}
				}).error(function (data, status, headers, config) {
					$scope.isLoading = false;
					if (status === 400) {
						$scope.message = data.message;
						$scope.validationIssues = data.validationIssues;
					} else {
						$scope.error = data;
					}
				});
		};

		$scope.uploadImage = function () {
			var fd = new FormData();
			fd.append('file', document.getElementById('imageFile').files[0]);
			$http.post('./rest/tenants' + $scope.tenantId + '/models/' + $scope.model.id.prettyFormat + '/images/', fd, {
					transformRequest: angular.identity,
					headers: {
						'Content-Type': undefined
					}
				})
				.success(function (result) {
					$timeout(function () {
						$window.location.reload();
					}, 500);
				}).error(function (data, status, headers, config) {
					if (status == 403) {
						$rootScope.error = "Operation is Forbidden";
					} else if (status == 401) {
						$rootScope.error = "Unauthorized Operation";
					} else if (status == 400) {
						$rootScope.error = "Bad Request. Server Down";
					} else if (status == 500) {
						$rootScope.error = data.message;
					} else {
						$rootScope.error = "Failed Request with response status " + status;
					}
				});
		};

		$scope.hasImage = function () {
			if (model.hasImage) {
				return "image"
			} else {
				return "placeHolder"
			}
		};

		$scope.chooseImageFile = function () {
			document.getElementById("imageFile").click();
		};

		$scope.getAttachments = function (model) {
			$http.get("./api/v1/attachments/" + model.id.prettyFormat)
				.success(function (attachments) {
					$scope.attachments = attachments;
				})
				.error(function (error) {});
		};
		
		$scope.getMappings = function () {
			$scope.modelMappings = [];
			for(var i = 0; i < Object.keys($scope.model.platformMappings).length;i++){
				var key = Object.keys($scope.model.platformMappings)[i];
				$http.get("./api/v1/models/" + $scope.model.platformMappings[key].prettyFormat + "?key=key").then(
				    (function(key) {
				        return function(result) {
				        	var mapping = {
								"id" : result.data.id,
								"state" : result.data.state,
								"targetPlatform" : key
							};
							$scope.modelMappings.push(mapping);
					}
				    })(key),
				    function(data) {
				        //
				    }); 
			}
		};
		
		$scope.getReferences = function () {
			references = $scope.model.references;
			$scope.modelReferences = [];
			$scope.modelReferences.show = false;
			var tmpIdx = 0;
			for( var index in references){
		        $http.get("./api/v1/models/" + references[index].prettyFormat)
				.success(function (result) {
					$scope.modelReferences[tmpIdx] = {
						"modelId" : result.id.prettyFormat,
						"state" : result.state,
						"type" : result.type,
						"hasAccess" : true
					};
					$scope.modelReferences.show = true;
					tmpIdx++;
				}).error(function (result) {
					$scope.modelReferences[tmpIdx] = {
						"modelId" : references[tmpIdx].prettyFormat,
						"state" : null,
						"hasAccess" : false
					};
					$scope.canGenerate = false;
					$scope.modelReferences.show = true;
					tmpIdx++;
				});
			}
		};
		
		$scope.getReferencedBy = function () {
			referencedBy = $scope.model.referencedBy;
			$scope.modelReferencedBy = [];
			$scope.modelReferencedBy.show = false;
			var tmpIdx = 0;
			for( var index in referencedBy){
		        $http.get("./api/v1/models/" + referencedBy[index].prettyFormat)
				.success(function (result) {
					$scope.modelReferencedBy[tmpIdx] = {
						"modelId" : result.id.prettyFormat,
						"type" : result.type,
						"state" : result.state,
						"hasAccess" : true
					};
					$scope.modelReferencedBy.show = true;
					tmpIdx++;
				}).error(function (result) {
					$scope.modelReferencedBy[tmpIdx] = {
						"modelId" : referencedBy[tmpIdx].prettyFormat,
						"state" : null,
						"hasAccess" : false
					};
					$scope.modelReferencedBy.show = true;
					tmpIdx++;
				});
			}
		};

		$scope.getDetails = function (modelId) {
			$scope.modelIsLoading = true;
			$http.get("./api/v1/models/" + modelId)
				.success(function (result) {
					$scope.model = result;
					if($scope.model.author.length === 64) {
						$scope.model.author = 'other user';
					}
					$scope.getMappings();
					$scope.getReferences();
					$scope.getReferencedBy();
					$scope.getAttachments(result);
					
					if ($rootScope.authenticated) {
						$scope.getUserPolicy();
					}

					if ($scope.model.references.length < 2) $scope.showReferences = true;
					if ($scope.model.referencedBy.length < 2) $scope.showUsages = true;
					$scope.modelIsLoading = false;

				}).error(function (error, status) {					
					if(status == 401) {
						$location.path('/login');
					} else {
						$scope.errorLoading = error.message;
					}
					$scope.modelIsLoading = false;	
				});
		};

		$scope.getContent = function (modelId) {
			$http.get("./api/v1/models/" + modelId + "/file")
				.success(function (result) {
					$timeout(function () {
							$scope.modelEditorSession.getDocument().setValue(result);
						}, 1000);
				}).error(function (data, status, headers, config) {
					$scope.error = data.message;
				});
		};
		
		$scope.getPlatformGenerators = function () {
			$scope.isLoadingGenerators = true;
			$http.get('./api/v1/generators')
				.success(function (result) {
					$scope.isLoadingGenerators = false;
					var productionGenerators = $scope.filterByTag(result, "production");
					var demoGenerators = $scope.filterByTag(result, "demo");
					$scope.platformGeneratorMatrix = $scope.listToMatrix(productionGenerators, 2);
					$scope.platformDemoGeneratorMatrix = $scope.listToMatrix(demoGenerators, 2);					
				});
		};

		$scope.filterByTag = function (result, tag) {
			var filteredList = [];
			result.forEach(function (e) {
				if (e.tags.includes(tag)) {
					filteredList.push(e);
				}
			});
			return filteredList;
		};

		$scope.isFilled = function (rating, value) {
			if (rating === value) {
				return "filled"
			}
		};

		$scope.listToMatrix = function (list, n) {
			var grid = [],
				i = 0,
				x = list.length,
				col, row = -1;
			for (var i = 0; i < x; i++) {
				col = i % n;
				if (col === 0) {
					grid[++row] = [];
				}
				grid[row][col] = list[i];
			}
			return grid;
		}


		$scope.modelId = $routeParams.modelId;
		$scope.tenantId = $routeParams.tenantId;
		
		$scope.getDetails($scope.modelId);
		$scope.getContent($scope.modelId);
		
		$scope.getPlatformGenerators();

		/*
		 * Start - Handling Comments
		 */
        $scope.comments = [];
		$authority = $rootScope.authority;

		$scope.getCommentsForModelId = function (modelId) {

			$http.get('./rest/' + $scope.tenantId + '/comments/' + modelId)
				.success(function (result) {
                    $scope.comments = result;
					$scope.comments.reverse();
				}).error(function (data, status, headers, config) {

					if (status == 403) {
						$rootScope.error = "Operation is Forbidden";
					} else if (status == 401) {
						$rootScope.error = "Unauthorized Operation";
					} else if (status == 400) {
						$rootScope.error = "Could not be removed because it references other models " + JSON.stringify(data);
					} else if (status == 500) {
						$rootScope.error = "Internal Server Error";
					} else {
						$rootScope.error = "Failed Request with response status " + status;
					}
				});
		}

		$scope.getCommentsForModelId($scope.modelId);
		
		$scope.createComment = function () {

			$scope.date = new Date();

			var comment = {
				"modelId": $scope.modelId,
				"author": $scope.user,
				"date": $scope.date,
				"content": $scope.newComment.value
			};
			
			$http.post('./rest/' + $scope.tenantId + '/comments', comment)
				.success(function (result) {
                    $scope.getCommentsForModelId($scope.modelId);
				}).error(function (data, status, headers, config) {
					if (status == 403) {
						$rootScope.error = "Operation is Forbidden";
					} else if (status == 401) {
						$rootScope.error = "Unauthorized Operation";
					} else if (status == 400) {
						$rootScope.error = "Could not be removed because it references other models " + JSON.stringify(data);
					} else if (status == 500) {
						$rootScope.error = "Internal Server Error";
					} else {
						$rootScope.error = "Failed Request with response status " + status;
					}
				});

			$scope.newComment.value = "";
		}

		/*
		 * Stop - Handling Comments
		 */

		$scope.openGeneratorConfig = function (generator) {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: "GeneratorConfigController",
				templateUrl: "webjars/repository-web/dist/partials/generator-config-template.html",
				size: "lg",
				resolve: {
					generator: function () {
						return generator;
					},
					model: function () {
						return $scope.model;
					}
				}
			});
		};
		/*
		 * Start - Workflow
		 */
		$scope.getWorkflowActions = function () {
			$http.get('./rest/tenants/' + $scope.tenantId + '/workflows/' + $scope.modelId + '/actions')
				.success(function (result) {
					$scope.workflowActions = result;
				});
		};

		$scope.getWorkflowActions();

		$scope.openWorkflowActionDialog = function (action) {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: function ($scope, model, tenantId) {
					$scope.action = action;
					$scope.actionModel = null;
					$scope.model = model;
					$scope.errorMessage = "";
					$scope.hasError = false;
					
					$scope.takeWorkflowAction = function () {
						$http.put('./rest/tenants/' + tenantId + '/workflows/' + $scope.model.id.prettyFormat + '/actions/' + $scope.action)
							.success(function (result) {
								console.log(JSON.stringify(result));
								if (result.hasErrors) {
									$scope.hasErrors = true;
									$scope.errorMessage = result.errorMessage;
								} else {
									modalInstance.close();
								}
							})
							.error(function (data, status, headers, config) {
								console.log(JSON.stringify(data) + ":" + JSON.stringify(status) + ":" + JSON.stringify(headers));
							});
					};

					$scope.getModel = function () {
						$http.get('./rest/tenants/' + tenantId + '/workflows/' + $scope.model.id.prettyFormat)
								.success(function (result) {
									for (var i = 0; i < result.actions.length; i++) {
										if (result.actions[i].name === $scope.action) {
											$scope.actionModel = result.actions[i];
											return;
										}
									}
								});
					};

					$scope.getModel();

					$scope.cancel = function () {
						modalInstance.dismiss();
					};
				},
				templateUrl: "workflowActionDialog.html",
				size: "lg",
				resolve: {
					action: function () {
						return action;
					},
					model: function () {
						return $scope.model;
					},
					tenantId: function() {
					    return $scope.tenantId;
					}
				}
			});

			modalInstance.result.then(
				function () {
					$window.location.reload();
				});
		};
		/*
		 * Start - Handle Modal
		 */
		$scope.openDeleteDialog = function (model) {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: function ($scope, model, tenantId) {
					$scope.model = model;

					$scope.delete = function () {
						$http.delete('./rest/tenants/' + tenantId + '/models/' + model.id.prettyFormat)
							.success(function (result) {
								modalInstance.close();
							});
					};

					$scope.cancel = function () {
						modalInstance.dismiss();
					};
				},
				templateUrl: "deleteActionDialog.html",
				size: "lg",
				resolve: {
					model: function () {
						return $scope.model;
					},
					tenantId: function() {
					    return $scope.tenantId;
					}
				}
			});

			modalInstance.result.then(
				function () {
					$location.path('/');
				});
		};

		$scope.openCreateModelDialog = openCreateModelDialog($scope);
		
		$scope.openCreateModelVersionDialog = function (action) {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: function ($scope,model, tenantId) {
					$scope.errorMessage = null;
					$scope.model = model;
					$scope.modelVersion = "";

					$scope.createVersion = function () {
						$scope.isLoading = true;
						$http.post("./rest/tenants/" + tenantId + "/models/" + $scope.model.id.prettyFormat + "/versions/" + $scope.modelVersion, null)
							.success(function (result) {
								$scope.isLoading = false;
								modalInstance.close(result);
							}).error(function (data, status, header, config) {
								$scope.isLoading = false;
								if (status === 409) {
									$scope.errorMessage = "Model with this name and namespace already exists.";
								} else {
									$scope.errorMessage = status.message;
								}
							});
					};

					$scope.cancel = function () {
						modalInstance.dismiss();
					};
				},
				templateUrl: "webjars/repository-web/dist/partials/createversion-template.html",
				size: "lg",
				resolve: {
					model: function () {
						return $scope.model;
					},
					tenantId: function() {
					    return $scope.tenantId;
					}
				}
			});

			modalInstance.result.then(
				function (model) {
					$location.path("/details/" + $scope.tenantId + "/" + model.id.prettyFormat);
				});
		};
		
		$scope.openPayloadMappingDialog = function () {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: function ($scope, model, tenantId) {
					$scope.model = model;
					$scope.errorMessage = null;
					$scope.targetPlatform = "";
	
					$scope.create = function () {
						$scope.isLoading = true;
						$http.post('./rest/' + tenantId + '/mappings/' + $scope.model.id.prettyFormat + '/' + $scope.targetPlatform)
							.success(function (result) {
								$scope.isLoading = false;
								var data = {"targetPlatform" : $scope.targetPlatform, 
											"mappingId" : result.mappingId };
								modalInstance.close(data);
							}).error(function (data, status, header, config) {
								$scope.isLoading = false;
								if (status === 409) {
									$scope.errorMessage = "Mapping with key '"+$scope.targetPlatform+"' already exists.";
								} else {
									$scope.errorMessage = status.message;
								}
							});
					};

					$scope.cancel = function () {
						modalInstance.dismiss();
					};
				},
				templateUrl: "payloadMappingDialog.html",
				size: "lg",
				resolve: {
					model: function () {
						return $scope.model;
					},
				    tenantId: function() {
				        return $scope.tenantId;
				    }
				}
			});

			modalInstance.result.then(
				function (data) {
					$location.path("/payloadmapping/" + $scope.model.id.prettyFormat + "/" + data.targetPlatform + "/" + data.mappingId);
				});
		};

		$scope.openSearchDialog = function () {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: function ($scope, model) {
					$scope.currentModel = model;
					$scope.isLoading = false;
					$scope.searchResult = [];
					$scope.searchModelType = 'all';
					$scope.searchFilter = "";

					$scope.searchReferences = function () {
						$scope.isLoading = true;
						var filter = null;
						if ($scope.searchModelType === 'all') {
							filter = $scope.searchFilter;
						} else {
							filter = $scope.searchFilter + " " + $scope.searchModelType;
						}
						$http.get('./api/v1/search/models?expression=' + filter).success(
							function (data, status, headers, config) {
								$scope.searchResult = data;
								$scope.isLoading = false;
							}).error(function (data, status, headers, config) {
							$scope.searchResult = [];
							$scope.isLoading = false;
						});
					};

					$scope.copyToClipboard = function (modelId) {
						var $temp_input = $("<input>");
						$("body").append($temp_input);
						$temp_input.val("using " + modelId.namespace + "." + modelId.name + ";" + modelId.version).select();
						document.execCommand("copy");
						$temp_input.remove();
						modalInstance.dismiss();
					};

					$scope.searchReferences();

					$scope.cancel = function () {
						modalInstance.dismiss();
					};

					$scope.displayedSearchResult = [].concat($scope.searchResult);
					$scope.itemsByPage = 6;

				},
				templateUrl: "searchDialog.html",
				size: "lg",
				resolve: {
					model: function () {
						return $scope.model;
					}
				}
			});

		};

		/*Model Attachments upload & download*/
		$scope.openUploadAttachmentDialog = function (modelId) {
			var updateAttachments = $scope.getAttachments;
			var model = $scope.model;
			var uploadDialog = $uibModal.open({
				animation: true,
				templateUrl: "uploadAttachment.html",
				size: "lg",
				controller: function ($scope) {
					$scope.successfullyUploaded = false;
					$scope.failedToUpload = false;
					$scope.isUploading = false
					$scope.modelId = modelId;
					$scope.fileSizeValid = true;
					$scope.fileNameValid = true;
					$scope.selectedFile = null;
					$scope.errorMessage = "";
					$scope.attachmentValid = true;
                    $scope.attachmentNote = "Max file size " + $rootScope.context.attachmentAllowedSize + " MB."

					$scope.cancel = function () {
						$scope.successfullyUploaded = false;
						$scope.failedToUpload = false;
						$scope.errorMessage = "";
						uploadDialog.dismiss();
					};

					//validate if file size should be smaller than 64 kBytes
					$scope.isFileSizeValid = function () {
						var input = document.getElementById("file-upload");

						if (input.files[0]) {
							$scope.selectedFile = input.files[0];
							$scope.$apply(function () {
								$scope.fileSizeValid = true;
								$scope.fileNameValid = true;
							});
						}
					};

					$scope.fileNameChanged = function (element) {
						$scope.$apply(function ($scope) {
							$scope.browsedFile = element.files[0].name;
						});
						$scope.fileAdded = true;
						$scope.$digest();
					};

					$scope.uploadAttachment = function () {
						$scope.isUploading = true;
                        $scope.attachmentValid = true;
                        var payload = new FormData();
                        payload.append('file', $scope.selectedFile, encodeURIComponent($scope.selectedFile.name));

                        const attachment_url = './api/v1/attachments/' + $scope.modelId;
                        $scope.attachmentNote = "Uploading..."
                        $http.put(attachment_url, payload, {
                            transformRequest: angular.identity,
                            headers: {
                                'Content-Type': undefined
                            }
                        })
                        .success(function (data, status, headers, config) {
                            updateAttachments(model);
                            $scope.isUploading = false;
                            if(data.success){
                                    $scope.successfullyUploaded = true;
                                    $timeout($scope.cancel,2000);
                               }else{
                                    $scope.successfullyUploaded = false;
                                    $scope.attachmentValid = false;
                                    $scope.failedToUpload = false;
                                    $scope.errorMessage = data.errorMessage;
                               }
                        })
                        .error(function (data, status, headers, config) {
                                $scope.successfullyUploaded = false;
                                $scope.attachmentValid = false;
                                $scope.failedToUpload = false;
                                $scope.isUploading = false;
                                $scope.errorMessage = "File size exceeded. Allowed max size: " + $rootScope.context.attachmentAllowedSize +" MB";
                        });

					};
				}
			});
		};

		$scope.openDeleteAttachmentDialog = function (modelId, fileToDelete) {
			var updateAttachments = $scope.getAttachments;
			var model = $scope.model;
			var dialog = $uibModal.open({
				animation: true,
				templateUrl: "dipAttachment.html",
				size: "lg",
				controller: function ($scope) {
					$scope.modelId = modelId;
					$scope.fileToDelete = fileToDelete;
					$scope.isDeleting = false;
					$scope.successfullyDeleted = false;
					$scope.failedToDelete = false;

					$scope.cancel = function () {
						$scope.failedToDelete = false;
						dialog.dismiss();
					};

					$scope.deleteAttachment = function () {
						$scope.isDeleting = true;
						const attachment_url = './api/v1/attachments/' + $scope.modelId + '/files/' + encodeURIComponent($scope.fileToDelete);

						$http.delete(attachment_url, {
								transformRequest: angular.identity,
								headers: {
									'Content-Type': undefined
								}
							})
							.success(function (data, status, headers, config) {
								$scope.isDeleting = false;
								$scope.successfullyDeleted = true;
								updateAttachments(model);
								$timeout($scope.cancel,1000);
							})
							.error(function (data, status, headers, config) {
								$scope.isDeleting = false;
								$scope.successfullyDeleted = false;
								$scope.failedToDelete = true;
							});
					};
				},
				templateUrl: "deleteAttachmentDialog.html",
				size: "lg"
			});
		};
		
		$scope.diagnoseModel = function () {
			$http.get('./rest/' + $scope.tenantId + '/models/' + $scope.modelId + '/diagnostics')
				.success(function (result) {
					$scope.diagnostics = result;
				});
		};
		
		if ($rootScope.hasAuthority("ROLE_SYS_ADMIN")) { 
			$scope.diagnoseModel();
		}
		
		$scope.getPolicies = function() {
			$http.get('./rest/tenants/' + $scope.tenantId + '/models/' + $scope.modelId + '/policies')
				.success(function (result) {
					$scope.aclEntries = result;
				});
		};
		
		$scope.getUserPolicy = function() {
			$http.get('./rest/tenants/' + $scope.tenantId + '/models/' + $scope.modelId + '/policy')
				.success(function (result) {
					console.log("Policy for model = " + JSON.stringify(result));
					$scope.permission = result.permission;
					if ($scope.model.state === 'InReview' || $scope.model.released === true || $rootScope.authenticated === false || $scope.permission === "READ") {
						$scope.modelEditor.setReadOnly(true);
					}
					
					if ($scope.permission === "FULL_ACCESS" || $rootScope.hasAuthority("ROLE_SYS_ADMIN")) { // load policies only if user is model owner
						$scope.getPolicies();
					}
				}).error(function (data, status, headers, config) {
					// TODO : should be unnecessary
					$scope.permission = "READ";
					if (($scope.model.state === 'InReview' || $scope.model.released === true || $rootScope.authenticated === false || $scope.permission === "READ") && !$rootScope.hasAuthority("ROLE_SYS_ADMIN")) {
						$scope.modelEditor.setReadOnly(true);
					}
					
					if ($rootScope.hasAuthority("ROLE_SYS_ADMIN")) {
						$scope.getPolicies();
					}
				});
		};
		
		$scope.isEditingVisible = function(model) {
			return $rootScope.hasAuthority('ROLE_SYS_ADMIN') || ($scope.permission !== 'READ' && !model.released);
		};
		
		$scope.modifyPermission = function(entry) {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: function ($scope, model, tenantId) {
					$scope.model = model;
					$scope.isLoading = false;
					$scope.editMode = true;
					$scope.applyOnDependencies = false;
					$scope.entry = {
						"permission" : entry.permission,
						"principalId" : entry.principalId,
						"principalType" : entry.principalType
					};
					
					$scope.createEntry = function (entry) {
						$scope.isLoading = true;
						$http.put('./rest/tenants/' + tenantId + '/models/' + model.id.prettyFormat + '/policies',entry)
							.success(function (result) {
								$scope.isLoading = false;
								modalInstance.close();
						});
					};

					$scope.cancel = function () {
						modalInstance.dismiss();
					};
				},
				templateUrl: "webjars/repository-web/dist/partials/dialog/create_policy_entry-dialog.html",
				size: "lg",
				resolve: {
					model: function () {
						return $scope.model;
					},
				    tenantId: function () {
                        return $scope.tenantId;
                    } 
				}
			});
			
			modalInstance.result.then(
				function (data) {
					$scope.getPolicies();
				});
		};
		
		$scope.removePermission = function(entry) {
			$http.delete('./rest/tenants/' + $scope.tenantId + '/models/' + $scope.modelId + '/policies/'+entry.principalId+'/'+entry.principalType)
				.success(function (result) {
					$scope.getPolicies();
				});
		};
		
		$scope.openCreatePolicyEntryDialog = function (model) {
			var modalInstance = $uibModal.open({
				animation: true,
				controller: function ($scope, model, tenantId) {
					$scope.model = model;
					$scope.isLoading = false;
					$scope.editMode = false;
					$scope.applyOnDependencies = false;
					$scope.entry = {
						"permission" : "READ",
						"principalId" : "",
						"principalType" : "User"
					};
					$scope.createEntry = function (entry) {
						if (entry.principalId === $rootScope.user) {
							$scope.errorMessage = "You cannot create policy for yourself!";
							return;
						}
						$scope.isLoading = true;
						$http.put('./rest/tenants/' + tenantId + '/models/' + model.id.prettyFormat + '/policies',entry)
							.success(function (result) {
								$scope.isLoading = false;
								modalInstance.close();
						}).error(function (data, status, headers, config) {
								console.log(status);
								$scope.isLoading = false;
								$scope.errorMessage = data.message;
							});
					};

					$scope.cancel = function () {
						modalInstance.dismiss();
					};
				},
				templateUrl: "webjars/repository-web/dist/partials/dialog/create_policy_entry-dialog.html",
				size: "lg",
				resolve: {
					model: function () {
						return $scope.model;
					},
					tenantId: function() {
					    return $scope.tenantId;
					}
				}
			});
			
			modalInstance.result.then(
				function (data) {
					$scope.getPolicies();
				});
		};
	}
	
]);