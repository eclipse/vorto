/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.template.basedriver

import org.eclipse.vorto.codegen.api.ITemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.fbbasedriver.tasks.BaseDriverUtil
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class DummyEventReadTaskTemplate implements ITemplate<FunctionblockModel> {
	
	override getContent(FunctionblockModel model,InvocationContext context) {
		'''package «BaseDriverUtil.getDriverPackage»;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bosch.functionblock.dummy.api.device.IDummyDevice;

/**
 * This Class create an entry containing function block property value and watch it for changes
 * If changes is detected, it will fire a event to OSGI event bus
 * @author Luke, Liao Yunhe
 */
public class DummyEventReadTask extends TimerTask {
	protected static final Logger logger = LoggerFactory
			.getLogger(DummyEventReadTask.class.getName());

	Map<String, Timestamp> eventsUpdateTracker = new HashMap<String, Timestamp>();
	IDummyDevice dummyDevice;
	
	public DummyEventReadTask(IDummyDevice dummyDevice){
		this.dummyDevice = dummyDevice;
	}
		
	@Override
	public void run() {		
		for (String eventName : dummyDevice.getAllEventPropertyNames().keySet()) {
			monitorEventFile(eventName);
		}		
	}
	
	//Delete event files 
	public void deleteDummyEventFiles() {
		for (String eventName : dummyDevice.getAllEventPropertyNames().keySet()) {
			File eventFile = getEventFile(eventName);
			if (eventFile.exists()) {
				eventFile.delete();
			}
		}
	}

	/**
	 * Monitor event
	 * @param eventName
	 */
	private void monitorEventFile(String eventName) {

		if (!isEventFileExist(eventName)) {
			createDummyEventFile(eventName);
		}

		if (isEventFileUpdated(eventName)) {
			Map<String, String> eventStore;
			try {
				eventStore = readEventDataFromFile(eventName);
				updateEventLastUpdateTime(eventName,
						this.getEventFile(eventName));
				this.dummyDevice.sendEvent(eventName,eventStore);			
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}

		}
	}
	
	/**
	 * Read event data from file 
	 * @param eventName: Name of event
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, String> readEventDataFromFile(String eventName)
			throws FileNotFoundException, IOException {
		Properties eventProperties = new Properties();
		eventProperties.load(new FileInputStream(getEventFile(eventName)));
		return new HashMap(eventProperties);
	}

	/**
	 * Check if event file is changed since last read
	 * @param eventName
	 * @return true if changed, false otherwise
	 */
	private boolean isEventFileUpdated(String eventName) {
		Timestamp lastUpdatedTime = this.eventsUpdateTracker.get(eventName);
		if (lastUpdatedTime == null) {
			return true;
		}
		return lastUpdatedTime.getTime() != getEventFile(eventName)
				.lastModified();
	}

	//Create a dummy event file in file system
	private void createDummyEventFile(String eventName) {
		Properties eventProperties = new Properties();
		OutputStream propertyOutputStream = null;

		try {

			File eventFile = getEventFile(eventName);
			propertyOutputStream = new FileOutputStream(eventFile);

			for(String eventPropertyName: dummyDevice.getAllEventPropertyNames().get(eventName)){
				eventProperties.put(eventPropertyName, "");
			}
			eventProperties.store(propertyOutputStream, null);
		} catch (IOException io) {
			logger.error(io.getMessage(), io);
		} finally {
			if (propertyOutputStream != null) {
				try {
					propertyOutputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}

		}
	}

	//Update time of last event file change, and put it in memory for checking purpose during next scan
	private void updateEventLastUpdateTime(String eventName, File eventFile) {
		this.eventsUpdateTracker.put(eventName,
				new Timestamp(eventFile.lastModified()));
	}

	private boolean isEventFileExist(String eventName) {
		return this.getEventFile(eventName).exists();
	}

	private File getEventFile(String eventName) {
		return new File(Paths.get(".").toFile().getParentFile(),
				"etc/dummy_event_data_" + eventName + ".properties");
	}
}		
'''
	}
	
}
