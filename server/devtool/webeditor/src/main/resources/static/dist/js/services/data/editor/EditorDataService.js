define(["../../init/AppService"], function(services) {
  services.factory("EditorDataService", EditorDataService);

  EditorDataService.$inject = ["$http", "$q"];

  function EditorDataService($http, $q) {

    var editorConfig = {
      infomodel: {
        searchUrl: "./rest/editor/infomodel/search=",
        importModelUrl: "./rest/editor/infomodel/link/model",
        referenceResourceUrl: "./rest/editor/infomodel/link/resource",
        loadUrl: "./infomodel/xtext-service/load?resource=",
        updateResourceUrl: "./infomodel/xtext-service/update?resource=",
        validateResourceUrl: "./infomodel/xtext-service/validate?resource="
      },
      fbmodel: {
        searchUrl: "./rest/editor/functionblock/search=",
        importModelUrl: "./rest/editor/functionblock/link/model",
        referenceResourceUrl: "./rest/editor/functionblock/link/resource",
        loadUrl: "./functionblock/xtext-service/load?resource=",
        updateResourceUrl: "./functionblock/xtext-service/update?resource=",
        validateResourceUrl: "./functionblock/xtext-service/validate?resource="
      },
      type: {
        searchUrl: "./rest/editor/datatype/search=",
        importModelUrl: "./rest/editor/datatype/link/model",
        referenceResourceUrl: "./rest/editor/datatype/link/resource",
        loadUrl: "./datatype/xtext-service/load?resource=",
        updateResourceUrl: "./datatype/xtext-service/update?resource=",
        validateResourceUrl: "./datatype/xtext-service/validate?resource="
      }
    }

    var service = {
      loadResource: loadResource,
      importModel: importModel,
      referenceResource: referenceResource,
      searchRepository: searchRepository,
      validateResource: validateResource,
      updateResource: updateResource
    }

    return service;

    function loadResource(params) {
      var editorLanguage = params.language;
      var resourceId = params.resourceId;
      var url = editorConfig[editorLanguage].loadUrl + resourceId;
      return $http.get(url).then(httpCallSuccess).catch(httpCallFailure);
    }

    function importModel(params) {
      var editorLanguage = params.language;
      var importModelRequest = params.importModelRequest;
      var url = editorConfig[editorLanguage].importModelUrl;
      return $http.post(url, importModelRequest).then(httpCallSuccess).catch(httpCallFailure);
    }

    function referenceResource(params) {
      var editorLanguage = params.language;
      var referenceResourceRequest = params.referenceResourceRequest;
      var url = editorConfig[editorLanguage].referenceResourceUrl;
      return $http.post(url, referenceResourceRequest).then(httpCallSuccess).catch(httpCallFailure);
    }

    function searchRepository(params) {
      var editorLanguage = params.language;
      var filter = params.filter;
      var url = editorConfig[editorLanguage].searchUrl + filter;
      return $http.get(url).then(httpCallSuccess).catch(httpCallFailure);
    }

    function updateResource(params) {
      var editorLanguage = params.language;
      var resourceId = params.resourceId;
      var headers = {};
      headers["Content-Type"] = "application/x-www-form-urlencoded; charset=UTF-8]";
      var config = {};
      config["headers"] = headers;
      var url = editorConfig[editorLanguage].updateResourceUrl + resourceId;
      return $http.put(url, $.param(params), config).then(httpCallSuccess).catch(httpCallFailure);
    }

    function validateResource(params) {
      var editorLanguage = params.language;
      var resourceId = params.resourceId;
      var stateId = params.stateId;
      var url = editorConfig[editorLanguage].validateResourceUrl + resourceId + "&requiredStateId=" + stateId;
      return $http.get(url).then(httpCallSuccess).catch(httpCallFailure);
    }

    function httpCallSuccess(response) {
      return response.data;
    }

    function httpCallFailure(error) {
      return $q.reject(error)
    }
  }
});
