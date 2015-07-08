/**
 */
package org.eclipse.vorto.core.api.model.datatype;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getObjectPropertyType()
 * @model
 * @generated
 */
public interface ObjectPropertyType extends PropertyType {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(Type)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getObjectPropertyType_Type()
	 * @model
	 * @generated
	 */
	Type getType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.ObjectPropertyType#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(Type value);

} // ObjectPropertyType
