/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.kura.templates.bluetooth

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann
 */
class DeviceBluetoothFinderTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»BluetoothFinder.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.javaPackageBasePath»/bluetooth'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package «Utils.javaPackage».bluetooth;
		
		import java.util.ArrayList;
		import java.util.Date;
		import java.util.List;
		import java.util.Map;
		import java.util.concurrent.Executors;
		import java.util.concurrent.ScheduledExecutorService;
		import java.util.concurrent.ScheduledFuture;
		import java.util.concurrent.TimeUnit;
		
		import org.eclipse.kura.KuraException;
		import org.eclipse.kura.bluetooth.BluetoothAdapter;
		import org.eclipse.kura.bluetooth.BluetoothDevice;
		import org.eclipse.kura.bluetooth.BluetoothGattSecurityLevel;
		import org.eclipse.kura.bluetooth.BluetoothLeScanListener;
		import org.eclipse.kura.bluetooth.BluetoothService;
		«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("false")»
		import org.eclipse.kura.cloud.CloudClient;
		import org.eclipse.kura.cloud.CloudClientListener;
		import org.eclipse.kura.cloud.CloudService;
		«ENDIF»
		import org.eclipse.kura.configuration.ConfigurableComponent;
		import org.eclipse.kura.message.KuraPayload;
		import org.osgi.service.component.ComponentContext;
		import org.osgi.service.component.ComponentException;
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		
		«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
		import «Utils.javaPackage».cloud.*;
		import «Utils.javaPackage».cloud.bosch.BoschDataService;
		«ENDIF»
		
		public class «element.name»BluetoothFinder implements ConfigurableComponent, BluetoothLeScanListener«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("false")», CloudClientListener«ENDIF»  {
		
			private static final Logger logger = LoggerFactory.getLogger(«element.name»BluetoothFinder.class);
		
			private final String APP_ID = "BLE_APP_V1";
			private final String PROPERTY_SCAN = "scan_enable";
			private final String PROPERTY_SCANTIME = "scan_time";
			private final String PROPERTY_PERIOD = "period";
			«FOR fbProperty : element.properties»
			private final String PROPERTY_«fbProperty.name.toUpperCase» = "enable«fbProperty.name.toFirstUpper»";
			«ENDFOR»
			
			«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
			private final String PROPERTY_BOSCHCLOUD_ENDPOINT = "boschcloud_endpoint";
			private final String PROPERTY_BOSCHCLOUD_SOLUTIONID = "boschcloud_solutionid";
		«ENDIF»
		
			private final String PROPERTY_INAME = "iname";
			
			«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("false")»
			private CloudService cloudService;
			private static CloudClient cloudClient;
			private static String topic = "data";
		«ENDIF»
			private List<«element.name»Device> «element.name.toLowerCase»List;
			private BluetoothService bluetoothService;
			private BluetoothAdapter bluetoothAdapter;
			private ScheduledExecutorService worker;
			private ScheduledFuture<?> handle;
		
			private int period = 10;
			private int scantime = 5;
			private long startTime;
			private boolean connected = false;
			private String iname = "hci0";
			private boolean enableScan = false;
			
			«FOR fbProperty : element.properties»
			private boolean enable«fbProperty.name.toFirstUpper» = false;
		«ENDFOR»
		
		«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
			private IDataService dataService;
		«ENDIF»
		
			public void setBluetoothService(BluetoothService bluetoothService) {
				this.bluetoothService = bluetoothService;
			}
		
			public void unsetBluetoothService(BluetoothService bluetoothService) {
				this.bluetoothService = null;
			}
		
			// --------------------------------------------------------------------
			//
			// Activation APIs
			//
			// --------------------------------------------------------------------
			protected void activate(ComponentContext context, Map<String, Object> properties) {
				logger.info("Activating «element.name» App...");
		
				readProperties(properties);
		
				this.«element.name.toLowerCase»List = new ArrayList<«element.name»Device>();
		
				«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("false")»
				try {
					cloudClient = this.cloudService.newCloudClient(this.APP_ID);
					cloudClient.addCloudClientListener(this);
				} catch (KuraException e1) {
					logger.error("Error starting component", e1);
					throw new ComponentException(e1);
				}
				«ENDIF»
				if (this.enableScan) {
		
					this.worker = Executors.newSingleThreadScheduledExecutor();
		
					try {
		
						// Get Bluetooth adapter and ensure it is enabled
						this.bluetoothAdapter = this.bluetoothService.getBluetoothAdapter(this.iname);
						if (this.bluetoothAdapter != null) {
							logger.info("Bluetooth adapter interface => " + this.iname);
							logger.info("Bluetooth adapter address => " + this.bluetoothAdapter.getAddress());
							logger.info("Bluetooth adapter le enabled => " + this.bluetoothAdapter.isLeReady());
		
							if (!this.bluetoothAdapter.isEnabled()) {
								logger.info("Enabling bluetooth adapter...");
								this.bluetoothAdapter.enable();
								logger.info("Bluetooth adapter address => " + this.bluetoothAdapter.getAddress());
							}
							this.startTime = 0;
							this.connected = false;
							this.handle = this.worker.scheduleAtFixedRate(new Runnable() {
		
								@Override
								public void run() {
									checkScan();
								}
							}, 0, 1, TimeUnit.SECONDS);
						} else {
							logger.warn("No Bluetooth adapter found ...");
						}
					} catch (Exception e) {
						logger.error("Error starting component", e);
						throw new ComponentException(e);
					}
				}
			}
		
			protected void deactivate(ComponentContext context) {
		
				logger.debug("Deactivating «element.name» App...");
				if (this.bluetoothAdapter != null && this.bluetoothAdapter.isScanning()) {
					logger.debug("m_bluetoothAdapter.isScanning");
					this.bluetoothAdapter.killLeScan();
				}
		
				for («element.name»Device «element.name.toLowerCase» : this.«element.name.toLowerCase»List) {
					if («element.name.toLowerCase» != null) {
						«element.name.toLowerCase».disconnect();
					}
				}
				this.«element.name.toLowerCase»List.clear();
		
				// cancel a current worker handle if one if active
				if (this.handle != null) {
					this.handle.cancel(true);
				}
		
				// shutting down the worker and cleaning up the properties
				if (this.worker != null) {
					this.worker.shutdown();
				}
		
				// cancel bluetoothAdapter
				this.bluetoothAdapter = null;
		
				logger.debug("Deactivating «element.name» App... Done.");
			}
		
			protected void updated(Map<String, Object> properties) {
		
				readProperties(properties);
		
				try {
					logger.debug("Deactivating «element.name» App...");
					if (this.bluetoothAdapter != null && this.bluetoothAdapter.isScanning()) {
						logger.debug("m_bluetoothAdapter.isScanning");
						this.bluetoothAdapter.killLeScan();
					}
		
					for («element.name»Device «element.name.toLowerCase» : this.«element.name.toLowerCase»List) {
						if («element.name.toLowerCase» != null) {
							«element.name.toLowerCase».disconnect();
						}
					}
					this.«element.name.toLowerCase»List.clear();
		
					// cancel a current worker handle if one is active
					if (this.handle != null) {
						this.handle.cancel(true);
					}
		
					// shutting down the worker and cleaning up the properties
					if (this.worker != null) {
						this.worker.shutdown();
					}
		
					// cancel bluetoothAdapter
					this.bluetoothAdapter = null;
		
					if (this.enableScan) {
						// re-create the worker
						this.worker = Executors.newSingleThreadScheduledExecutor();
		
						// Get Bluetooth adapter and ensure it is enabled
						this.bluetoothAdapter = this.bluetoothService.getBluetoothAdapter(this.iname);
						if (this.bluetoothAdapter != null) {
							logger.info("Bluetooth adapter interface => " + this.iname);
							logger.info("Bluetooth adapter address => " + this.bluetoothAdapter.getAddress());
							logger.info("Bluetooth adapter le enabled => " + this.bluetoothAdapter.isLeReady());
		
							if (!this.bluetoothAdapter.isEnabled()) {
								logger.info("Enabling bluetooth adapter...");
								this.bluetoothAdapter.enable();
								logger.info("Bluetooth adapter address => " + this.bluetoothAdapter.getAddress());
							}
							this.startTime = 0;
							this.connected = false;
							this.handle = this.worker.scheduleAtFixedRate(new Runnable() {
		
								@Override
								public void run() {
									checkScan();
								}
							}, 0, 1, TimeUnit.SECONDS);
						} else {
							logger.warn("No Bluetooth adapter found ...");
						}
					}
				} catch (Exception e) {
					logger.error("Error starting component", e);
					throw new ComponentException(e);
				}
		
				logger.debug("Updating Bluetooth Service... Done.");
			}
		
			// --------------------------------------------------------------------
			//
			// Main task executed every second
			//
			// --------------------------------------------------------------------
		
			void checkScan() {
		
				// Scan for bluetooth devices
				if (this.bluetoothAdapter.isScanning()) {
					logger.info("m_bluetoothAdapter.isScanning");
					if (System.currentTimeMillis() - this.startTime >= this.scantime * 1000) {
						this.bluetoothAdapter.killLeScan();
					}
				} else {
					if (System.currentTimeMillis() - this.startTime >= this.period * 1000) {
						logger.info("startLeScan");
						this.bluetoothAdapter.startLeScan(this);
						this.startTime = System.currentTimeMillis();
					}
				}
		
			}
		
			// --------------------------------------------------------------------
			//
			// Private Methods
			//
			// --------------------------------------------------------------------
		
			private boolean search«element.name»(String address) {
		
				for («element.name»Device «element.name.toLowerCase» : this.«element.name.toLowerCase»List) {
					if («element.name.toLowerCase».getBluetoothDevice().getAdress().equals(address)) {
						return true;
					}
				}
				return false;
			}
		
			private void readProperties(Map<String, Object> properties) {
				if (properties != null) {
					if (properties.get(this.PROPERTY_SCAN) != null) {
						this.enableScan = (Boolean) properties.get(this.PROPERTY_SCAN);
					}
					if (properties.get(this.PROPERTY_SCANTIME) != null) {
						this.scantime = (Integer) properties.get(this.PROPERTY_SCANTIME);
					}
					if (properties.get(this.PROPERTY_PERIOD) != null) {
						this.period = (Integer) properties.get(this.PROPERTY_PERIOD);
					}
					«FOR fbProperty : element.properties»
					if (properties.get(this.PROPERTY_«fbProperty.name.toUpperCase») != null) {
						this.enable«fbProperty.name.toFirstUpper» = (Boolean) properties.get(this.PROPERTY_«fbProperty.name.toUpperCase»);
					}
					«ENDFOR»
					if (properties.get(this.PROPERTY_INAME) != null) {
						this.iname = (String) properties.get(this.PROPERTY_INAME);
					}
					«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
					dataService = new BoschDataService((String) properties.get(PROPERTY_BOSCHCLOUD_SOLUTIONID),
							(String) properties.get(PROPERTY_BOSCHCLOUD_ENDPOINT));
					«ENDIF»
		
				}
			}
		
			// --------------------------------------------------------------------
			//
			// BluetoothLeScanListener APIs
			//
			// --------------------------------------------------------------------
			@Override
			public void onScanFailed(int errorCode) {
				logger.error("Error during scan");
		
			}
		
			@Override
			public void onScanResults(List<BluetoothDevice> scanResults) {
		
				// Scan for «element.name» devices
				for (BluetoothDevice bluetoothDevice : scanResults) {
					logger.info("Address " + bluetoothDevice.getAdress() + " Name " + bluetoothDevice.getName());
		
					if (bluetoothDevice.getName().contains("«element.name»")) {
						logger.info("«element.name» " + bluetoothDevice.getAdress() + " found.");
						if (!search«element.name»(bluetoothDevice.getAdress())) {
							«element.name»Device «element.name.toLowerCase» = new «element.name»Device(bluetoothDevice);
							this.«element.name.toLowerCase»List.add(«element.name.toLowerCase»);
						}
					} else {
						logger.info("Found device = " + bluetoothDevice.getAdress());
					}
				}
		
				logger.debug("Found " + this.«element.name.toLowerCase»List.size() + " «element.name» devices");
		
				// connect to «element.name» device
				for («element.name»Device my«element.name» : this.«element.name.toLowerCase»List) {
		
					if (!my«element.name».isConnected()) {
						logger.info("Connecting to «element.name»...");
						this.connected = my«element.name».connect(this.iname);
						if (this.connected) {
							logger.info("Set security level to low.");
							my«element.name».setSecurityLevel(BluetoothGattSecurityLevel.LOW);
							logger.info("Security Level : " + my«element.name».getSecurityLevel().toString());
						}
					} else {
						logger.info("«element.name» device already connected!");
						this.connected = true;
					}
		
					if (this.connected) {
						«FOR fbProperty : element.properties»
						if (this.enable«fbProperty.name.toFirstUpper») {
							my«element.name».enable«fbProperty.name.toFirstUpper»();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							«fbProperty.type.name» «fbProperty.type.name.toLowerCase» = my«element.name».read«fbProperty.name.toFirstUpper»();
						
							// send «fbProperty.type.name.toLowerCase» to iot cloud backend
							«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("false")»
							KuraPayload payload = new KuraPayload();
							payload.setTimestamp(new Date());
							payload.addMetric("«fbProperty.type.name.toLowerCase»", «fbProperty.type.name.toLowerCase»);
							«ENDIF»
						
							try {
								«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
								dataService.publish«fbProperty.name.toFirstUpper»(my«element.name».getResourceId(), «fbProperty.type.name.toLowerCase»);
								«ELSE»
								if (!payload.metricNames().isEmpty()) {
									cloudClient.publish(topic + "/" + my«element.name».getResourceId(), payload, 0, false);
								}
								«ENDIF»
								
							} catch (Exception e) {
								logger.error("Problem sending data to cloud", e);
							}
						}
						«ENDFOR»
					} else {
						logger.info("Cannot connect to «element.name» device " + my«element.name».getBluetoothDevice().getAdress() + ".");
					}
		
				}
		
			}
			
			«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("false")»
			public void setCloudService(CloudService cloudService) {
				this.cloudService = cloudService;
			}
			
			public void unsetCloudService(CloudService cloudService) {
				this.cloudService = null;
			}
			
			// --------------------------------------------------------------------
			//
			// CloudClientListener APIs
			//
			// --------------------------------------------------------------------
			@Override
			public void onControlMessageArrived(String deviceId, String appTopic, KuraPayload msg, int qos, boolean retain) {
			
			}
			
			@Override
			public void onMessageArrived(String deviceId, String appTopic, KuraPayload msg, int qos, boolean retain) {
			
			}
			
			@Override
			public void onConnectionLost() {
			
			}
			
			@Override
			public void onConnectionEstablished() {
			
			}
			
			@Override
			public void onMessageConfirmed(int messageId, String appTopic) {
			
			}
			
			@Override
			public void onMessagePublished(int messageId, String appTopic) {
			
			}
		«ENDIF»
		}
		
		'''
	}
	
}