define(["../../init/AppService"], function(services) {
  services.factory("LoginDataService", LoginDataService);

  LoginDataService.$inject = ["$http", "$q"];

  function LoginDataService($http, $q) {

    var service = {
      getUser: getUser,
      getContext: getContext,
      logout: logout
    }

    return service;

    function getUser(params) {
      var url = "./rest/user";
      return $http.get(url).then(httpCallSuccess).catch(httpCallFailure);
    }

    function getContext() {
      var url = "./rest/context";
      return $http.get(url).then(httpCallSuccess).catch(httpCallFailure);
    }

    function logout() {
      var url = "logout";
      return $http.post(url).then(httpCallSuccess).catch(httpCallFailure);
    }

    function httpCallSuccess(response) {
      return response.data;
    }

    function httpCallFailure(error){
      return $q.reject(error)
    }
  }
});
