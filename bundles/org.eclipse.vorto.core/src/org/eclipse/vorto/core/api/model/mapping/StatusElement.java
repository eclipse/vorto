/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.datatype.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Status Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.StatusElement#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getStatusElement()
 * @model
 * @generated
 */
public interface StatusElement extends FunctionBlockElement {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' reference.
	 * @see #setValue(Property)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getStatusElement_Value()
	 * @model
	 * @generated
	 */
	Property getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.StatusElement#getValue <em>Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Property value);

} // StatusElement
