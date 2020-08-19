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
define(["../../init/appController"], function (repositoryControllers) {

  repositoryControllers.controller("namespaceUserManagementController",
      ["$rootScope", "$scope", "$http", "$uibModal", "namespace",
        "dialogConfirm", "$routeParams", "modal", "$location",
        function ($rootScope, $scope, $http, $uibModal, namespace,
            dialogConfirm, $routeParams, modal, $location) {

          // retrieves logged on user's name
          $scope.currentUser = null;

          // infers whether this page is loaded as modal or standalone
          $scope.modal = modal;
          // trying first with injected namespace object if present
          // note that we may be returning to this page so the $scope variable
          // may already exist
          if (!$scope.namespace) {
            $scope.namespace = namespace;
          }
          // handling namespace object injected as null, aka in standalone mode
          if (!$scope.namespace && $routeParams.namespace) {
            $scope.namespace = {
              name: $routeParams.namespace
            }
          }

          /*
           Target user to provide access to: this will be used by the standalone
           parametrized version of the form, typically through a  link received
           by e-mail. When present, the form will attempt to open the modal to
           add/update a user and pre-populate the child modal with the relevant
           data.
           */
          $scope.targetUsername = $routeParams.targetUser;
          // will be constructed based on retrieval of targetUsername in namespace
          // collaborators if present, or by calling the back-end otherwise
          $scope.targetUser = null;
          // this flag will signal an error retrieving the user if that fails
          $scope.failedToRetrieveUser = false;

          $scope.desiredRoles = $routeParams.desiredRoles
              ? $routeParams.desiredRoles.split(",") : null;

          // flag to infer whether user can manage the given namespace
          // proper authorization takes place in the back-end.
          // this is a weak/cosmetic feature to prevent even viewing the
          // collaborators and buttons if one is not admin of the namespace
          $scope.authorized = null;

          // Flag to check if a user is attempting to edit themselves as
          // collaborator trough standalone form parametrization.
          // Note that the real authorization is in the back-end - this just
          // provides an early stop.
          $scope.editingSelf = false;

          $scope.isRetrievingNamespaceUsers = false;
          $scope.namespaceUsers = [];

          // if in standalone mode, we want an additional authorization check
          // on whether the logged on user manages the given namespace, and fail
          // to load anything otherwise
          if (!$scope.modal && $scope.namespace) {
            $http.get(
                "./rest/namespaces/namespace_admin/" + $scope.namespace.name)
            .then(
                // returns boolean
                function (result) {
                  $scope.authorized = result.data;
                  if ($scope.authorized) {
                    // retrieves existing collaborators for the namespace
                    $scope.initialize($scope.namespace.name);
                  }
                },
                function (error) {
                  $scope.authorized = false;
                }
            )
          } else {
            $scope.authorized = true;
          }

          $scope.retrieveNamespaceUsers = function(namespacename) {
            $http.get("./rest/namespaces/" + namespacename + "/users")
            .then(
                function (result) {
                  $scope.isRetrievingNamespaceUsers = false;
                  $scope.namespaceUsers = result.data;
                  // checks if user attempting to edit themselves as collaborator
                  if ($scope.targetUsername && $scope.targetUsername
                      == $scope.currentUser.name) {
                    $scope.editingSelf = true;
                    return;
                  }
                  // defines target user by target username and either existing
                  // data or data to be fetched
                  // checks if given user name is present in retrieved users
                  if (
                      $scope.targetUsername &&
                      $scope.namespaceUsers.map(u => u.userId).includes(
                          $scope.targetUsername)
                  ) {
                    $scope.targetUser = $scope.namespaceUsers.find(
                        e => e.userId == $scope.targetUsername);
                    // if the form is parametrized with a user, then we jump to
                    // the edit or add modal

                    // replaces the target user's roles with the desired roles
                    // if present
                    if ($scope.desiredRoles) {
                      $scope.targetUser.roles = $scope.desiredRoles;
                    }
                    $scope.createOrUpdateUser(
                        $scope.editableUser($scope.targetUser));
                  }
                  // need to load the user from the back-end
                  else if ($scope.targetUsername) {
                    $http.get("./rest/accounts/" + $scope.targetUsername)
                    .then(
                        function (result) {
                          $scope.targetUser = result.data;
                          // replaces the target user's roles with the desired roles
                          // if present
                          if ($scope.desiredRoles) {
                            $scope.targetUser.roles = $scope.desiredRoles;
                          }
                          // if the form is parametrized with a user, then we jump to
                          // the edit or add modal
                          $scope.createOrUpdateUser(
                              editableUser($scope.targetUser));
                        },
                        function (error) {
                          // will need to add a user manually at this point
                          $scope.failedToRetrieveUser = true;
                        }
                    );
                  }
                },
                function (reason) {
                  $scope.isRetrievingNamespaceUsers = false;
                  // TODO : handling of failures
                }
            );
          }

          $scope.initialize = function (namespacename) {
            // no namespace provided or retrieved - redirect to manage
            if (!namespacename) {
              $location.path("/manage");
              return;
            }
            $scope.isRetrievingNamespaceUsers = true;
            if (!$scope.currentUser) {
              // first, initialize the information about the logged in user
              $http.get("./user")
              .then(
                  function (result) {
                    $scope.currentUser = result.data;
                    $scope.retrieveNamespaceUsers(namespacename);
                  },
                  function (error) {
                    $scope.failedToRetrieveUser = true;
                  }
              );
            }
            else {
              $scope.retrieveNamespaceUsers(namespacename);
            }
          };

          if ($scope.authorized) {
            $scope.initialize($scope.namespace ? $scope.namespace.name : null);
          }

          $scope.newUser = function () {
            return {
              edit: false,
              username: "",
              roleModelCreator: false,
              roleModelPromoter: false,
              roleModelReviewer: false,
              roleModelPublisher: false,
              roleUser: true,
              roleAdmin: false
            };
          };

          $scope.editableUser = function (user) {
            return {
              edit: true,
              userId: user.userId,
              roleModelCreator: user.roles.includes("model_creator"),
              roleModelPromoter: user.roles.includes("model_promoter"),
              roleModelReviewer: user.roles.includes("model_reviewer"),
              roleModelPublisher: user.roles.includes("model_publisher"),
              roleUser: user.roles.includes("model_viewer"),
              roleAdmin: user.roles.includes("namespace_admin")
            };
          };

          $scope.createOrUpdateUser = function (user) {
            $scope.mode = user.edit ? "Update" : "Add";
            let modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "webjars/repository-web/dist/partials/admin/createOrUpdateUser.html",
              size: "md",
              controller: "createOrUpdateUserController",
              resolve: {
                user: function () {
                  return user;
                },
                namespace: function () {
                  return $scope.namespace;
                }
              }
            });

            modalInstance.result.finally(function (result) {
              // resets query params and internal params derived from route
              $location.search({namespace: $scope.namespace.name});
              $scope.targetUsername = null;
              $scope.targetUser = null;
              $scope.suggestedRoles = null;
              $scope.retrieveNamespaceUsers($scope.namespace.name);
            });
          };

          $scope.deleteUser = function (user) {
            var dialog = dialogConfirm($scope,
                "Are you sure you want to remove '" + user.userId
                + "' as a collaborator from the namespace?",
                ["Confirm", "Cancel"]);

            dialog.setCallback("Confirm", function () {
              $http
              .delete("./rest/namespaces/" + $scope.namespace.name + "/users/"
                  + user.userId)
              .then(
                  function (result) {
                    $scope.initialize($scope.namespace.name);
                  },
                  function (reason) {
                    // TODO : Show error on window
                  }
              );
            });

            dialog.run();
          };

          $scope.hasUserRole = function (role, roles) {
            return roles.includes(role);
          }
        }
      ]);

  repositoryControllers.controller("createOrUpdateUserController",
      ["$rootScope", "$scope", "$uibModal", "$uibModalInstance", "$http",
        "user", "namespace",
        function ($rootScope, $scope, $uibModal, $uibModalInstance, $http, user,
            namespace) {

          $scope.mode = user.edit ? "Update" : "Add";
          $scope.user = user;
          $scope.namespace = namespace;
          $scope.isCurrentlyAddingOrUpdating = false;
          $scope.errorMessage = "";
          $scope.selectedAuthenticationProviderId = null;
          $scope.userPartial = "";
          $scope.selectedUser = null;
          $scope.lastHighlightedUser = null;
          $scope.retrievedUsers = [];
          $scope.technicalUserSubject = null;

          $scope.selectUser = function (user) {
            if (user) {
              $scope.selectedUser = user;
              document.getElementById(
                  'userId').value = $scope.selectedUser.userId;
            }
            $scope.retrievedUsers = [];
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

          $scope.selectOtherUser = function (event, selected) {
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
              $http.get("./rest/accounts/search/" + $scope.userPartial)
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
            $scope.toggleSubmitButton();
          };

          $scope.cancel = function () {
            $uibModalInstance.dismiss("Canceled.");
          };

          $scope.promptCreateNewTechnicalUser = function () {
            let modalInstance = $uibModal.open({
              animation: true,
              templateUrl: "webjars/repository-web/dist/partials/admin/createTechnicalUser.html",
              size: "md",
              controller: "createTechnicalUserController",
              resolve: {
                user: function () {
                  return user;
                },
                namespace: function () {
                  return namespace;
                },
                context: function () {
                  return $rootScope.context;
                }
              }
            });

            modalInstance.result.finally(function (result) {
              $uibModalInstance.close($scope.user);
            });
          };

          $scope.toggleSubmitButton = function () {
            let button = document.getElementById("submitButton");
            if (button) {
              button.disabled = !(
                  ($scope.user && $scope.user.userId)
                  ||
                  $scope.userPartial
                  ||
                  $scope.selectedUser
              );
            }
            return button && !button.disabled;
          }

          $scope.addOrUpdateUser = function () {
            // adds username to scope user by either using selected user from
            // drop-down if any, or using the string in user's input     box
            if ($scope.selectedUser) {
              $scope.user.userId = $scope.selectedUser.userId;
            } else if ($scope.userPartial) {
              $scope.user.userId = $scope.userPartial;
            }
            $scope.validate($scope.user, function (result) {
              if (result.valid) {
                $scope.isCurrentlyAddingOrUpdating = false;
                $http.put(
                    "./rest/namespaces/" + $scope.namespace.name + "/users", {
                      "userId": $scope.user.userId,
                      "roles": $scope.getRoles($scope.user)
                    })
                .then(
                    function (result) {
                      $uibModalInstance.close($scope.user);
                    },
                    function (reason) {
                      $scope.errorMessage = "You cannot change your own permissions.";
                    }
                );
              } else {
                $scope.promptCreateNewTechnicalUser();
              }
            });
          };

          $scope.getRoles = function (user) {
            var roles = [];
            if ($scope.user.roleModelCreator) {
              roles.push("model_creator")
            }

            if ($scope.user.roleModelPromoter) {
              roles.push("model_promoter")
            }

            if ($scope.user.roleModelReviewer) {
              roles.push("model_reviewer")
            }

            if ($scope.user.roleModelPublisher) {
              roles.push("model_publisher")
            }

            if ($scope.user.roleUser) {
              roles.push("model_viewer")
            }

            if ($scope.user.roleAdmin) {
              roles.push("namespace_admin")
            }

            return roles;
          };

          $scope.validate = function (user, callback) {
            if (user.userId === undefined || user.userId.trim() === '') {
              callback({
                valid: false,
                errorMessage: "userId must not be null."
              });
              return;
            }

            $http.get("./rest/accounts/" + user.userId)
            .then(
                function (result) {
                  callback({valid: true});
                },
                function (reason) {
                  if (reason.status == 404) {
                    callback({
                      valid: false,
                      errorMessage: "User doesn't exist."
                    });
                  } else {
                    callback({
                      valid: false,
                      errorMessage: "Error while accessing the server."
                    });
                  }
                }
            );
          };
        }
      ]);
});
