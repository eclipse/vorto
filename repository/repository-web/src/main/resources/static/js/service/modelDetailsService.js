repository.factory('ModelDetailsService',['$rootScope', '$http','$q','$location',function($rootScope, $http, $q, $location){

    var ATTACHMENT_URL = './api/v1/' + $rootScope.tenant + '/attachments/';

    var factory = {
        uploadAttachment: uploadAttachment
    };

    return factory;

    function uploadAttachment(passingValue) {
         var deferred = $q.defer();

         $http.put(ATTACHMENT_URL + passingValue.modelId,
                    passingValue.payload,
                        {
         					transformRequest: angular.identity,
         					headers: {
         						'Content-Type': undefined
         					}
         				}
         			)
                .then(
                    function(response) {
                        deferred.resolve(response.data);
                    },
                    function(errResponse){
                        deferred.reject(errResponse);
                    }
                );
          return deferred.promise;
    }
}]);