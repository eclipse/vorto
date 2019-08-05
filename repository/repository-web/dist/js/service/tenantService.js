define(["../init/appService"], function(repository) {

repository.factory('TenantService',['$rootScope', '$http','$q',function($rootScope, $http, $q){

    var factory = {
        getNamespacesForRole: getNamespacesForRole
    };

    return factory;
    
    
    function getNamespacesForRole(role) {
    	var deferred = $q.defer();
    	var userNamespaces = [];
        $http.get("./rest/tenants?role="+role)
        	.then(function(result) {
            	var tenants = result.data;
                if (tenants != null) {
                	for(var i=0; i < tenants.length; i++) {
                    	if (tenants[i].namespaces != null) {
                        	for(var k=0; k < tenants[i].namespaces.length; k++) {
                            	userNamespaces.push({
                                	tenant: tenants[i].tenantId,
                                    namespace: tenants[i].namespaces[k]
                                }); 
                             }
                         }
                     }
                }
                if (userNamespaces.length > 0) {
                	userNamespaces.sort(function (a, b) {
                    	return a.namespace.localeCompare(b.namespace);
                    });
                }
                deferred.resolve(userNamespaces);
             }, function(reason) {
             	deferred.reject(reason);
             });
             
             return deferred.promise;
      };
}]);

});