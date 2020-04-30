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
define(["../init/appService"], function(repository) {

repository.factory("dialogConfirm",
    ["$rootScope", "$uibModal",
    function($rootScope, $uibModal) {
    	return function($scope, confirmationMsg, actions) {
    		var dialog = {};
    		
    		dialog.callbacks = [];
    		
    		dialog.run = function() {
    			var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: "webjars/repository-web/dist/partials/dialog/confirmation_dialog.html",
                    size: "sm",
                    controller: function($scope) {
                    	$scope.confirmationMsg = confirmationMsg;
                    	$scope.actions = actions;
                    	
                    	$scope.cancel = function() {
                            modalInstance.dismiss("Canceled.");  
                        };
                        
                        $scope.buttonClick = function(action) {
                        	modalInstance.close(action);
                        };
                    }
    			});
    			
    			modalInstance.result.then(function(result) {
    				for(var i = 0; i < dialog.callbacks.length; i++) {
    					if (dialog.callbacks[i].actionName === result) {
    						dialog.callbacks[i].callback();
    					}
    				}
    			});
    		};
    		
    		dialog.setCallback = function(action, fn) {
    			dialog.callbacks.push({ actionName: action, callback: fn})
    		};
    		
    		return dialog;
    	}
    }
]);

repository.factory("dialogPrompt",
    ["$rootScope", "$uibModal",
    function($rootScope, $uibModal) {
    	return function($scope, promptTitle, promptMsg, actions) {
    		var dialog = {};
    		
    		dialog.callbacks = [];
    		
    		dialog.run = function() {
    			var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: "webjars/repository-web/dist/partials/dialog/prompt_dialog.html",
                    size: "sm",
                    controller: function($scope) {
                    	$scope.prompt = {};
                    	$scope.prompt.title = promptTitle;
                    	$scope.prompt.msg = promptMsg;
                    	$scope.prompt.reply = "";
                    	$scope.actions = actions;
                    	
                    	$scope.cancel = function() {
                            modalInstance.dismiss("Canceled.");  
                        };
                        
                        $scope.buttonClick = function(action) {
                        	modalInstance.close({ 
                        			action: action,
                        			promptReply: $scope.prompt.reply
                        		});
                        };
                    }
    			});
    			
    			modalInstance.result.then(function(result) {
    				for(var i = 0; i < dialog.callbacks.length; i++) {
    					if (dialog.callbacks[i].actionName === result.action) {
    						dialog.callbacks[i].callback(result.promptReply);
    					}
    				}
    			});
    		};
    		
    		dialog.setCallback = function(action, fn) {
    			dialog.callbacks.push({ actionName: action, callback: fn})
    		};
    		
    		return dialog;
    	}
    }
]);

repository.factory("confirmPublish",
    ["$rootScope", "$uibModal",
    function($rootScope, $uibModal) {
    	return function($scope) {
    		var dialog = {};
    		
    		dialog.callbacks = [];
    		
    		dialog.run = function() {
    			var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: "webjars/repository-web/dist/partials/dialog/confirmation_publish.html",
                    size: "lg",
                    controller: function($scope) {
                    	$scope.cancel = function() {
                            modalInstance.dismiss("Canceled.");  
                        };
                        
                        $scope.buttonClick = function(action) {
                        	modalInstance.close(action);
                        };
                    }
    			});
    			
    			modalInstance.result.then(function(result) {
    				for(var i = 0; i < dialog.callbacks.length; i++) {
    					if (dialog.callbacks[i].actionName === result) {
    						dialog.callbacks[i].callback();
    					}
    				}
    			});
    		};
    		
    		dialog.setConfirmCallback = function(fn) {
    			dialog.callbacks.push({ actionName: "confirm", callback: fn})
    		};
    		
    		dialog.setCancelCallback = function(fn) {
    			dialog.callbacks.push({ actionName: "cancel", callback: fn})
    		};
    		
    		return dialog;
    	}
    }
]);

});
	    