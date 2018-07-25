repositoryControllers.controller('ImportController', ['$scope', '$rootScope', '$http','$location', '$uibModal', 
	function ($scope, $rootScope, $http, $location, $uibModal) {

	$scope.selectedImporter = {key: 'Vorto'};
	
    $scope.isLoading = false;
    $scope.fileAdded = false;
    $scope.beingUploaded = false;
    $scope.beingCheckedIn = false;

    $scope.uploadModel = function () {
        $scope.showResultBox = false;
        $scope.uploadResult = {};
        $scope.resultMessage = "";
 
        $scope.beingUploaded = true;
        $scope.error = null;

        var fileToUpload = document.getElementById('file-upload').files[0];
        if(fileToUpload != undefined) {
            var filename = document.getElementById('file-upload').files[0].name;
            var extn = filename.split(".").pop();
           	upload('./rest/importers/', fileToUpload);
        } else {
            $rootScope.error = "Choose model file(s) and click Upload.";
        }
    };

    $scope.fileNameChanged = function(element) {
        $scope.$apply(function($scope) {
            $scope.browsedFile= element.files[0].name;
        });  
        $scope.fileAdded = true;
        $scope.showCheckin = false;
        $scope.$digest();
    };

    upload = function(url, fileToUpload) {
        $scope.isLoading = true;
        $scope.modelStats = {};
        var infocount = 0, fbcount  = 0, typecount = 0, mappingcount = 0;
        var fd = new FormData();
        fd.append('file', fileToUpload);
        fd.append('key', $scope.selectedImporter.key);
        $http.post(url,fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(result){
            $scope.isLoading = false;
            if(result.result.reports[0].detailedReport !== undefined && result.result.reports[0].detailedReport.messageType == "WARNING"
                && result.result.reports[0].detailedReport.messageType !== undefined){
                console.log("something");
                result.result.valid = true;
                $scope.uploadResult = result;
                $scope.uploadResult.result.reports[0].valid = true;
                $scope.showResultBox = false;
                $scope.openOverwriteModelDialog();
            }else{
                $scope.showResultBox = true;
                $scope.showCheckin = true;
            }
            $scope.stateArr = [];
            $scope.uploadResult = result;
            $scope.fileAdded = false;

            if($scope.uploadResult.result.reports.length > 0 && $scope.uploadResult.result.valid) {
                angular.forEach($scope.uploadResult.result.reports, function (resultObject, idx) {
                    var item =  (idx == 0) ? {active: false} : {active: true} ;
                    var modelType = resultObject.model.type;
                    switch (modelType) {
                    case "Functionblock":
                        fbcount++;
                        break;
                    case "InformationModel":
                        infocount++;
                        break;
                    case "Datatype":
                        typecount++;
                        break;
                    case "Mapping":
                        mappingcount++;
                        break;
                    }
                    $scope.stateArr.push(item);
                    $scope.showCheckin = (resultObject.valid && $scope.showCheckin);
                    $scope.fileAdded = !(resultObject.valid && $scope.showCheckin);
                });
            } else {
                $scope.showCheckin = false;
                $scope.fileAdded = true;
            }

            $scope.modelStats = {infocount:infocount, fbcount:fbcount, typecount:typecount, mappingcount:mappingcount };           
            $scope.beingUploaded = false;
            $scope.resultMessage = result.message;
        }).error(function(data, status, headers, config) {
            $scope.isLoading = false;
            $scope.beingUploaded = false;
            if(status == 403){
                $scope.error = "Operation is Forbidden";
            }else if(status == 401){
                $scope.error= "Unauthorized Operation";
            }else if(status == 400){
                $scope.error = "Bad Request. Server Down";
            }else if(status == 500){
                $scope.error = "Internal Server Error";
            }else{
                $scope.error = "Failed Request with response status "+status;
            }
        });
    };

    $scope.openOverwriteModelDialog = function() {
        var dialog = $uibModal.open({
            animation: true,
            size: "lg",
            controller: function($scope){
                $scope.overwriteAccepted = function() {
                    overwriteModel();
                    dialog.dismiss();
                }
                
                $scope.dismissModal = function() {
                    dialog.dismiss();
                }
            },
            templateUrl: "overwriteModelDialog.html",
            size:"md"
        });
    };

    overwriteModel = function() {
        $scope.showResultBox = true;
        $scope.showCheckin = true;
        $scope.fileAdded = false;
    }

    $scope.isMissing = function(reference, missingReferences) {
        var index;
        for (index = 0; missingReferences != null && index < missingReferences.length; index++) {
            if (missingReferences[index].namespace == reference.namespace
                && missingReferences[index].name == reference.name
                && missingReferences[index].version == reference.version) {
                return true;
            }
        }
        return false;
    };

    $scope.checkin = function () {
    	$scope.isLoading = true;
        $scope.showCheckin = false;
        $scope.fileAdded = true;
        $scope.beingCheckedIn = true;
    	$scope.loadMessage = "Importing models... Please wait!";
        $rootScope.error = "";
        checkinSingle($scope.uploadResult.result.handleId);
    };

    checkinSingle = function (handleId) {
        $http.put('./rest/importers/'+handleId+'?key='+$scope.selectedImporter.key)
        .success(function(result){
            $scope.showResultBox = true;
            $scope.beingCheckedIn = false;
            $scope.resultMessage = "Import was successful!";
            $scope.showCheckin = false;
            $scope.fileAdded = true;
            $scope.isLoading = false;
            $rootScope.modelsSaved = null;
        }).error(function(data, status, headers, config) {
            if(status == 403){
                $scope.error = "Operation is Forbidden";
            }else if(status == 401){
                $scope.error= "Unauthorized Operation";
            }else if(status == 400){
                $scope.error = "Bad Request. Server Down";
            }else if(status == 500){
                $scope.error = "Internal Server Error";
            }else{
                $scope.error = "Failed Request with response status "+status;
            }
            $scope.isLoading = false;
            $scope.beingCheckedIn = false;
        });;
    };
    
     $scope.getImporters = function () {
        $http.get('./rest/importers')
        .success(function(result){
            $scope.importers = result;
        });
    };
    
    $scope.getImporters();
    
    
    $scope.getSelectedImporterInfo = function(selectedImporter) {
    	if ($scope.importers === undefined) {
    		return;
    	}
		for(var i=0; i<$scope.importers.length; i++) {
       	 if ($scope.importers[i].key == selectedImporter) return $scope.importers[i];
    	}
};

}]);
