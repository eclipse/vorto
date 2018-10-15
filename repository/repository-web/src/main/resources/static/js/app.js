var repository = angular.module("repository", [ "ngRoute", "repositoryControllers", "repositoryDirectives",
    "swaggerUi", "smart-table", "ngAnimate","ui.bootstrap","ui.bootstrap.tpls","ui.ace" ,"ngPrettyJson"]);

repository.config([ "$routeProvider", "$httpProvider", function($routeProvider, $httpProvider) {

    $routeProvider.when("/", {
        templateUrl : "partials/search-template.html",
        controller : "SearchController",
        reloadOnSearch: false
    }).when("/import", {
        templateUrl : "partials/import-template.html",
        controller : "ImportController"
    }).when("/payloadmapping/:modelId/:targetPlatform/:mappingId", {
        templateUrl : "partials/mapping/mappingcreator.html",
        controller : "MappingBuilderController"
    }).when("/details/:modelId", {
        templateUrl : "partials/details-template.html",
        controller : "DetailsController"
    }).when("/generators", {
        templateUrl : "partials/generators-template.html",
        controller : "GeneratorController"
    }).when("/api", {
        templateUrl : "partials/swagger-template.html",
        controller : "SwaggerController"
    }).when("/login", {
        templateUrl : "partials/login-template.html",
        controller : "LoginController"
    }).when("/signup", {
        templateUrl : "partials/signup-template.html",
        controller : "SignUpController"
    }).when("/update", {
        templateUrl : "partials/update-template.html",
        controller : "UpdateController"
    }).when("/settings", {
        templateUrl : "partials/settings-template.html",
        controller : "SettingsController"
    }).when("/manage", {
        templateUrl : "partials/admin-template.html",
        controller : "AdminController"
    }).otherwise({
        redirectTo : "/"
    });

    $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";

} ]).run(function($location, $http, $rootScope) {

	$rootScope.unrestrictedUrls = ["/login", "/api", "/generators"];
	
	$rootScope.authenticated = false;

    $rootScope.user = null;
    
    $rootScope.tenant = "default";
    
    $rootScope.context = {
        githubEnabled: false,
        eidpEnabled: false,
        authenticatedSearchMode: false,
        singleTenantMode: true
    };
    
    $rootScope.modelId = function(namespace,name,version) {
    	return namespace + ":" + name + ":" + version;
	};
    
    $rootScope.logout = function() {
        window.location.href = $rootScope.context.logOutUrl;
    };

    $rootScope.setUser = function(user) {
        if (user != null && user.name !== null) {
            $rootScope.userInfo = user;
            $rootScope.user = user.name;
            $rootScope.displayName = user.displayName;
            $rootScope.authenticated = true;
            $rootScope.authority = user.role;
            // TODO : set the $rootScope.tenant to the tenant of the user
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
    
    $rootScope.init = function() {    	
        var getContextSucceeded = function(result) {
            $rootScope.context = result.data;
            if ($rootScope.context.singleTenantMode) {
                $rootScope.tenant = 'default';
            }
            if (!$rootScope.context.authenticatedSearchMode) {
                $rootScope.unrestrictedUrls = ["/", "/details", "/login", "/api", "/generators" , "/payloadmapping"];
            } else {
                $rootScope.unrestrictedUrls = ["/login", "/api", "/generators"];
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
