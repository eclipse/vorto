/**
 */
package org.eclipse.vorto.core.api.model.functionblock;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockFactory
 * @model kind="package"
 * @generated
 */
public interface FunctionblockPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "functionblock";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/vorto/metamodel/Functionblock";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "functionblock";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FunctionblockPackage eINSTANCE = org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getFunctionblockModel()
	 * @generated
	 */
	int FUNCTIONBLOCK_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__NAME = ModelPackage.MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__NAMESPACE = ModelPackage.MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__VERSION = ModelPackage.MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__REFERENCES = ModelPackage.MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__DESCRIPTION = ModelPackage.MODEL__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Displayname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__DISPLAYNAME = ModelPackage.MODEL__DISPLAYNAME;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__CATEGORY = ModelPackage.MODEL__CATEGORY;

	/**
	 * The feature id for the '<em><b>Functionblock</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK = ModelPackage.MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Entities</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__ENTITIES = ModelPackage.MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Enums</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__ENUMS = ModelPackage.MODEL_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Super Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL__SUPER_TYPE = ModelPackage.MODEL_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_MODEL_FEATURE_COUNT = ModelPackage.MODEL_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl <em>Function Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getFunctionBlock()
	 * @generated
	 */
	int FUNCTION_BLOCK = 1;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK__CONFIGURATION = 0;

	/**
	 * The feature id for the '<em><b>Status</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK__STATUS = 1;

	/**
	 * The feature id for the '<em><b>Fault</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK__FAULT = 2;

	/**
	 * The feature id for the '<em><b>Events</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK__EVENTS = 3;

	/**
	 * The feature id for the '<em><b>Operations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK__OPERATIONS = 4;

	/**
	 * The number of structural features of the '<em>Function Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTION_BLOCK_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ConfigurationImpl <em>Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ConfigurationImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getConfiguration()
	 * @generated
	 */
	int CONFIGURATION = 2;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION__PROPERTIES = 0;

	/**
	 * The number of structural features of the '<em>Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIGURATION_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.StatusImpl <em>Status</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.StatusImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getStatus()
	 * @generated
	 */
	int STATUS = 3;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS__PROPERTIES = 0;

	/**
	 * The number of structural features of the '<em>Status</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATUS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.FaultImpl <em>Fault</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FaultImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getFault()
	 * @generated
	 */
	int FAULT = 4;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT__PROPERTIES = 0;

	/**
	 * The number of structural features of the '<em>Fault</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FAULT_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.OperationImpl <em>Operation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.OperationImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getOperation()
	 * @generated
	 */
	int OPERATION = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Params</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__PARAMS = 1;

	/**
	 * The feature id for the '<em><b>Return Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__RETURN_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Breakable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION__BREAKABLE = 4;

	/**
	 * The number of structural features of the '<em>Operation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ReturnTypeImpl <em>Return Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ReturnTypeImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getReturnType()
	 * @generated
	 */
	int RETURN_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_TYPE__MULTIPLICITY = 0;

	/**
	 * The number of structural features of the '<em>Return Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ReturnObjectTypeImpl <em>Return Object Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ReturnObjectTypeImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getReturnObjectType()
	 * @generated
	 */
	int RETURN_OBJECT_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_OBJECT_TYPE__MULTIPLICITY = RETURN_TYPE__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Return Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_OBJECT_TYPE__RETURN_TYPE = RETURN_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Return Object Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_OBJECT_TYPE_FEATURE_COUNT = RETURN_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ReturnPrimitiveTypeImpl <em>Return Primitive Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ReturnPrimitiveTypeImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getReturnPrimitiveType()
	 * @generated
	 */
	int RETURN_PRIMITIVE_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_PRIMITIVE_TYPE__MULTIPLICITY = RETURN_TYPE__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Return Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_PRIMITIVE_TYPE__RETURN_TYPE = RETURN_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Constraint Rule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE = RETURN_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Return Primitive Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_PRIMITIVE_TYPE_FEATURE_COUNT = RETURN_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ParamImpl <em>Param</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ParamImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getParam()
	 * @generated
	 */
	int PARAM = 11;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM__MULTIPLICITY = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM__NAME = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM__DESCRIPTION = 2;

	/**
	 * The number of structural features of the '<em>Param</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.PrimitiveParamImpl <em>Primitive Param</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.PrimitiveParamImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getPrimitiveParam()
	 * @generated
	 */
	int PRIMITIVE_PARAM = 9;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_PARAM__MULTIPLICITY = PARAM__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_PARAM__NAME = PARAM__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_PARAM__DESCRIPTION = PARAM__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_PARAM__TYPE = PARAM_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Constraint Rule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_PARAM__CONSTRAINT_RULE = PARAM_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Primitive Param</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_PARAM_FEATURE_COUNT = PARAM_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.RefParamImpl <em>Ref Param</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.RefParamImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getRefParam()
	 * @generated
	 */
	int REF_PARAM = 10;

	/**
	 * The feature id for the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REF_PARAM__MULTIPLICITY = PARAM__MULTIPLICITY;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REF_PARAM__NAME = PARAM__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REF_PARAM__DESCRIPTION = PARAM__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REF_PARAM__TYPE = PARAM_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Ref Param</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REF_PARAM_FEATURE_COUNT = PARAM_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.EventImpl <em>Event</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.EventImpl
	 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getEvent()
	 * @generated
	 */
	int EVENT = 12;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT__PROPERTIES = 1;

	/**
	 * The number of structural features of the '<em>Event</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EVENT_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
	 * @generated
	 */
	EClass getFunctionblockModel();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getFunctionblock <em>Functionblock</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Functionblock</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getFunctionblock()
	 * @see #getFunctionblockModel()
	 * @generated
	 */
	EReference getFunctionblockModel_Functionblock();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getEntities <em>Entities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Entities</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getEntities()
	 * @see #getFunctionblockModel()
	 * @generated
	 */
	EReference getFunctionblockModel_Entities();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getEnums <em>Enums</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Enums</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getEnums()
	 * @see #getFunctionblockModel()
	 * @generated
	 */
	EReference getFunctionblockModel_Enums();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getSuperType <em>Super Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Super Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getSuperType()
	 * @see #getFunctionblockModel()
	 * @generated
	 */
	EReference getFunctionblockModel_SuperType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock <em>Function Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Function Block</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionBlock
	 * @generated
	 */
	EClass getFunctionBlock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Configuration</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getConfiguration()
	 * @see #getFunctionBlock()
	 * @generated
	 */
	EReference getFunctionBlock_Configuration();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getStatus <em>Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Status</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getStatus()
	 * @see #getFunctionBlock()
	 * @generated
	 */
	EReference getFunctionBlock_Status();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getFault <em>Fault</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Fault</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getFault()
	 * @see #getFunctionBlock()
	 * @generated
	 */
	EReference getFunctionBlock_Fault();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getEvents <em>Events</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Events</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getEvents()
	 * @see #getFunctionBlock()
	 * @generated
	 */
	EReference getFunctionBlock_Events();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getOperations <em>Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Operations</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getOperations()
	 * @see #getFunctionBlock()
	 * @generated
	 */
	EReference getFunctionBlock_Operations();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.Configuration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Configuration</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Configuration
	 * @generated
	 */
	EClass getConfiguration();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.Configuration#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Configuration#getProperties()
	 * @see #getConfiguration()
	 * @generated
	 */
	EReference getConfiguration_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.Status <em>Status</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Status</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Status
	 * @generated
	 */
	EClass getStatus();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.Status#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Status#getProperties()
	 * @see #getStatus()
	 * @generated
	 */
	EReference getStatus_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.Fault <em>Fault</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fault</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Fault
	 * @generated
	 */
	EClass getFault();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.Fault#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Fault#getProperties()
	 * @see #getFault()
	 * @generated
	 */
	EReference getFault_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.Operation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Operation
	 * @generated
	 */
	EClass getOperation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.Operation#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Operation#getName()
	 * @see #getOperation()
	 * @generated
	 */
	EAttribute getOperation_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.Operation#getParams <em>Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Params</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Operation#getParams()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_Params();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.functionblock.Operation#getReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Return Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Operation#getReturnType()
	 * @see #getOperation()
	 * @generated
	 */
	EReference getOperation_ReturnType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.Operation#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Operation#getDescription()
	 * @see #getOperation()
	 * @generated
	 */
	EAttribute getOperation_Description();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.Operation#isBreakable <em>Breakable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Breakable</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Operation#isBreakable()
	 * @see #getOperation()
	 * @generated
	 */
	EAttribute getOperation_Breakable();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Return Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.ReturnType
	 * @generated
	 */
	EClass getReturnType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnType#isMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Multiplicity</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.ReturnType#isMultiplicity()
	 * @see #getReturnType()
	 * @generated
	 */
	EAttribute getReturnType_Multiplicity();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType <em>Return Object Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Return Object Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType
	 * @generated
	 */
	EClass getReturnObjectType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType#getReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Return Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType#getReturnType()
	 * @see #getReturnObjectType()
	 * @generated
	 */
	EReference getReturnObjectType_ReturnType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType <em>Return Primitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Return Primitive Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType
	 * @generated
	 */
	EClass getReturnPrimitiveType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType#getReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Return Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType#getReturnType()
	 * @see #getReturnPrimitiveType()
	 * @generated
	 */
	EAttribute getReturnPrimitiveType_ReturnType();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType#getConstraintRule <em>Constraint Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Constraint Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType#getConstraintRule()
	 * @see #getReturnPrimitiveType()
	 * @generated
	 */
	EReference getReturnPrimitiveType_ConstraintRule();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam <em>Primitive Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Primitive Param</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam
	 * @generated
	 */
	EClass getPrimitiveParam();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam#getType()
	 * @see #getPrimitiveParam()
	 * @generated
	 */
	EAttribute getPrimitiveParam_Type();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam#getConstraintRule <em>Constraint Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Constraint Rule</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam#getConstraintRule()
	 * @see #getPrimitiveParam()
	 * @generated
	 */
	EReference getPrimitiveParam_ConstraintRule();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.RefParam <em>Ref Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ref Param</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.RefParam
	 * @generated
	 */
	EClass getRefParam();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.functionblock.RefParam#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.RefParam#getType()
	 * @see #getRefParam()
	 * @generated
	 */
	EReference getRefParam_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.Param <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Param</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Param
	 * @generated
	 */
	EClass getParam();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.Param#isMultiplicity <em>Multiplicity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Multiplicity</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Param#isMultiplicity()
	 * @see #getParam()
	 * @generated
	 */
	EAttribute getParam_Multiplicity();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.Param#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Param#getName()
	 * @see #getParam()
	 * @generated
	 */
	EAttribute getParam_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.Param#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Param#getDescription()
	 * @see #getParam()
	 * @generated
	 */
	EAttribute getParam_Description();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.functionblock.Event <em>Event</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Event</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Event
	 * @generated
	 */
	EClass getEvent();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.functionblock.Event#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Event#getName()
	 * @see #getEvent()
	 * @generated
	 */
	EAttribute getEvent_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.functionblock.Event#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.vorto.core.api.model.functionblock.Event#getProperties()
	 * @see #getEvent()
	 * @generated
	 */
	EReference getEvent_Properties();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FunctionblockFactory getFunctionblockFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getFunctionblockModel()
		 * @generated
		 */
		EClass FUNCTIONBLOCK_MODEL = eINSTANCE.getFunctionblockModel();

		/**
		 * The meta object literal for the '<em><b>Functionblock</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK = eINSTANCE.getFunctionblockModel_Functionblock();

		/**
		 * The meta object literal for the '<em><b>Entities</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTIONBLOCK_MODEL__ENTITIES = eINSTANCE.getFunctionblockModel_Entities();

		/**
		 * The meta object literal for the '<em><b>Enums</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTIONBLOCK_MODEL__ENUMS = eINSTANCE.getFunctionblockModel_Enums();

		/**
		 * The meta object literal for the '<em><b>Super Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTIONBLOCK_MODEL__SUPER_TYPE = eINSTANCE.getFunctionblockModel_SuperType();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl <em>Function Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionBlockImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getFunctionBlock()
		 * @generated
		 */
		EClass FUNCTION_BLOCK = eINSTANCE.getFunctionBlock();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK__CONFIGURATION = eINSTANCE.getFunctionBlock_Configuration();

		/**
		 * The meta object literal for the '<em><b>Status</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK__STATUS = eINSTANCE.getFunctionBlock_Status();

		/**
		 * The meta object literal for the '<em><b>Fault</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK__FAULT = eINSTANCE.getFunctionBlock_Fault();

		/**
		 * The meta object literal for the '<em><b>Events</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK__EVENTS = eINSTANCE.getFunctionBlock_Events();

		/**
		 * The meta object literal for the '<em><b>Operations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTION_BLOCK__OPERATIONS = eINSTANCE.getFunctionBlock_Operations();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ConfigurationImpl <em>Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ConfigurationImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getConfiguration()
		 * @generated
		 */
		EClass CONFIGURATION = eINSTANCE.getConfiguration();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIGURATION__PROPERTIES = eINSTANCE.getConfiguration_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.StatusImpl <em>Status</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.StatusImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getStatus()
		 * @generated
		 */
		EClass STATUS = eINSTANCE.getStatus();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATUS__PROPERTIES = eINSTANCE.getStatus_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.FaultImpl <em>Fault</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FaultImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getFault()
		 * @generated
		 */
		EClass FAULT = eINSTANCE.getFault();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FAULT__PROPERTIES = eINSTANCE.getFault_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.OperationImpl <em>Operation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.OperationImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getOperation()
		 * @generated
		 */
		EClass OPERATION = eINSTANCE.getOperation();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION__NAME = eINSTANCE.getOperation_Name();

		/**
		 * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION__PARAMS = eINSTANCE.getOperation_Params();

		/**
		 * The meta object literal for the '<em><b>Return Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION__RETURN_TYPE = eINSTANCE.getOperation_ReturnType();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION__DESCRIPTION = eINSTANCE.getOperation_Description();

		/**
		 * The meta object literal for the '<em><b>Breakable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION__BREAKABLE = eINSTANCE.getOperation_Breakable();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ReturnTypeImpl <em>Return Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ReturnTypeImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getReturnType()
		 * @generated
		 */
		EClass RETURN_TYPE = eINSTANCE.getReturnType();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RETURN_TYPE__MULTIPLICITY = eINSTANCE.getReturnType_Multiplicity();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ReturnObjectTypeImpl <em>Return Object Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ReturnObjectTypeImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getReturnObjectType()
		 * @generated
		 */
		EClass RETURN_OBJECT_TYPE = eINSTANCE.getReturnObjectType();

		/**
		 * The meta object literal for the '<em><b>Return Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RETURN_OBJECT_TYPE__RETURN_TYPE = eINSTANCE.getReturnObjectType_ReturnType();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ReturnPrimitiveTypeImpl <em>Return Primitive Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ReturnPrimitiveTypeImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getReturnPrimitiveType()
		 * @generated
		 */
		EClass RETURN_PRIMITIVE_TYPE = eINSTANCE.getReturnPrimitiveType();

		/**
		 * The meta object literal for the '<em><b>Return Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RETURN_PRIMITIVE_TYPE__RETURN_TYPE = eINSTANCE.getReturnPrimitiveType_ReturnType();

		/**
		 * The meta object literal for the '<em><b>Constraint Rule</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE = eINSTANCE.getReturnPrimitiveType_ConstraintRule();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.PrimitiveParamImpl <em>Primitive Param</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.PrimitiveParamImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getPrimitiveParam()
		 * @generated
		 */
		EClass PRIMITIVE_PARAM = eINSTANCE.getPrimitiveParam();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PRIMITIVE_PARAM__TYPE = eINSTANCE.getPrimitiveParam_Type();

		/**
		 * The meta object literal for the '<em><b>Constraint Rule</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PRIMITIVE_PARAM__CONSTRAINT_RULE = eINSTANCE.getPrimitiveParam_ConstraintRule();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.RefParamImpl <em>Ref Param</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.RefParamImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getRefParam()
		 * @generated
		 */
		EClass REF_PARAM = eINSTANCE.getRefParam();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REF_PARAM__TYPE = eINSTANCE.getRefParam_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.ParamImpl <em>Param</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.ParamImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getParam()
		 * @generated
		 */
		EClass PARAM = eINSTANCE.getParam();

		/**
		 * The meta object literal for the '<em><b>Multiplicity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM__MULTIPLICITY = eINSTANCE.getParam_Multiplicity();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM__NAME = eINSTANCE.getParam_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM__DESCRIPTION = eINSTANCE.getParam_Description();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.functionblock.impl.EventImpl <em>Event</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.EventImpl
		 * @see org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl#getEvent()
		 * @generated
		 */
		EClass EVENT = eINSTANCE.getEvent();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EVENT__NAME = eINSTANCE.getEvent_Name();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EVENT__PROPERTIES = eINSTANCE.getEvent_Properties();

	}

} //FunctionblockPackage
