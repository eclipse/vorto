/**
 */
package org.eclipse.vorto.core.api.model.functionblock;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Return Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.ReturnType#isMultiplicity <em>Multiplicity</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getReturnType()
 * @model
 * @generated
 */
public interface ReturnType extends EObject {
	/**
	 * Returns the value of the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplicity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiplicity</em>' attribute.
	 * @see #setMultiplicity(boolean)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getReturnType_Multiplicity()
	 * @model
	 * @generated
	 */
	boolean isMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.ReturnType#isMultiplicity <em>Multiplicity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplicity</em>' attribute.
	 * @see #isMultiplicity()
	 * @generated
	 */
	void setMultiplicity(boolean value);

} // ReturnType
