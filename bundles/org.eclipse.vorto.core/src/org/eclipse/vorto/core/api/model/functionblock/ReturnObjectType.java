/**
 */
package org.eclipse.vorto.core.api.model.functionblock;

import org.eclipse.vorto.core.api.model.datatype.Type;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Return Object Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType#getReturnType <em>Return Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getReturnObjectType()
 * @model
 * @generated
 */
public interface ReturnObjectType extends ReturnType {
	/**
	 * Returns the value of the '<em><b>Return Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Type</em>' reference.
	 * @see #setReturnType(Type)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getReturnObjectType_ReturnType()
	 * @model
	 * @generated
	 */
	Type getReturnType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnObjectType#getReturnType <em>Return Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Type</em>' reference.
	 * @see #getReturnType()
	 * @generated
	 */
	void setReturnType(Type value);

} // ReturnObjectType
