define(["../../init/AppService"], function(services) {
  services.factory("EditorDataService", EditorDataService);

  EditorDataService.$inject = ["$http", "$q"];

  function EditorDataService($http, $q) {

    var editorConfig = {
      infomodel: {
        searchUrl: "./rest/editor/infomodel/search=",
        importModelUrl: "./rest/editor/infomodel/link/model",
        referenceResourceUrl: "./rest/editor/infomodel/link/resource",
        loadUrl: "./infomodel/xtext-service/load?resource="
      },
      fbmodel: {
        searchUrl: "./rest/editor/functionblock/search=",
        importModelUrl: "./rest/editor/functionblock/link/model",
        referenceResourceUrl: "./rest/editor/functionblock/link/resource",
        loadUrl: "./functionblock/xtext-service/load?resource="
      },
      type: {
        searchUrl: "./rest/editor/datatype/search=",
        importModelUrl: "./rest/editor/datatype/link/model",
        referenceResourceUrl: "./rest/editor/datatype/link/resource",
        loadUrl: "./datatype/xtext-service/load?resource="
      }
    }

    var service = {
      loadResource: loadResource,
      importModel: importModel,
      referenceResource: referenceResource,
      searchRepository: searchRepository
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

    function httpCallSuccess(response) {
      return response.data;
    }

    function httpCallFailure(error){
      return $q.reject(error)
    }
  }
});
