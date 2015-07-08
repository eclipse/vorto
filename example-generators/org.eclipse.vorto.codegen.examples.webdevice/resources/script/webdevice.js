saveConfiguration = function(functionBlockName) {	
	var configurationObj = $('fieldset#' + functionBlockName +'_configuration_fieldset input').serializeObject();
    $.ajax({
	    headers: { 
	        'Accept': 'application/json',
	        'Content-Type': 'application/json' 
	    },    
    	url: "service/" + functionBlockName + "/saveConfiguration",
    	type: "POST",
    	dataType: 'json',
    	data : JSON.stringify(configurationObj),
    	success: function(result){
			 console.log("configuration saved!");
			 displayProperties(functionBlockName);
    }});			
};

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};


displayInputFieldProperty = function(functionBlockName, propertyName, propertyObject){
	var keys = Object.keys(propertyObject);
	for(var i=0; i<keys.length; i++){
		var key = keys[i];
		var labelId = functionBlockName + "_" + propertyName + "_id_" + key;
		
		var inputField = document.getElementById(labelId);
		if( inputField){
			inputField.value = propertyObject[key] + "";
		}						
	}							
}

displayLabelFieldProperty = function(functionBlockName, propertyName, propertyObject){
	var keys = Object.keys(propertyObject);
	for(var i=0; i<keys.length; i++){
		var key = keys[i];
		var labelId = functionBlockName + "_" + propertyName + "_id_" + key;
		
		var labelField = document.getElementById(labelId);
		if(labelField){
			labelField.innerHTML = propertyObject[key];	
		}					
	}							
}

displayProperties = function(functionBlockName){	
	var instanceJsonValue = httpGet("service/" + functionBlockName + "/instance");
	var instanceObject = JSON.parse(instanceJsonValue);
	var configurationObject = instanceObject.configuration;
	var statusObject = instanceObject.status;
	var faultObject = instanceObject.fault;

	displayLabelFieldProperty(functionBlockName, "status", statusObject);	
	displayLabelFieldProperty(functionBlockName, "fault", faultObject);	
	displayInputFieldProperty(functionBlockName, "configuration", configurationObject);		
}

invokeOperation = function(functionBlockName, operation){		
	httpPut("service/" + functionBlockName + "/" + operation);
	displayProperties(functionBlockName);
}
		
function httpGet(restUrl)
{
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open( "GET", restUrl, false );
	xmlHttp.send( null );
	return xmlHttp.responseText;
}

function httpPut(restUrl)
{
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("PUT", restUrl, false);
	xmlHttp.setRequestHeader("Content-Type", "text/plain");
	xmlHttp.send(null);
}
