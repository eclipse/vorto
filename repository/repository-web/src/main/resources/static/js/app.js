var repository = angular.module("repository", [ "ngRoute", "repositoryControllers", "repositoryDirectives",
    "swaggerUi", "smart-table", "ngAnimate","ui.bootstrap","ui.bootstrap.tpls","ui.ace"]);

repository.config([ "$routeProvider", "$httpProvider", function($routeProvider, $httpProvider) {

    $routeProvider.when("/", {
        templateUrl : "partials/search-template.html",
        controller : "SearchController",
        reloadOnSearch: false
    }).when("/import", {
        templateUrl : "partials/import-template.html",
        controller : "ImportController"
    }).when("/details/:namespace/:name/:version", {
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
    
    $rootScope.context = {
        githubEnabled: false,
        eidpEnabled: false,
        authenticatedSearchMode: false,
    };
    
    $rootScope.modelId = function(namespace,name,version) {
    	return namespace + "." + name + ":" + version;
	};
    
    $rootScope.logout = function() {
    	var postLogout = function() {
            $rootScope.setUser(null);
            $rootScope.init();
            $location.path("/");
    	};

        $http.post("logout", {}).success(postLogout).error(postLogout);
    };

    $rootScope.setUser = function(user) {
    	if (user != null && user.name !== null) {
            $rootScope.userInfo = user;
            $rootScope.user = user.name;
            $rootScope.displayName = user.displayName;
            $rootScope.authenticated = true;
            $rootScope.authority = user.role;
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
    	return (split.length > 1) && ($rootScope.unrestrictedUrls.indexOf("/" + split[1]) == -1);
    };
    
    $rootScope.init = function() {    	
    	var getContextSucceeded = function(result) {
            $rootScope.context = result.data;
            if (!$rootScope.context.authenticatedSearchMode) {
                $rootScope.unrestrictedUrls = ["/", "/details", "/login", "/api", "/generators"];
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
