package device.mysensor.service.hono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import device.mysensor.service.IDataService;
import com.google.gson.Gson;

/**
 * Data Service Implementation that sends device data to Eclipse Hono MQTT Endpoint
 *
 */
public class HonoDataService implements IDataService {
	
	private String mqttHostUrl;
	private String honoTenant;
	private String dittoTopic;
	private String authId;
	private String deviceId;
	private String password;
	private Map<String, HonoMqttClient> deviceClients = new HashMap<String, HonoMqttClient>();
	private Gson gson = new Gson();
	
	public HonoDataService(String mqttHostUrl, String honoTenant, String dittoTopic, String deviceId, String authId, String password) {
		this.mqttHostUrl = Objects.requireNonNull(mqttHostUrl);
		this.honoTenant = Objects.requireNonNull(honoTenant);
		this.dittoTopic = Objects.requireNonNull(dittoTopic);
		this.deviceId = Objects.requireNonNull(deviceId);
		this.authId = Objects.requireNonNull(authId);
		this.password = Objects.requireNonNull(password);
	}
	
	public void publishStatusPropertiesFunctionBlock(String resourceId, StatusPropertiesFunctionBlock statusPropertiesFunctionBlock) {
		getConnectedHonoClient(resourceId).send("telemetry/" + honoTenant + "/" + resourceId, gson.toJson(wrap(statusPropertiesFunctionBlock.getStatusProperties(),statusPropertiesFunctionBlock.getConfigurationProperties(),"statusPropertiesFunctionBlock")));
	}
	public void publishConfigPropertiesFunctionBlock(String resourceId, ConfigPropertiesFunctionBlock configPropertiesFunctionBlock) {
		getConnectedHonoClient(resourceId).send("telemetry/" + honoTenant + "/" + resourceId, gson.toJson(wrap(configPropertiesFunctionBlock.getStatusProperties(),configPropertiesFunctionBlock.getConfigurationProperties(),"configPropertiesFunctionBlock")));
	}
	public void publishEventsAndOperationsFunctionBlock(String resourceId, eventsAndOperationsFunctionBlock eventsAndOperationsFunctionBlock) {
		getConnectedHonoClient(resourceId).send("telemetry/" + honoTenant + "/" + resourceId, gson.toJson(wrap(eventsAndOperationsFunctionBlock.getStatusProperties(),eventsAndOperationsFunctionBlock.getConfigurationProperties(),"eventsAndOperationsFunctionBlock")));
	}
	
	private <T> Map<String, Object> wrap(T StatusProperties, T ConfigurationProperties, String featureName) {				
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("response-required", Boolean.FALSE);
		
		Map<String, Object> wrapper = new HashMap<String, Object>();
		wrapper.put("topic", dittoTopic + "/things/twin/commands/modify");
	    wrapper.put("path", "/features/"+featureName+"/properties");
	    wrapper.put("value", createValue(StatusProperties,ConfigurationProperties));
		wrapper.put("headers", headers);

		return wrapper; 
	}
	
	private <T> Map<String, Object> createValue(T StatusProperties, T ConfigurationProperties) {
		Map<String, Object> properties = new HashMap<String, Object>();
		
		properties.put("status",StatusProperties);
		properties.put("configuration",ConfigurationProperties);
		return properties;
	}

	private HonoMqttClient getConnectedHonoClient(String resourceId) {
		HonoMqttClient client = deviceClients.get(resourceId);
		if (client == null) {
			client = new HonoMqttClient(mqttHostUrl, resourceId, authId, password);
			deviceClients.put(resourceId, client);
		}
		
		if (!client.isConnected()) {
			client.connect();
		}
		
		return client;
	}
}
