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

import java.util.Optional;
import java.util.UUID;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.BooleanAttributeProperty;
import org.eclipse.vorto.repository.api.content.BooleanAttributePropertyType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.IPropertyAttribute;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Stereotype;

public class BleGattDeviceBuilder {

	private BleGattDevice device;
	private Infomodel infomodel;
	private IMappingSpecification mappingSpec;

	public static BleGattDeviceBuilder newBuilder() {
		return new BleGattDeviceBuilder();
	}
	
	public BleGattDeviceBuilder withSpecification(IMappingSpecification mappingSpec) {
		this.infomodel = mappingSpec.getInfoModel();
		this.mappingSpec = mappingSpec;
		return this;
	}
 	
	public BleGattDevice build() {
		this.device = new BleGattDevice();

		for (ModelProperty fbprop : infomodel.getFunctionblocks())
		{
			BleGattService service = new BleGattService();
			FunctionblockModel fb = mappingSpec.getFunctionBlock((ModelId) fbprop.getType());
			
			Optional<Stereotype> stereotype = fb.getStereotype("source");
			if (stereotype.isPresent()) {
				String uuid = stereotype.get().getAttributes().get("uuid");
				if (uuid != null) {
					service.setUuid(UUID.fromString(uuid));
				}
			}
			
			for (ModelProperty prop : fb.getConfigurationProperties())
			{
				BleGattCharacteristic ch = buildCharacteristic(prop); 
				service.addCharacteristics(ch);
			}
			for (ModelProperty prop : fb.getStatusProperties())
			{
				BleGattCharacteristic ch = buildCharacteristic(prop); 
				service.addCharacteristics(ch);
			}	
			
			device.addService(service);
		}
		
		return device;
	}
	
	private BleGattCharacteristic buildCharacteristic(ModelProperty property) {
		
		BleGattCharacteristic ch = new BleGattCharacteristic();
		
		// Determine if a characteristic uses notification (derived from "eventable" attribute)
		for (IPropertyAttribute attr : property.getAttributes())
		{
			if (attr instanceof BooleanAttributeProperty)
			{
				BooleanAttributeProperty attrprop = (BooleanAttributeProperty)attr;
				if ((attrprop.getType() == BooleanAttributePropertyType.EVENTABLE) && (attrprop.isValue()))
				{
					ch.setNotified(true);
				}
			}
		}

		// Get the relevant information for BLE GATT Characteristics
		Optional<Stereotype> sourceSt = property.getStereotype("source");
		if (sourceSt.isPresent()) {
			Stereotype source = sourceSt.get();
		
			if (source.getAttributes().containsKey("uuid"))
			{
				ch.setUuid(UUID.fromString(source.getAttributes().get("uuid").toString()));
			}
			if (source.getAttributes().containsKey("onConnect"))
			{
				BleGattInitSequenceElement elt = new BleGattInitSequenceElement(ch, source.getAttributes().get("onConnect"));
				device.addInitSequenceElement(elt);
			}
			if (source.getAttributes().containsKey("handle"))
			{
				ch.setHandle(source.getAttributes().get("handle"));
			}
		}
		
		return ch;
	}
}
