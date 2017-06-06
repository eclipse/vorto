package com.bosch.iotsuite.console.generator.application.templates.resources.js

import com.bosch.iotsuite.console.generator.application.templates.resources.ui.IFunctionBlockUITemplate
import com.bosch.iotsuite.console.generator.application.templates.resources.ui.components.BarChartUITemplate
import com.bosch.iotsuite.console.generator.application.templates.resources.ui.components.DefaultUITemplate
import com.bosch.iotsuite.console.generator.application.templates.resources.ui.components.GaugeUITemplate
import com.bosch.iotsuite.console.generator.application.templates.resources.ui.components.LocationMapUITemplate
import java.util.HashMap
import java.util.Map
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.core.api.model.model.ModelId
import org.eclipse.vorto.core.api.model.model.ModelType

class DetailsControllerTemplate implements IFileTemplate<InformationModel> {
	
	public static final Map<ModelId,IFunctionBlockUITemplate> UI_COMPONENTS = initComponents();

	public static final IFunctionBlockUITemplate DEFAULT_UI_TEMPLATE = new DefaultUITemplate();

	static def initComponents() {
		var components = new HashMap();
		components.put(new ModelId(ModelType.Functionblock,"Accelerometer","com.ipso.smartobjects","0.0.1"),new BarChartUITemplate("x_value","y_value","z_value"));
		components.put(new ModelId(ModelType.Functionblock,"Location","com.ipso.smartobjects","0.0.1"),new LocationMapUITemplate("latitude","longitude"));
		components.put(new ModelId(ModelType.Functionblock,"Humidity","com.ipso.smartobjects","0.0.1"),new GaugeUITemplate("sensor_units","min_range_value","max_range_value","sensor_value"));
		components.put(new ModelId(ModelType.Functionblock,"Pressure","com.ipso.smartobjects","0.0.1"),new GaugeUITemplate("sensor_units","min_range_value","max_range_value","sensor_value"));
		components.put(new ModelId(ModelType.Functionblock,"Temperature","com.ipso.smartobjects","0.0.1"),new GaugeUITemplate("sensor_units","min_range_value","max_range_value","sensor_value"));
		components.put(new ModelId(ModelType.Functionblock,"Illuminance","com.ipso.smartobjects","0.0.1"),new GaugeUITemplate("sensor_units","min_range_value","max_range_value","sensor_value"));
		components.put(new ModelId(ModelType.Functionblock,"Magnetometer","com.ipso.smartobjects","0.0.1"),new BarChartUITemplate("x_value","y_value","z_value"));
		components.put(new ModelId(ModelType.Functionblock,"Gyrometer","com.ipso.smartobjects","0.0.1"),new BarChartUITemplate("x_value","y_value","z_value"));
		components.put(new ModelId(ModelType.Functionblock,"Barometer","com.ipso.smartobjects","0.0.1"),new GaugeUITemplate("sensor_units","min_range_value","max_range_value","sensor_value"));
        components.put(new ModelId(ModelType.Functionblock,"DistanceSensor","devices.fb","1.0.0"),new GaugeUITemplate("sensor_units","min_range_value","max_range_value","distance"));
        components.put(new ModelId(ModelType.Functionblock,"InclineSensor","devices.fb","1.0.0"),new GaugeUITemplate("sensor_units","min_range_value","max_range_value","degree"));

		return components;
	}
	
	override getFileName(InformationModel context) {
		'''details-controller.js'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/js'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		var «element.name.toLowerCase»Details = angular.module('«element.name.toLowerCase».details', []);
		
		«element.name.toLowerCase»Details.controller('DetailsController', ['$rootScope', '$scope', '$location', '$http','$state','$stateParams',
			function($rootScope, $scope, $location, $http, $state,$stateParams) {
				
				// Stuff for websockets	in order to automatically retrieve device values without refreshing	
				$scope.websocket = {
					socket      : null,
					stompClient : null
				};
					
				$scope.thing = null;
						
				$scope.getThing = function() {
					$http.get("rest/devices/"+$stateParams.thingId)
						.success(function(data, status) {
						$scope.thing = data;
						$scope.isLoading = false;
						$scope.errorMessage = null;
						
						«FOR fbProperty : element.properties»
						$scope.set«fbProperty.name.toFirstUpper»();
						«ENDFOR»
																							
						$scope.initSockets();
						
						
					})
					.error(function(data, status, headers, config, statusText) {
						$scope.isLoading = false;
						$scope.errorMessage = "Could not load details!";
					});
				};
				
				«FOR fbProperty : element.properties»
					«var modelId = new ModelId(ModelType.Functionblock,fbProperty.type.name,fbProperty.type.namespace,fbProperty.type.version)»
					«var template = UI_COMPONENTS.getOrDefault(modelId,DEFAULT_UI_TEMPLATE)»
					«template.renderJavascript(fbProperty,context)»
				«ENDFOR»
		       	
				$scope.reconnect = function() {
					setTimeout($scope.initSockets, 10000);
				};
				
				$scope.subscribeToThingChanges = function() {
					$scope.websocket.stompClient.subscribe("/topic/device/" + $scope.thing.thingId, function(status) {
						$scope.$apply(function() {
							$scope.thing = angular.fromJson(status.body);
							«FOR fbProperty : element.properties»
							$scope.set«fbProperty.name.toFirstUpper»();
							«ENDFOR»
						});
					});
				
					// start subscribing to this thing ID
					$scope.websocket.stompClient.send("/«element.name.toLowerCase»/«element.name.toLowerCase»endpoint/subscribe", {}, $scope.thing.thingId);
				};
				
				$scope.$on("$destroy", function() {
					if ($scope.websocket && $scope.websocket.stompClient) {
						$scope.websocket.stompClient.send("/«element.name.toLowerCase»/«element.name.toLowerCase»endpoint/unsubscribe", {}, $scope.thing.thingId);
					}
			    });
				
				$scope.initSockets = function() {
					$scope.websocket.socket = new SockJS('«element.name.toLowerCase»endpoint');
					$scope.websocket.stompClient = Stomp.over($scope.websocket.socket);
					$scope.websocket.stompClient.connect({}, function() {
						console.log("Connected to websocket. Now subscribing.");
						$scope.subscribeToThingChanges();
					});
					$scope.websocket.stompClient.onclose = $scope.reconnect;
				};
						
				$scope.getThing();	 		
			}]);
		'''
	}
	
}