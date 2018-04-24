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
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.kura.templates.Utils

/**
 * 
 * @author Erle Czar Mantos - Robert Bosch (SEA) Pte. Ltd.
 *
 */
class ConfigurationTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»Configuration.java'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.getJavaPackageBasePath(context)»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.getJavaPackage(element)»;

import java.util.Map;

public class «element.name»Configuration {
	private final static String PROPERTY_INAME = "iname";
	private final static String PROPERTY_SCAN = "scan_enable";
	private final static String PROPERTY_SCANINTERVAL = "scan_interval";
	private final static String PROPERTY_SCANTIME = "scan_time";
	private final static String PROPERTY_UPDATEINTERVAL = "update_interval";
	private final static String PROPERTY_DEVICEFILTER = "device_filter";
	
	«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
	private final static String PROPERTY_BOSCHCLOUD_ENDPOINT = "boschcloud_endpoint";
	private final static String PROPERTY_BOSCHCLOUD_SOLUTIONID = "boschcloud_solutionid";
	«ELSE»
	«IF context.configurationProperties.getOrDefault("boschhub","false").equalsIgnoreCase("true")»
	private final static String PROPERTY_HUB_URL = "mqttHostUrl";
	private final static String PROPERTY_HUB_TENANT = "hubTenant";
	«ELSE»
	private final static String PROPERTY_PUBLISHTOPIC = "publishTopic";
	«ENDIF»
	«ENDIF»

	«FOR fbProperty : element.properties»
	private final static String PROPERTY_«fbProperty.name.toUpperCase» = "enable«fbProperty.name.toFirstUpper»";
	«ENDFOR»
	
	public int scantime = 10;
	public int scaninterval = 60;
	public int updateInterval = 10;
	public String iname = "hci0";
	public boolean enableScan = false;
	public String deviceFilter;
	«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
	public String endpoint;
	public String solutionId;
	«ELSE»
	«IF context.configurationProperties.getOrDefault("boschhub","false").equalsIgnoreCase("true")»
	public String hubUrl;
	public String hubTenant;
	«ELSE»
	public String publishTopic;
	«ENDIF»
	«ENDIF»

	«FOR fbProperty : element.properties»
	public boolean enable«fbProperty.name.toFirstUpper» = false;
	«ENDFOR»
	
	public static «element.name»Configuration newConfiguration() {
		return new «element.name»Configuration();
	}
	
	public static «element.name»Configuration configurationFrom(Map<String, Object> properties) {
			«element.name»Configuration «element.name.toLowerCase»Configuration = new «element.name»Configuration();
			
			if (properties != null) {
				if (properties.get(«element.name»Configuration.PROPERTY_SCAN) != null) {
					«element.name.toLowerCase»Configuration.enableScan = (Boolean) properties.get(«element.name»Configuration.PROPERTY_SCAN);
				}
				
				if (properties.get(«element.name»Configuration.PROPERTY_SCANTIME) != null) {
					«element.name.toLowerCase»Configuration.scantime = (Integer) properties.get(«element.name»Configuration.PROPERTY_SCANTIME);
				}
				
				if (properties.get(«element.name»Configuration.PROPERTY_SCANINTERVAL) != null) {
					«element.name.toLowerCase»Configuration.scaninterval = (Integer) properties.get(«element.name»Configuration.PROPERTY_SCANINTERVAL);
				}
				
				if (properties.get(«element.name»Configuration.PROPERTY_UPDATEINTERVAL) != null) {
					«element.name.toLowerCase»Configuration.updateInterval = (Integer) properties.get(«element.name»Configuration.PROPERTY_UPDATEINTERVAL);
				}
				
				if (properties.get(«element.name»Configuration.PROPERTY_DEVICEFILTER) != null) {
					«element.name.toLowerCase»Configuration.deviceFilter = (String) properties.get(«element.name»Configuration.PROPERTY_DEVICEFILTER);
				}
				
				if (properties.get(«element.name»Configuration.PROPERTY_INAME) != null) {
					«element.name.toLowerCase»Configuration.iname = (String) properties.get(«element.name»Configuration.PROPERTY_INAME);
				}
				
				«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
				if (properties.get(«element.name»Configuration.PROPERTY_BOSCHCLOUD_ENDPOINT) != null) {
					«element.name.toLowerCase»Configuration.endpoint = (String) properties.get(«element.name»Configuration.PROPERTY_BOSCHCLOUD_ENDPOINT);
				}
				
				if (properties.get(«element.name»Configuration.PROPERTY_BOSCHCLOUD_SOLUTIONID) != null) {
					«element.name.toLowerCase»Configuration.solutionId = (String) properties.get(«element.name»Configuration.PROPERTY_BOSCHCLOUD_SOLUTIONID);
				}
				
				«ELSE»
				«IF context.configurationProperties.getOrDefault("boschhub","false").equalsIgnoreCase("true")»
				if (properties.get(«element.name»Configuration.PROPERTY_HUB_URL) != null) {
					«element.name.toLowerCase»Configuration.hubUrl = (String) properties.get(«element.name»Configuration.PROPERTY_HUB_URL);
				}
				
				if (properties.get(«element.name»Configuration.PROPERTY_HUB_TENANT) != null) {
					«element.name.toLowerCase»Configuration.hubTenant = (String) properties.get(«element.name»Configuration.PROPERTY_HUB_TENANT);
				}
				
				«ELSE»
				if (properties.get(«element.name»Configuration.PROPERTY_PUBLISHTOPIC) != null) {
					«element.name.toLowerCase»Configuration.publishTopic = (String) properties.get(«element.name»Configuration.PROPERTY_PUBLISHTOPIC);
				}
				
				«ENDIF»
				«ENDIF»
				«FOR fbProperty : element.properties»
				if (properties.get(«element.name»Configuration.PROPERTY_«fbProperty.name.toUpperCase») != null) {
					«element.name.toLowerCase»Configuration.enable«fbProperty.name.toFirstUpper» = (Boolean) properties.get(«element.name»Configuration.PROPERTY_«fbProperty.name.toUpperCase»);
				}
				
				«ENDFOR»
			}
			
			return «element.name.toLowerCase»Configuration;
		}
	}
'''
	}
	
}