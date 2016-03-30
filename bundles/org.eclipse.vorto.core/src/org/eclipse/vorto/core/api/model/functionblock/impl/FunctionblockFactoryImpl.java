/**
 */
package org.eclipse.vorto.core.api.model.functionblock.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.vorto.core.api.model.functionblock.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FunctionblockFactoryImpl extends EFactoryImpl implements FunctionblockFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FunctionblockFactory init() {
		try {
			FunctionblockFactory theFunctionblockFactory = (FunctionblockFactory)EPackage.Registry.INSTANCE.getEFactory(FunctionblockPackage.eNS_URI);
			if (theFunctionblockFactory != null) {
				return theFunctionblockFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new FunctionblockFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockFactoryImpl() {
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
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL: return createFunctionblockModel();
			case FunctionblockPackage.FUNCTION_BLOCK: return createFunctionBlock();
			case FunctionblockPackage.CONFIGURATION: return createConfiguration();
			case FunctionblockPackage.STATUS: return createStatus();
			case FunctionblockPackage.FAULT: return createFault();
			case FunctionblockPackage.OPERATION: return createOperation();
			case FunctionblockPackage.RETURN_TYPE: return createReturnType();
			case FunctionblockPackage.RETURN_OBJECT_TYPE: return createReturnObjectType();
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE: return createReturnPrimitiveType();
			case FunctionblockPackage.PRIMITIVE_PARAM: return createPrimitiveParam();
			case FunctionblockPackage.REF_PARAM: return createRefParam();
			case FunctionblockPackage.PARAM: return createParam();
			case FunctionblockPackage.EVENT: return createEvent();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockModel createFunctionblockModel() {
		FunctionblockModelImpl functionblockModel = new FunctionblockModelImpl();
		return functionblockModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlock createFunctionBlock() {
		FunctionBlockImpl functionBlock = new FunctionBlockImpl();
		return functionBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Configuration createConfiguration() {
		ConfigurationImpl configuration = new ConfigurationImpl();
		return configuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Status createStatus() {
		StatusImpl status = new StatusImpl();
		return status;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Fault createFault() {
		FaultImpl fault = new FaultImpl();
		return fault;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Operation createOperation() {
		OperationImpl operation = new OperationImpl();
		return operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReturnType createReturnType() {
		ReturnTypeImpl returnType = new ReturnTypeImpl();
		return returnType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReturnObjectType createReturnObjectType() {
		ReturnObjectTypeImpl returnObjectType = new ReturnObjectTypeImpl();
		return returnObjectType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ReturnPrimitiveType createReturnPrimitiveType() {
		ReturnPrimitiveTypeImpl returnPrimitiveType = new ReturnPrimitiveTypeImpl();
		return returnPrimitiveType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PrimitiveParam createPrimitiveParam() {
		PrimitiveParamImpl primitiveParam = new PrimitiveParamImpl();
		return primitiveParam;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RefParam createRefParam() {
		RefParamImpl refParam = new RefParamImpl();
		return refParam;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Param createParam() {
		ParamImpl param = new ParamImpl();
		return param;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event createEvent() {
		EventImpl event = new EventImpl();
		return event;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockPackage getFunctionblockPackage() {
		return (FunctionblockPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static FunctionblockPackage getPackage() {
		return FunctionblockPackage.eINSTANCE;
	}

} //FunctionblockFactoryImpl
