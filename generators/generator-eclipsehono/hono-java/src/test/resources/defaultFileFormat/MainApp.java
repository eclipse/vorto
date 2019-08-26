package device.mysensor;

import org.apache.log4j.Logger;

import device.mysensor.service.hono.HonoDataService;

/**
 * Example App that uses the Hono Data Service by sending random data to MQTT Connector
 */
public class MySensorApp {
	
	private static final Logger LOGGER = Logger.getLogger(MySensorApp.class);
	
	/**************************************************************************/
	/* Configuration Section */
	/* Adjust according to your Endpoint Configuration*/
	/**************************************************************************/
	
	private static final String HUB_ADAPTER_HOST = "ssl://mqtt.bosch-iot-hub.com:8883";
	
	private static final String TENANT_ID = "ADD TENANT ID HERE";
	
	private static final String DEVICE_ID = "ADD DEVICE ID HERE";
	
	private static final String AUTH_ID = "ADD AUTH ID HERE";
	
	private static final String DITTO_TOPIC = "ADD DITTO TOPIC HERE, e.g. com.mycompany/1234";
	
	private static final String DEVICE_PASSWORD = "ADD DEVICE PASSWORD HERE";
	
	private static final long SEND_INTERVAL_IN_SECONDS = 2;
	
	public static void main(final String... args) {
		HonoDataService honoDataService = new HonoDataService(HUB_ADAPTER_HOST, TENANT_ID, DITTO_TOPIC, 
				DEVICE_ID, AUTH_ID + "@" + TENANT_ID, DEVICE_PASSWORD);
		
		while (true) {
			honoDataService.publishStatusPropertiesFunctionBlock(DEVICE_ID,readStatusPropertiesFunctionBlock());
			honoDataService.publishConfigPropertiesFunctionBlock(DEVICE_ID,readConfigPropertiesFunctionBlock());
			honoDataService.publishEventsAndOperationsFunctionBlock(DEVICE_ID,readEventsAndOperationsFunctionBlock());
			try {
				Thread.sleep(SEND_INTERVAL_IN_SECONDS * 1000);
			} catch (InterruptedException e) {
				LOGGER.error(e);
			}
		}
	}

	/**
	* Reads the statusPropertiesFunctionBlock from the device
	*/
	private static StatusPropertiesFunctionBlock readStatusPropertiesFunctionBlock() {
		StatusPropertiesFunctionBlock statusPropertiesFunctionBlock = new StatusPropertiesFunctionBlock();
		//Status properties
		statusPropertiesFunctionBlock.setStatusValue("");
		statusPropertiesFunctionBlock.setStatusBoolean(new java.util.Random().nextBoolean());
		return statusPropertiesFunctionBlock;
	}
	/**
	* Reads the configPropertiesFunctionBlock from the device
	*/
	private static ConfigPropertiesFunctionBlock readConfigPropertiesFunctionBlock() {
		ConfigPropertiesFunctionBlock configPropertiesFunctionBlock = new ConfigPropertiesFunctionBlock();
		//Configuration properties
		configPropertiesFunctionBlock.setConfigValue("");
		configPropertiesFunctionBlock.setConfigBoolean(new java.util.Random().nextBoolean());
		return configPropertiesFunctionBlock;
	}
	/**
	* Reads the eventsAndOperationsFunctionBlock from the device
	*/
	private static eventsAndOperationsFunctionBlock readEventsAndOperationsFunctionBlock() {
		eventsAndOperationsFunctionBlock eventsAndOperationsFunctionBlock = new eventsAndOperationsFunctionBlock();
		return eventsAndOperationsFunctionBlock;
	}

}
