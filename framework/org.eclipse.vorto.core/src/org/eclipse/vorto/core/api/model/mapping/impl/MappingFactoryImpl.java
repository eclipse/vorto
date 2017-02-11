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
/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.vorto.core.api.model.mapping.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MappingFactoryImpl extends EFactoryImpl implements MappingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MappingFactory init() {
		try {
			MappingFactory theMappingFactory = (MappingFactory)EPackage.Registry.INSTANCE.getEFactory(MappingPackage.eNS_URI);
			if (theMappingFactory != null) {
				return theMappingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MappingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case MappingPackage.MAPPING_MODEL: return createMappingModel();
			case MappingPackage.INFO_MODEL_MAPPING_MODEL: return createInfoModelMappingModel();
			case MappingPackage.INFO_MODEL_MAPPING_RULE: return createInfoModelMappingRule();
			case MappingPackage.INFOMODEL_SOURCE: return createInfomodelSource();
			case MappingPackage.INFO_MODEL_PROPERTY_SOURCE: return createInfoModelPropertySource();
			case MappingPackage.INFO_MODEL_ATTRIBUTE_SOURCE: return createInfoModelAttributeSource();
			case MappingPackage.FUNCTION_BLOCK_MAPPING_MODEL: return createFunctionBlockMappingModel();
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE: return createFunctionBlockMappingRule();
			case MappingPackage.FUNCTION_BLOCK_SOURCE: return createFunctionBlockSource();
			case MappingPackage.FUNCTION_BLOCK_PROPERTY_SOURCE: return createFunctionBlockPropertySource();
			case MappingPackage.FUNCTION_BLOCK_ATTRIBUTE_SOURCE: return createFunctionBlockAttributeSource();
			case MappingPackage.CONFIGURATION_SOURCE: return createConfigurationSource();
			case MappingPackage.STATUS_SOURCE: return createStatusSource();
			case MappingPackage.OPERATION_SOURCE: return createOperationSource();
			case MappingPackage.EVENT_SOURCE: return createEventSource();
			case MappingPackage.ENTITY_MAPPING_MODEL: return createEntityMappingModel();
			case MappingPackage.ENTITY_MAPPING_RULE: return createEntityMappingRule();
			case MappingPackage.ENTITY_SOURCE: return createEntitySource();
			case MappingPackage.ENTITY_PROPERTY_SOURCE: return createEntityPropertySource();
			case MappingPackage.ENTITY_ATTRIBUTE_SOURCE: return createEntityAttributeSource();
			case MappingPackage.ENUM_MAPPING_MODEL: return createEnumMappingModel();
			case MappingPackage.ENUM_MAPPING_RULE: return createEnumMappingRule();
			case MappingPackage.ENUM_SOURCE: return createEnumSource();
			case MappingPackage.ENUM_PROPERTY_SOURCE: return createEnumPropertySource();
			case MappingPackage.ENUM_ATTRIBUTE_SOURCE: return createEnumAttributeSource();
			case MappingPackage.DATA_TYPE_MAPPING_MODEL: return createDataTypeMappingModel();
			case MappingPackage.TARGET: return createTarget();
			case MappingPackage.REFERENCE_TARGET: return createReferenceTarget();
			case MappingPackage.STEREO_TYPE_TARGET: return createStereoTypeTarget();
			case MappingPackage.ATTRIBUTE: return createAttribute();
			case MappingPackage.SOURCE: return createSource();
			case MappingPackage.FAULT_SOURCE: return createFaultSource();
			case MappingPackage.MAPPING_RULE: return createMappingRule();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case MappingPackage.MODEL_ATTRIBUTE:
				return createModelAttributeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case MappingPackage.MODEL_ATTRIBUTE:
				return convertModelAttributeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingModel createMappingModel() {
		MappingModelImpl mappingModel = new MappingModelImpl();
		return mappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelMappingModel createInfoModelMappingModel() {
		InfoModelMappingModelImpl infoModelMappingModel = new InfoModelMappingModelImpl();
		return infoModelMappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelMappingRule createInfoModelMappingRule() {
		InfoModelMappingRuleImpl infoModelMappingRule = new InfoModelMappingRuleImpl();
		return infoModelMappingRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfomodelSource createInfomodelSource() {
		InfomodelSourceImpl infomodelSource = new InfomodelSourceImpl();
		return infomodelSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelPropertySource createInfoModelPropertySource() {
		InfoModelPropertySourceImpl infoModelPropertySource = new InfoModelPropertySourceImpl();
		return infoModelPropertySource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelAttributeSource createInfoModelAttributeSource() {
		InfoModelAttributeSourceImpl infoModelAttributeSource = new InfoModelAttributeSourceImpl();
		return infoModelAttributeSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockMappingModel createFunctionBlockMappingModel() {
		FunctionBlockMappingModelImpl functionBlockMappingModel = new FunctionBlockMappingModelImpl();
		return functionBlockMappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockMappingRule createFunctionBlockMappingRule() {
		FunctionBlockMappingRuleImpl functionBlockMappingRule = new FunctionBlockMappingRuleImpl();
		return functionBlockMappingRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockSource createFunctionBlockSource() {
		FunctionBlockSourceImpl functionBlockSource = new FunctionBlockSourceImpl();
		return functionBlockSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockPropertySource createFunctionBlockPropertySource() {
		FunctionBlockPropertySourceImpl functionBlockPropertySource = new FunctionBlockPropertySourceImpl();
		return functionBlockPropertySource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockAttributeSource createFunctionBlockAttributeSource() {
		FunctionBlockAttributeSourceImpl functionBlockAttributeSource = new FunctionBlockAttributeSourceImpl();
		return functionBlockAttributeSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigurationSource createConfigurationSource() {
		ConfigurationSourceImpl configurationSource = new ConfigurationSourceImpl();
		return configurationSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusSource createStatusSource() {
		StatusSourceImpl statusSource = new StatusSourceImpl();
		return statusSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationSource createOperationSource() {
		OperationSourceImpl operationSource = new OperationSourceImpl();
		return operationSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventSource createEventSource() {
		EventSourceImpl eventSource = new EventSourceImpl();
		return eventSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityMappingModel createEntityMappingModel() {
		EntityMappingModelImpl entityMappingModel = new EntityMappingModelImpl();
		return entityMappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityMappingRule createEntityMappingRule() {
		EntityMappingRuleImpl entityMappingRule = new EntityMappingRuleImpl();
		return entityMappingRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntitySource createEntitySource() {
		EntitySourceImpl entitySource = new EntitySourceImpl();
		return entitySource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityPropertySource createEntityPropertySource() {
		EntityPropertySourceImpl entityPropertySource = new EntityPropertySourceImpl();
		return entityPropertySource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityAttributeSource createEntityAttributeSource() {
		EntityAttributeSourceImpl entityAttributeSource = new EntityAttributeSourceImpl();
		return entityAttributeSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumMappingModel createEnumMappingModel() {
		EnumMappingModelImpl enumMappingModel = new EnumMappingModelImpl();
		return enumMappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumMappingRule createEnumMappingRule() {
		EnumMappingRuleImpl enumMappingRule = new EnumMappingRuleImpl();
		return enumMappingRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumSource createEnumSource() {
		EnumSourceImpl enumSource = new EnumSourceImpl();
		return enumSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumPropertySource createEnumPropertySource() {
		EnumPropertySourceImpl enumPropertySource = new EnumPropertySourceImpl();
		return enumPropertySource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumAttributeSource createEnumAttributeSource() {
		EnumAttributeSourceImpl enumAttributeSource = new EnumAttributeSourceImpl();
		return enumAttributeSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataTypeMappingModel createDataTypeMappingModel() {
		DataTypeMappingModelImpl dataTypeMappingModel = new DataTypeMappingModelImpl();
		return dataTypeMappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Target createTarget() {
		TargetImpl target = new TargetImpl();
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReferenceTarget createReferenceTarget() {
		ReferenceTargetImpl referenceTarget = new ReferenceTargetImpl();
		return referenceTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StereoTypeTarget createStereoTypeTarget() {
		StereoTypeTargetImpl stereoTypeTarget = new StereoTypeTargetImpl();
		return stereoTypeTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Attribute createAttribute() {
		AttributeImpl attribute = new AttributeImpl();
		return attribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Source createSource() {
		SourceImpl source = new SourceImpl();
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FaultSource createFaultSource() {
		FaultSourceImpl faultSource = new FaultSourceImpl();
		return faultSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingRule createMappingRule() {
		MappingRuleImpl mappingRule = new MappingRuleImpl();
		return mappingRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelAttribute createModelAttributeFromString(EDataType eDataType, String initialValue) {
		ModelAttribute result = ModelAttribute.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertModelAttributeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingPackage getMappingPackage() {
		return (MappingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MappingPackage getPackage() {
		return MappingPackage.eINSTANCE;
	}

} //MappingFactoryImpl
