/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.vorto.core.api.model.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.mapping.MappingFactory
 * @model kind="package"
 * @generated
 */
public interface MappingPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "mapping";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/vorto/metamodel/Mapping";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "mapping";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MappingPackage eINSTANCE = org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getMappingModel()
	 * @generated
	 */
	int MAPPING_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__NAME = ModelPackage.MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__NAMESPACE = ModelPackage.MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__VERSION = ModelPackage.MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__REFERENCES = ModelPackage.MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Info Model Mapping Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__INFO_MODEL_MAPPING_RULES = ModelPackage.MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Function Block Mapping Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__FUNCTION_BLOCK_MAPPING_RULES = ModelPackage.MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Data Type Mapping Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__DATA_TYPE_MAPPING_RULES = ModelPackage.MODEL_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL_FEATURE_COUNT = ModelPackage.MODEL_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL_OPERATION_COUNT = ModelPackage.MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl <em>Info Model Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelMappingRule()
	 * @generated
	 */
	int INFO_MODEL_MAPPING_RULE = 1;

	/**
	 * The feature id for the '<em><b>Info Model Source Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Target Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT = 1;

	/**
	 * The number of structural features of the '<em>Info Model Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Info Model Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelSourceElementImpl <em>Info Model Source Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelSourceElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelSourceElement()
	 * @generated
	 */
	int INFO_MODEL_SOURCE_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Info Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Info Model Child</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD = 1;

	/**
	 * The number of structural features of the '<em>Info Model Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_SOURCE_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Info Model Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_SOURCE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelChildImpl <em>Info Model Child</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelChildImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelChild()
	 * @generated
	 */
	int INFO_MODEL_CHILD = 3;

	/**
	 * The number of structural features of the '<em>Info Model Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_CHILD_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Info Model Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_CHILD_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelFbElementImpl <em>Info Model Fb Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelFbElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelFbElement()
	 * @generated
	 */
	int INFO_MODEL_FB_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Functionblock</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK = INFO_MODEL_CHILD_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Function Block Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_FB_ELEMENT__FUNCTION_BLOCK_ELEMENT = INFO_MODEL_CHILD_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Info Model Fb Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_FB_ELEMENT_FEATURE_COUNT = INFO_MODEL_CHILD_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Info Model Fb Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_FB_ELEMENT_OPERATION_COUNT = INFO_MODEL_CHILD_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InformationModelPropertyImpl <em>Information Model Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InformationModelPropertyImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInformationModelProperty()
	 * @generated
	 */
	int INFORMATION_MODEL_PROPERTY = 5;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL_PROPERTY__ATTRIBUTE = INFO_MODEL_CHILD_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Information Model Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL_PROPERTY_FEATURE_COUNT = INFO_MODEL_CHILD_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Information Model Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL_PROPERTY_OPERATION_COUNT = INFO_MODEL_CHILD_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl <em>Function Block Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockMappingRule()
	 * @generated
	 */
	int FUNCTION_BLOCK_MAPPING_RULE = 6;

	/**
	 * The feature id for the '<em><b>Function Block Source Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Target Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT = 1;

	/**
	 * The number of structural features of the '<em>Function Block Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Function Block Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceElementImpl <em>Function Block Source Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockSourceElement()
	 * @generated
	 */
	int FUNCTION_BLOCK_SOURCE_ELEMENT = 7;

	/**
	 * The feature id for the '<em><b>Functionblock</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_SOURCE_ELEMENT__FUNCTIONBLOCK = 0;

	/**
	 * The feature id for the '<em><b>Function Block Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_SOURCE_ELEMENT__FUNCTION_BLOCK_ELEMENT = 1;

	/**
	 * The number of structural features of the '<em>Function Block Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_SOURCE_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Function Block Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_SOURCE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementImpl <em>Function Block Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockElement()
	 * @generated
	 */
	int FUNCTION_BLOCK_ELEMENT = 8;

	/**
	 * The number of structural features of the '<em>Function Block Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Function Block Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementAttributeImpl <em>Function Block Element Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementAttributeImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockElementAttribute()
	 * @generated
	 */
	int FUNCTION_BLOCK_ELEMENT_ATTRIBUTE = 9;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ELEMENT_ATTRIBUTE__ATTRIBUTE = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Function Block Element Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ELEMENT_ATTRIBUTE_FEATURE_COUNT = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Function Block Element Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ELEMENT_ATTRIBUTE_OPERATION_COUNT = FUNCTION_BLOCK_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockChildElementImpl <em>Function Block Child Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockChildElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockChildElement()
	 * @generated
	 */
	int FUNCTION_BLOCK_CHILD_ELEMENT = 10;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_CHILD_ELEMENT__TYPE = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_CHILD_ELEMENT__TYPE_REF = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Function Block Child Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_CHILD_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Function Block Child Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_CHILD_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.OperationElementImpl <em>Operation Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.OperationElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getOperationElement()
	 * @generated
	 */
	int OPERATION_ELEMENT = 11;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_ELEMENT__OPERATION = 0;

	/**
	 * The number of structural features of the '<em>Operation Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Operation Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationElementImpl <em>Configuration Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getConfigurationElement()
	 * @generated
	 */
	int CONFIGURATION_ELEMENT = 12;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__TYPE = FUNCTION_BLOCK_CHILD_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__TYPE_REF = FUNCTION_BLOCK_CHILD_ELEMENT__TYPE_REF;

	/**
	 * The number of structural features of the '<em>Configuration Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_CHILD_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Configuration Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_CHILD_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StatusElementImpl <em>Status Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.StatusElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStatusElement()
	 * @generated
	 */
	int STATUS_ELEMENT = 13;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_ELEMENT__TYPE = FUNCTION_BLOCK_CHILD_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_ELEMENT__TYPE_REF = FUNCTION_BLOCK_CHILD_ELEMENT__TYPE_REF;

	/**
	 * The number of structural features of the '<em>Status Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_CHILD_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Status Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_CHILD_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FaultElementImpl <em>Fault Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FaultElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFaultElement()
	 * @generated
	 */
	int FAULT_ELEMENT = 14;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_ELEMENT__TYPE = FUNCTION_BLOCK_CHILD_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_ELEMENT__TYPE_REF = FUNCTION_BLOCK_CHILD_ELEMENT__TYPE_REF;

	/**
	 * The number of structural features of the '<em>Fault Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_CHILD_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Fault Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_CHILD_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EventElementImpl <em>Event Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EventElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEventElement()
	 * @generated
	 */
	int EVENT_ELEMENT = 15;

	/**
	 * The feature id for the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_ELEMENT__TYPE = FUNCTION_BLOCK_CHILD_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_ELEMENT__TYPE_REF = FUNCTION_BLOCK_CHILD_ELEMENT__TYPE_REF;

	/**
	 * The feature id for the '<em><b>Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_ELEMENT__EVENT = FUNCTION_BLOCK_CHILD_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Event Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_CHILD_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Event Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_CHILD_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FBTypeElementImpl <em>FB Type Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FBTypeElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFBTypeElement()
	 * @generated
	 */
	int FB_TYPE_ELEMENT = 16;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT__PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Child</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT__CHILD = 1;

	/**
	 * The number of structural features of the '<em>FB Type Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>FB Type Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FBTypeElementChildImpl <em>FB Type Element Child</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FBTypeElementChildImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFBTypeElementChild()
	 * @generated
	 */
	int FB_TYPE_ELEMENT_CHILD = 17;

	/**
	 * The number of structural features of the '<em>FB Type Element Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT_CHILD_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>FB Type Element Child</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT_CHILD_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FBTypePropertyImpl <em>FB Type Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FBTypePropertyImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFBTypeProperty()
	 * @generated
	 */
	int FB_TYPE_PROPERTY = 18;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_PROPERTY__PROPERTY = FB_TYPE_ELEMENT_CHILD_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>FB Type Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_PROPERTY_FEATURE_COUNT = FB_TYPE_ELEMENT_CHILD_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>FB Type Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_PROPERTY_OPERATION_COUNT = FB_TYPE_ELEMENT_CHILD_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingRuleImpl <em>Data Type Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeMappingRule()
	 * @generated
	 */
	int DATA_TYPE_MAPPING_RULE = 19;

	/**
	 * The feature id for the '<em><b>Data Type Mapping Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_RULE__DATA_TYPE_MAPPING_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Data Type Source Element</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_RULE__DATA_TYPE_SOURCE_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Target Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_RULE__TARGET_ELEMENT = 2;

	/**
	 * The number of structural features of the '<em>Data Type Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_RULE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Data Type Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeSourceElementImpl <em>Data Type Source Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeSourceElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeSourceElement()
	 * @generated
	 */
	int DATA_TYPE_SOURCE_ELEMENT = 20;

	/**
	 * The number of structural features of the '<em>Data Type Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_SOURCE_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Data Type Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_SOURCE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypePropertyElementImpl <em>Data Type Property Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypePropertyElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypePropertyElement()
	 * @generated
	 */
	int DATA_TYPE_PROPERTY_ELEMENT = 21;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_PROPERTY_ELEMENT__TYPE_REF = DATA_TYPE_SOURCE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_PROPERTY_ELEMENT__ATTRIBUTE = DATA_TYPE_SOURCE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Data Type Property Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_PROPERTY_ELEMENT_FEATURE_COUNT = DATA_TYPE_SOURCE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Data Type Property Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_PROPERTY_ELEMENT_OPERATION_COUNT = DATA_TYPE_SOURCE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionRefImpl <em>Entity Expression Ref</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionRefImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityExpressionRef()
	 * @generated
	 */
	int ENTITY_EXPRESSION_REF = 22;

	/**
	 * The number of structural features of the '<em>Entity Expression Ref</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EXPRESSION_REF_FEATURE_COUNT = DATA_TYPE_SOURCE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Entity Expression Ref</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EXPRESSION_REF_OPERATION_COUNT = DATA_TYPE_SOURCE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeAttributeImpl <em>Data Type Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeAttributeImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeAttribute()
	 * @generated
	 */
	int DATA_TYPE_ATTRIBUTE = 23;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_ATTRIBUTE__ATTRIBUTE = FB_TYPE_ELEMENT_CHILD_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Data Type Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_ATTRIBUTE_FEATURE_COUNT = FB_TYPE_ELEMENT_CHILD_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Data Type Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_ATTRIBUTE_OPERATION_COUNT = FB_TYPE_ELEMENT_CHILD_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.TargetElementImpl <em>Target Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.TargetElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getTargetElement()
	 * @generated
	 */
	int TARGET_ELEMENT = 24;

	/**
	 * The feature id for the '<em><b>Stereo Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_ELEMENT__STEREO_TYPES = 0;

	/**
	 * The number of structural features of the '<em>Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeImpl <em>Stereo Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoType()
	 * @generated
	 */
	int STEREO_TYPE = 25;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE__ATTRIBUTES = 1;

	/**
	 * The number of structural features of the '<em>Stereo Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Stereo Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.AttributeImpl <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.AttributeImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getAttribute()
	 * @generated
	 */
	int ATTRIBUTE = 26;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Attribute</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ATTRIBUTE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.NestedEntityExpressionImpl <em>Nested Entity Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.NestedEntityExpressionImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getNestedEntityExpression()
	 * @generated
	 */
	int NESTED_ENTITY_EXPRESSION = 27;

	/**
	 * The feature id for the '<em><b>Ref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_ENTITY_EXPRESSION__REF = ENTITY_EXPRESSION_REF_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Tail</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_ENTITY_EXPRESSION__TAIL = ENTITY_EXPRESSION_REF_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Nested Entity Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_ENTITY_EXPRESSION_FEATURE_COUNT = ENTITY_EXPRESSION_REF_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Nested Entity Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NESTED_ENTITY_EXPRESSION_OPERATION_COUNT = ENTITY_EXPRESSION_REF_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionImpl <em>Entity Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityExpression()
	 * @generated
	 */
	int ENTITY_EXPRESSION = 28;

	/**
	 * The feature id for the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EXPRESSION__ENTITY = ENTITY_EXPRESSION_REF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Entity Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EXPRESSION_FEATURE_COUNT = ENTITY_EXPRESSION_REF_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Entity Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EXPRESSION_OPERATION_COUNT = ENTITY_EXPRESSION_REF_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute <em>Info Model Attribute</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelAttribute()
	 * @generated
	 */
	int INFO_MODEL_ATTRIBUTE = 29;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute <em>Functionblock Model Attribute</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionblockModelAttribute()
	 * @generated
	 */
	int FUNCTIONBLOCK_MODEL_ATTRIBUTE = 30;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.ModelAttribute <em>Model Attribute</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.ModelAttribute
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getModelAttribute()
	 * @generated
	 */
	int MODEL_ATTRIBUTE = 31;


	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel
	 * @generated
	 */
	EClass getMappingModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getInfoModelMappingRules <em>Info Model Mapping Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Info Model Mapping Rules</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel#getInfoModelMappingRules()
	 * @see #getMappingModel()
	 * @generated
	 */
	EReference getMappingModel_InfoModelMappingRules();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getFunctionBlockMappingRules <em>Function Block Mapping Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Function Block Mapping Rules</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel#getFunctionBlockMappingRules()
	 * @see #getMappingModel()
	 * @generated
	 */
	EReference getMappingModel_FunctionBlockMappingRules();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getDataTypeMappingRules <em>Data Type Mapping Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Data Type Mapping Rules</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel#getDataTypeMappingRules()
	 * @see #getMappingModel()
	 * @generated
	 */
	EReference getMappingModel_DataTypeMappingRules();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule <em>Info Model Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Mapping Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule
	 * @generated
	 */
	EClass getInfoModelMappingRule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getInfoModelSourceElements <em>Info Model Source Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Info Model Source Elements</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getInfoModelSourceElements()
	 * @see #getInfoModelMappingRule()
	 * @generated
	 */
	EReference getInfoModelMappingRule_InfoModelSourceElements();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getTargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getTargetElement()
	 * @see #getInfoModelMappingRule()
	 * @generated
	 */
	EReference getInfoModelMappingRule_TargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement <em>Info Model Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Source Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement
	 * @generated
	 */
	EClass getInfoModelSourceElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement#getInfoModel <em>Info Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Info Model</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement#getInfoModel()
	 * @see #getInfoModelSourceElement()
	 * @generated
	 */
	EReference getInfoModelSourceElement_InfoModel();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement#getInfoModelChild <em>Info Model Child</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Info Model Child</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement#getInfoModelChild()
	 * @see #getInfoModelSourceElement()
	 * @generated
	 */
	EReference getInfoModelSourceElement_InfoModelChild();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelChild <em>Info Model Child</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Child</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelChild
	 * @generated
	 */
	EClass getInfoModelChild();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement <em>Info Model Fb Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Fb Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement
	 * @generated
	 */
	EClass getInfoModelFbElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionblock <em>Functionblock</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Functionblock</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionblock()
	 * @see #getInfoModelFbElement()
	 * @generated
	 */
	EReference getInfoModelFbElement_Functionblock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionBlockElement <em>Function Block Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Function Block Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionBlockElement()
	 * @see #getInfoModelFbElement()
	 * @generated
	 */
	EReference getInfoModelFbElement_FunctionBlockElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelProperty <em>Information Model Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Information Model Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InformationModelProperty
	 * @generated
	 */
	EClass getInformationModelProperty();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelProperty#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InformationModelProperty#getAttribute()
	 * @see #getInformationModelProperty()
	 * @generated
	 */
	EAttribute getInformationModelProperty_Attribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule <em>Function Block Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Mapping Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule
	 * @generated
	 */
	EClass getFunctionBlockMappingRule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getFunctionBlockSourceElements <em>Function Block Source Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Function Block Source Elements</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getFunctionBlockSourceElements()
	 * @see #getFunctionBlockMappingRule()
	 * @generated
	 */
	EReference getFunctionBlockMappingRule_FunctionBlockSourceElements();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getTargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getTargetElement()
	 * @see #getFunctionBlockMappingRule()
	 * @generated
	 */
	EReference getFunctionBlockMappingRule_TargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement <em>Function Block Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Source Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement
	 * @generated
	 */
	EClass getFunctionBlockSourceElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement#getFunctionblock <em>Functionblock</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Functionblock</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement#getFunctionblock()
	 * @see #getFunctionBlockSourceElement()
	 * @generated
	 */
	EReference getFunctionBlockSourceElement_Functionblock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement#getFunctionBlockElement <em>Function Block Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Function Block Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement#getFunctionBlockElement()
	 * @see #getFunctionBlockSourceElement()
	 * @generated
	 */
	EReference getFunctionBlockSourceElement_FunctionBlockElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement <em>Function Block Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement
	 * @generated
	 */
	EClass getFunctionBlockElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute <em>Function Block Element Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Element Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute
	 * @generated
	 */
	EClass getFunctionBlockElementAttribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute#getAttribute()
	 * @see #getFunctionBlockElementAttribute()
	 * @generated
	 */
	EAttribute getFunctionBlockElementAttribute_Attribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement <em>Function Block Child Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Child Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement
	 * @generated
	 */
	EClass getFunctionBlockChildElement();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement#getType()
	 * @see #getFunctionBlockChildElement()
	 * @generated
	 */
	EReference getFunctionBlockChildElement_Type();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Type Ref</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement#getTypeRef()
	 * @see #getFunctionBlockChildElement()
	 * @generated
	 */
	EReference getFunctionBlockChildElement_TypeRef();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.OperationElement <em>Operation Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.OperationElement
	 * @generated
	 */
	EClass getOperationElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.OperationElement#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Operation</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.OperationElement#getOperation()
	 * @see #getOperationElement()
	 * @generated
	 */
	EReference getOperationElement_Operation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.ConfigurationElement <em>Configuration Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configuration Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.ConfigurationElement
	 * @generated
	 */
	EClass getConfigurationElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.StatusElement <em>Status Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Status Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StatusElement
	 * @generated
	 */
	EClass getStatusElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FaultElement <em>Fault Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fault Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FaultElement
	 * @generated
	 */
	EClass getFaultElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EventElement <em>Event Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EventElement
	 * @generated
	 */
	EClass getEventElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EventElement#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Event</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EventElement#getEvent()
	 * @see #getEventElement()
	 * @generated
	 */
	EReference getEventElement_Event();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElement <em>FB Type Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FB Type Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeElement
	 * @generated
	 */
	EClass getFBTypeElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElement#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeElement#getProperty()
	 * @see #getFBTypeElement()
	 * @generated
	 */
	EReference getFBTypeElement_Property();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElement#getChild <em>Child</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Child</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeElement#getChild()
	 * @see #getFBTypeElement()
	 * @generated
	 */
	EReference getFBTypeElement_Child();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElementChild <em>FB Type Element Child</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FB Type Element Child</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeElementChild
	 * @generated
	 */
	EClass getFBTypeElementChild();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeProperty <em>FB Type Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>FB Type Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeProperty
	 * @generated
	 */
	EClass getFBTypeProperty();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeProperty#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FBTypeProperty#getProperty()
	 * @see #getFBTypeProperty()
	 * @generated
	 */
	EReference getFBTypeProperty_Property();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule <em>Data Type Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Type Mapping Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule
	 * @generated
	 */
	EClass getDataTypeMappingRule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getDataTypeMappingElements <em>Data Type Mapping Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Data Type Mapping Elements</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getDataTypeMappingElements()
	 * @see #getDataTypeMappingRule()
	 * @generated
	 */
	EReference getDataTypeMappingRule_DataTypeMappingElements();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getDataTypeSourceElement <em>Data Type Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Data Type Source Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getDataTypeSourceElement()
	 * @see #getDataTypeMappingRule()
	 * @generated
	 */
	EReference getDataTypeMappingRule_DataTypeSourceElement();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getTargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getTargetElement()
	 * @see #getDataTypeMappingRule()
	 * @generated
	 */
	EReference getDataTypeMappingRule_TargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeSourceElement <em>Data Type Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Type Source Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeSourceElement
	 * @generated
	 */
	EClass getDataTypeSourceElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement <em>Data Type Property Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Type Property Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement
	 * @generated
	 */
	EClass getDataTypePropertyElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type Ref</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement#getTypeRef()
	 * @see #getDataTypePropertyElement()
	 * @generated
	 */
	EReference getDataTypePropertyElement_TypeRef();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement#getAttribute()
	 * @see #getDataTypePropertyElement()
	 * @generated
	 */
	EReference getDataTypePropertyElement_Attribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityExpressionRef <em>Entity Expression Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Expression Ref</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityExpressionRef
	 * @generated
	 */
	EClass getEntityExpressionRef();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeAttribute <em>Data Type Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Type Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeAttribute
	 * @generated
	 */
	EClass getDataTypeAttribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeAttribute#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeAttribute#getAttribute()
	 * @see #getDataTypeAttribute()
	 * @generated
	 */
	EAttribute getDataTypeAttribute_Attribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.TargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.TargetElement
	 * @generated
	 */
	EClass getTargetElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.TargetElement#getStereoTypes <em>Stereo Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Stereo Types</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.TargetElement#getStereoTypes()
	 * @see #getTargetElement()
	 * @generated
	 */
	EReference getTargetElement_StereoTypes();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.StereoType <em>Stereo Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stereo Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoType
	 * @generated
	 */
	EClass getStereoType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.StereoType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoType#getName()
	 * @see #getStereoType()
	 * @generated
	 */
	EAttribute getStereoType_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.StereoType#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoType#getAttributes()
	 * @see #getStereoType()
	 * @generated
	 */
	EReference getStereoType_Attributes();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Attribute
	 * @generated
	 */
	EClass getAttribute();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.Attribute#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Attribute#getName()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.Attribute#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Attribute#getValue()
	 * @see #getAttribute()
	 * @generated
	 */
	EAttribute getAttribute_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression <em>Nested Entity Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Nested Entity Expression</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression
	 * @generated
	 */
	EClass getNestedEntityExpression();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression#getRef <em>Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ref</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression#getRef()
	 * @see #getNestedEntityExpression()
	 * @generated
	 */
	EReference getNestedEntityExpression_Ref();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression#getTail <em>Tail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Tail</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression#getTail()
	 * @see #getNestedEntityExpression()
	 * @generated
	 */
	EReference getNestedEntityExpression_Tail();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityExpression <em>Entity Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Expression</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityExpression
	 * @generated
	 */
	EClass getEntityExpression();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EntityExpression#getEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Entity</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityExpression#getEntity()
	 * @see #getEntityExpression()
	 * @generated
	 */
	EReference getEntityExpression_Entity();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute <em>Info Model Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Info Model Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute
	 * @generated
	 */
	EEnum getInfoModelAttribute();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute <em>Functionblock Model Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Functionblock Model Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute
	 * @generated
	 */
	EEnum getFunctionblockModelAttribute();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.vorto.core.api.model.mapping.ModelAttribute <em>Model Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Model Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.ModelAttribute
	 * @generated
	 */
	EEnum getModelAttribute();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MappingFactory getMappingFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getMappingModel()
		 * @generated
		 */
		EClass MAPPING_MODEL = eINSTANCE.getMappingModel();

		/**
		 * The meta object literal for the '<em><b>Info Model Mapping Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_MODEL__INFO_MODEL_MAPPING_RULES = eINSTANCE.getMappingModel_InfoModelMappingRules();

		/**
		 * The meta object literal for the '<em><b>Function Block Mapping Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_MODEL__FUNCTION_BLOCK_MAPPING_RULES = eINSTANCE.getMappingModel_FunctionBlockMappingRules();

		/**
		 * The meta object literal for the '<em><b>Data Type Mapping Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_MODEL__DATA_TYPE_MAPPING_RULES = eINSTANCE.getMappingModel_DataTypeMappingRules();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl <em>Info Model Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelMappingRule()
		 * @generated
		 */
		EClass INFO_MODEL_MAPPING_RULE = eINSTANCE.getInfoModelMappingRule();

		/**
		 * The meta object literal for the '<em><b>Info Model Source Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS = eINSTANCE.getInfoModelMappingRule_InfoModelSourceElements();

		/**
		 * The meta object literal for the '<em><b>Target Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT = eINSTANCE.getInfoModelMappingRule_TargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelSourceElementImpl <em>Info Model Source Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelSourceElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelSourceElement()
		 * @generated
		 */
		EClass INFO_MODEL_SOURCE_ELEMENT = eINSTANCE.getInfoModelSourceElement();

		/**
		 * The meta object literal for the '<em><b>Info Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL = eINSTANCE.getInfoModelSourceElement_InfoModel();

		/**
		 * The meta object literal for the '<em><b>Info Model Child</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD = eINSTANCE.getInfoModelSourceElement_InfoModelChild();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelChildImpl <em>Info Model Child</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelChildImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelChild()
		 * @generated
		 */
		EClass INFO_MODEL_CHILD = eINSTANCE.getInfoModelChild();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelFbElementImpl <em>Info Model Fb Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelFbElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelFbElement()
		 * @generated
		 */
		EClass INFO_MODEL_FB_ELEMENT = eINSTANCE.getInfoModelFbElement();

		/**
		 * The meta object literal for the '<em><b>Functionblock</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK = eINSTANCE.getInfoModelFbElement_Functionblock();

		/**
		 * The meta object literal for the '<em><b>Function Block Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_FB_ELEMENT__FUNCTION_BLOCK_ELEMENT = eINSTANCE.getInfoModelFbElement_FunctionBlockElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InformationModelPropertyImpl <em>Information Model Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InformationModelPropertyImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInformationModelProperty()
		 * @generated
		 */
		EClass INFORMATION_MODEL_PROPERTY = eINSTANCE.getInformationModelProperty();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INFORMATION_MODEL_PROPERTY__ATTRIBUTE = eINSTANCE.getInformationModelProperty_Attribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl <em>Function Block Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockMappingRule()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_MAPPING_RULE = eINSTANCE.getFunctionBlockMappingRule();

		/**
		 * The meta object literal for the '<em><b>Function Block Source Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS = eINSTANCE.getFunctionBlockMappingRule_FunctionBlockSourceElements();

		/**
		 * The meta object literal for the '<em><b>Target Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT = eINSTANCE.getFunctionBlockMappingRule_TargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceElementImpl <em>Function Block Source Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockSourceElement()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_SOURCE_ELEMENT = eINSTANCE.getFunctionBlockSourceElement();

		/**
		 * The meta object literal for the '<em><b>Functionblock</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_SOURCE_ELEMENT__FUNCTIONBLOCK = eINSTANCE.getFunctionBlockSourceElement_Functionblock();

		/**
		 * The meta object literal for the '<em><b>Function Block Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_SOURCE_ELEMENT__FUNCTION_BLOCK_ELEMENT = eINSTANCE.getFunctionBlockSourceElement_FunctionBlockElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementImpl <em>Function Block Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockElement()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_ELEMENT = eINSTANCE.getFunctionBlockElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementAttributeImpl <em>Function Block Element Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementAttributeImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockElementAttribute()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_ELEMENT_ATTRIBUTE = eINSTANCE.getFunctionBlockElementAttribute();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUNCTION_BLOCK_ELEMENT_ATTRIBUTE__ATTRIBUTE = eINSTANCE.getFunctionBlockElementAttribute_Attribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockChildElementImpl <em>Function Block Child Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockChildElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockChildElement()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_CHILD_ELEMENT = eINSTANCE.getFunctionBlockChildElement();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_CHILD_ELEMENT__TYPE = eINSTANCE.getFunctionBlockChildElement_Type();

		/**
		 * The meta object literal for the '<em><b>Type Ref</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_CHILD_ELEMENT__TYPE_REF = eINSTANCE.getFunctionBlockChildElement_TypeRef();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.OperationElementImpl <em>Operation Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.OperationElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getOperationElement()
		 * @generated
		 */
		EClass OPERATION_ELEMENT = eINSTANCE.getOperationElement();

		/**
		 * The meta object literal for the '<em><b>Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_ELEMENT__OPERATION = eINSTANCE.getOperationElement_Operation();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationElementImpl <em>Configuration Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getConfigurationElement()
		 * @generated
		 */
		EClass CONFIGURATION_ELEMENT = eINSTANCE.getConfigurationElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StatusElementImpl <em>Status Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.StatusElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStatusElement()
		 * @generated
		 */
		EClass STATUS_ELEMENT = eINSTANCE.getStatusElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FaultElementImpl <em>Fault Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FaultElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFaultElement()
		 * @generated
		 */
		EClass FAULT_ELEMENT = eINSTANCE.getFaultElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EventElementImpl <em>Event Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EventElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEventElement()
		 * @generated
		 */
		EClass EVENT_ELEMENT = eINSTANCE.getEventElement();

		/**
		 * The meta object literal for the '<em><b>Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_ELEMENT__EVENT = eINSTANCE.getEventElement_Event();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FBTypeElementImpl <em>FB Type Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FBTypeElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFBTypeElement()
		 * @generated
		 */
		EClass FB_TYPE_ELEMENT = eINSTANCE.getFBTypeElement();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FB_TYPE_ELEMENT__PROPERTY = eINSTANCE.getFBTypeElement_Property();

		/**
		 * The meta object literal for the '<em><b>Child</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FB_TYPE_ELEMENT__CHILD = eINSTANCE.getFBTypeElement_Child();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FBTypeElementChildImpl <em>FB Type Element Child</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FBTypeElementChildImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFBTypeElementChild()
		 * @generated
		 */
		EClass FB_TYPE_ELEMENT_CHILD = eINSTANCE.getFBTypeElementChild();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FBTypePropertyImpl <em>FB Type Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FBTypePropertyImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFBTypeProperty()
		 * @generated
		 */
		EClass FB_TYPE_PROPERTY = eINSTANCE.getFBTypeProperty();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FB_TYPE_PROPERTY__PROPERTY = eINSTANCE.getFBTypeProperty_Property();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingRuleImpl <em>Data Type Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeMappingRule()
		 * @generated
		 */
		EClass DATA_TYPE_MAPPING_RULE = eINSTANCE.getDataTypeMappingRule();

		/**
		 * The meta object literal for the '<em><b>Data Type Mapping Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_TYPE_MAPPING_RULE__DATA_TYPE_MAPPING_ELEMENTS = eINSTANCE.getDataTypeMappingRule_DataTypeMappingElements();

		/**
		 * The meta object literal for the '<em><b>Data Type Source Element</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_TYPE_MAPPING_RULE__DATA_TYPE_SOURCE_ELEMENT = eINSTANCE.getDataTypeMappingRule_DataTypeSourceElement();

		/**
		 * The meta object literal for the '<em><b>Target Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_TYPE_MAPPING_RULE__TARGET_ELEMENT = eINSTANCE.getDataTypeMappingRule_TargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeSourceElementImpl <em>Data Type Source Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeSourceElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeSourceElement()
		 * @generated
		 */
		EClass DATA_TYPE_SOURCE_ELEMENT = eINSTANCE.getDataTypeSourceElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypePropertyElementImpl <em>Data Type Property Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypePropertyElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypePropertyElement()
		 * @generated
		 */
		EClass DATA_TYPE_PROPERTY_ELEMENT = eINSTANCE.getDataTypePropertyElement();

		/**
		 * The meta object literal for the '<em><b>Type Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_TYPE_PROPERTY_ELEMENT__TYPE_REF = eINSTANCE.getDataTypePropertyElement_TypeRef();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_TYPE_PROPERTY_ELEMENT__ATTRIBUTE = eINSTANCE.getDataTypePropertyElement_Attribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionRefImpl <em>Entity Expression Ref</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionRefImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityExpressionRef()
		 * @generated
		 */
		EClass ENTITY_EXPRESSION_REF = eINSTANCE.getEntityExpressionRef();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeAttributeImpl <em>Data Type Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeAttributeImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeAttribute()
		 * @generated
		 */
		EClass DATA_TYPE_ATTRIBUTE = eINSTANCE.getDataTypeAttribute();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_TYPE_ATTRIBUTE__ATTRIBUTE = eINSTANCE.getDataTypeAttribute_Attribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.TargetElementImpl <em>Target Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.TargetElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getTargetElement()
		 * @generated
		 */
		EClass TARGET_ELEMENT = eINSTANCE.getTargetElement();

		/**
		 * The meta object literal for the '<em><b>Stereo Types</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TARGET_ELEMENT__STEREO_TYPES = eINSTANCE.getTargetElement_StereoTypes();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeImpl <em>Stereo Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoType()
		 * @generated
		 */
		EClass STEREO_TYPE = eINSTANCE.getStereoType();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STEREO_TYPE__NAME = eINSTANCE.getStereoType_Name();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STEREO_TYPE__ATTRIBUTES = eINSTANCE.getStereoType_Attributes();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.AttributeImpl <em>Attribute</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.AttributeImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getAttribute()
		 * @generated
		 */
		EClass ATTRIBUTE = eINSTANCE.getAttribute();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__NAME = eINSTANCE.getAttribute_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ATTRIBUTE__VALUE = eINSTANCE.getAttribute_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.NestedEntityExpressionImpl <em>Nested Entity Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.NestedEntityExpressionImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getNestedEntityExpression()
		 * @generated
		 */
		EClass NESTED_ENTITY_EXPRESSION = eINSTANCE.getNestedEntityExpression();

		/**
		 * The meta object literal for the '<em><b>Ref</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NESTED_ENTITY_EXPRESSION__REF = eINSTANCE.getNestedEntityExpression_Ref();

		/**
		 * The meta object literal for the '<em><b>Tail</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NESTED_ENTITY_EXPRESSION__TAIL = eINSTANCE.getNestedEntityExpression_Tail();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionImpl <em>Entity Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityExpression()
		 * @generated
		 */
		EClass ENTITY_EXPRESSION = eINSTANCE.getEntityExpression();

		/**
		 * The meta object literal for the '<em><b>Entity</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_EXPRESSION__ENTITY = eINSTANCE.getEntityExpression_Entity();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute <em>Info Model Attribute</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelAttribute()
		 * @generated
		 */
		EEnum INFO_MODEL_ATTRIBUTE = eINSTANCE.getInfoModelAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute <em>Functionblock Model Attribute</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionblockModelAttribute()
		 * @generated
		 */
		EEnum FUNCTIONBLOCK_MODEL_ATTRIBUTE = eINSTANCE.getFunctionblockModelAttribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.ModelAttribute <em>Model Attribute</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.ModelAttribute
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getModelAttribute()
		 * @generated
		 */
		EEnum MODEL_ATTRIBUTE = eINSTANCE.getModelAttribute();

	}

} //MappingPackage
