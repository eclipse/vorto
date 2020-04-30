/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.mapping.engine.serializer

import org.eclipse.vorto.mapping.engine.model.spec.MappingSpecification
import org.eclipse.vorto.model.EntityModel
import org.eclipse.vorto.model.EnumModel
import org.eclipse.vorto.model.FunctionblockModel
import org.eclipse.vorto.model.Infomodel
import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.model.ModelProperty
import org.eclipse.vorto.model.PrimitiveType
import org.junit.Test

class MappingSpecSerializerTest {

	@Test
	def void testSimpleModels() {
		var spec = new MappingSpecification();

		var unitEnum = EnumModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Enum:1.0.0")).literal("Celcius",
			null).literal("Fahrenheit", null).build();

		var temperatureModel = FunctionblockModel.Builder(
			ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0")).statusProperty(
			ModelProperty.Builder("value", PrimitiveType.FLOAT).withXPathStereotype("/temperature", "iotbutton").build()
		).statusProperty(
			ModelProperty.Builder("unit", unitEnum).withXPathStereotype("'Celcius'", "iotbutton").build()
		).build();

		var infomodel = Infomodel.Builder(ModelId.fromPrettyFormat("com.acme:MyDevice:1.0.0")).withProperty(
			ModelProperty.Builder("indoor", temperatureModel).build()).reference(temperatureModel.id).build

		spec.infoModel = infomodel

		var serializer = MappingSpecificationSerializer.create(spec, createTargetPlatformKey(infomodel.id))
		
		serializer.iterator.forEach[System.out.println(it.serialize)]
	}
	
	@Test
	def void testModelsWithNestedEntity() {
		var spec = new MappingSpecification();

		var sensorValue = EntityModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:SensorValue:1.0.0"))
			.property(ModelProperty.Builder("value",PrimitiveType.FLOAT).withXPathStereotype("/value", "iotbutton").build)
			.build();

		var temperatureModel = FunctionblockModel.Builder(
			ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0")).statusProperty(
			ModelProperty.Builder("value", sensorValue).build()
		).statusProperty(
			ModelProperty.Builder("unit", PrimitiveType.STRING).withXPathStereotype("'Celcius'", "iotbutton").build()
		).build();

		var infomodel = Infomodel.Builder(ModelId.fromPrettyFormat("com.acme:MyDevice:1.0.0")).withProperty(
			ModelProperty.Builder("indoor", temperatureModel).build()).reference(temperatureModel.id).build

		spec.infoModel = infomodel

		var serializer = MappingSpecificationSerializer.create(spec, createTargetPlatformKey(infomodel.id))
		
		serializer.iterator.forEach[System.out.println(it.serialize)]
	}
	
	@Test
	def void testModelsWithDoubleNestedEntity() {
		var spec = new MappingSpecification();

		var nestedType = EntityModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:Nested:1.0.0"))
			.property(ModelProperty.Builder("nestedValue",PrimitiveType.STRING).withXPathStereotype("/n", "iotbutton").build)
			.build();
			
		var sensorValue = EntityModel.Builder(ModelId.fromPrettyFormat("org.eclipse.vorto:SensorValue:1.0.0"))
			.property(ModelProperty.Builder("threshold",PrimitiveType.BOOLEAN).withXPathStereotype("/threshold", "iotbutton").build)
			.property(ModelProperty.Builder("value",nestedType).build)
			.build();

		var temperatureModel = FunctionblockModel.Builder(
			ModelId.fromPrettyFormat("org.eclipse.vorto:Temperature:1.0.0")).statusProperty(
			ModelProperty.Builder("value", sensorValue).build()
		).statusProperty(
			ModelProperty.Builder("unit", PrimitiveType.STRING).withXPathStereotype("'Celcius'", "iotbutton").build()
		).build();

		var infomodel = Infomodel.Builder(ModelId.fromPrettyFormat("com.acme:MyDevice:1.0.0")).withProperty(
			ModelProperty.Builder("indoor", temperatureModel).build()).reference(temperatureModel.id).build

		spec.infoModel = infomodel

		var serializer = MappingSpecificationSerializer.create(spec, createTargetPlatformKey(infomodel.id))
		
		serializer.iterator.forEach[System.out.println(it.serialize)]
	}

	def String createTargetPlatformKey(ModelId modelId) {
		return modelId.getPrettyFormat().replace(".", "_").replace(":", "_");
	}
}
