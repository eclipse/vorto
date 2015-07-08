/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
	 * The feature id for the '<em><b>Infomodel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__INFOMODEL = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__TARGET = 1;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__RULES = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.RuleImpl <em>Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.RuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getRule()
	 * @generated
	 */
	int RULE = 1;

	/**
	 * The feature id for the '<em><b>Information Model Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__INFORMATION_MODEL_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Target Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE__TARGET_ELEMENT = 1;

	/**
	 * The number of structural features of the '<em>Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InformationModelElementImpl <em>Information Model Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InformationModelElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInformationModelElement()
	 * @generated
	 */
	int INFORMATION_MODEL_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Functionblock Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL_ELEMENT__FUNCTIONBLOCK_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Tail</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL_ELEMENT__TAIL = 1;

	/**
	 * The number of structural features of the '<em>Information Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Information Model Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementImpl <em>Function Block Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockElement()
	 * @generated
	 */
	int FUNCTION_BLOCK_ELEMENT = 3;

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
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.OperationElementImpl <em>Operation Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.OperationElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getOperationElement()
	 * @generated
	 */
	int OPERATION_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_ELEMENT__VALUE = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Operation Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Operation Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationElementImpl <em>Configuration Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getConfigurationElement()
	 * @generated
	 */
	int CONFIGURATION_ELEMENT = 5;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT__VALUE = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Configuration Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Configuration Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StatusElementImpl <em>Status Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.StatusElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStatusElement()
	 * @generated
	 */
	int STATUS_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_ELEMENT__VALUE = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Status Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Status Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FaultElementImpl <em>Fault Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FaultElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFaultElement()
	 * @generated
	 */
	int FAULT_ELEMENT = 7;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_ELEMENT__VALUE = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Fault Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Fault Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EventElementImpl <em>Event Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EventElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEventElement()
	 * @generated
	 */
	int EVENT_ELEMENT = 8;

	/**
	 * The feature id for the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_ELEMENT__VALUE = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Event Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_ELEMENT_FEATURE_COUNT = FUNCTION_BLOCK_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Event Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_ELEMENT_OPERATION_COUNT = FUNCTION_BLOCK_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.TargetElementImpl <em>Target Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.TargetElementImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getTargetElement()
	 * @generated
	 */
	int TARGET_ELEMENT = 9;

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
	int STEREO_TYPE = 10;

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
	int ATTRIBUTE = 11;

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel
	 * @generated
	 */
	EClass getMappingModel();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getInfomodel <em>Infomodel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Infomodel</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel#getInfomodel()
	 * @see #getMappingModel()
	 * @generated
	 */
	EReference getMappingModel_Infomodel();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel#getTarget()
	 * @see #getMappingModel()
	 * @generated
	 */
	EAttribute getMappingModel_Target();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getRules <em>Rules</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rules</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel#getRules()
	 * @see #getMappingModel()
	 * @generated
	 */
	EReference getMappingModel_Rules();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.Rule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Rule
	 * @generated
	 */
	EClass getRule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.Rule#getInformationModelElements <em>Information Model Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Information Model Elements</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Rule#getInformationModelElements()
	 * @see #getRule()
	 * @generated
	 */
	EReference getRule_InformationModelElements();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.Rule#getTargetElement <em>Target Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Rule#getTargetElement()
	 * @see #getRule()
	 * @generated
	 */
	EReference getRule_TargetElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelElement <em>Information Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Information Model Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InformationModelElement
	 * @generated
	 */
	EClass getInformationModelElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelElement#getFunctionblockModel <em>Functionblock Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Functionblock Model</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InformationModelElement#getFunctionblockModel()
	 * @see #getInformationModelElement()
	 * @generated
	 */
	EReference getInformationModelElement_FunctionblockModel();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelElement#getTail <em>Tail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Tail</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InformationModelElement#getTail()
	 * @see #getInformationModelElement()
	 * @generated
	 */
	EReference getInformationModelElement_Tail();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.OperationElement <em>Operation Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Element</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.OperationElement
	 * @generated
	 */
	EClass getOperationElement();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.OperationElement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.OperationElement#getValue()
	 * @see #getOperationElement()
	 * @generated
	 */
	EReference getOperationElement_Value();

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
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.ConfigurationElement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.ConfigurationElement#getValue()
	 * @see #getConfigurationElement()
	 * @generated
	 */
	EReference getConfigurationElement_Value();

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
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.StatusElement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StatusElement#getValue()
	 * @see #getStatusElement()
	 * @generated
	 */
	EReference getStatusElement_Value();

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
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.FaultElement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FaultElement#getValue()
	 * @see #getFaultElement()
	 * @generated
	 */
	EReference getFaultElement_Value();

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
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EventElement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EventElement#getValue()
	 * @see #getEventElement()
	 * @generated
	 */
	EReference getEventElement_Value();

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
		 * The meta object literal for the '<em><b>Infomodel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_MODEL__INFOMODEL = eINSTANCE.getMappingModel_Infomodel();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAPPING_MODEL__TARGET = eINSTANCE.getMappingModel_Target();

		/**
		 * The meta object literal for the '<em><b>Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_MODEL__RULES = eINSTANCE.getMappingModel_Rules();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.RuleImpl <em>Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.RuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getRule()
		 * @generated
		 */
		EClass RULE = eINSTANCE.getRule();

		/**
		 * The meta object literal for the '<em><b>Information Model Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE__INFORMATION_MODEL_ELEMENTS = eINSTANCE.getRule_InformationModelElements();

		/**
		 * The meta object literal for the '<em><b>Target Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE__TARGET_ELEMENT = eINSTANCE.getRule_TargetElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InformationModelElementImpl <em>Information Model Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InformationModelElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInformationModelElement()
		 * @generated
		 */
		EClass INFORMATION_MODEL_ELEMENT = eINSTANCE.getInformationModelElement();

		/**
		 * The meta object literal for the '<em><b>Functionblock Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFORMATION_MODEL_ELEMENT__FUNCTIONBLOCK_MODEL = eINSTANCE.getInformationModelElement_FunctionblockModel();

		/**
		 * The meta object literal for the '<em><b>Tail</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFORMATION_MODEL_ELEMENT__TAIL = eINSTANCE.getInformationModelElement_Tail();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.OperationElementImpl <em>Operation Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.OperationElementImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getOperationElement()
		 * @generated
		 */
		EClass OPERATION_ELEMENT = eINSTANCE.getOperationElement();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_ELEMENT__VALUE = eINSTANCE.getOperationElement_Value();

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
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIGURATION_ELEMENT__VALUE = eINSTANCE.getConfigurationElement_Value();

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
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATUS_ELEMENT__VALUE = eINSTANCE.getStatusElement_Value();

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
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FAULT_ELEMENT__VALUE = eINSTANCE.getFaultElement_Value();

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
		 * The meta object literal for the '<em><b>Value</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_ELEMENT__VALUE = eINSTANCE.getEventElement_Value();

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

	}

} //MappingPackage
