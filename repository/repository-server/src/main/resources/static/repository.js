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
define("repository", [
  "angular",
  "angular-route",
  "angular-animate",
  "angular-bootstrap",
  "angular-bootstrap-templates",
  "angular-ui-select",
  "angular-ace",
  "jquery",
  "smart-table",
  "pretty-json",
  "webjars/repository-web/dist/js/init/appControllerLoader",
  "webjars/repository-web/dist/js/init/appServiceLoader",
  "webjars/repository-web/dist/js/init/appDirectiveLoader"
], function (angular) {

  var repository = angular.module("repository",
      ["ngRoute", "apps.controller", "apps.service", "apps.directive",
        "smart-table", "ngAnimate", "ui.bootstrap", "ui.bootstrap.tpls",
        "ngPrettyJson", "ui.ace", "ui.select"]);

  repository.bootstrap = function () {
    angular.bootstrap(document, ["repository"]);
  };

  repository.config(["$routeProvider", "$httpProvider", "$locationProvider",
    function ($routeProvider, $httpProvider, $locationProvider) {

      // resets the default "/#!" hash notation post AngularJS 1.6.0 for
      // backwards compatibility - mostly wrt direct links to models
      $locationProvider.hashPrefix('');

      $routeProvider.when("/", {
        templateUrl: "webjars/repository-web/dist/partials/search-template.html",
        controller: "SearchController",
        reloadOnSearch: false
      }).when("/import", {
        templateUrl: "webjars/repository-web/dist/partials/import-template.html",
        controller: "ImportController"
      }).when("/payloadmapping/:modelId", {
        templateUrl: "webjars/repository-web/dist/partials/mapping/mappingcreator.html",
        controller: "MappingBuilderController"
      }).when("/details/:modelId", {
        templateUrl: "webjars/repository-web/dist/partials/details-template.html",
        controller: "DetailsController"
      }).when("/generators", {
        templateUrl: "webjars/repository-web/dist/partials/generators-template.html",
        controller: "GeneratorController"
      }).when("/login", {
        templateUrl: "webjars/repository-web/dist/partials/login-template.html",
        controller: "LoginController"
      }).when("/signup", {
        templateUrl: "webjars/repository-web/dist/partials/signup-template.html",
        controller: "SignUpController"
      }).when("/update", {
        templateUrl: "webjars/repository-web/dist/partials/update-template.html",
        controller: "UpdateController"
      }).when("/settings", {
        templateUrl: "webjars/repository-web/dist/partials/settings-template.html",
        controller: "SettingsController"
      }).when("/manage", {
        templateUrl: "webjars/repository-web/dist/partials/admin-template.html",
        controller: "AdminController"
      }).when("/requestAccessToNamespace", {
        templateUrl: "/webjars/repository-web/dist/partials/admin/requestAccessToNamespace.html",
        controller: "requestAccessToNamespaceController"
      }).when("/privacy", {
        templateUrl: "webjars/repository-web/dist/partials/privacypolicy-template.html"
      }).when("/postLogin", {
        redirectTo: function () {
          let cookies = document.cookie.split(";");
          var redirectUri = "/";
          cookies.forEach(cookie => {
            let cookieKey = cookie.trim();
            if (cookieKey.startsWith("postLoginRedirect")) {
              redirectUri = cookieKey.replace("postLoginRedirect=", "");
            }
          });
          document.cookie = "postLoginRedirect=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;"
          if ("/login" === redirectUri) {
            return "/";
          }
          return redirectUri;
        }
      }).otherwise({
        redirectTo: "/"
      });

      $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";

    }]).run(function ($location, $http, $rootScope, $uibModal) {

    $rootScope.privateNamespacePrefix = "vorto.private.";

    $rootScope.unrestrictedUrls = ["/login", "/api", "/generators"];

    $rootScope.authenticated = false;

    $rootScope.domReady = false;

    angular.element(document).ready(function () {
      $rootScope.domReady = true;
    });

    $rootScope.context = {
      providers: null,
      authenticatedSearchMode: false
    };

    $rootScope.modelId = function (namespace, name, version) {
      return namespace + ":" + name + ":" + version;
    };

    $rootScope.logout = function () {
      window.location.href = $rootScope.userInfo.logOutUrl;
    };

    $rootScope.showPrivacyPolicy = function () {
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

    $rootScope.setUser = function (user) {
      if (user != null && user.name !== null) {
        $rootScope.userInfo = user;
        $rootScope.user = user.name;
        $rootScope.displayName = user.displayName;
        // fixes the annoying "Welcome, {{displayName}}" glitch
        let userWelcome = document.getElementById('userWelcome');
        if (userWelcome && $rootScope.domReady) {
          userWelcome.innerHTML = 'Welcome, '
              + $rootScope.displayName;
          // unlikely case where DOM not ready after setting user
        } else {
          angular.element(document).ready(function () {
            $rootScope.domReady = true;
            let userWelcome = document.getElementById('userWelcome');
            // assuming element *has* to exist now
            userWelcome.innerHTML = 'Welcome, '
                + $rootScope.displayName;
          });
        }

        $rootScope.authenticated = true;
        $rootScope.authority = user.roles;

      } else {
        $rootScope.userInfo = null;
        $rootScope.user = null;
        $rootScope.displayName = null;
        $rootScope.authenticated = false;
        $rootScope.authority = null;
      }
    };

    $rootScope.watchLocationChanges = function () {
      $rootScope.$on("$locationChangeStart", function (event, next, current) {
        $rootScope.error = false;
        // saves previous location before forwarding, so it can be sent as a
        // "redirect" parameter to the login request
        if (!$rootScope.previousLocation) {
          /*
           This is pretty hack-ish.
           In the typical use case (a user punching a direct Vorto URL in an
           empty tab/window), the URL we want to store for the back-end to
           redirect to post-authentication is the "current" argument.
           However when punching a direct Vorto URL in a tab/window where Vorto
           is already running (albeit unauthenticated in our case), the correct
           argument to pick is "next" - this is an uncommon edge case.
           In order to make sure we pick the right one, we default to "current"
           (most typical use case) but check that the "current" one is not the
           actual login page - in which case we pick "next".
           */
          let parameter = (current && !current.endsWith("/login")
              && !current.endsWith("/#/")) ? current : next;
          // stores the parameter as a relative path, so any absolute paths
          // can be intercepted and ignored in the back-end for security reasons
          if (parameter.includes("/#")) {
            parameter = parameter.substring(parameter.indexOf("/#") + 2);
          }
          $rootScope.previousLocation = parameter;
        }
        if ($rootScope.needAuthentication() && $rootScope.authenticated
            === false) {
          $location.path("/login");
        }
      });
    };

    $rootScope.needAuthentication = function () {
      var split = $location.path().split("/");
      return (split.length > 1) && ($rootScope.unrestrictedUrls.indexOf(
          "/" + split[1]) === -1);
    };

    $rootScope.hasAuthority = function (role) {
      var flag = false;
      if ($rootScope.authority != undefined) {
        for (var element of $rootScope.authority) {
          if (element === role) {
            flag = true;
            break;
          } else {
            flag = false;
          }
        }
      }
      return flag;
    }

    $rootScope.redirectToLogin = function (url) {
      // parameter is defined in login-template and should never be undefined,
      // so this debatable default should formally never be in use
      let result = "github/login";
      if (url) {
        result = url;
        /*
        Checks if the log in page landing is caused by a location change and
        if so, adds the previous location as a request parameter, so the
        back-end can re-direct to it.
        Note that the $rootScope is cleared after a log on, so the
        previousLocation will be "cleared" if the log on succeeds.
        */
        if ($rootScope.previousLocation) {
          document.cookie = "postLoginRedirect=" + $rootScope.previousLocation + "; Path=/;";
        }
      }
      return result;
    }

    $rootScope.init = function () {
      var getContextSucceeded = function (result) {
        $rootScope.context = result.data;
        if (!$rootScope.context.authenticatedSearchMode) {
          $rootScope.unrestrictedUrls = ["/", "/details", "/login", "/api",
            "/generators", "/payloadmapping", "/privacy"];
        } else {
          $rootScope.unrestrictedUrls = ["/login", "/api", "/generators",
            "/privacy"];
        }
        return result;
      };

      var getContextFailed = function (reason) {
        return null;
      };

      var getUser = function () {
        return $http.get("./user");
      };

      var getUserSucceeded = function (result) {
        $rootScope.setUser(result.data);
        $rootScope.$broadcast("USER_CONTEXT_UPDATED", result.data);
        return result.data;
      };

      var getUserFailed = function (reason) {
        $rootScope.setUser(null);
        return null;
      };

      var userResultAction = function (user) {
        if (user != null) {
          if (user.isRegistered === "false") {
            $location.path("/signup");
          } else if (user.needUpdate === "true") {
            $location.path("/update");
          }
        } else {
          if ($rootScope.needAuthentication()) {
            $location.path("/login");
          }
        }
        return user;
      };

      $http.get("./context")
      .then(getContextSucceeded, getContextFailed)
      .then(getUser)
      .then(getUserSucceeded, getUserFailed)
      .then(userResultAction)
      .finally($rootScope.watchLocationChanges);
    };

    $rootScope.init();
  });

  return repository;

})
;

