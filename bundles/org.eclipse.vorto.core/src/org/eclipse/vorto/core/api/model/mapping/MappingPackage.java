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
	 * The feature id for the '<em><b>Mapping Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__MAPPING_TYPE = ModelPackage.MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL_FEATURE_COUNT = ModelPackage.MODEL_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL_OPERATION_COUNT = ModelPackage.MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingTypeImpl <em>Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingTypeImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getMappingType()
	 * @generated
	 */
	int MAPPING_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_TYPE__NAME = 0;

	/**
	 * The number of structural features of the '<em>Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_TYPE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingImpl <em>Info Model Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelMapping()
	 * @generated
	 */
	int INFO_MODEL_MAPPING = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING__NAME = MAPPING_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Info Model Mapping Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES = MAPPING_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Info Model Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_FEATURE_COUNT = MAPPING_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Info Model Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_OPERATION_COUNT = MAPPING_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl <em>Info Model Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelMappingRule()
	 * @generated
	 */
	int INFO_MODEL_MAPPING_RULE = 3;

	/**
	 * The feature id for the '<em><b>Info Model Source Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE__TARGET = 1;

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
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelTargetElementImpl <em>Info Model Target Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelTargetElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelTargetElement()
	 * @generated
	 */
	int INFO_MODEL_TARGET_ELEMENT = 4;

	/**
	 * The number of structural features of the '<em>Info Model Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_TARGET_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Info Model Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_TARGET_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelSourceElementImpl <em>Info Model Source Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelSourceElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelSourceElement()
	 * @generated
	 */
	int INFO_MODEL_SOURCE_ELEMENT = 5;

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
	int INFO_MODEL_CHILD = 6;

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
	int INFO_MODEL_FB_ELEMENT = 7;

	/**
	 * The feature id for the '<em><b>Functionblock</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK = INFO_MODEL_CHILD_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Info Model Fb Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_FB_ELEMENT_FEATURE_COUNT = INFO_MODEL_CHILD_FEATURE_COUNT + 1;

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
	int INFORMATION_MODEL_PROPERTY = 8;

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
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingImpl <em>Function Block Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockMapping()
	 * @generated
	 */
	int FUNCTION_BLOCK_MAPPING = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING__NAME = MAPPING_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Function Block Mapping Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES = MAPPING_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Function Block Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_FEATURE_COUNT = MAPPING_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Function Block Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_OPERATION_COUNT = MAPPING_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl <em>Function Block Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockMappingRule()
	 * @generated
	 */
	int FUNCTION_BLOCK_MAPPING_RULE = 10;

	/**
	 * The feature id for the '<em><b>Function Block Source Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE__TARGET = 1;

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
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockTargetElementImpl <em>Function Block Target Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockTargetElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockTargetElement()
	 * @generated
	 */
	int FUNCTION_BLOCK_TARGET_ELEMENT = 11;

	/**
	 * The number of structural features of the '<em>Function Block Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_TARGET_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Function Block Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_TARGET_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceElementImpl <em>Function Block Source Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockSourceElement()
	 * @generated
	 */
	int FUNCTION_BLOCK_SOURCE_ELEMENT = 12;

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
	int FUNCTION_BLOCK_ELEMENT = 13;

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
	int FUNCTION_BLOCK_ELEMENT_ATTRIBUTE = 14;

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
	int FUNCTION_BLOCK_CHILD_ELEMENT = 15;

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
	int OPERATION_ELEMENT = 16;

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
	int CONFIGURATION_ELEMENT = 17;

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
	int STATUS_ELEMENT = 18;

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
	int FAULT_ELEMENT = 19;

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
	int EVENT_ELEMENT = 20;

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
	int FB_TYPE_ELEMENT = 21;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT__PROPERTY = 0;

	/**
	 * The number of structural features of the '<em>FB Type Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>FB Type Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FB_TYPE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingImpl <em>Data Type Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeMapping()
	 * @generated
	 */
	int DATA_TYPE_MAPPING = 37;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING__NAME = MAPPING_TYPE__NAME;

	/**
	 * The number of structural features of the '<em>Data Type Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_FEATURE_COUNT = MAPPING_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Data Type Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_OPERATION_COUNT = MAPPING_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingImpl <em>Entity Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityMapping()
	 * @generated
	 */
	int ENTITY_MAPPING = 22;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING__NAME = DATA_TYPE_MAPPING__NAME;

	/**
	 * The feature id for the '<em><b>Entity Mapping Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING__ENTITY_MAPPING_RULES = DATA_TYPE_MAPPING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Entity Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_FEATURE_COUNT = DATA_TYPE_MAPPING_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Entity Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_OPERATION_COUNT = DATA_TYPE_MAPPING_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl <em>Entity Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityMappingRule()
	 * @generated
	 */
	int ENTITY_MAPPING_RULE = 23;

	/**
	 * The feature id for the '<em><b>Entity Mapping Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Entity Source Element</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Target Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE__TARGET_ELEMENT = 2;

	/**
	 * The number of structural features of the '<em>Entity Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Entity Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityTargetElementImpl <em>Entity Target Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityTargetElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityTargetElement()
	 * @generated
	 */
	int ENTITY_TARGET_ELEMENT = 24;

	/**
	 * The number of structural features of the '<em>Entity Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_TARGET_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Entity Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_TARGET_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntitySourceElementImpl <em>Entity Source Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntitySourceElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntitySourceElement()
	 * @generated
	 */
	int ENTITY_SOURCE_ELEMENT = 25;

	/**
	 * The number of structural features of the '<em>Entity Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_SOURCE_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Entity Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_SOURCE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityPropertyElementImpl <em>Entity Property Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityPropertyElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityPropertyElement()
	 * @generated
	 */
	int ENTITY_PROPERTY_ELEMENT = 26;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROPERTY_ELEMENT__TYPE_REF = ENTITY_SOURCE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROPERTY_ELEMENT__ATTRIBUTE = ENTITY_SOURCE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Entity Property Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROPERTY_ELEMENT_FEATURE_COUNT = ENTITY_SOURCE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Entity Property Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROPERTY_ELEMENT_OPERATION_COUNT = ENTITY_SOURCE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionRefImpl <em>Entity Expression Ref</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityExpressionRefImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityExpressionRef()
	 * @generated
	 */
	int ENTITY_EXPRESSION_REF = 27;

	/**
	 * The number of structural features of the '<em>Entity Expression Ref</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EXPRESSION_REF_FEATURE_COUNT = ENTITY_SOURCE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Entity Expression Ref</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EXPRESSION_REF_OPERATION_COUNT = ENTITY_SOURCE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingImpl <em>Enum Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumMapping()
	 * @generated
	 */
	int ENUM_MAPPING = 28;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING__NAME = DATA_TYPE_MAPPING__NAME;

	/**
	 * The feature id for the '<em><b>Enum Mapping Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING__ENUM_MAPPING_RULES = DATA_TYPE_MAPPING_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_FEATURE_COUNT = DATA_TYPE_MAPPING_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Enum Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_OPERATION_COUNT = DATA_TYPE_MAPPING_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl <em>Enum Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumMappingRule()
	 * @generated
	 */
	int ENUM_MAPPING_RULE = 29;

	/**
	 * The feature id for the '<em><b>Enum Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE__ENUM_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Enum Source Element</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Target Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE__TARGET_ELEMENT = 2;

	/**
	 * The number of structural features of the '<em>Enum Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Enum Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumTargetElementImpl <em>Enum Target Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumTargetElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumTargetElement()
	 * @generated
	 */
	int ENUM_TARGET_ELEMENT = 30;

	/**
	 * The number of structural features of the '<em>Enum Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_TARGET_ELEMENT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Enum Target Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_TARGET_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumSourceElementImpl <em>Enum Source Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumSourceElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumSourceElement()
	 * @generated
	 */
	int ENUM_SOURCE_ELEMENT = 31;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_SOURCE_ELEMENT__TYPE_REF = 0;

	/**
	 * The number of structural features of the '<em>Enum Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_SOURCE_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Enum Source Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_SOURCE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumPropertyElementImpl <em>Enum Property Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumPropertyElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumPropertyElement()
	 * @generated
	 */
	int ENUM_PROPERTY_ELEMENT = 32;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_PROPERTY_ELEMENT__TYPE_REF = ENUM_SOURCE_ELEMENT__TYPE_REF;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_PROPERTY_ELEMENT__ATTRIBUTE = ENUM_SOURCE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum Property Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_PROPERTY_ELEMENT_FEATURE_COUNT = ENUM_SOURCE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Enum Property Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_PROPERTY_ELEMENT_OPERATION_COUNT = ENUM_SOURCE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumExpressionImpl <em>Enum Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumExpressionImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumExpression()
	 * @generated
	 */
	int ENUM_EXPRESSION = 33;

	/**
	 * The feature id for the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_EXPRESSION__TYPE_REF = ENUM_SOURCE_ELEMENT__TYPE_REF;

	/**
	 * The feature id for the '<em><b>Literal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_EXPRESSION__LITERAL = ENUM_SOURCE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_EXPRESSION_FEATURE_COUNT = ENUM_SOURCE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Enum Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_EXPRESSION_OPERATION_COUNT = ENUM_SOURCE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumReferenceImpl <em>Enum Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumReferenceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumReference()
	 * @generated
	 */
	int ENUM_REFERENCE = 34;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_REFERENCE__REFERENCE = ENUM_TARGET_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_REFERENCE_FEATURE_COUNT = ENUM_TARGET_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Enum Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_REFERENCE_OPERATION_COUNT = ENUM_TARGET_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockReferenceImpl <em>Function Block Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockReferenceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockReference()
	 * @generated
	 */
	int FUNCTION_BLOCK_REFERENCE = 35;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_REFERENCE__REFERENCE = INFO_MODEL_TARGET_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Function Block Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_REFERENCE_FEATURE_COUNT = INFO_MODEL_TARGET_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Function Block Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_REFERENCE_OPERATION_COUNT = INFO_MODEL_TARGET_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeReferenceImpl <em>Data Type Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeReferenceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeReference()
	 * @generated
	 */
	int DATA_TYPE_REFERENCE = 36;

	/**
	 * The feature id for the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_REFERENCE__REFERENCE = FUNCTION_BLOCK_TARGET_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Data Type Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_REFERENCE_FEATURE_COUNT = FUNCTION_BLOCK_TARGET_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Data Type Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_REFERENCE_OPERATION_COUNT = FUNCTION_BLOCK_TARGET_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeReferenceImpl <em>Stereo Type Reference</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeReferenceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoTypeReference()
	 * @generated
	 */
	int STEREO_TYPE_REFERENCE = 38;

	/**
	 * The feature id for the '<em><b>Target Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_REFERENCE__TARGET_ELEMENT = INFO_MODEL_TARGET_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Stereo Type Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_REFERENCE_FEATURE_COUNT = INFO_MODEL_TARGET_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Stereo Type Reference</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_REFERENCE_OPERATION_COUNT = INFO_MODEL_TARGET_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeElementImpl <em>Stereo Type Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoTypeElement()
	 * @generated
	 */
	int STEREO_TYPE_ELEMENT = 39;

	/**
	 * The feature id for the '<em><b>Stereo Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_ELEMENT__STEREO_TYPES = 0;

	/**
	 * The number of structural features of the '<em>Stereo Type Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_ELEMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Stereo Type Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeImpl <em>Stereo Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoType()
	 * @generated
	 */
	int STEREO_TYPE = 40;

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
	int ATTRIBUTE = 41;

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
	int NESTED_ENTITY_EXPRESSION = 42;

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
	int ENTITY_EXPRESSION = 43;

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
	int INFO_MODEL_ATTRIBUTE = 44;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute <em>Functionblock Model Attribute</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionblockModelAttribute()
	 * @generated
	 */
	int FUNCTIONBLOCK_MODEL_ATTRIBUTE = 45;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.ModelAttribute <em>Model Attribute</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.ModelAttribute
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getModelAttribute()
	 * @generated
	 */
	int MODEL_ATTRIBUTE = 46;


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
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getMappingType <em>Mapping Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Mapping Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel#getMappingType()
	 * @see #getMappingModel()
	 * @generated
	 */
	EReference getMappingModel_MappingType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.MappingType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingType
	 * @generated
	 */
	EClass getMappingType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.MappingType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingType#getName()
	 * @see #getMappingType()
	 * @generated
	 */
	EAttribute getMappingType_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMapping <em>Info Model Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Mapping</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMapping
	 * @generated
	 */
	EClass getInfoModelMapping();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMapping#getInfoModelMappingRules <em>Info Model Mapping Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Info Model Mapping Rules</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMapping#getInfoModelMappingRules()
	 * @see #getInfoModelMapping()
	 * @generated
	 */
	EReference getInfoModelMapping_InfoModelMappingRules();

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
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getTarget()
	 * @see #getInfoModelMappingRule()
	 * @generated
	 */
	EReference getInfoModelMappingRule_Target();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelTargetElement <em>Info Model Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelTargetElement
	 * @generated
	 */
	EClass getInfoModelTargetElement();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping <em>Function Block Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Mapping</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping
	 * @generated
	 */
	EClass getFunctionBlockMapping();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping#getFunctionBlockMappingRules <em>Function Block Mapping Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Function Block Mapping Rules</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping#getFunctionBlockMappingRules()
	 * @see #getFunctionBlockMapping()
	 * @generated
	 */
	EReference getFunctionBlockMapping_FunctionBlockMappingRules();

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
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getTarget()
	 * @see #getFunctionBlockMappingRule()
	 * @generated
	 */
	EReference getFunctionBlockMappingRule_Target();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockTargetElement <em>Function Block Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockTargetElement
	 * @generated
	 */
	EClass getFunctionBlockTargetElement();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityMapping <em>Entity Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Mapping</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMapping
	 * @generated
	 */
	EClass getEntityMapping();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.EntityMapping#getEntityMappingRules <em>Entity Mapping Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entity Mapping Rules</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMapping#getEntityMappingRules()
	 * @see #getEntityMapping()
	 * @generated
	 */
	EReference getEntityMapping_EntityMappingRules();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule <em>Entity Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Mapping Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMappingRule
	 * @generated
	 */
	EClass getEntityMappingRule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getEntityMappingElements <em>Entity Mapping Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entity Mapping Elements</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getEntityMappingElements()
	 * @see #getEntityMappingRule()
	 * @generated
	 */
	EReference getEntityMappingRule_EntityMappingElements();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getEntitySourceElement <em>Entity Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entity Source Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getEntitySourceElement()
	 * @see #getEntityMappingRule()
	 * @generated
	 */
	EReference getEntityMappingRule_EntitySourceElement();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getTargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getTargetElement()
	 * @see #getEntityMappingRule()
	 * @generated
	 */
	EReference getEntityMappingRule_TargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityTargetElement <em>Entity Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityTargetElement
	 * @generated
	 */
	EClass getEntityTargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntitySourceElement <em>Entity Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Source Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntitySourceElement
	 * @generated
	 */
	EClass getEntitySourceElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityPropertyElement <em>Entity Property Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Property Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityPropertyElement
	 * @generated
	 */
	EClass getEntityPropertyElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EntityPropertyElement#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type Ref</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityPropertyElement#getTypeRef()
	 * @see #getEntityPropertyElement()
	 * @generated
	 */
	EReference getEntityPropertyElement_TypeRef();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.EntityPropertyElement#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityPropertyElement#getAttribute()
	 * @see #getEntityPropertyElement()
	 * @generated
	 */
	EAttribute getEntityPropertyElement_Attribute();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumMapping <em>Enum Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Mapping</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMapping
	 * @generated
	 */
	EClass getEnumMapping();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.EnumMapping#getEnumMappingRules <em>Enum Mapping Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Enum Mapping Rules</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMapping#getEnumMappingRules()
	 * @see #getEnumMapping()
	 * @generated
	 */
	EReference getEnumMapping_EnumMappingRules();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule <em>Enum Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Mapping Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMappingRule
	 * @generated
	 */
	EClass getEnumMappingRule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getEnumElements <em>Enum Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Enum Elements</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getEnumElements()
	 * @see #getEnumMappingRule()
	 * @generated
	 */
	EReference getEnumMappingRule_EnumElements();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getEnumSourceElement <em>Enum Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Enum Source Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getEnumSourceElement()
	 * @see #getEnumMappingRule()
	 * @generated
	 */
	EReference getEnumMappingRule_EnumSourceElement();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getTargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getTargetElement()
	 * @see #getEnumMappingRule()
	 * @generated
	 */
	EReference getEnumMappingRule_TargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumTargetElement <em>Enum Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumTargetElement
	 * @generated
	 */
	EClass getEnumTargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumSourceElement <em>Enum Source Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Source Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumSourceElement
	 * @generated
	 */
	EClass getEnumSourceElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EnumSourceElement#getTypeRef <em>Type Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type Ref</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumSourceElement#getTypeRef()
	 * @see #getEnumSourceElement()
	 * @generated
	 */
	EReference getEnumSourceElement_TypeRef();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumPropertyElement <em>Enum Property Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Property Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumPropertyElement
	 * @generated
	 */
	EClass getEnumPropertyElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.EnumPropertyElement#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumPropertyElement#getAttribute()
	 * @see #getEnumPropertyElement()
	 * @generated
	 */
	EAttribute getEnumPropertyElement_Attribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumExpression <em>Enum Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Expression</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumExpression
	 * @generated
	 */
	EClass getEnumExpression();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EnumExpression#getLiteral <em>Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Literal</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumExpression#getLiteral()
	 * @see #getEnumExpression()
	 * @generated
	 */
	EReference getEnumExpression_Literal();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumReference <em>Enum Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Reference</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumReference
	 * @generated
	 */
	EClass getEnumReference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EnumReference#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumReference#getReference()
	 * @see #getEnumReference()
	 * @generated
	 */
	EReference getEnumReference_Reference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference <em>Function Block Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Reference</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference
	 * @generated
	 */
	EClass getFunctionBlockReference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference#getReference()
	 * @see #getFunctionBlockReference()
	 * @generated
	 */
	EReference getFunctionBlockReference_Reference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeReference <em>Data Type Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Type Reference</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeReference
	 * @generated
	 */
	EClass getDataTypeReference();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeReference#getReference <em>Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Reference</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeReference#getReference()
	 * @see #getDataTypeReference()
	 * @generated
	 */
	EReference getDataTypeReference_Reference();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMapping <em>Data Type Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Type Mapping</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeMapping
	 * @generated
	 */
	EClass getDataTypeMapping();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeReference <em>Stereo Type Reference</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stereo Type Reference</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeReference
	 * @generated
	 */
	EClass getStereoTypeReference();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeReference#getTargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeReference#getTargetElement()
	 * @see #getStereoTypeReference()
	 * @generated
	 */
	EReference getStereoTypeReference_TargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeElement <em>Stereo Type Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stereo Type Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeElement
	 * @generated
	 */
	EClass getStereoTypeElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeElement#getStereoTypes <em>Stereo Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Stereo Types</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeElement#getStereoTypes()
	 * @see #getStereoTypeElement()
	 * @generated
	 */
	EReference getStereoTypeElement_StereoTypes();

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
		 * The meta object literal for the '<em><b>Mapping Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_MODEL__MAPPING_TYPE = eINSTANCE.getMappingModel_MappingType();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingTypeImpl <em>Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingTypeImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getMappingType()
		 * @generated
		 */
		EClass MAPPING_TYPE = eINSTANCE.getMappingType();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAPPING_TYPE__NAME = eINSTANCE.getMappingType_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingImpl <em>Info Model Mapping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelMapping()
		 * @generated
		 */
		EClass INFO_MODEL_MAPPING = eINSTANCE.getInfoModelMapping();

		/**
		 * The meta object literal for the '<em><b>Info Model Mapping Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES = eINSTANCE.getInfoModelMapping_InfoModelMappingRules();

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
		 * The meta object literal for the '<em><b>Target</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_MAPPING_RULE__TARGET = eINSTANCE.getInfoModelMappingRule_Target();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelTargetElementImpl <em>Info Model Target Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelTargetElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelTargetElement()
		 * @generated
		 */
		EClass INFO_MODEL_TARGET_ELEMENT = eINSTANCE.getInfoModelTargetElement();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingImpl <em>Function Block Mapping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockMapping()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_MAPPING = eINSTANCE.getFunctionBlockMapping();

		/**
		 * The meta object literal for the '<em><b>Function Block Mapping Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES = eINSTANCE.getFunctionBlockMapping_FunctionBlockMappingRules();

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
		 * The meta object literal for the '<em><b>Target</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_MAPPING_RULE__TARGET = eINSTANCE.getFunctionBlockMappingRule_Target();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockTargetElementImpl <em>Function Block Target Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockTargetElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockTargetElement()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_TARGET_ELEMENT = eINSTANCE.getFunctionBlockTargetElement();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingImpl <em>Entity Mapping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityMapping()
		 * @generated
		 */
		EClass ENTITY_MAPPING = eINSTANCE.getEntityMapping();

		/**
		 * The meta object literal for the '<em><b>Entity Mapping Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_MAPPING__ENTITY_MAPPING_RULES = eINSTANCE.getEntityMapping_EntityMappingRules();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl <em>Entity Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityMappingRule()
		 * @generated
		 */
		EClass ENTITY_MAPPING_RULE = eINSTANCE.getEntityMappingRule();

		/**
		 * The meta object literal for the '<em><b>Entity Mapping Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS = eINSTANCE.getEntityMappingRule_EntityMappingElements();

		/**
		 * The meta object literal for the '<em><b>Entity Source Element</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT = eINSTANCE.getEntityMappingRule_EntitySourceElement();

		/**
		 * The meta object literal for the '<em><b>Target Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_MAPPING_RULE__TARGET_ELEMENT = eINSTANCE.getEntityMappingRule_TargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityTargetElementImpl <em>Entity Target Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityTargetElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityTargetElement()
		 * @generated
		 */
		EClass ENTITY_TARGET_ELEMENT = eINSTANCE.getEntityTargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntitySourceElementImpl <em>Entity Source Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntitySourceElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntitySourceElement()
		 * @generated
		 */
		EClass ENTITY_SOURCE_ELEMENT = eINSTANCE.getEntitySourceElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityPropertyElementImpl <em>Entity Property Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityPropertyElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityPropertyElement()
		 * @generated
		 */
		EClass ENTITY_PROPERTY_ELEMENT = eINSTANCE.getEntityPropertyElement();

		/**
		 * The meta object literal for the '<em><b>Type Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_PROPERTY_ELEMENT__TYPE_REF = eINSTANCE.getEntityPropertyElement_TypeRef();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY_PROPERTY_ELEMENT__ATTRIBUTE = eINSTANCE.getEntityPropertyElement_Attribute();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingImpl <em>Enum Mapping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumMapping()
		 * @generated
		 */
		EClass ENUM_MAPPING = eINSTANCE.getEnumMapping();

		/**
		 * The meta object literal for the '<em><b>Enum Mapping Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_MAPPING__ENUM_MAPPING_RULES = eINSTANCE.getEnumMapping_EnumMappingRules();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl <em>Enum Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumMappingRule()
		 * @generated
		 */
		EClass ENUM_MAPPING_RULE = eINSTANCE.getEnumMappingRule();

		/**
		 * The meta object literal for the '<em><b>Enum Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_MAPPING_RULE__ENUM_ELEMENTS = eINSTANCE.getEnumMappingRule_EnumElements();

		/**
		 * The meta object literal for the '<em><b>Enum Source Element</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT = eINSTANCE.getEnumMappingRule_EnumSourceElement();

		/**
		 * The meta object literal for the '<em><b>Target Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_MAPPING_RULE__TARGET_ELEMENT = eINSTANCE.getEnumMappingRule_TargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumTargetElementImpl <em>Enum Target Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumTargetElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumTargetElement()
		 * @generated
		 */
		EClass ENUM_TARGET_ELEMENT = eINSTANCE.getEnumTargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumSourceElementImpl <em>Enum Source Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumSourceElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumSourceElement()
		 * @generated
		 */
		EClass ENUM_SOURCE_ELEMENT = eINSTANCE.getEnumSourceElement();

		/**
		 * The meta object literal for the '<em><b>Type Ref</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_SOURCE_ELEMENT__TYPE_REF = eINSTANCE.getEnumSourceElement_TypeRef();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumPropertyElementImpl <em>Enum Property Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumPropertyElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumPropertyElement()
		 * @generated
		 */
		EClass ENUM_PROPERTY_ELEMENT = eINSTANCE.getEnumPropertyElement();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUM_PROPERTY_ELEMENT__ATTRIBUTE = eINSTANCE.getEnumPropertyElement_Attribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumExpressionImpl <em>Enum Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumExpressionImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumExpression()
		 * @generated
		 */
		EClass ENUM_EXPRESSION = eINSTANCE.getEnumExpression();

		/**
		 * The meta object literal for the '<em><b>Literal</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_EXPRESSION__LITERAL = eINSTANCE.getEnumExpression_Literal();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumReferenceImpl <em>Enum Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumReferenceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumReference()
		 * @generated
		 */
		EClass ENUM_REFERENCE = eINSTANCE.getEnumReference();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_REFERENCE__REFERENCE = eINSTANCE.getEnumReference_Reference();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockReferenceImpl <em>Function Block Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockReferenceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockReference()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_REFERENCE = eINSTANCE.getFunctionBlockReference();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK_REFERENCE__REFERENCE = eINSTANCE.getFunctionBlockReference_Reference();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeReferenceImpl <em>Data Type Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeReferenceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeReference()
		 * @generated
		 */
		EClass DATA_TYPE_REFERENCE = eINSTANCE.getDataTypeReference();

		/**
		 * The meta object literal for the '<em><b>Reference</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_TYPE_REFERENCE__REFERENCE = eINSTANCE.getDataTypeReference_Reference();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingImpl <em>Data Type Mapping</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeMapping()
		 * @generated
		 */
		EClass DATA_TYPE_MAPPING = eINSTANCE.getDataTypeMapping();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeReferenceImpl <em>Stereo Type Reference</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeReferenceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoTypeReference()
		 * @generated
		 */
		EClass STEREO_TYPE_REFERENCE = eINSTANCE.getStereoTypeReference();

		/**
		 * The meta object literal for the '<em><b>Target Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STEREO_TYPE_REFERENCE__TARGET_ELEMENT = eINSTANCE.getStereoTypeReference_TargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeElementImpl <em>Stereo Type Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoTypeElement()
		 * @generated
		 */
		EClass STEREO_TYPE_ELEMENT = eINSTANCE.getStereoTypeElement();

		/**
		 * The meta object literal for the '<em><b>Stereo Types</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STEREO_TYPE_ELEMENT__STEREO_TYPES = eINSTANCE.getStereoTypeElement_StereoTypes();

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
