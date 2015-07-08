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
 package org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates

import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.TestInforModelFactory
import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.IndexHtmlFileTemplate
import org.junit.Test

import static org.junit.Assert.assertEquals

class IndexHtmlFileTemplateTest {

	@Test
	def testGeneration() {
		var model = TestInforModelFactory.createInformationModel();

		var result = new IndexHtmlFileTemplate().getContent(model);
		assertEquals(fetchExpected, result);
	}

	private def String fetchExpected() {
		'''<html>
<head>
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css">
	<link rel="stylesheet" href="css/webdevice.css">
	<script src="http://code.jquery.com/jquery-2.1.1.min.js"></script>
	<script src="http://code.jquery.com/ui/1.10.2/jquery-ui.min.js"></script>		
	<script src="script/webdevice.js"></script>				
	<script>
		$(function() {
			$( "#tabs" ).tabs();
			displayProperties("Fridge");	
			displayProperties("Lamp");	
		});
	</script>						
</head>
<body>
	<table border="0" align="center" width="75%">
		<tr>
			<td>
				<fieldset id="device_meta">
					<legend>Device Information:</legend>
					<table border="0" align="center" width="100%">
						<tr>
							<td  width="20%"><label>Device Name:</label></td>
							<td width="30%"><label id="device_name" class="display">Lighting Device</label></td>
							<td  width="20%"><label>Description:</label></td>
							<td width="30%"><label id="device_description" class="display">comment</label></td>
						</tr>
						<tr>
							<td  width="20%"><label>Serial No.</label></td>
							<td width="30%"><label id="device_id" class="display">LightingDevice-12345</label></td>
							<td  width="20%"><label>Manufacturer:</label></td>
							<td width="30%"><label id="device_manufacturer" class="display">www.bosch.com</label></td>
						</tr>
						<tr>
							<td  width="20%"><label>Owner</label></td>
							<td width="30%"><label id="device_owner" class="display">admin</label></td>
							<td  width="20%"><label>Registration Date:</label></td>
							<td width="30%"><label id="device_regitration_date" class="display">2015-02-27</label></td>
						</tr>								
					</table>
				</fieldset>
			</td>
		</tr>				
	</table>
	<table border="0" align="center" width="75%">
	    <tr><td>			
		<div id="tabs">
			<ul>
				<li><a href="#functionblocktab-fridge">Fridge</a></li>
				<li><a href="#functionblocktab-lamp">Lamp</a></li>
			</ul>
			<div id="functionblocktab-fridge">	
			    <table border="0" align="center" width="100%">
			    				<tr>
			    					<td align="center">
			    					&nbsp;<button type="button" value="on" title="Turn device on" onClick="javascript:invokeOperation('Fridge', 'on')">On</button>&nbsp;
			    					&nbsp;<button type="button" value="Off" title="Turn device off" onClick="javascript:invokeOperation('Fridge', 'Off')">Off</button>&nbsp;
			    					&nbsp;<button type="button" value="Toggle" title="Toggle device" onClick="javascript:invokeOperation('Fridge', 'Toggle')">Toggle</button>&nbsp;
			    					</td>
			    				</tr>				
			    				<tr>
			    					<td>
			    						<fieldset id="Fridge_status_fieldset">
			    							<legend>Status:</legend>
			    							<table border="0" align="center" width="100%">
			    							<tr>
			    								<td  width="20%"><label>Test String:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testString" class="display"></label></td>
			    								<td  width="20%"><label>Test Short:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testShort" class="display"></label></td>
			    							</tr>
			    							<tr>
			    								<td  width="20%"><label>Test Int:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testInt" class="display"></label></td>
			    								<td  width="20%"><label>Test Long:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testLong" class="display"></label></td>
			    							</tr>
			    							<tr>
			    								<td  width="20%"><label>Test Float:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testFloat" class="display"></label></td>
			    								<td  width="20%"><label>Test Double:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testDouble" class="display"></label></td>
			    							</tr>
			    							<tr>
			    								<td  width="20%"><label>Test Datetime:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testDatetime" class="display"></label></td>
			    								<td  width="20%"><label>Test Byte:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testByte" class="display"></label></td>
			    							</tr>
			    							<tr>
			    								<td  width="20%"><label>Test Base64:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testBase64" class="display"></label></td>
			    								<td  width="20%"><label>Test Boolean:</label></td>
			    								<td width="30%"><label id="Fridge_status_id_testBoolean" class="display"></label></td>
			    							</tr>
			    							</table>
			    						</fieldset>
			    					</td>
			    				</tr>
			    				<tr>
			    					<td>
			    						<fieldset id="Fridge_configuration_fieldset">
			    							<legend>Configuration:</legend>
			    							<table border="0" align="center" width="100%">
			    								<tr>
			    									<td  width="20%"><label>Test String:</label></td>
			    									<td width="30%"><input type="text" name="Fridge_configuration_id_testString" id="Fridge_configuration_id_testString">
			    									<td  width="20%"><label>Test Short:</label></td>
			    									<td width="30%"><input type="text" name="Fridge_configuration_id_testShort" id="Fridge_configuration_id_testShort">
			    								</tr>
			    								<tr>
			    									<td  width="20%"><label>Test Int:</label></td>
			    									<td width="30%"><input type="text" name="Fridge_configuration_id_testInt" id="Fridge_configuration_id_testInt">
			    										<td  width="20%"><label></td>
			    										<td  width="30%"><label></td>
			    									</tr>
			    								<tr>
			    								<td  width="20%"></td>
			    								<td width="30%">
			    								</td>
			    								<td  width="20%">
			    								
			    								</td>
			    								<td width="30%" align="right">
			    									<button type="button" value="setR" title="" onClick="javascript:saveConfiguration('Fridge')">Save</button>
			    								</td>												
			    								</td>
			    							</tr>								
			    							</table>
			    						</fieldset>
			    					</td>
			    				</tr>				
			    				<tr>
			    					<td>
			    						<fieldset id="Fridge_fault_fieldset">
			    							<legend>Fault:</legend>
			    							<table border="0" align="center" width="100%">
			    							<tr>
			    								<td  width="20%"><label>Is Fault:</label></td>
			    								<td width="30%"><label id="Fridge_fault_id_isFault" class="display"></label></td>
			    								<td  width="20%"><label></td>
			    								<td  width="30%"><label></td>
			    							</tr>
			    							</table>
			    						</fieldset>
			    					</td>
			    				</tr>
			    			</table>
			    			<table border="0" width="100%" cellpadding="0" cellspacing="0" class="event">
			    				<tr>
			    					<th align="center">Event Id</th>
			    					<th align="center">Type</th>
			    					<th align="center">Priority</th>
			    					<th align="center">Date Time</th>
			    					<th align="center">Details</th>
			    				</tr>	
			    				<tr>
			    					<td align="center">1</td>
			    					<td align="center">Device Detected</td>
			    					<td align="center">30</td>
			    					<td align="center">2015-02-27 10:10:02</td>
			    					<td align="center">New Fridge function block instance detected.</td>
			    				</tr>	
			    			</table>			
			</div>
			<div id="functionblocktab-lamp">	
			    <table border="0" align="center" width="100%">
			    				<tr>
			    					<td align="center">
			    					&nbsp;<button type="button" value="on" title="Turn device on" onClick="javascript:invokeOperation('Lamp', 'on')">On</button>&nbsp;
			    					&nbsp;<button type="button" value="Off" title="Turn device off" onClick="javascript:invokeOperation('Lamp', 'Off')">Off</button>&nbsp;
			    					&nbsp;<button type="button" value="Toggle" title="Toggle device" onClick="javascript:invokeOperation('Lamp', 'Toggle')">Toggle</button>&nbsp;
			    					</td>
			    				</tr>				
			    				<tr>
			    					<td>
			    						<fieldset id="Lamp_status_fieldset">
			    							<legend>Status:</legend>
			    							<div class="column">
			    								<label>No status information is available</label>
			    							</div>
			    						</fieldset>
			    					</td>
			    				</tr>
			    				<tr>
			    					<td>
			    						<fieldset id="Lamp_configuration_fieldset">
			    							<legend>Configuration:</legend>
			    							<div class="column">
			    								<label>No configuration information is available</label>
			    							</div>
			    						</fieldset>
			    					</td>
			    				</tr>				
			    				<tr>
			    					<td>
			    						<fieldset id="Lamp_fault_fieldset">
			    							<legend>Fault:</legend>
			    							<div class="column">
			    								<label>No fault information is available</label>
			    							</div>
			    						</fieldset>
			    					</td>
			    				</tr>
			    			</table>
			    			<table border="0" width="100%" cellpadding="0" cellspacing="0" class="event">
			    				<tr>
			    					<th align="center">Event Id</th>
			    					<th align="center">Type</th>
			    					<th align="center">Priority</th>
			    					<th align="center">Date Time</th>
			    					<th align="center">Details</th>
			    				</tr>	
			    				<tr>
			    					<td align="center">1</td>
			    					<td align="center">Device Detected</td>
			    					<td align="center">30</td>
			    					<td align="center">2015-02-27 10:10:02</td>
			    					<td align="center">New Lamp function block instance detected.</td>
			    				</tr>	
			    			</table>			
			</div>
		</div>		
	</td></tr></table>			
</body>
</html>
'''
	}
}
