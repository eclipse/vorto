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
			case MappingPackage.MAPPING_TYPE: return createMappingType();
			case MappingPackage.INFO_MODEL_MAPPING: return createInfoModelMapping();
			case MappingPackage.INFO_MODEL_MAPPING_RULE: return createInfoModelMappingRule();
			case MappingPackage.INFO_MODEL_TARGET_ELEMENT: return createInfoModelTargetElement();
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT: return createInfoModelSourceElement();
			case MappingPackage.INFO_MODEL_CHILD: return createInfoModelChild();
			case MappingPackage.INFO_MODEL_FB_ELEMENT: return createInfoModelFbElement();
			case MappingPackage.INFORMATION_MODEL_PROPERTY: return createInformationModelProperty();
			case MappingPackage.FUNCTION_BLOCK_MAPPING: return createFunctionBlockMapping();
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE: return createFunctionBlockMappingRule();
			case MappingPackage.FUNCTION_BLOCK_TARGET_ELEMENT: return createFunctionBlockTargetElement();
			case MappingPackage.FUNCTION_BLOCK_SOURCE_ELEMENT: return createFunctionBlockSourceElement();
			case MappingPackage.FUNCTION_BLOCK_ELEMENT: return createFunctionBlockElement();
			case MappingPackage.FUNCTION_BLOCK_ELEMENT_ATTRIBUTE: return createFunctionBlockElementAttribute();
			case MappingPackage.FUNCTION_BLOCK_CHILD_ELEMENT: return createFunctionBlockChildElement();
			case MappingPackage.OPERATION_ELEMENT: return createOperationElement();
			case MappingPackage.CONFIGURATION_ELEMENT: return createConfigurationElement();
			case MappingPackage.STATUS_ELEMENT: return createStatusElement();
			case MappingPackage.FAULT_ELEMENT: return createFaultElement();
			case MappingPackage.EVENT_ELEMENT: return createEventElement();
			case MappingPackage.FB_TYPE_ELEMENT: return createFBTypeElement();
			case MappingPackage.ENTITY_MAPPING: return createEntityMapping();
			case MappingPackage.ENTITY_MAPPING_RULE: return createEntityMappingRule();
			case MappingPackage.ENTITY_TARGET_ELEMENT: return createEntityTargetElement();
			case MappingPackage.ENTITY_SOURCE_ELEMENT: return createEntitySourceElement();
			case MappingPackage.ENTITY_PROPERTY_ELEMENT: return createEntityPropertyElement();
			case MappingPackage.ENTITY_EXPRESSION_REF: return createEntityExpressionRef();
			case MappingPackage.ENUM_MAPPING: return createEnumMapping();
			case MappingPackage.ENUM_MAPPING_RULE: return createEnumMappingRule();
			case MappingPackage.ENUM_TARGET_ELEMENT: return createEnumTargetElement();
			case MappingPackage.ENUM_SOURCE_ELEMENT: return createEnumSourceElement();
			case MappingPackage.ENUM_PROPERTY_ELEMENT: return createEnumPropertyElement();
			case MappingPackage.ENUM_EXPRESSION: return createEnumExpression();
			case MappingPackage.ENUM_REFERENCE: return createEnumReference();
			case MappingPackage.FUNCTION_BLOCK_REFERENCE: return createFunctionBlockReference();
			case MappingPackage.DATA_TYPE_REFERENCE: return createDataTypeReference();
			case MappingPackage.DATA_TYPE_MAPPING: return createDataTypeMapping();
			case MappingPackage.STEREO_TYPE_REFERENCE: return createStereoTypeReference();
			case MappingPackage.STEREO_TYPE_ELEMENT: return createStereoTypeElement();
			case MappingPackage.STEREO_TYPE: return createStereoType();
			case MappingPackage.ATTRIBUTE: return createAttribute();
			case MappingPackage.NESTED_ENTITY_EXPRESSION: return createNestedEntityExpression();
			case MappingPackage.ENTITY_EXPRESSION: return createEntityExpression();
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
			case MappingPackage.INFO_MODEL_ATTRIBUTE:
				return createInfoModelAttributeFromString(eDataType, initialValue);
			case MappingPackage.FUNCTIONBLOCK_MODEL_ATTRIBUTE:
				return createFunctionblockModelAttributeFromString(eDataType, initialValue);
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
			case MappingPackage.INFO_MODEL_ATTRIBUTE:
				return convertInfoModelAttributeToString(eDataType, instanceValue);
			case MappingPackage.FUNCTIONBLOCK_MODEL_ATTRIBUTE:
				return convertFunctionblockModelAttributeToString(eDataType, instanceValue);
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
	public MappingType createMappingType() {
		MappingTypeImpl mappingType = new MappingTypeImpl();
		return mappingType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelMapping createInfoModelMapping() {
		InfoModelMappingImpl infoModelMapping = new InfoModelMappingImpl();
		return infoModelMapping;
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
	public InfoModelTargetElement createInfoModelTargetElement() {
		InfoModelTargetElementImpl infoModelTargetElement = new InfoModelTargetElementImpl();
		return infoModelTargetElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelSourceElement createInfoModelSourceElement() {
		InfoModelSourceElementImpl infoModelSourceElement = new InfoModelSourceElementImpl();
		return infoModelSourceElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelChild createInfoModelChild() {
		InfoModelChildImpl infoModelChild = new InfoModelChildImpl();
		return infoModelChild;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelFbElement createInfoModelFbElement() {
		InfoModelFbElementImpl infoModelFbElement = new InfoModelFbElementImpl();
		return infoModelFbElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModelProperty createInformationModelProperty() {
		InformationModelPropertyImpl informationModelProperty = new InformationModelPropertyImpl();
		return informationModelProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockMapping createFunctionBlockMapping() {
		FunctionBlockMappingImpl functionBlockMapping = new FunctionBlockMappingImpl();
		return functionBlockMapping;
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
	public FunctionBlockTargetElement createFunctionBlockTargetElement() {
		FunctionBlockTargetElementImpl functionBlockTargetElement = new FunctionBlockTargetElementImpl();
		return functionBlockTargetElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockSourceElement createFunctionBlockSourceElement() {
		FunctionBlockSourceElementImpl functionBlockSourceElement = new FunctionBlockSourceElementImpl();
		return functionBlockSourceElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockElement createFunctionBlockElement() {
		FunctionBlockElementImpl functionBlockElement = new FunctionBlockElementImpl();
		return functionBlockElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockElementAttribute createFunctionBlockElementAttribute() {
		FunctionBlockElementAttributeImpl functionBlockElementAttribute = new FunctionBlockElementAttributeImpl();
		return functionBlockElementAttribute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockChildElement createFunctionBlockChildElement() {
		FunctionBlockChildElementImpl functionBlockChildElement = new FunctionBlockChildElementImpl();
		return functionBlockChildElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationElement createOperationElement() {
		OperationElementImpl operationElement = new OperationElementImpl();
		return operationElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigurationElement createConfigurationElement() {
		ConfigurationElementImpl configurationElement = new ConfigurationElementImpl();
		return configurationElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatusElement createStatusElement() {
		StatusElementImpl statusElement = new StatusElementImpl();
		return statusElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FaultElement createFaultElement() {
		FaultElementImpl faultElement = new FaultElementImpl();
		return faultElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventElement createEventElement() {
		EventElementImpl eventElement = new EventElementImpl();
		return eventElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FBTypeElement createFBTypeElement() {
		FBTypeElementImpl fbTypeElement = new FBTypeElementImpl();
		return fbTypeElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityMapping createEntityMapping() {
		EntityMappingImpl entityMapping = new EntityMappingImpl();
		return entityMapping;
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
	public EntityTargetElement createEntityTargetElement() {
		EntityTargetElementImpl entityTargetElement = new EntityTargetElementImpl();
		return entityTargetElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntitySourceElement createEntitySourceElement() {
		EntitySourceElementImpl entitySourceElement = new EntitySourceElementImpl();
		return entitySourceElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityPropertyElement createEntityPropertyElement() {
		EntityPropertyElementImpl entityPropertyElement = new EntityPropertyElementImpl();
		return entityPropertyElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityExpressionRef createEntityExpressionRef() {
		EntityExpressionRefImpl entityExpressionRef = new EntityExpressionRefImpl();
		return entityExpressionRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumMapping createEnumMapping() {
		EnumMappingImpl enumMapping = new EnumMappingImpl();
		return enumMapping;
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
	public EnumTargetElement createEnumTargetElement() {
		EnumTargetElementImpl enumTargetElement = new EnumTargetElementImpl();
		return enumTargetElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumSourceElement createEnumSourceElement() {
		EnumSourceElementImpl enumSourceElement = new EnumSourceElementImpl();
		return enumSourceElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumPropertyElement createEnumPropertyElement() {
		EnumPropertyElementImpl enumPropertyElement = new EnumPropertyElementImpl();
		return enumPropertyElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumExpression createEnumExpression() {
		EnumExpressionImpl enumExpression = new EnumExpressionImpl();
		return enumExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumReference createEnumReference() {
		EnumReferenceImpl enumReference = new EnumReferenceImpl();
		return enumReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockReference createFunctionBlockReference() {
		FunctionBlockReferenceImpl functionBlockReference = new FunctionBlockReferenceImpl();
		return functionBlockReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataTypeReference createDataTypeReference() {
		DataTypeReferenceImpl dataTypeReference = new DataTypeReferenceImpl();
		return dataTypeReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataTypeMapping createDataTypeMapping() {
		DataTypeMappingImpl dataTypeMapping = new DataTypeMappingImpl();
		return dataTypeMapping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StereoTypeReference createStereoTypeReference() {
		StereoTypeReferenceImpl stereoTypeReference = new StereoTypeReferenceImpl();
		return stereoTypeReference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StereoTypeElement createStereoTypeElement() {
		StereoTypeElementImpl stereoTypeElement = new StereoTypeElementImpl();
		return stereoTypeElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StereoType createStereoType() {
		StereoTypeImpl stereoType = new StereoTypeImpl();
		return stereoType;
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
	public NestedEntityExpression createNestedEntityExpression() {
		NestedEntityExpressionImpl nestedEntityExpression = new NestedEntityExpressionImpl();
		return nestedEntityExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityExpression createEntityExpression() {
		EntityExpressionImpl entityExpression = new EntityExpressionImpl();
		return entityExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelAttribute createInfoModelAttributeFromString(EDataType eDataType, String initialValue) {
		InfoModelAttribute result = InfoModelAttribute.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInfoModelAttributeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockModelAttribute createFunctionblockModelAttributeFromString(EDataType eDataType, String initialValue) {
		FunctionblockModelAttribute result = FunctionblockModelAttribute.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFunctionblockModelAttributeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
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
