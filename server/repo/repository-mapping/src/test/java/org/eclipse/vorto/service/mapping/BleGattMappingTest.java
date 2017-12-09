/**
 * Copyright (c) 2017 Oliver Meili
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
 * - Oliver Meili <omi@ieee.org>
 */
package org.eclipse.vorto.service.mapping;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.BooleanAttributeProperty;
import org.eclipse.vorto.repository.api.content.BooleanAttributePropertyType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.IPropertyAttribute;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.PrimitiveType;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.service.mapping.ble.BleGattCharacteristic;
import org.eclipse.vorto.service.mapping.ble.BleGattDevice;
import org.eclipse.vorto.service.mapping.ble.BleGattDeviceBuilder;
import org.eclipse.vorto.service.mapping.ditto.DittoData;
import org.eclipse.vorto.service.mapping.ditto.Feature;
import org.eclipse.vorto.service.mapping.internal.converter.JavascriptFunctions;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;
import org.junit.Test;

public class BleGattMappingTest {

	@Test
	public void testBleGattMapping() throws Exception {
		
		// SETUP
		TestMappingSpecification mapping = new TestMappingSpecification();
		
		BleGattDevice bleGattDevice = BleGattDeviceBuilder.newBuilder()
									.withSpecification(mapping).build();		
		IDataMapper<DittoData> mapper = IDataMapper.newBuilder()
									.withSpecification(mapping).buildDittoMapper();
		
		// RUNTIME
		BleGattCharacteristic barometerValue = bleGattDevice.getCharacteristics().get("f000aa41-0451-4000-b000-000000000000");
		barometerValue.setData(new Short[] {0x00, 0x00, 0x00, 0xD0, 0x07, 0x00});
		
		BleGattCharacteristic accelerometerValue = bleGattDevice.getCharacteristics().get("f000aa81-0451-4000-b000-000000000000");
		accelerometerValue.setData(new Short[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, /* X Value = 1.0 */0x00, 0x40, /* Y Value = -1 */ 0x00, 0xC0, /* Z Value  = 0.0 */ 0x00, 0x00 });
		
		DittoData mappedDittoOutput = mapper.map(DataInput.newInstance().fromObject(bleGattDevice),MappingContext.empty());
		
		// TEST
		Feature buttonFeature = mappedDittoOutput.getFeatures().get("accelerometer");
		
		assertEquals(1.0,buttonFeature.getProperty("x_value"));
		assertEquals(-1.0,buttonFeature.getProperty("y_value"));	
		
		Feature voltageFeature = mappedDittoOutput.getFeatures().get("barometer");
		
		assertEquals(20.00,voltageFeature.getProperty("sensor_value"));
		
		System.out.println(mappedDittoOutput.toJson());
		
	}

	private static class TestMappingSpecification implements IMappingSpecification {

		private static Map<String, FunctionblockModel> FBS = new HashMap<String, FunctionblockModel>(2);
		
