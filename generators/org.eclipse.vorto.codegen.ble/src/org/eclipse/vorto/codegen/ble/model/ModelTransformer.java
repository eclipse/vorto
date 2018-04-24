package org.eclipse.vorto.codegen.ble.model;

import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.mapping.IMapped;
import org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic;
import org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty;
import org.eclipse.vorto.codegen.ble.model.blegatt.Device;
import org.eclipse.vorto.codegen.ble.model.blegatt.ModelFactory;
import org.eclipse.vorto.codegen.ble.model.blegatt.Service;
import org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelFactoryImpl;
import org.eclipse.vorto.codegen.utils.Utils;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;

public class ModelTransformer {

	private ModelFactory factory;
	private InformationModel infomodel;
	private InvocationContext context;
	private Device device;
	
	public ModelTransformer(InformationModel infomodel, InvocationContext context) {
		this.infomodel = infomodel;
		this.context = context;
	}
	
	public Device transform() {
		this.factory = ModelFactoryImpl.init();
		this.device = this.factory.createDevice();
		this.device.setName(this.infomodel.getName());
		this.device.setInfomodel(this.infomodel);
		
		IMapped<InformationModel> map = this.context.getMappedElement(infomodel, "DeviceInfoProfile");
		if (map.hasAttribute("modelNumber")) {
			String modelNumber = map.getAttributeValue("modelNumber", "0");
			Service service = this.factory.createService();
			service.setName("DeviceInformation");
			service.setUuid("0000180a-0000-1000-8000-00805f9b34fb");
			
			Characteristic characteristic = this.factory.createCharacteristic();
			characteristic.setName("ModelNumber");
			characteristic.setUuid("00002a24-0000-1000-8000-00805f9b34fb");
			characteristic.setLength(modelNumber.length() / 2);
			StringBuffer mnBuf = new StringBuffer();
			mnBuf.append("{");
			for (int i = 0; i < modelNumber.length(); i += 2) {
				mnBuf.append("0x"+ modelNumber.substring(i, i+2));
				if (i < modelNumber.length() - 2) {
					mnBuf.append(", ");
				}
			}
			mnBuf.append("}");
			characteristic.setValue(mnBuf.toString());
			
			service.getCharacteristics().add(characteristic);
			
			this.device.getServices().add(service);
		}
		
		for (FunctionblockProperty property : infomodel.getProperties()) {
			transformFunctionblock(property.getType());
		}
		
		return this.device;
	}
	
	private void transformFunctionblock(FunctionblockModel fbm) {
		IMapped<FunctionblockModel> map = this.context.getMappedElement(fbm, "Service");
		if (map.hasAttribute("uuid")) {
			String uuid = map.getAttributeValue("uuid", "");
			Service service = null;
			for (Service s : this.device.getServices()) {
				if (s.getUuid().equals(uuid)) {
					service = s;
					break;
				}
			}
			if (service == null) {
				service = this.factory.createService();
				service.setUuid(uuid);
				if (map.hasAttribute("serviceName")) {
					service.setName(map.getAttributeValue("serviceName", ""));
				} else {
					service.setName(fbm.getName());
				}
				this.device.getServices().add(service);
			}
			service.getFunctionblocks().add(fbm);
			
			FunctionBlock fb = fbm.getFunctionblock();
			if (fb.getStatus() != null) {
				for (Property property : fb.getStatus().getProperties()) {
					transformProperty(service, property);
				}
			}
			if (fb.getConfiguration() != null) {
				for (Property property : fb.getConfiguration().getProperties()) {
					transformProperty(service, property);
				}
			}
			if (fb.getFault() != null) {
				for (Property property : fb.getFault().getProperties()) {
					transformProperty(service, property);
				}
			}
		}
	}

	private void transformProperty(Service service, Property property) {
		IMapped<Property> map = this.context.getMappedElement(property, "source");
		if (map.hasAttribute("uuid")) {
			String uuid = map.getAttributeValue("uuid", "");
			Characteristic characteristic = null;
			for (Characteristic ch : service.getCharacteristics()) {
				if (ch.getUuid().equals(uuid)) {
					characteristic = ch;
					break;
				}
			}
			if (characteristic == null) {
				characteristic = this.factory.createCharacteristic();
				characteristic.setUuid(uuid);
				if (map.hasAttribute("name")) {
					characteristic.setName(map.getAttributeValue("name", ""));
				} else {
					characteristic.setName(((FunctionblockModel)(property.eContainer().eContainer().eContainer())).getName() + property.getName());
				}
				service.getCharacteristics().add(characteristic);
			}
			
			if (Integer.valueOf(map.getAttributeValue("length", "0")) + Integer.valueOf(map.getAttributeValue("offset", "0")) > characteristic.getLength()) {
				characteristic.setLength(Integer.valueOf(map.getAttributeValue("length", "0")) + Integer.valueOf(map.getAttributeValue("offset", "0")));
			}
			
			if (Utils.isReadable(property)) {
				characteristic.setIsReadable(true);
			}
			if (Utils.isWritable(property)) {
				characteristic.setIsWritable(true);
			}
			if (Utils.isEventable(property)) {
				characteristic.setIsEventable(true);
			}
			if (map.hasAttribute("length") && map.hasAttribute("offset") && map.hasAttribute("datatype")) {
				CharacteristicProperty cp = this.factory.createCharacteristicProperty();
				cp.setDatatype(map.getAttributeValue("datatype", ""));
				cp.setLength(Integer.parseInt(map.getAttributeValue("length", "0")));
				cp.setOffset(Integer.parseInt(map.getAttributeValue("offset", "0")));
				cp.setProperty(property);
				characteristic.getProperties().add(cp);
			}
		}
	}
}
