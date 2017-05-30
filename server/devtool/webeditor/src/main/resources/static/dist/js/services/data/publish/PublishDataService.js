define(["../../init/AppService"], function(services) {
  services.factory("PublishDataService", PublishDataService);

  PublishDataService.$inject = ["$http", "$q"];

  function PublishDataService($http, $q) {

    var service = {
      validateProject: validateProject,
      checkInMultiple: checkInMultiple,
      checkInSingle: checkInSingle
    }

    return service;

    function validateProject(params) {
      var projectName = params.projectName;
      var url = "./rest/publish/" + projectName + "/validate";
      return $http.get(url).then(httpCallSuccess).catch(httpCallFailure);
    }

    function checkInMultiple(params) {
      var uploadHandles = params.uploadHandles;
      var url = "./rest/publish/checkInMultiple";
      return $http.put(url, uploadHandles).then(httpCallSuccess).catch(httpCallFailure);
    }

    function checkInSingle(params) {
      var handleId = params.handleId;
      var url = "./rest/publish/" + handleId;
      return $http.put(url).then(httpCallSuccess).catch(httpCallFailure);
    }

    function httpCallSuccess(response) {
      return response.data;
    }

    function httpCallFailure(error){
      return $q.reject(error)
    }
  }
});
