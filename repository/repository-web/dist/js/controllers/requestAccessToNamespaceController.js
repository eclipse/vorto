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
define(["../init/appController"], function (repositoryControllers) {

  repositoryControllers.controller("requestAccessToNamespaceController",
      ["$rootScope", "$scope", "$http", "$routeParams", "$uibModal",
        "$location", "modal", "$window",
        function ($rootScope, $scope, $http, $routeParams, $uibModal,
            $location, modal, $window) {

          // defines the currently logged on user
          $scope.currentUser = null;

          // infers whether this page is loaded as modal or standalone
          $scope.modal = modal;

          // general error data
          $scope.errorMessage = null;
          $scope.error = false;

          // technical user error data when parametrized (has to appear
          // in a different place than general error)
          $scope.createTechnicalUserError = false;
          $scope.createTechnicalUserErrorMessage = null;

          // parses GET parameters if any and assigns/digests values
          // for usage with external service integrations
          $scope.paramUserId = null;
          $scope.paramOauthProvider = null;
          $scope.paramClientName = null;
          $scope.paramSubject = null;
          $scope.parametrized = $routeParams.userId;

          // parametrizable also, optionally
          $scope.selectedNamespace = null;
          $scope.namespacePartial = "";

          $scope.userRadio = "myself";
          $scope.namespaces = [];
          $scope.lastHighlightedNamespace = null;
          $scope.userPartial = "";
          $scope.selectedUser = null;
          $scope.retrievedUsers = [];
          $scope.lastHighlightedUser = null;
          $scope.desiredRoles = [true];
          $scope.ack = false;
          $scope.isSendingRequest = false;
          $scope.success = false;

          // defaults flag to display a loading overlay if parametrized
          $scope.isLoadingUserData = $scope.parametrized;
          $scope.isCreatingTechnicalUser = false;

          // object representing the technical user that either exists already,
          // or the one to create
          $scope.technicalUser = null;

          // default caption for the "create technical user button" - may change
          // if the user exists already or has just been created
          $scope.createUserButtonCaption = "Create...";
          // legalese before creating the technical user
          $scope.ackCreateTechnicalUser = false;

          // user data initialization / error handling
          $scope.errorLoadingUserData = false;
          $scope.selectedSubject = null;

          /**
           * Fetches the required information about the currently logged on user.
           * In some cases, this will already be available in the root scope.
           * In other cases, this requires an additional REST call to the
           * back-end.
           */
          $scope.getCurrentUser = function() {
            if (
                $rootScope && $rootScope.userInfo && $rootScope.userInfo.name &&
                $rootScope.userInfo.provider && $rootScope.userInfo.provider.id) {
              $scope.currentUser = {
                "username" : $rootScope.userInfo.name,
                "displayName" : $rootScope.userInfo.displayName,
                "authenticationProvider": $rootScope.userInfo.provider.id,
                // optional
                "subject": $rootScope.userInfo.subject
              }
            }
            else {
              $http.get("./user")
              .then(
                  function (result) {
                    // not optional
                    if (result.data) {
                      $scope.currentUser = {
                        "username" : result.data.name,
                        "displayName": result.data.displayName,
                        "authenticationProvider": result.data.provider.id,
                        // optional
                        "subject": result.data.subject
                      }
                    } else {
                      $scope.errorLoadingUserData = true;
                    }
                    // eagerly sets the subject in case the request will be for
                    // the current user and the current user has one
                    if ($scope.currentUser.subject) {
                      $scope.selectedSubject = $scope.currentUser.subject;
                    }
                  },
                  function (error) {
                    $scope.errorLoadingUserData = true;
                  }
              )
            }
          }

          /**
           * Infers whether the form is being loaded standalone and parametrized,
           * and populates data accordingly when parametrization is valid.
           */
          $scope.consumeParametrization = function() {
            // initializes scope fields if applicable
            if ($scope.parametrized) {
              $scope.paramUserId = $routeParams.userId;
              $scope.paramOauthProvider = $routeParams.oauthProvider;
              $scope.paramClientName = $routeParams.clientName;
              $scope.paramSubject = $routeParams.subject;
              // checks if namespace specified
              if ($routeParams.namespace) {
                $scope.namespacePartial = $routeParams.namespace;
                $scope.selectedNamespace = {
                  name: $scope.namespacePartial
                };
              }
              // no subject provided as parameter - using the client name
              if (!$scope.paramSubject) {
                $scope.paramSubject = $scope.paramClientName;
              }
            }
          }

          $scope.reloadPage = function () {
            $location.search({});
            $window.location.reload();
          }

          /**
           * Loads data about a specific user (not the logged on user).
           */
          $scope.loadUserData = function () {
            $scope.isLoadingUserData = true;
            let path = $scope.paramUserId;
            if ($scope.paramOauthProvider) {
              path += "/" + $scope.paramOauthProvider;
            }
            $http.get("./rest/accounts/" + path)
            .then(
                function (result) {
                  // builds tech user data from result
                  $scope.technicalUser = result.data;
                  // assigns selected user for form submit
                  $scope.selectedUser = $scope.technicalUser;
                  // stop loading data
                  $scope.isLoadingUserData = false;
                  // replaces GET parameter values with actual technical user values
                  $scope.paramOauthProvider = $scope.technicalUser.authenticationProvider;
                  $scope.paramSubject = $scope.technicalUser.subject;
                  // disable create user button and change caption
                  $scope.createUserButtonCaption = "User exists";
                  let button = document.getElementById(
                      "createTechnicalUserButton");
                  if (button) {
                    button.disabled = true;
                  }

                  // hides the technical user creation legalese
                  let div = document.getElementById(
                      "ackCreateTechnicalUserDiv");
                  if (div) {
                    div.style.visibility = "hidden";
                  }
                  // verifies form submittable or not
                  $scope.computeSubmitAvailability();
                },
                function (error) {
                  // no error handling here: the user simply doesn't exist (yet)
                  $scope.technicalUser = null;
                  $scope.isLoadingUserData = false;
                  $scope.selectedUser = null;
                  $scope.computeSubmitAvailability();
                }
            )
          }

          $scope.computeCreateTechnicalUserAvailability = function () {
            let button = document.getElementById(
                "createTechnicalUserButton");
            if (button) {
              button.disabled =
                $scope.technicalUser ||
                !$scope.ackCreateTechnicalUser ||
                !$scope.paramUserId ||
                !$scope.paramOauthProvider;
            }
          }

          $scope.createTechnicalUser = function () {
            $scope.isCreatingTechnicalUser = true;
            $http.post('./rest/accounts/createTechnicalUser',
                {
                  "username": $scope.paramUserId,
                  "authenticationProvider": $scope.paramOauthProvider,
                  "subject": $scope.paramSubject,
                  "isTechnicalUser": true,
                  "dateCreated": null,
                  "lastUpdated": null,
                  "email": ""
                }
            )
            .then(
                function (result) {
                  // ignoring result message
                  $scope.technicalUser =
                      {
                        "username": $scope.paramUserId,
                        "authenticationProvider": $scope.paramOauthProvider,
                        "subject": "", // TODO if we decide to parametrize with subject
                        "isTechnicalUser": true
                      };
                  $scope.selectedUser = $scope.technicalUser;
                  $scope.isCreatingTechnicalUser = false;
                  // disable create user button and change caption
                  $scope.createUserButtonCaption = "Technical user created";
                  let button = document.getElementById(
                      "createTechnicalUserButton");
                  if (button) {
                    button.disabled = true;
                  }
                  // disabling ack checkbox as it cannot be "taken back" once
                  // the tech user is created
                  let checkbox = document.getElementById(
                      "ackCreateTechnicalUser");
                  if (checkbox) {
                    checkbox.disabled = true;
                  }
                  $scope.computeSubmitAvailability();
                },
                function (error) {
                  $scope.createTechnicalUserError = true;
                  if (error.data) {
                    $scope.createTechnicalUserErrorMessage = error.data.errorMessage;
                  }
                  // disables button if tech user already exists
                  if (error.status && error.status == 409) {
                    $scope.createUserButtonCaption = "Technical user exists";
                    let button = document.getElementById(
                        "createTechnicalUserButton");
                    if (button) {
                      button.disabled = true;
                    }
                  }
                  $scope.isCreatingTechnicalUser = false;
                  $scope.selectedUser = null;
                  $scope.computeSubmitAvailability();
                }
            );
          }

          $scope.computeSubmitAvailability = function () {
            let element = document.getElementById("submit");
            if (element) {
              element.disabled = !$scope.selectedNamespace
                  || !$scope.selectedUser || !$scope.ack;
            }
          }

          $scope.focusOnNamespaceSearch = function () {
            let element = document.getElementById("namespaceSearch");
            if (element) {
              element.focus();
            }
          }

          $scope.highlightNamespace = function (namespace) {
            let list = document.getElementById("namespaceDropdown");
            if (namespace) {
              let element = document.getElementById(namespace.name);
              if (list && element) {
                element.style.backgroundColor = '#7fc6e7';
                element.style.color = '#ffffff';
                list.scrollTop = element.offsetTop - element.clientHeight;
                $scope.lastHighlightedNamespace = namespace;
              }
            }
          }

          $scope.unhighlightNamespace = function (namespace) {
            let element = document.getElementById(namespace.name);
            if (element) {
              element.style.backgroundColor = 'initial';
              element.style.color = 'initial';
            }
          }

          $scope.selectNamespace = function (namespace) {
            if (namespace) {
              $scope.selectedNamespace = namespace;
              document.getElementById(
                  'namespaceSearch').value = $scope.selectedNamespace.name;
            }
            $scope.namespaces = [];
            $scope.computeSubmitAvailability();
          }

          $scope.selectOtherNamespace = function (event, selected) {
            let length = $scope.namespaces.length;
            let key = event.key;
            if (length == 1 && key !== 'Enter') {
              $scope.highlightNamespace($scope.namespaces[0]);
              return;
            }
            let nextOrPrevious = null;
            if (key === 'ArrowUp') {
              nextOrPrevious = false;
            } else if (key === 'ArrowDown') {
              nextOrPrevious = true;
            } else if (key === 'Enter') {
              if ($scope.lastHighlightedNamespace) {
                $scope.selectNamespace($scope.lastHighlightedNamespace);
                return;
              }
            }
            if ($scope.namespaces && nextOrPrevious !== null) {
              // if this is invoked from the input text, no namespace is selected
              let nextIndex = nextOrPrevious ? 0 : length - 1;
              if (selected) {
                let selectedIndex = $scope.namespaces.indexOf(selected);
                // increment/decrement
                nextIndex = nextOrPrevious ? selectedIndex + 1
                    : selectedIndex - 1;
                // handle rotation
                if (nextIndex >= length) {
                  nextIndex = 0;
                } else if (nextIndex < 0) {
                  nextIndex = length - 1;
                }
              }
              // highlighting target list element
              $scope.highlightNamespace($scope.namespaces[nextIndex]);
              // unhighlighting previously highlighted element - forward
              if (nextOrPrevious) {
                $scope.unhighlightNamespace(
                    $scope.namespaces[nextIndex > 0 ? nextIndex - 1 : length
                        - 1]);
              } else {
                $scope.unhighlightNamespace(
                    $scope.namespaces[nextIndex < length - 1 ? nextIndex + 1
                        : 0]);
              }
            }
          }

          $scope.selectOtherTechnicalUser = function (event, selected) {
            let length = $scope.retrievedUsers.length;
            let key = event.key;
            if (length == 1 && key !== 'Enter') {
              $scope.highlightUser($scope.retrievedUsers[0]);
              return;
            }
            let nextOrPrevious = null;
            if (key === 'ArrowUp') {
              nextOrPrevious = false;
            } else if (key === 'ArrowDown') {
              nextOrPrevious = true;
            } else if (key === 'Enter') {
              if ($scope.lastHighlightedUser) {
                $scope.selectUser($scope.lastHighlightedUser);
                return;
              }
            }
            if ($scope.retrievedUsers && nextOrPrevious !== null) {
              // if this is invoked from the input text, no namespace is selected
              let nextIndex = nextOrPrevious ? 0 : length - 1;
              if (selected) {
                let selectedIndex = $scope.retrievedUsers.indexOf(selected);
                // increment/decrement
                nextIndex = nextOrPrevious ? selectedIndex + 1
                    : selectedIndex - 1;
                // handle rotation
                if (nextIndex >= length) {
                  nextIndex = 0;
                } else if (nextIndex < 0) {
                  nextIndex = length - 1;
                }
              }
              // highlighting target list element
              $scope.highlightUser($scope.retrievedUsers[nextIndex]);
              // unhighlighting previously highlighted element - forward
              if (nextOrPrevious) {
                $scope.unhighlightUser(
                    $scope.retrievedUsers[nextIndex > 0 ? nextIndex - 1 : length
                        - 1]);
              } else {
                $scope.unhighlightUser(
                    $scope.retrievedUsers[nextIndex < length - 1 ? nextIndex + 1
                        : 0]);
              }
            }
          }

          $scope.findNamespaces = function () {
            // only initiates user search if partial name is larger >= 4 characters
            // this is to prevent unmanageably large drop-downs
            if ($scope.namespacePartial && $scope.namespacePartial.length
                >= 4) {
              $http.get("./rest/namespaces/search/" + $scope.namespacePartial)
              .then(
                  function (result) {
                    if (result.data) {
                      $scope.namespaces = result.data;
                    } else {
                      $scope.namespaces = [];
                      $scope.selectedNamespace = {};
                    }
                  },
                  function (error) {
                  }
              );
            } else {
              $scope.namespaces = [];
              $scope.selectedNamespace = null;
            }
            $scope.computeSubmitAvailability();
          }

          $scope.selectUser = function (user) {
            if (user) {
              $scope.selectedUser = user;
              $scope.selectedSubject = user.subject;
              document.getElementById(
                  'userId').value = $scope.selectedUser.displayName;
            }
            $scope.retrievedUsers = [];
            $scope.computeSubmitAvailability();
          }

          $scope.highlightUser = function (user) {
            let list = document.getElementById("userDropDown");
            let element = document.getElementById(user.displayName);
            if (list && element) {
              element.style.backgroundColor = '#7fc6e7';
              element.style.color = '#ffffff';
              list.scrollTop = element.offsetTop - element.clientHeight;
              $scope.lastHighlightedUser = user;
            }
          }

          $scope.unhighlightUser = function (user) {
            let element = document.getElementById(user.displayName);
            if (element) {
              element.style.backgroundColor = 'initial';
              element.style.color = 'initial';
            }
          }

          $scope.openCreateTechnicalUserDialog = function () {
            let modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "webjars/repository-web/dist/partials/admin/createTechnicalUser.html",
              size: "md",
              controller: "createTechnicalUserController",
              resolve: {
                user: function () {
                  return {
                    "userId": $scope.userPartial
                  };
                },
                namespace: function () {
                  return null;
                },
                context: function () {
                  return $rootScope.context;
                }
              }
            });
            // if the modal returns with a "user" created, selects it
            modalInstance.result.then(
                function (result) {
                  $scope.selectUser(result);
                }
            );

          }

          $scope.clearUserSearch = function () {
            $scope.userPartial = "";
            $scope.selectedUser = null;
            $scope.retrievedUsers = [];
          }

          $scope.findUsers = function () {
            // only initiates user search if partial name is larger >= 3 characters
            // this is to prevent unmanageably large drop-downs
            if ($scope.userPartial && $scope.userPartial.length >= 3) {
              $http.get("./rest/accounts/search/" + $scope.userPartial
                  + "?onlyTechnicalUsers=true")
              .then(
                  function (result) {
                    if (result.data) {
                      $scope.retrievedUsers = result.data;
                      // adds display name and finds duplicate usernames / adds
                      // oauth provider id to display name
                      $scope.retrievedUsers.forEach(function(value, index, array) {
                        // only suitable for 2+ element arrays
                        if (array.length < 2) {
                          return;
                        }
                        // sets a display value as userId regardless
                        value.displayName = value.userId;
                        // decorates duplicates
                        if (index > 0 && array[index - 1].userId == value.userId) {
                          value.displayName = value.userId + " ("
                              + value.authenticationProviderId + ")";
                          array[index - 1].displayName = array[index - 1].userId
                              + " (" + array[index - 1].authenticationProviderId
                              + ")";
                        }
                      });
                    } else {
                      $scope.retrievedUsers = [];
                      $scope.selectedUser = null;
                    }
                  },
                  function (error) {
                  }
              );
            } else {
              $scope.retrievedUsers = [];
              $scope.selectedUser = null;
            }
            $scope.computeSubmitAvailability();
          };

          /**
           * This does two things:
           * 1) toggles between user search box enabled/disabled based on radio
           * 2) sets the selected user according to radio (can be undefined)
           */
          $scope.toggleUserSearchEnabled = function (value) {
            let userSearch = document.getElementById("userId");
            let userSearchButton = document.getElementById(
                "technicalUserSearchButton");
            let userCreateButton = document.getElementById(
                "technicalUserCreateButton");
            let clearUserSearchButton = document.getElementById(
                "technicalUserClearButton");

            if (userSearch) {
              if (value == "myself") {
                userSearch.disabled = true;
                if (userSearchButton) {
                  userSearchButton.disabled = true;
                }
                if (userCreateButton) {
                  userCreateButton.disabled = true;
                }
                if (clearUserSearchButton) {
                  clearUserSearchButton.disabled = true;
                }
                $scope.selectedUser = $scope.currentUser;
                $scope.selectedSubject = $scope.currentUser.subject;
              } else {
                userSearch.disabled = false;
                if (userSearchButton) {
                  userSearchButton.disabled = false;
                }
                if (userCreateButton) {
                  userCreateButton.disabled = false;
                }
                if (clearUserSearchButton) {
                  clearUserSearchButton.disabled = false;
                }
                $scope.userPartial = "";
                $scope.selectedUser = null;
                $scope.selectedSubject = null;
              }
            }
            $scope.computeSubmitAvailability();
          };

          // ugly
          $scope.disableAndCheckOtherCheckBoxes = function () {
            let toggle = $scope.desiredRoles[5];
            let element = document.getElementById("roleView");
            if (element) {
              element.checked = toggle;
              element.disabled = toggle;
            }
            element = document.getElementById("roleCreate");
            if (element) {
              element.checked = toggle;
              element.disabled = toggle;
            }
            element = document.getElementById("roleRelease");
            if (element) {
              element.checked = toggle;
              element.disabled = toggle;
            }
            element = document.getElementById("roleReview");
            if (element) {
              element.checked = toggle;
              element.disabled = toggle;
            }
            element = document.getElementById("rolePublish");
            if (element) {
              element.checked = toggle;
              element.disabled = toggle;
            }
          }

          $scope.submit = function () {
            // roles to convey if any
            const allRoles = ['model_viewer', 'model_creator', 'model_promoter',
              'model_reviewer', 'model_publisher', 'namespace_admin'];
            let rolesToConvey = [];
            // namespace_admin implies all roles
            if ($scope.desiredRoles[5]) {
              rolesToConvey = allRoles;
            } else {
              let i = 0;
              for (i = 0; i < allRoles.length; i++) {
                if ($scope.desiredRoles[i]) {
                  rolesToConvey.push(allRoles[i]);
                }
              }
            }

            let payload = {
              'requestingUser': $scope.currentUser,
              'targetUser': $scope.selectedUser,
              'namespaceName': $scope.selectedNamespace.name,
              'suggestedRoles': rolesToConvey,
              'conditionsAcknowledged': $scope.ack,
              'targetSubject': $scope.selectedSubject
            };
            $scope.isSendingRequest = true;

            $http
            .post("./rest/namespaces/requestAccess", payload)
            .then(
                function (result) {
                  $scope.isSendingRequest = false;
                  $scope.success = true;
                  $scope.disableCancelButton();
                  $scope.disableSendButton();
                },
                function (reason) {
                  $scope.isSendingRequest = false;
                  $scope.error = true;
                  if (reason) {
                    $scope.errorMessage = reason.data.errorMessage;
                    switch (reason.status) {
                        // no e-mails present among admins - preventing user from sending again
                      case 412: {
                        $scope.disableSendButton();
                        break;
                      }
                        // e-mail could not be sent - preventing user from sending again right away
                      case 503: {
                        $scope.disableSendButton();
                        break;
                      }
                    }
                  } else {
                    $scope.errorMessage = 'Request failed for unknown reason. Please try again later.';
                    $scope.disableCancelButton();
                    $scope.disableSendButton();
                  }
                }
            );
          };

          $scope.disableCancelButton = function () {
            let cancelButton = document.getElementById("cancel");
            if (cancelButton) {
              cancelButton.disabled = true;
            }
          }

          $scope.disableSendButton = function () {
            let sendButton = document.getElementById("submit");
            if (sendButton) {
              sendButton.disabled = true;
            }
          }

          /**
           * Performs basic initialization of the controller's required data.
           */
          $scope.initialize = function () {
            $scope.getCurrentUser();
            $scope.consumeParametrization();
            // starts attempting to load an existing technical user if the form
            // is parametrized
            if ($scope.parametrized) {
              $scope.loadUserData();
            }
            $scope.computeCreateTechnicalUserAvailability();
          }

          $scope.initialize();

          /**
           * Initialization procedures that require the DOM to be ready prior.
           */
          angular.element(document).ready(function () {
            $scope.focusOnNamespaceSearch();
            $scope.computeSubmitAvailability();
            $scope.toggleUserSearchEnabled('myself');
            $scope.computeCreateTechnicalUserAvailability();

            // this is incredibly stupid but neither ng-model, nor ng-checked,
            // nor HTML checked, nor plain Javascript work - some unknown
            // AngularJS event handler occurring after DOM ready prevents
            // checking the default radio
            setTimeout(() => {
              // still possible to have undefined on DOM ready in certain cases
              let element = document.getElementById("myself");
              if (element) {
                element.checked = true;
              }
            }, 200);
          });

        }
      ]);

});