		static {
			
			//################# BAROMETER Function Block ####################
			
			FunctionblockModel barometerModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.Barometer:1.0.0"), ModelType.Functionblock);
			barometerModel.setDisplayName("Barometer");
			barometerModel.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> barometerFunctionsAttributes = new HashMap<String, String>();
			barometerFunctionsAttributes.put("_namespace", "custom");
			barometerFunctionsAttributes.put("convertSensorValue", "function convertSensorValue(value) { return value*0.01; }");
			barometerModel.addStereotype(Stereotype.create("functions", barometerFunctionsAttributes));

			HashMap<String, String> barometerSourceAttributes = new HashMap<String, String>();
			barometerFunctionsAttributes.put("uuid", "f000aa40-0451-4000-b000-000000000000");
			barometerModel.addStereotype(Stereotype.create("source", barometerSourceAttributes));
			
			ModelProperty barometerConfigProperty = new ModelProperty();
			barometerConfigProperty.setMandatory(true);
			barometerConfigProperty.setName("sensor_enable");
			barometerConfigProperty.setType(PrimitiveType.INT);
			barometerConfigProperty.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> barometerConfigAttributes = new HashMap<String, String>();
			barometerConfigAttributes.put("uuid", "f000aa42-0451-4000-b000-000000000000");
			barometerConfigAttributes.put("offset", "0");
			barometerConfigAttributes.put("onConnect", "1");
			barometerConfigAttributes.put("datatype", "uint8");
			
			barometerConfigProperty.addStereotype(Stereotype.create("source", barometerConfigAttributes));

			ModelProperty barometerPeriodProperty = new ModelProperty();
			barometerPeriodProperty.setMandatory(true);
			barometerPeriodProperty.setName("sensor_period");
			barometerPeriodProperty.setType(PrimitiveType.INT);
			barometerPeriodProperty.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> barometerPeriodAttributes = new HashMap<String, String>();
			barometerPeriodAttributes.put("uuid", "f000aa44-0451-4000-b000-000000000000");
			barometerPeriodAttributes.put("offset", "0");
			barometerPeriodAttributes.put("datatype", "uint8");
			
			barometerPeriodProperty.addStereotype(Stereotype.create("source", barometerPeriodAttributes));
			
			barometerModel.setConfigurationProperties(Arrays.asList(new ModelProperty[] {barometerConfigProperty,barometerPeriodProperty}));

			ModelProperty barometerValueProperty = new ModelProperty();
			barometerValueProperty.setMandatory(true);
			barometerValueProperty.setName("sensor_value");
			barometerValueProperty.setType(PrimitiveType.INT);
			barometerValueProperty.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> barometerValueAttributes = new HashMap<String, String>();
			barometerValueAttributes.put("uuid", "f000aa41-0451-4000-b000-000000000000");
			barometerValueAttributes.put("offset", "3");
			barometerValueAttributes.put("length", "3");
			barometerValueAttributes.put("datatype", "uint8");
			barometerValueAttributes.put("xpath", "custom:convertSensorValue(conversion:byteArrayToInt(characteristics/${uuid}/data, ${offset}, 0, 0, ${length}))");
			
			List<IPropertyAttribute> barometerValueAttrs = new ArrayList<IPropertyAttribute>();
			barometerValueAttrs.add(new BooleanAttributeProperty(BooleanAttributePropertyType.EVENTABLE, true));
			barometerValueProperty.setAttributes(barometerValueAttrs);
			
			barometerValueProperty.addStereotype(Stereotype.create("source", barometerValueAttributes));
			
			barometerModel.setStatusProperties(Arrays.asList(new ModelProperty[] {barometerValueProperty}));
			
			FBS.put("barometer", barometerModel);
			
			//################# ACELLEROMETER Function Block ####################

			FunctionblockModel accelerometerModel = new FunctionblockModel(ModelId.fromPrettyFormat("demo.fb.Accelerometer:1.0.0"), ModelType.Functionblock);
			accelerometerModel.setDisplayName("Accelerometer");
			accelerometerModel.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> accelerometerFunctionsAttributes = new HashMap<String, String>();
			accelerometerFunctionsAttributes.put("_namespace", "custom");
			accelerometerFunctionsAttributes.put("convertAccelValue", "function convertAccelValue(value) { return (((value > 32767) ? (value - 65536) : value) / (32768 / 2)); }");
			accelerometerModel.addStereotype(Stereotype.create("functions", accelerometerFunctionsAttributes));

			HashMap<String, String> accelerometerSourceAttributes = new HashMap<String, String>();
			accelerometerFunctionsAttributes.put("uuid", "f000aa80-0451-4000-b000-000000000000");
			accelerometerModel.addStereotype(Stereotype.create("source", accelerometerSourceAttributes));
			
			ModelProperty accelerometerConfigProperty = new ModelProperty();
			accelerometerConfigProperty.setMandatory(true);
			accelerometerConfigProperty.setName("sensor_enable");
			accelerometerConfigProperty.setType(PrimitiveType.INT);
			accelerometerConfigProperty.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> accelerometerConfigAttributes = new HashMap<String, String>();
			accelerometerConfigAttributes.put("uuid", "f000aa82-0451-4000-b000-000000000000");
			accelerometerConfigAttributes.put("offset", "0");
			accelerometerConfigAttributes.put("onConnect", "FF 00");
			accelerometerConfigAttributes.put("datatype", "uint8");
			
			accelerometerConfigProperty.addStereotype(Stereotype.create("source", accelerometerConfigAttributes));

			ModelProperty accelerometerPeriodProperty = new ModelProperty();
			accelerometerPeriodProperty.setMandatory(true);
			accelerometerPeriodProperty.setName("sensor_period");
			accelerometerPeriodProperty.setType(PrimitiveType.INT);
			accelerometerPeriodProperty.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> accelerometerPeriodAttributes = new HashMap<String, String>();
			accelerometerPeriodAttributes.put("uuid", "f000aa83-0451-4000-b000-000000000000");
			accelerometerPeriodAttributes.put("offset", "0");
			accelerometerPeriodAttributes.put("datatype", "uint8");
			
			accelerometerPeriodProperty.addStereotype(Stereotype.create("source", accelerometerPeriodAttributes));
			
			accelerometerModel.setConfigurationProperties(Arrays.asList(new ModelProperty[] {accelerometerConfigProperty,accelerometerPeriodProperty}));

			ModelProperty accelerometerXValueProperty = new ModelProperty();
			accelerometerXValueProperty.setMandatory(true);
			accelerometerXValueProperty.setName("x_value");
			accelerometerXValueProperty.setType(PrimitiveType.INT);
			accelerometerXValueProperty.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> accelerometerXValueAttributes = new HashMap<String, String>();
			accelerometerXValueAttributes.put("uuid", "f000aa81-0451-4000-b000-000000000000");
			accelerometerXValueAttributes.put("offset", "6");
			accelerometerXValueAttributes.put("length", "2");
			accelerometerXValueAttributes.put("datatype", "uint8");
			accelerometerXValueAttributes.put("xpath", "custom:convertAccelValue(conversion:byteArrayToInt(characteristics/${uuid}/data, ${offset}, 0, 0, ${length}))");
			
			List<IPropertyAttribute> accelerometerXValueAttrs = new ArrayList<IPropertyAttribute>();
			accelerometerXValueAttrs.add(new BooleanAttributeProperty(BooleanAttributePropertyType.EVENTABLE, true));
			accelerometerXValueProperty.setAttributes(accelerometerXValueAttrs);
			
			accelerometerXValueProperty.addStereotype(Stereotype.create("source", accelerometerXValueAttributes));
			
			ModelProperty accelerometerYValueProperty = new ModelProperty();
			accelerometerYValueProperty.setMandatory(true);
			accelerometerYValueProperty.setName("y_value");
			accelerometerYValueProperty.setType(PrimitiveType.INT);
			accelerometerYValueProperty.setTargetPlatformKey("blegatt");
			
			HashMap<String, String> accelerometerYValueAttributes = new HashMap<String, String>();
			accelerometerYValueAttributes.put("uuid", "f000aa81-0451-4000-b000-000000000000");
			accelerometerYValueAttributes.put("offset", "8");
			accelerometerYValueAttributes.put("length", "2");
			accelerometerYValueAttributes.put("datatype", "uint8");
			accelerometerYValueAttributes.put("xpath", "custom:convertAccelValue(conversion:byteArrayToInt(characteristics/${uuid}/data, ${offset}, 0, 0, ${length}))");

			List<IPropertyAttribute> accelerometerYValueAttrs = new ArrayList<IPropertyAttribute>();
			accelerometerYValueAttrs.add(new BooleanAttributeProperty(BooleanAttributePropertyType.EVENTABLE, true));
			accelerometerYValueProperty.setAttributes(accelerometerYValueAttrs);
			
			accelerometerYValueProperty.addStereotype(Stereotype.create("source", accelerometerYValueAttributes));

			ModelProperty accelerometerZValueProperty = new ModelProperty();
			accelerometerZValueProperty.setMandatory(true);
			accelerometerZValueProperty.setName("z_value");
			accelerometerZValueProperty.setType(PrimitiveType.INT);
			accelerometerZValueProperty.setTargetPlatformKey("blegatt");
		
			HashMap<String, String> accelerometerZValueAttributes = new HashMap<String, String>();
			accelerometerZValueAttributes.put("uuid", "f000aa81-0451-4000-b000-000000000000");
			accelerometerZValueAttributes.put("offset", "10");
			accelerometerZValueAttributes.put("length", "2");
			accelerometerZValueAttributes.put("datatype", "uint8");
			accelerometerZValueAttributes.put("xpath", "custom:convertAccelValue(conversion:byteArrayToInt(characteristics/${uuid}/data, ${offset}, 0, 0, ${length}))");

			List<IPropertyAttribute> accelerometerZValueAttrs = new ArrayList<IPropertyAttribute>();
			accelerometerZValueAttrs.add(new BooleanAttributeProperty(BooleanAttributePropertyType.EVENTABLE, true));
			accelerometerZValueProperty.setAttributes(accelerometerZValueAttrs);
			
			accelerometerZValueProperty.addStereotype(Stereotype.create("source", accelerometerZValueAttributes));

			accelerometerModel.setStatusProperties(Arrays.asList(new ModelProperty[] {accelerometerXValueProperty, accelerometerYValueProperty, accelerometerZValueProperty}));
			
			FBS.put("accelerometer", accelerometerModel);			
		}
		
