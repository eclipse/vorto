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
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_MODEL__RULES = ModelPackage.MODEL_FEATURE_COUNT + 0;

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
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingImpl <em>Info Model Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelMapping()
	 * @generated
	 */
	int INFO_MODEL_MAPPING = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING__NAME = MAPPING_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING__NAMESPACE = MAPPING_MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING__VERSION = MAPPING_MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING__REFERENCES = MAPPING_MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING__RULES = MAPPING_MODEL__RULES;

	/**
	 * The number of structural features of the '<em>Info Model Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_FEATURE_COUNT = MAPPING_MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Info Model Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_OPERATION_COUNT = MAPPING_MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingRuleImpl <em>Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getMappingRule()
	 * @generated
	 */
	int MAPPING_RULE = 32;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_RULE__TARGET = 0;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_RULE__SOURCES = 1;

	/**
	 * The number of structural features of the '<em>Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_RULE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_RULE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl <em>Info Model Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelMappingRule()
	 * @generated
	 */
	int INFO_MODEL_MAPPING_RULE = 2;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE__TARGET = MAPPING_RULE__TARGET;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE__SOURCES = MAPPING_RULE__SOURCES;

	/**
	 * The number of structural features of the '<em>Info Model Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE_FEATURE_COUNT = MAPPING_RULE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Info Model Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_MAPPING_RULE_OPERATION_COUNT = MAPPING_RULE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.SourceImpl <em>Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.SourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getSource()
	 * @generated
	 */
	int SOURCE = 30;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE__MODEL = 0;

	/**
	 * The number of structural features of the '<em>Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfomodelSourceImpl <em>Infomodel Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfomodelSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfomodelSource()
	 * @generated
	 */
	int INFOMODEL_SOURCE = 3;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFOMODEL_SOURCE__MODEL = SOURCE__MODEL;

	/**
	 * The number of structural features of the '<em>Infomodel Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFOMODEL_SOURCE_FEATURE_COUNT = SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Infomodel Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFOMODEL_SOURCE_OPERATION_COUNT = SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelPropertySourceImpl <em>Info Model Property Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelPropertySourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelPropertySource()
	 * @generated
	 */
	int INFO_MODEL_PROPERTY_SOURCE = 4;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_PROPERTY_SOURCE__MODEL = INFOMODEL_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_PROPERTY_SOURCE__PROPERTY = INFOMODEL_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Info Model Property Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_PROPERTY_SOURCE_FEATURE_COUNT = INFOMODEL_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Info Model Property Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_PROPERTY_SOURCE_OPERATION_COUNT = INFOMODEL_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelAttributeSourceImpl <em>Info Model Attribute Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelAttributeSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelAttributeSource()
	 * @generated
	 */
	int INFO_MODEL_ATTRIBUTE_SOURCE = 5;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_ATTRIBUTE_SOURCE__MODEL = INFOMODEL_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_ATTRIBUTE_SOURCE__ATTRIBUTE = INFOMODEL_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Info Model Attribute Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_ATTRIBUTE_SOURCE_FEATURE_COUNT = INFOMODEL_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Info Model Attribute Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFO_MODEL_ATTRIBUTE_SOURCE_OPERATION_COUNT = INFOMODEL_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingImpl <em>Function Block Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockMapping()
	 * @generated
	 */
	int FUNCTION_BLOCK_MAPPING = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING__NAME = MAPPING_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING__NAMESPACE = MAPPING_MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING__VERSION = MAPPING_MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING__REFERENCES = MAPPING_MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING__RULES = MAPPING_MODEL__RULES;

	/**
	 * The number of structural features of the '<em>Function Block Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_FEATURE_COUNT = MAPPING_MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Function Block Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_OPERATION_COUNT = MAPPING_MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl <em>Function Block Mapping Rule</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockMappingRule()
	 * @generated
	 */
	int FUNCTION_BLOCK_MAPPING_RULE = 7;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE__TARGET = MAPPING_RULE__TARGET;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE__SOURCES = MAPPING_RULE__SOURCES;

	/**
	 * The number of structural features of the '<em>Function Block Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE_FEATURE_COUNT = MAPPING_RULE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Function Block Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_MAPPING_RULE_OPERATION_COUNT = MAPPING_RULE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceImpl <em>Function Block Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockSource()
	 * @generated
	 */
	int FUNCTION_BLOCK_SOURCE = 8;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_SOURCE__MODEL = SOURCE__MODEL;

	/**
	 * The number of structural features of the '<em>Function Block Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_SOURCE_FEATURE_COUNT = SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Function Block Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_SOURCE_OPERATION_COUNT = SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockPropertySourceImpl <em>Function Block Property Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockPropertySourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockPropertySource()
	 * @generated
	 */
	int FUNCTION_BLOCK_PROPERTY_SOURCE = 9;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_PROPERTY_SOURCE__MODEL = FUNCTION_BLOCK_SOURCE__MODEL;

	/**
	 * The number of structural features of the '<em>Function Block Property Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_PROPERTY_SOURCE_FEATURE_COUNT = FUNCTION_BLOCK_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Function Block Property Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_PROPERTY_SOURCE_OPERATION_COUNT = FUNCTION_BLOCK_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockAttributeSourceImpl <em>Function Block Attribute Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockAttributeSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockAttributeSource()
	 * @generated
	 */
	int FUNCTION_BLOCK_ATTRIBUTE_SOURCE = 10;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ATTRIBUTE_SOURCE__MODEL = FUNCTION_BLOCK_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ATTRIBUTE_SOURCE__ATTRIBUTE = FUNCTION_BLOCK_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Function Block Attribute Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ATTRIBUTE_SOURCE_FEATURE_COUNT = FUNCTION_BLOCK_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Function Block Attribute Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_ATTRIBUTE_SOURCE_OPERATION_COUNT = FUNCTION_BLOCK_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationSourceImpl <em>Configuration Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getConfigurationSource()
	 * @generated
	 */
	int CONFIGURATION_SOURCE = 11;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_SOURCE__MODEL = FUNCTION_BLOCK_PROPERTY_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_SOURCE__PROPERTY = FUNCTION_BLOCK_PROPERTY_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Configuration Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_SOURCE_FEATURE_COUNT = FUNCTION_BLOCK_PROPERTY_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Configuration Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_SOURCE_OPERATION_COUNT = FUNCTION_BLOCK_PROPERTY_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StatusSourceImpl <em>Status Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.StatusSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStatusSource()
	 * @generated
	 */
	int STATUS_SOURCE = 12;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_SOURCE__MODEL = FUNCTION_BLOCK_PROPERTY_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_SOURCE__PROPERTY = FUNCTION_BLOCK_PROPERTY_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Status Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_SOURCE_FEATURE_COUNT = FUNCTION_BLOCK_PROPERTY_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Status Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_SOURCE_OPERATION_COUNT = FUNCTION_BLOCK_PROPERTY_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.OperationSourceImpl <em>Operation Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.OperationSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getOperationSource()
	 * @generated
	 */
	int OPERATION_SOURCE = 13;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_SOURCE__MODEL = FUNCTION_BLOCK_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_SOURCE__OPERATION = FUNCTION_BLOCK_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Operation Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_SOURCE_FEATURE_COUNT = FUNCTION_BLOCK_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Operation Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_SOURCE_OPERATION_COUNT = FUNCTION_BLOCK_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EventSourceImpl <em>Event Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EventSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEventSource()
	 * @generated
	 */
	int EVENT_SOURCE = 14;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_SOURCE__MODEL = FUNCTION_BLOCK_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_SOURCE__EVENT = FUNCTION_BLOCK_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Event Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_SOURCE__EVENT_PROPERTY = FUNCTION_BLOCK_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Event Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_SOURCE_FEATURE_COUNT = FUNCTION_BLOCK_SOURCE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Event Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_SOURCE_OPERATION_COUNT = FUNCTION_BLOCK_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingImpl <em>Data Type Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.DataTypeMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getDataTypeMapping()
	 * @generated
	 */
	int DATA_TYPE_MAPPING = 25;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING__NAME = MAPPING_MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING__NAMESPACE = MAPPING_MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING__VERSION = MAPPING_MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING__REFERENCES = MAPPING_MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING__RULES = MAPPING_MODEL__RULES;

	/**
	 * The number of structural features of the '<em>Data Type Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_FEATURE_COUNT = MAPPING_MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Data Type Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TYPE_MAPPING_OPERATION_COUNT = MAPPING_MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingImpl <em>Entity Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityMapping()
	 * @generated
	 */
	int ENTITY_MAPPING = 15;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING__NAME = DATA_TYPE_MAPPING__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING__NAMESPACE = DATA_TYPE_MAPPING__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING__VERSION = DATA_TYPE_MAPPING__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING__REFERENCES = DATA_TYPE_MAPPING__REFERENCES;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING__RULES = DATA_TYPE_MAPPING__RULES;

	/**
	 * The number of structural features of the '<em>Entity Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_FEATURE_COUNT = DATA_TYPE_MAPPING_FEATURE_COUNT + 0;

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
	int ENTITY_MAPPING_RULE = 16;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE__TARGET = MAPPING_RULE__TARGET;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE__SOURCES = MAPPING_RULE__SOURCES;

	/**
	 * The number of structural features of the '<em>Entity Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE_FEATURE_COUNT = MAPPING_RULE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Entity Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_MAPPING_RULE_OPERATION_COUNT = MAPPING_RULE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntitySourceImpl <em>Entity Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntitySourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntitySource()
	 * @generated
	 */
	int ENTITY_SOURCE = 17;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_SOURCE__MODEL = SOURCE__MODEL;

	/**
	 * The number of structural features of the '<em>Entity Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_SOURCE_FEATURE_COUNT = SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Entity Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_SOURCE_OPERATION_COUNT = SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityPropertySourceImpl <em>Entity Property Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityPropertySourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityPropertySource()
	 * @generated
	 */
	int ENTITY_PROPERTY_SOURCE = 18;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROPERTY_SOURCE__MODEL = ENTITY_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROPERTY_SOURCE__PROPERTY = ENTITY_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Entity Property Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROPERTY_SOURCE_FEATURE_COUNT = ENTITY_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Entity Property Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_PROPERTY_SOURCE_OPERATION_COUNT = ENTITY_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityAttributeSourceImpl <em>Entity Attribute Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityAttributeSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityAttributeSource()
	 * @generated
	 */
	int ENTITY_ATTRIBUTE_SOURCE = 19;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_ATTRIBUTE_SOURCE__MODEL = ENTITY_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_ATTRIBUTE_SOURCE__ATTRIBUTE = ENTITY_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Entity Attribute Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_ATTRIBUTE_SOURCE_FEATURE_COUNT = ENTITY_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Entity Attribute Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_ATTRIBUTE_SOURCE_OPERATION_COUNT = ENTITY_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingImpl <em>Enum Mapping</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumMapping()
	 * @generated
	 */
	int ENUM_MAPPING = 20;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING__NAME = DATA_TYPE_MAPPING__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING__NAMESPACE = DATA_TYPE_MAPPING__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING__VERSION = DATA_TYPE_MAPPING__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING__REFERENCES = DATA_TYPE_MAPPING__REFERENCES;

	/**
	 * The feature id for the '<em><b>Rules</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING__RULES = DATA_TYPE_MAPPING__RULES;

	/**
	 * The number of structural features of the '<em>Enum Mapping</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_FEATURE_COUNT = DATA_TYPE_MAPPING_FEATURE_COUNT + 0;

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
	int ENUM_MAPPING_RULE = 21;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE__TARGET = MAPPING_RULE__TARGET;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE__SOURCES = MAPPING_RULE__SOURCES;

	/**
	 * The number of structural features of the '<em>Enum Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE_FEATURE_COUNT = MAPPING_RULE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Enum Mapping Rule</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_MAPPING_RULE_OPERATION_COUNT = MAPPING_RULE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumSourceImpl <em>Enum Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumSource()
	 * @generated
	 */
	int ENUM_SOURCE = 22;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_SOURCE__MODEL = SOURCE__MODEL;

	/**
	 * The number of structural features of the '<em>Enum Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_SOURCE_FEATURE_COUNT = SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Enum Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_SOURCE_OPERATION_COUNT = SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumPropertySourceImpl <em>Enum Property Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumPropertySourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumPropertySource()
	 * @generated
	 */
	int ENUM_PROPERTY_SOURCE = 23;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_PROPERTY_SOURCE__MODEL = ENUM_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_PROPERTY_SOURCE__PROPERTY = ENUM_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum Property Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_PROPERTY_SOURCE_FEATURE_COUNT = ENUM_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Enum Property Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_PROPERTY_SOURCE_OPERATION_COUNT = ENUM_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumAttributeSourceImpl <em>Enum Attribute Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumAttributeSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumAttributeSource()
	 * @generated
	 */
	int ENUM_ATTRIBUTE_SOURCE = 24;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_ATTRIBUTE_SOURCE__MODEL = ENUM_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Attribute</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_ATTRIBUTE_SOURCE__ATTRIBUTE = ENUM_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum Attribute Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_ATTRIBUTE_SOURCE_FEATURE_COUNT = ENUM_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Enum Attribute Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_ATTRIBUTE_SOURCE_OPERATION_COUNT = ENUM_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.TargetImpl <em>Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.TargetImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getTarget()
	 * @generated
	 */
	int TARGET = 26;

	/**
	 * The number of structural features of the '<em>Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TARGET_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.ReferenceTargetImpl <em>Reference Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.ReferenceTargetImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getReferenceTarget()
	 * @generated
	 */
	int REFERENCE_TARGET = 27;

	/**
	 * The feature id for the '<em><b>Mapping Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TARGET__MAPPING_MODEL = TARGET_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Reference Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TARGET_FEATURE_COUNT = TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Reference Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REFERENCE_TARGET_OPERATION_COUNT = TARGET_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeTargetImpl <em>Stereo Type Target</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeTargetImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoTypeTarget()
	 * @generated
	 */
	int STEREO_TYPE_TARGET = 28;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_TARGET__NAME = TARGET_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_TARGET__ATTRIBUTES = TARGET_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Stereo Type Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_TARGET_FEATURE_COUNT = TARGET_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Stereo Type Target</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STEREO_TYPE_TARGET_OPERATION_COUNT = TARGET_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.AttributeImpl <em>Attribute</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.AttributeImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getAttribute()
	 * @generated
	 */
	int ATTRIBUTE = 29;

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
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FaultSourceImpl <em>Fault Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.FaultSourceImpl
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFaultSource()
	 * @generated
	 */
	int FAULT_SOURCE = 31;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_SOURCE__MODEL = FUNCTION_BLOCK_PROPERTY_SOURCE__MODEL;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_SOURCE__PROPERTY = FUNCTION_BLOCK_PROPERTY_SOURCE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Fault Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_SOURCE_FEATURE_COUNT = FUNCTION_BLOCK_PROPERTY_SOURCE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Fault Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_SOURCE_OPERATION_COUNT = FUNCTION_BLOCK_PROPERTY_SOURCE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute <em>Info Model Attribute</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelAttribute()
	 * @generated
	 */
	int INFO_MODEL_ATTRIBUTE = 33;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute <em>Functionblock Model Attribute</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionblockModelAttribute()
	 * @generated
	 */
	int FUNCTIONBLOCK_MODEL_ATTRIBUTE = 34;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.mapping.ModelAttribute <em>Model Attribute</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.mapping.ModelAttribute
	 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getModelAttribute()
	 * @generated
	 */
	int MODEL_ATTRIBUTE = 35;


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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMapping <em>Info Model Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Mapping</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMapping
	 * @generated
	 */
	EClass getInfoModelMapping();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfomodelSource <em>Infomodel Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Infomodel Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfomodelSource
	 * @generated
	 */
	EClass getInfomodelSource();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource <em>Info Model Property Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Property Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource
	 * @generated
	 */
	EClass getInfoModelPropertySource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource#getProperty()
	 * @see #getInfoModelPropertySource()
	 * @generated
	 */
	EReference getInfoModelPropertySource_Property();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource <em>Info Model Attribute Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Info Model Attribute Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource
	 * @generated
	 */
	EClass getInfoModelAttributeSource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource#getAttribute()
	 * @see #getInfoModelAttributeSource()
	 * @generated
	 */
	EAttribute getInfoModelAttributeSource_Attribute();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule <em>Function Block Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Mapping Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule
	 * @generated
	 */
	EClass getFunctionBlockMappingRule();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource <em>Function Block Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource
	 * @generated
	 */
	EClass getFunctionBlockSource();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockPropertySource <em>Function Block Property Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Property Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockPropertySource
	 * @generated
	 */
	EClass getFunctionBlockPropertySource();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource <em>Function Block Attribute Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block Attribute Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource
	 * @generated
	 */
	EClass getFunctionBlockAttributeSource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource#getAttribute()
	 * @see #getFunctionBlockAttributeSource()
	 * @generated
	 */
	EAttribute getFunctionBlockAttributeSource_Attribute();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.ConfigurationSource <em>Configuration Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configuration Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.ConfigurationSource
	 * @generated
	 */
	EClass getConfigurationSource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.ConfigurationSource#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.ConfigurationSource#getProperty()
	 * @see #getConfigurationSource()
	 * @generated
	 */
	EReference getConfigurationSource_Property();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.StatusSource <em>Status Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Status Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StatusSource
	 * @generated
	 */
	EClass getStatusSource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.StatusSource#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StatusSource#getProperty()
	 * @see #getStatusSource()
	 * @generated
	 */
	EReference getStatusSource_Property();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.OperationSource <em>Operation Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.OperationSource
	 * @generated
	 */
	EClass getOperationSource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.OperationSource#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Operation</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.OperationSource#getOperation()
	 * @see #getOperationSource()
	 * @generated
	 */
	EReference getOperationSource_Operation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EventSource <em>Event Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EventSource
	 * @generated
	 */
	EClass getEventSource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EventSource#getEvent <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Event</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EventSource#getEvent()
	 * @see #getEventSource()
	 * @generated
	 */
	EReference getEventSource_Event();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EventSource#getEventProperty <em>Event Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Event Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EventSource#getEventProperty()
	 * @see #getEventSource()
	 * @generated
	 */
	EReference getEventSource_EventProperty();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule <em>Entity Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Mapping Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMappingRule
	 * @generated
	 */
	EClass getEntityMappingRule();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntitySource <em>Entity Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntitySource
	 * @generated
	 */
	EClass getEntitySource();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityPropertySource <em>Entity Property Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Property Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityPropertySource
	 * @generated
	 */
	EClass getEntityPropertySource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EntityPropertySource#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityPropertySource#getProperty()
	 * @see #getEntityPropertySource()
	 * @generated
	 */
	EReference getEntityPropertySource_Property();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource <em>Entity Attribute Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity Attribute Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource
	 * @generated
	 */
	EClass getEntityAttributeSource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource#getAttribute()
	 * @see #getEntityAttributeSource()
	 * @generated
	 */
	EAttribute getEntityAttributeSource_Attribute();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule <em>Enum Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Mapping Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMappingRule
	 * @generated
	 */
	EClass getEnumMappingRule();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumSource <em>Enum Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumSource
	 * @generated
	 */
	EClass getEnumSource();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumPropertySource <em>Enum Property Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Property Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumPropertySource
	 * @generated
	 */
	EClass getEnumPropertySource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.EnumPropertySource#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumPropertySource#getProperty()
	 * @see #getEnumPropertySource()
	 * @generated
	 */
	EReference getEnumPropertySource_Property();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource <em>Enum Attribute Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Attribute Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource
	 * @generated
	 */
	EClass getEnumAttributeSource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource#getAttribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Attribute</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource#getAttribute()
	 * @see #getEnumAttributeSource()
	 * @generated
	 */
	EAttribute getEnumAttributeSource_Attribute();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.Target <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Target</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Target
	 * @generated
	 */
	EClass getTarget();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.ReferenceTarget <em>Reference Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reference Target</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.ReferenceTarget
	 * @generated
	 */
	EClass getReferenceTarget();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.ReferenceTarget#getMappingModel <em>Mapping Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Mapping Model</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.ReferenceTarget#getMappingModel()
	 * @see #getReferenceTarget()
	 * @generated
	 */
	EReference getReferenceTarget_MappingModel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget <em>Stereo Type Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Stereo Type Target</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget
	 * @generated
	 */
	EClass getStereoTypeTarget();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget#getName()
	 * @see #getStereoTypeTarget()
	 * @generated
	 */
	EAttribute getStereoTypeTarget_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget#getAttributes <em>Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Attributes</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget#getAttributes()
	 * @see #getStereoTypeTarget()
	 * @generated
	 */
	EReference getStereoTypeTarget_Attributes();

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
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.Source <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Source
	 * @generated
	 */
	EClass getSource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.Source#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Model</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.Source#getModel()
	 * @see #getSource()
	 * @generated
	 */
	EReference getSource_Model();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.FaultSource <em>Fault Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fault Source</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FaultSource
	 * @generated
	 */
	EClass getFaultSource();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.mapping.FaultSource#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.FaultSource#getProperty()
	 * @see #getFaultSource()
	 * @generated
	 */
	EReference getFaultSource_Property();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.mapping.MappingRule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingRule
	 * @generated
	 */
	EClass getMappingRule();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.mapping.MappingRule#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingRule#getTarget()
	 * @see #getMappingRule()
	 * @generated
	 */
	EReference getMappingRule_Target();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.mapping.MappingRule#getSources <em>Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Sources</em>'.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingRule#getSources()
	 * @see #getMappingRule()
	 * @generated
	 */
	EReference getMappingRule_Sources();

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
		 * The meta object literal for the '<em><b>Rules</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_MODEL__RULES = eINSTANCE.getMappingModel_Rules();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl <em>Info Model Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelMappingRule()
		 * @generated
		 */
		EClass INFO_MODEL_MAPPING_RULE = eINSTANCE.getInfoModelMappingRule();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfomodelSourceImpl <em>Infomodel Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfomodelSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfomodelSource()
		 * @generated
		 */
		EClass INFOMODEL_SOURCE = eINSTANCE.getInfomodelSource();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelPropertySourceImpl <em>Info Model Property Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelPropertySourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelPropertySource()
		 * @generated
		 */
		EClass INFO_MODEL_PROPERTY_SOURCE = eINSTANCE.getInfoModelPropertySource();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFO_MODEL_PROPERTY_SOURCE__PROPERTY = eINSTANCE.getInfoModelPropertySource_Property();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelAttributeSourceImpl <em>Info Model Attribute Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.InfoModelAttributeSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getInfoModelAttributeSource()
		 * @generated
		 */
		EClass INFO_MODEL_ATTRIBUTE_SOURCE = eINSTANCE.getInfoModelAttributeSource();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INFO_MODEL_ATTRIBUTE_SOURCE__ATTRIBUTE = eINSTANCE.getInfoModelAttributeSource_Attribute();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl <em>Function Block Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockMappingRule()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_MAPPING_RULE = eINSTANCE.getFunctionBlockMappingRule();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceImpl <em>Function Block Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockSource()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_SOURCE = eINSTANCE.getFunctionBlockSource();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockPropertySourceImpl <em>Function Block Property Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockPropertySourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockPropertySource()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_PROPERTY_SOURCE = eINSTANCE.getFunctionBlockPropertySource();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockAttributeSourceImpl <em>Function Block Attribute Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockAttributeSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFunctionBlockAttributeSource()
		 * @generated
		 */
		EClass FUNCTION_BLOCK_ATTRIBUTE_SOURCE = eINSTANCE.getFunctionBlockAttributeSource();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUNCTION_BLOCK_ATTRIBUTE_SOURCE__ATTRIBUTE = eINSTANCE.getFunctionBlockAttributeSource_Attribute();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationSourceImpl <em>Configuration Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.ConfigurationSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getConfigurationSource()
		 * @generated
		 */
		EClass CONFIGURATION_SOURCE = eINSTANCE.getConfigurationSource();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIGURATION_SOURCE__PROPERTY = eINSTANCE.getConfigurationSource_Property();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StatusSourceImpl <em>Status Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.StatusSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStatusSource()
		 * @generated
		 */
		EClass STATUS_SOURCE = eINSTANCE.getStatusSource();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATUS_SOURCE__PROPERTY = eINSTANCE.getStatusSource_Property();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.OperationSourceImpl <em>Operation Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.OperationSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getOperationSource()
		 * @generated
		 */
		EClass OPERATION_SOURCE = eINSTANCE.getOperationSource();

		/**
		 * The meta object literal for the '<em><b>Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_SOURCE__OPERATION = eINSTANCE.getOperationSource_Operation();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EventSourceImpl <em>Event Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EventSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEventSource()
		 * @generated
		 */
		EClass EVENT_SOURCE = eINSTANCE.getEventSource();

		/**
		 * The meta object literal for the '<em><b>Event</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_SOURCE__EVENT = eINSTANCE.getEventSource_Event();

		/**
		 * The meta object literal for the '<em><b>Event Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT_SOURCE__EVENT_PROPERTY = eINSTANCE.getEventSource_EventProperty();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl <em>Entity Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityMappingRule()
		 * @generated
		 */
		EClass ENTITY_MAPPING_RULE = eINSTANCE.getEntityMappingRule();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntitySourceImpl <em>Entity Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntitySourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntitySource()
		 * @generated
		 */
		EClass ENTITY_SOURCE = eINSTANCE.getEntitySource();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityPropertySourceImpl <em>Entity Property Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityPropertySourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityPropertySource()
		 * @generated
		 */
		EClass ENTITY_PROPERTY_SOURCE = eINSTANCE.getEntityPropertySource();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_PROPERTY_SOURCE__PROPERTY = eINSTANCE.getEntityPropertySource_Property();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityAttributeSourceImpl <em>Entity Attribute Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EntityAttributeSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEntityAttributeSource()
		 * @generated
		 */
		EClass ENTITY_ATTRIBUTE_SOURCE = eINSTANCE.getEntityAttributeSource();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENTITY_ATTRIBUTE_SOURCE__ATTRIBUTE = eINSTANCE.getEntityAttributeSource_Attribute();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl <em>Enum Mapping Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumMappingRule()
		 * @generated
		 */
		EClass ENUM_MAPPING_RULE = eINSTANCE.getEnumMappingRule();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumSourceImpl <em>Enum Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumSource()
		 * @generated
		 */
		EClass ENUM_SOURCE = eINSTANCE.getEnumSource();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumPropertySourceImpl <em>Enum Property Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumPropertySourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumPropertySource()
		 * @generated
		 */
		EClass ENUM_PROPERTY_SOURCE = eINSTANCE.getEnumPropertySource();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_PROPERTY_SOURCE__PROPERTY = eINSTANCE.getEnumPropertySource_Property();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumAttributeSourceImpl <em>Enum Attribute Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.EnumAttributeSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getEnumAttributeSource()
		 * @generated
		 */
		EClass ENUM_ATTRIBUTE_SOURCE = eINSTANCE.getEnumAttributeSource();

		/**
		 * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ENUM_ATTRIBUTE_SOURCE__ATTRIBUTE = eINSTANCE.getEnumAttributeSource_Attribute();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.TargetImpl <em>Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.TargetImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getTarget()
		 * @generated
		 */
		EClass TARGET = eINSTANCE.getTarget();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.ReferenceTargetImpl <em>Reference Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.ReferenceTargetImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getReferenceTarget()
		 * @generated
		 */
		EClass REFERENCE_TARGET = eINSTANCE.getReferenceTarget();

		/**
		 * The meta object literal for the '<em><b>Mapping Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REFERENCE_TARGET__MAPPING_MODEL = eINSTANCE.getReferenceTarget_MappingModel();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeTargetImpl <em>Stereo Type Target</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeTargetImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getStereoTypeTarget()
		 * @generated
		 */
		EClass STEREO_TYPE_TARGET = eINSTANCE.getStereoTypeTarget();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STEREO_TYPE_TARGET__NAME = eINSTANCE.getStereoTypeTarget_Name();

		/**
		 * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STEREO_TYPE_TARGET__ATTRIBUTES = eINSTANCE.getStereoTypeTarget_Attributes();

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
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.SourceImpl <em>Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.SourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getSource()
		 * @generated
		 */
		EClass SOURCE = eINSTANCE.getSource();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOURCE__MODEL = eINSTANCE.getSource_Model();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.FaultSourceImpl <em>Fault Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.FaultSourceImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getFaultSource()
		 * @generated
		 */
		EClass FAULT_SOURCE = eINSTANCE.getFaultSource();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FAULT_SOURCE__PROPERTY = eINSTANCE.getFaultSource_Property();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingRuleImpl <em>Rule</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingRuleImpl
		 * @see org.eclipse.vorto.core.api.model.mapping.impl.MappingPackageImpl#getMappingRule()
		 * @generated
		 */
		EClass MAPPING_RULE = eINSTANCE.getMappingRule();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_RULE__TARGET = eINSTANCE.getMappingRule_Target();

		/**
		 * The meta object literal for the '<em><b>Sources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAPPING_RULE__SOURCES = eINSTANCE.getMappingRule_Sources();

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
