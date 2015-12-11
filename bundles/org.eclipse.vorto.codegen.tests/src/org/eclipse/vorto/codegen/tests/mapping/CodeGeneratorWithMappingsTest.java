/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.tests.mapping;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.codegen.api.ICodeGenerator;
import org.eclipse.vorto.codegen.api.mapping.IMappingAware;
import org.eclipse.vorto.core.api.model.datatype.DatatypeFactory;
import org.eclipse.vorto.core.api.model.datatype.PrimitivePropertyType;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.Configuration;
import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory;
import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.model.IMapping;
import org.eclipse.vorto.core.model.MappingResourceFactory;
import org.junit.Test;

public class CodeGeneratorWithMappingsTest {

	@Test
	public void testGetRuleForInfoModel() {
		InformationModel leftSideModel = createInformationModel();

		final List<MappingRule> rules = new ArrayList<MappingRule>();
		ICodeGenerator<InformationModel> generator = new AbstractGenerator() {
			IMapping mapping = null;

			@Override
			public void generate(InformationModel ctx, IProgressMonitor monitor) {

				rules.addAll(mapping.getRulesByStereoType("configDescription"));
			}

			@Override
			public String getTargetPlatform() {
				return "MyIotPlatform";
			}

			@Override
			public void setMapping(IMapping mapping) {
				this.mapping = mapping;
			}
		};

		MappingModel mappingModel = this.createMappingModel(leftSideModel);
		IMapping mapping = MappingResourceFactory.getInstance().createMapping(mappingModel, new ArrayList<IMapping>());
		((IMappingAware) generator).setMapping(mapping);

		generator.generate(leftSideModel, null);

		assertEquals(1, rules.size());
	}

	private abstract class AbstractGenerator implements ICodeGenerator<InformationModel>, IMappingAware {

		protected IMapping mapping;

		@Override
		public String getName() {
			return "DemoPlatform";
		}

	}

	private InformationModel createInformationModel() {
		InformationModel infoModel = InformationModelFactory.eINSTANCE.createInformationModel();
		infoModel.setName("BoschXYZ");

		FunctionblockModel fbm = FunctionblockFactory.eINSTANCE.createFunctionblockModel();
		fbm.setName("Switcher");

		FunctionBlock fb = FunctionblockFactory.eINSTANCE.createFunctionBlock();
		Configuration config = FunctionblockFactory.eINSTANCE.createConfiguration();
		Property property = DatatypeFactory.eINSTANCE.createProperty();

		PrimitivePropertyType p = DatatypeFactory.eINSTANCE.createPrimitivePropertyType();
		property.setName("brightness");
		p.setType(PrimitiveType.INT);

		property.setType(p);

		config.getProperties().add(property);

		fbm.setFunctionblock(fb);
		infoModel.getProperties().add(createPropertyFromFunctionBlockModel(fbm));
		return infoModel;
	}

	private static FunctionblockProperty createPropertyFromFunctionBlockModel(FunctionblockModel fbm) {
		FunctionblockProperty fbp = InformationModelFactory.eINSTANCE.createFunctionblockProperty();
		fbp.setName("fbm1");
		fbp.setType(fbm);
		return fbp;
	}

	private MappingModel createMappingModel(InformationModel infoModel) {
		MappingModel mappingModel = MappingFactory.eINSTANCE.createInfoModelMappingModel();
		mappingModel.setName("MyMapping");
		mappingModel.getRules().add(createRule());
		return mappingModel;
	}

	private static InfoModelMappingRule createRule() {
		InfoModelMappingRule rule = MappingFactory.eINSTANCE.createInfoModelMappingRule();
		StereoTypeTarget stereoType = MappingFactory.eINSTANCE.createStereoTypeTarget();
		Attribute typeAttribute = MappingFactory.eINSTANCE.createAttribute();
		typeAttribute.setName("type");
		typeAttribute.setValue("Number");

		Attribute nameAttribute = MappingFactory.eINSTANCE.createAttribute();
		nameAttribute.setName("name");
		nameAttribute.setValue("brightness");

		stereoType.setName("configDescription");
		stereoType.getAttributes().add(nameAttribute);
		stereoType.getAttributes().add(typeAttribute);

		rule.setTarget(stereoType);
		return rule;
	}

}