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
		'''«Utils.javaPackageBasePath»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package «Utils.javaPackage»;
		
		import java.util.ArrayList;
		import java.util.List;
		import java.util.Map;
		import java.util.concurrent.Executors;
		import java.util.concurrent.ScheduledExecutorService;
		import java.util.concurrent.ScheduledFuture;
		import java.util.concurrent.TimeUnit;
		
		import org.eclipse.kura.bluetooth.BluetoothAdapter;
		import org.eclipse.kura.bluetooth.BluetoothDevice;
		import org.eclipse.kura.bluetooth.BluetoothGattSecurityLevel;
		import org.eclipse.kura.bluetooth.BluetoothLeScanListener;
		import org.eclipse.kura.bluetooth.BluetoothService;
		import org.eclipse.kura.configuration.ConfigurableComponent;
		import org.osgi.service.component.ComponentContext;
		import org.osgi.service.component.ComponentException;
		import org.slf4j.Logger;
		import org.slf4j.LoggerFactory;
		
		import «Utils.javaPackage».cloud.IDataService;
		«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
		import «Utils.javaPackage».cloud.bosch.BoschDataService;
		«ELSE»
		import org.eclipse.kura.cloud.CloudService;
		«ENDIF»
		
		public class «element.name»BluetoothFinder implements ConfigurableComponent, BluetoothLeScanListener {
		
			private static final Logger logger = LoggerFactory.getLogger(«element.name»BluetoothFinder.class);
			private BluetoothAdapter bluetoothAdapter;
			private ScheduledExecutorService worker;
			private ScheduledFuture<?> handle;
		
			private long startTime;
			private «element.name»Configuration configuration = null;
			private IDataService dataService;
		
			private BluetoothService bluetoothService;
			
			public void setBluetoothService(BluetoothService bluetoothService) {
				this.bluetoothService = bluetoothService;
			}
			
			public void unsetBluetoothService(BluetoothService bluetoothService) {
				this.bluetoothService = null;
			}
			
			«IF !context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
			private CloudService cloudService;
			
			public void setCloudService(CloudService cloudService) {
				this.cloudService = cloudService;
			}
			
			public void unsetCloudService(CloudService cloudService) {
				this.cloudService = null;
			}
			«ENDIF»
		
			// --------------------------------------------------------------------
			//
			// Activation APIs
			//
			// --------------------------------------------------------------------
			protected void activate(ComponentContext context, Map<String, Object> properties) {
				logger.info("Activating «element.name» App...");
		
				readProperties(properties);
				runApplication();
			}
		
			protected void deactivate(ComponentContext context) {
				cleanup();		
				logger.debug("Deactivating «element.name» App... Done.");
			}
		
			protected void updated(Map<String, Object> properties) {
				readProperties(properties);
				cleanup();						
				runApplication();
				logger.debug("Updating Bluetooth Service... Done.");
			}
			
			private void runApplication() {
				if (configuration.enableScan) {
					this.worker = Executors.newSingleThreadScheduledExecutor();
					try {
						// Get Bluetooth adapter and ensure it is enabled
						this.bluetoothAdapter = this.bluetoothService.getBluetoothAdapter(configuration.iname);
						if (this.bluetoothAdapter != null) {
							logger.info("Bluetooth adapter interface => " + configuration.iname);
							logger.info("Bluetooth adapter address => " + this.bluetoothAdapter.getAddress());
							logger.info("Bluetooth adapter le enabled => " + this.bluetoothAdapter.isLeReady());
		
							if (!this.bluetoothAdapter.isEnabled()) {
								logger.info("Enabling bluetooth adapter...");
								this.bluetoothAdapter.enable();
								logger.info("Bluetooth adapter address => " + this.bluetoothAdapter.getAddress());
							}
							this.startTime = 0;
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
			
			private void cleanup() {
				logger.debug("Cleaning up «element.name» App...");
				if (this.bluetoothAdapter != null && this.bluetoothAdapter.isScanning()) {
					logger.debug("m_bluetoothAdapter.isScanning");
					this.bluetoothAdapter.killLeScan();
				}
		
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
					if (System.currentTimeMillis() - this.startTime >= configuration.scantime * 1000) {
						this.bluetoothAdapter.killLeScan();
					}
				} else {
					if (System.currentTimeMillis() - this.startTime >= configuration.period * 1000) {
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
		
			private void readProperties(Map<String, Object> properties) {
				if (properties != null) {
					configuration = «element.name»Configuration.configurationFrom(properties);
					«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
					dataService = new BoschDataService(configuration.solutionId, configuration.endpoint);
					«ELSE»
					dataService = new KuraCloudDataService(cloudService);
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
				scanResults.stream()
					// Filter for devices which are ours
					.filter(new «element.name»DeviceFilter())
					// This transformer 
					.map(new DeviceTo«element.name»Transformer(configuration))
					// Do something useful with the information model
					.forEach(new «element.name»Consumer(configuration, dataService));
		
			}
		}
		
		'''
	}
	
}