		@Override
		public Infomodel getInfoModel() {
			
			Infomodel infomodel = new Infomodel(ModelId.fromPrettyFormat("devices.TiSensorTag:1.0.0"), ModelType.InformationModel);
			infomodel.setTargetPlatformKey("blegatt");
			
			ModelProperty barometerProperty = new ModelProperty();
			barometerProperty.setName("barometer");
			barometerProperty.setType(ModelId.fromPrettyFormat("demo.fb.Barometer:1.0.0"));
			infomodel.getFunctionblocks().add(barometerProperty);
			
			ModelProperty accelerometerProperty = new ModelProperty();
			accelerometerProperty.setName("accelerometer");
			accelerometerProperty.setType(ModelId.fromPrettyFormat("demo.fb.Accelerometer:1.0.0"));
			infomodel.getFunctionblocks().add(accelerometerProperty);
						
			return infomodel;
		}

		@Override
		public FunctionblockModel getFunctionBlock(String name) {	
			return FBS.get(name);
		}

		@Override
		public Optional<Functions> getCustomFunctions() {
			JavascriptFunctions functions = new JavascriptFunctions("custom");
			functions.addFunction("convertAccelValue","function convertAccelValue(value) { return (((value > 32767) ? (value - 65536) : value) / (32768 / 2)); }");
			functions.addFunction("convertSensorValue","function convertSensorValue(value) { return value*0.01; }");
			return Optional.of(functions);
		}
		
	}

}
