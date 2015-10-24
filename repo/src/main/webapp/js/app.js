var repository = angular.module('repository', ['ngRoute','ngCookies','repositoryControllers']);

repository.config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
        $routeProvider
            .when('/', {
                templateUrl: "partials/browser-template.html",
                controller: 'BrowserController'
            })
            .when('/upload', {
                templateUrl: "partials/upload-template.html",
                controller: 'UploadController'
            })
            .when('/details/:namespace/:name/:version', {
                templateUrl: "partials/details-template.html",
                controller: 'DetailsController'
            })
            .when('/authenticate', {
                templateUrl: "partials/login-template.html",
                controller: 'AuthenticateController'
            })            
            .otherwise({redirectTo: '/'});
            
  /* Registers auth token interceptor, auth token is either passed by header or by query parameter
		     * as soon as there is an authenticated user */
		     
		    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
		        return {
		        	'request': function(config) {
		        		var isRestCall = config.url.indexOf('/rest/') >-1;
		        		if (isRestCall && angular.isDefined($rootScope.basicAuthHeader)) {		        			
		        			config.headers['Authorization'] = $rootScope.basicAuthHeader;
		        		}
		        		return config || $q.when(config);
		        	}
		        };
		    }
	    );            
            
            
    }]).run(function($rootScope, $location, $cookieStore, $http) {
    	
		var targetpath="/", basicAuthHeader, user, authenticated;

		/* Reset error when a route is to be loaded 
		 * Parameters: (angularEvent, nextRoute, currentRoute)
		 */
		$rootScope.$on('$routeChangeStart', function(anglurEvt, nextRoute, currentRoute) {
			//if(currentRoute){console.log("Route Change Start....from..."+ currentRoute.originalPath );}
			//if(nextRoute){console.log("....to..."+nextRoute.originalPath )};
			if(nextRoute.originalPath == "/upload"){
				console.log("Upload Controller Start......");
				$rootScope.targetpath = nextRoute.originalPath;	
				console.log("Upload Controller Require ROLE_ADMIN.");
				
				var redirectPath = "/";
				if(currentRoute){
					redirectPath = currentRoute.originalPath;
				}
				$rootScope.requireRole("ROLE_ADMIN",redirectPath,true);    
			}
			  
		});
		
		/* Reset error when a new view is loaded */
		$rootScope.$on('$viewContentLoaded', function() {
			delete $rootScope.error;
		});
		
		$rootScope.authenticationVerify = function(sCallback, fCallback){
			var authToken = $cookieStore.get("vorto_auth");
			console.log("User Authentication Verify --- Auth from Cookies : "+authToken);
			if(authToken){
				console.log("User Authentication Verify --- Sending out request to get User Details, since can't find it in current context.");
				$http({
						method: "GET",
						url: "./rest/user/verify"                    
					 }).success(function(data, status, headers, config) {
						if (status==200 && typeof data == 'object') {
							console.log("Authentication Verified. Record User Details from response.");
							$rootScope.basicAuthHeader = data.httpAuthHeader;
							$rootScope.user = data;
							$rootScope.authenticated = true;
							sCallback();
						} 			
					 }).error(function(data, status, headers, config) {
						console.log("Authentication Verification failed. Set authenticated to false");
						$rootScope.authenticated = false;
						fCallback();
					 });
			}else{
				$rootScope.authenticated = false;
				console.log("User Authentication Verify --- Can't find cookies of vorto authentication, REDIRECT TO AUTHENTICATION LOGIN");
				$location.path("/authenticate");
			}
		}
	    		
		$rootScope.requireRole = function(role, redirectPath, showingError) {
			var _role = role;
			var _redirectPath = redirectPath;
			var _showingError = showingError;
			
			console.log("requireRole(): Checking whether User has role of "+role);
			
			if(!$rootScope.authenticated){
				console.log("requireRole(): Looks no user details in current context, do authenticationVerify first.");
				$rootScope.authenticationVerify(
					function(){
							console.log("requireRole() sucess callback: Authenticated, check user roles");						
							$rootScope.checkUserRole(_role, _redirectPath, _showingError);
						}, 
					function(){
							console.log("requireRole() fail callback: After authenticationVerify, still can't get user details. Need to force user to do LOGIN");
							$location.path("/authenticate");
						}
				);
			}else{
				console.log("requireRole(): Authenticated, Checking whether User has role of "+role);
				$rootScope.checkUserRole(_role,_redirectPath, _showingError);
			}			
		};
		
		$rootScope.checkUserRole = function(role, redirectPath, showError){
			if ($rootScope.user === undefined || $rootScope.user.roles === undefined ||$rootScope.user.roles.length==0) {
				console.log("requireRole(): NO user or user roles");
				if(showError){$rootScope.error="No permission to access"; }
				if(redirectPath){$location.path(redirectPath);}
				return false;
			}
			
			if($rootScope.user.roles.indexOf(role)>-1){
				console.log("requireRole(): GET IT!");
				return true;
			}
			
			console.log("requireRole(): Too bad, even u r authenticated, but u don't have role to access! Bye!Bye!");
			if(showError){$rootScope.error="No permission to access"; }
			if(redirectPath){$location.path(redirectPath);}
			return false;
		}
		
		$rootScope.hasRole = function(role){
			return $rootScope.checkUserRole(role);
		}
		
		$rootScope.logout = function() {
			delete $rootScope.targetpath;
			delete $rootScope.authenticated;
			delete $rootScope.user;
			delete $rootScope.basicAuthHeader;
			$cookieStore.remove("vorto_auth");
			window.location.href="./logout";
          	window.reload();
		};
		
		var authToken = $cookieStore.get("vorto_auth");
		if(authToken){
			$rootScope.authenticationVerify(function(){},function(){});
		}
	});



