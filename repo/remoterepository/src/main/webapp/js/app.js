(function() {
  var app = angular.module("VortoRemoteRepository", []);

  app.controller('ListModelController', ["$http", function($http) {
  	var ctrl = this;
  	this.models = [];
  	this.model = "informationmodel";
  	this.filter = "";
  	this.fileToUpload = null;
  	
  	this.getModels = function() {
  		var queryFilter = '';
  		if (ctrl.filter.trim() != "") {
  			queryFilter = 'or(nameLike("' + ctrl.filter + '"),namespaceLike("' + ctrl.filter + '"),versionLike("' + ctrl.filter + '"))';
  		}
  		
  		if (ctrl.model != "") {
  		  if (queryFilter != '') {
  		    queryFilter = 'and(' + queryFilter + ',modelType("' + ctrl.model + '"))';
  		  } else {
  		    queryFilter = 'modelType("' + ctrl.model + '")';
  		  }
  		}
  		
  		$http.get('./rest/model/query=' + queryFilter).success(
	      function(data, status, headers, config) {
					console.log("success : " + JSON.stringify(data));
					ctrl.models = data.searchResult;
	      }).error(function(data, status, headers, config) {
					console.log("error : " + status);
					ctrl.models = [];
	    	}); 	
		};
		
		this.upload = function() {
			console.log("upload here");
		};
  	
  	this.getModels();
  }]);
  
})();