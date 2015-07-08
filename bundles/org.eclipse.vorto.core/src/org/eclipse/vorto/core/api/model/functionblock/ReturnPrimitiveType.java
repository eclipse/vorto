/**
 */
package org.eclipse.vorto.core.api.model.functionblock;

import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Return Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType#getReturnType <em>Return Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getReturnPrimitiveType()
 * @model
 * @generated
 */
public interface ReturnPrimitiveType extends ReturnType {
	/**
	 * Returns the value of the '<em><b>Return Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.datatype.PrimitiveType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see #setReturnType(PrimitiveType)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getReturnPrimitiveType_ReturnType()
	 * @model
	 * @generated
	 */
	PrimitiveType getReturnType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType#getReturnType <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see #getReturnType()
	 * @generated
	 */
	void setReturnType(PrimitiveType value);

} // ReturnPrimitiveType
