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
		'''«Utils.javaPackageBasePath»'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
package «Utils.javaPackage»;

import java.util.Map;

public class «element.name»Configuration {
	private final static String PROPERTY_INAME = "iname";
	private final static String PROPERTY_SCAN = "scan_enable";
	private final static String PROPERTY_SCANTIME = "scan_time";
	private final static String PROPERTY_PERIOD = "period";
	«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
	private final static String PROPERTY_BOSCHCLOUD_ENDPOINT = "boschcloud_endpoint";
	private final static String PROPERTY_BOSCHCLOUD_SOLUTIONID = "boschcloud_solutionid";
	«ENDIF»

	«FOR fbProperty : element.properties»
	private final static String PROPERTY_«fbProperty.name.toUpperCase» = "enable«fbProperty.name.toFirstUpper»";
	«ENDFOR»
	
	public int scantime = 5;
	public int period = 10;
	public String iname = "hci0";
	public boolean enableScan = false;
	«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
	public String endpoint;
	public String solutionId;
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
				if (properties.get(XDKConfiguration.PROPERTY_SCANTIME) != null) {
					«element.name.toLowerCase»Configuration.scantime = (Integer) properties.get(«element.name»Configuration.PROPERTY_SCANTIME);
				}
				if (properties.get(XDKConfiguration.PROPERTY_PERIOD) != null) {
					«element.name.toLowerCase»Configuration.period = (Integer) properties.get(«element.name»Configuration.PROPERTY_PERIOD);
				}
				if (properties.get(XDKConfiguration.PROPERTY_INAME) != null) {
					«element.name.toLowerCase»Configuration.iname = (String) properties.get(«element.name»Configuration.PROPERTY_INAME);
				}
				«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
				if (properties.get(XDKConfiguration.PROPERTY_BOSCHCLOUD_ENDPOINT) != null) {
					«element.name.toLowerCase»Configuration.endpoint = (String) properties.get(«element.name»Configuration.PROPERTY_BOSCHCLOUD_ENDPOINT);
				}
				if (properties.get(XDKConfiguration.PROPERTY_BOSCHCLOUD_SOLUTIONID) != null) {
					«element.name.toLowerCase»Configuration.solutionId = (String) properties.get(«element.name»Configuration.PROPERTY_BOSCHCLOUD_SOLUTIONID);
				}
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