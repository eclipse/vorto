/**
 */
package org.eclipse.vorto.core.api.model.functionblock;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage
 * @generated
 */
public interface FunctionblockFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FunctionblockFactory eINSTANCE = org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	FunctionblockModel createFunctionblockModel();

	/**
	 * Returns a new object of class '<em>Function Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block</em>'.
	 * @generated
	 */
	FunctionBlock createFunctionBlock();

	/**
	 * Returns a new object of class '<em>Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Configuration</em>'.
	 * @generated
	 */
	Configuration createConfiguration();

	/**
	 * Returns a new object of class '<em>Status</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Status</em>'.
	 * @generated
	 */
	Status createStatus();

	/**
	 * Returns a new object of class '<em>Fault</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fault</em>'.
	 * @generated
	 */
	Fault createFault();

	/**
	 * Returns a new object of class '<em>Operation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation</em>'.
	 * @generated
	 */
	Operation createOperation();

	/**
	 * Returns a new object of class '<em>Return Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Return Type</em>'.
	 * @generated
	 */
	ReturnType createReturnType();

	/**
	 * Returns a new object of class '<em>Return Object Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Return Object Type</em>'.
	 * @generated
	 */
	ReturnObjectType createReturnObjectType();

	/**
	 * Returns a new object of class '<em>Return Primitive Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Return Primitive Type</em>'.
	 * @generated
	 */
	ReturnPrimitiveType createReturnPrimitiveType();

	/**
	 * Returns a new object of class '<em>Primitive Param</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Primitive Param</em>'.
	 * @generated
	 */
	PrimitiveParam createPrimitiveParam();

	/**
	 * Returns a new object of class '<em>Ref Param</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ref Param</em>'.
	 * @generated
	 */
	RefParam createRefParam();

	/**
	 * Returns a new object of class '<em>Param</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Param</em>'.
	 * @generated
	 */
	Param createParam();

	/**
	 * Returns a new object of class '<em>Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Event</em>'.
	 * @generated
	 */
	Event createEvent();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	FunctionblockPackage getFunctionblockPackage();

} //FunctionblockFactory
