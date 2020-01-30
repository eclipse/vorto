define("repository",[
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

	 var repository = angular.module("repository", [ "ngRoute", "apps.controller", "apps.service","apps.directive", "smart-table", "ngAnimate","ui.bootstrap","ui.bootstrap.tpls","ngPrettyJson","ui.ace","ui.select"]);

	 repository.bootstrap = function() {
	    angular.bootstrap(document, ["repository"]);
	  };


repository.config([ "$routeProvider", "$httpProvider", function($routeProvider, $httpProvider) {

    $routeProvider.when("/", {
        templateUrl : "webjars/repository-web/dist/partials/search-template.html",
        controller : "SearchController",
        reloadOnSearch: false
    }).when("/import", {
        templateUrl : "webjars/repository-web/dist/partials/import-template.html",
        controller : "ImportController"
    }).when("/payloadmapping/:modelId", {
        templateUrl : "webjars/repository-web/dist/partials/mapping/mappingcreator.html",
        controller : "MappingBuilderController"
    }).when("/details/:modelId", {
        templateUrl : "webjars/repository-web/dist/partials/details-template.html",
        controller : "DetailsController"
    }).when("/generators", {
        templateUrl : "webjars/repository-web/dist/partials/generators-template.html",
        controller : "GeneratorController"
    }).when("/login", {
        templateUrl : "webjars/repository-web/dist/partials/login-template.html",
        controller : "LoginController"
    }).when("/signup", {
        templateUrl : "webjars/repository-web/dist/partials/signup-template.html",
        controller : "SignUpController"
    }).when("/update", {
        templateUrl : "webjars/repository-web/dist/partials/update-template.html",
        controller : "UpdateController"
    }).when("/settings", {
        templateUrl : "webjars/repository-web/dist/partials/settings-template.html",
        controller : "SettingsController"
    }).when("/manage", {
        templateUrl : "webjars/repository-web/dist/partials/admin-template.html",
        controller : "AdminController"
    }).when("/privacy", {
        templateUrl : "webjars/repository-web/dist/partials/privacypolicy-template.html"
    }).otherwise({
        redirectTo : "/"
    });

    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";

} ]).run(function($location, $http, $rootScope) {

    $rootScope.privateNamespacePrefix = "vorto.private.";
    
	$rootScope.unrestrictedUrls = ["/login", "/api", "/generators"];
	
	$rootScope.authenticated = false;

    $rootScope.user = null;
    
    $rootScope.context = {
        providers: null,
        authenticatedSearchMode: false
    };
    
    $rootScope.modelId = function(namespace,name,version) {
    	return namespace + ":" + name + ":" + version;
	};
    
    $rootScope.logout = function() {
        window.location.href = $rootScope.userInfo.logOutUrl;
    };

    $rootScope.setUser = function(user) {
        if (user != null && user.name !== null) {
            $rootScope.userInfo = user;
            $rootScope.user = user.name;
            $rootScope.displayName = user.displayName;
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
    
    $rootScope.watchLocationChanges = function() {
        $rootScope.$on("$locationChangeStart", function(event, next, current) {
            $rootScope.error = false;
            if ($rootScope.needAuthentication() && $rootScope.authenticated === false) {
                $location.path("/login");
            }
        });
    };
    
    $rootScope.needAuthentication = function() {
        var split = $location.path().split("/");
        return (split.length > 1) && ($rootScope.unrestrictedUrls.indexOf("/" + split[1]) === -1);
    };

    $rootScope.hasAuthority = function(role) {
        var flag = false;
        if($rootScope.authority != undefined){
            for (var element of $rootScope.authority) {
                if(element === role) {
                    flag = true;
                    break;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }
    
    $rootScope.init = function() {    	
        var getContextSucceeded = function(result) {
            $rootScope.context = result.data;
            if (!$rootScope.context.authenticatedSearchMode) {
                $rootScope.unrestrictedUrls = ["/", "/details", "/login", "/api", "/generators", "/payloadmapping", "/privacy"];
            } else {
                $rootScope.unrestrictedUrls = ["/login", "/api", "/generators", "/privacy"];
            }
            return result;
        };

        var getContextFailed = function(reason) {
            return null;
        };

        var getUser = function() {
            return $http.get("./user");
        };

        var getUserSucceeded = function(result) {
            $rootScope.setUser(result.data);
            $rootScope.$broadcast("USER_CONTEXT_UPDATED", result.data);
            return result.data;
        };

        var getUserFailed = function(reason) {
            $rootScope.setUser(null);
            return null;
        };

        var userResultAction = function(user) {
            if (user != null) {
                if (user.isRegistered === "false") {
                    $location.path("/signup");
                } else if(user.needUpdate === "true") {
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

});

