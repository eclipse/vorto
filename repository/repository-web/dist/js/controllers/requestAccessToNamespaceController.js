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
        "$location",
        function ($rootScope, $scope, $http, $routeParams, $uibModal,
            $location) {

          // infers whether this page is loaded as modal or standalone
          $scope.modal = $scope.$parent && $scope.$parent.modal;

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

          // initializes scope fields if applicable
          if ($scope.parametrized) {
            $scope.paramUserId = $routeParams.userId;
            $scope.paramOauthProvider = $routeParams.oauthProvider;
            $scope.paramClientName = $routeParams.clientName;
            $scope.paramSubject = $routeParams.subject;
            // no subject provided as parameter - using the client name
            if (!$scope.paramSubject) {
              $scope.paramSubject = $scope.paramClientName;
            }
          }
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

          $scope.reloadPage = function () {
            $location.search({});
          }

          $scope.showPrivacyPolicy = function () {
            var privacyPolicyModalInstance = $uibModal.open({
              animation: true,
              templateUrl: "webjars/repository-web/dist/partials/privacypolicy-dialog.html",
              size: "lg",
              controller: function ($scope) {
                $scope.cancel = function () {
                  privacyPolicyModalInstance.dismiss();
                };
              },
              backdrop: 'static'
            });
          };

          $scope.loadUserData = function () {
            $scope.isLoadingUserData = true;
            $http.get("./rest/accounts/" + $scope.paramUserId)
            .then(
                function (result) {
                  // builds tech user data from result
                  $scope.technicalUser = result.data;
                  // assigns selected user for form submit
                  $scope.selectedUser = $scope.technicalUser.username;
                  // stop loading data
                  $scope.isLoadingUserData = false;
                  // replaces GET parameter values with actual technical user values
                  $scope.paramOauthProvider = $scope.technicalUser.authenticationProvider;
                  $scope.paramSubject = $scope.technicalUser.subject;
                  // disable create user button and change caption
                  $scope.createUserButtonCaption = "Technical user exists";
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

          // starts attempting to load an existing technical user if the form
          // is parametrized
          if ($scope.parametrized) {
            $scope.loadUserData();
          }

          $scope.computeCreateTechnicalUserAvailability = function () {
            let button = document.getElementById(
                "createTechnicalUserButton");
            if (button) {
              button.disabled = !$scope.ackCreateTechnicalUser;
            }
          }

          $scope.computeCreateTechnicalUserAvailability();

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
                  $scope.selectedUser = $scope.technicalUser.username;
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
                  $scope.isCreatingTechnicalUser = false;
                  $scope.selectedUser = null;
                  $scope.computeSubmitAvailability();
                }
            );
          }

          // user data initialization / error handling
          $scope.errorLoadingUserData = false;
          $scope.username = null;
          $scope.loggedInUserSubject = null;
          $scope.selectedSubject = null;

          $scope.initialize = function () {
            // no user data in root scope: loaded as standalone
            // (requires additional networking call)
            if (!$scope.username && (!$rootScope.displayName
                || !$rootScope.userInfo)) {
              $http.get("./user")
              .then(
                  function (result) {
                    // not optional
                    if (result.data.displayName) {
                      $scope.username = result.data.displayName;
                    } else {
                      $scope.errorLoadingUserData = true;
                    }
                    // optional
                    if (result.data.subject) {
                      $scope.selectedSubject = result.data.subject;
                    }
                  },
                  function (error) {
                    $scope.errorLoadingUserData = true;
                  }
              )
            }
            // loaded as modal - user data available from root scope
            else {
              $scope.username = $rootScope.displayName;
              $scope.loggedInUserSubject = $rootScope.userInfo.subject;
              $scope.selectedSubject = $rootScope.userInfo.subject;
            }
          }

          $scope.userRadio = "myself";
          $scope.namespaces = [];
          $scope.selectedNamespace = null;
          $scope.lastHighlightedNamespace = null;
          $scope.namespacePartial = "";
          $scope.userPartial = "";
          $scope.selectedUser = null;
          $scope.retrievedUsers = [];
          $scope.lastHighlightedUser = null;
          $scope.desiredRoles = [true];
          $scope.ack = false;
          $scope.isSendingRequest = false;
          $scope.success = false;
          $scope.initialize();

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
            let element = document.getElementById(namespace.name);
            if (list && element) {
              element.style.backgroundColor = '#7fc6e7';
              element.style.color = '#ffffff';
              list.scrollTop = element.offsetTop - element.clientHeight;
              $scope.lastHighlightedNamespace = namespace;
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
              $scope.selectedUser = user.userId;
              $scope.selectedSubject = user.subject;
              document.getElementById(
                  'userId').value = $scope.selectedUser;
            }
            $scope.retrievedUsers = [];
            $scope.computeSubmitAvailability();
          }

          $scope.highlightUser = function (user) {
            let list = document.getElementById("userDropDown");
            let element = document.getElementById(user.userId);
            if (list && element) {
              element.style.backgroundColor = '#7fc6e7';
              element.style.color = '#ffffff';
              list.scrollTop = element.offsetTop - element.clientHeight;
              $scope.lastHighlightedUser = user;
            }
          }

          $scope.unhighlightUser = function (user) {
            let element = document.getElementById(user.userId);
            if (element) {
              element.style.backgroundColor = 'initial';
              element.style.color = 'initial';
            }
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
            let element = document.getElementById("userId");
            if (element) {
              if (value == "myself") {
                element.disabled = true;
                $scope.selectedUser = $scope.username;
                $scope.selectedSubject = $scope.loggedInUserSubject;
              } else {
                element.disabled = false;
                $scope.userPartial = "";
                $scope.selectedUser = null;
                $scope.selectedSubject = null;
              }
            }
            $scope.computeSubmitAvailability();
          };

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
              document.getElementById("myself").checked = true;
            }, 200);
          });

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
              'requestingUsername': $scope.username,
              'targetUsername': $scope.selectedUser,
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

        }
      ]);

});