/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.datatype.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FB Type Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FBTypeProperty#getProperty <em>Property</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFBTypeProperty()
 * @model
 * @generated
 */
public interface FBTypeProperty extends FBTypeElementChild {
	/**
	 * Returns the value of the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' reference.
	 * @see #setProperty(Property)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFBTypeProperty_Property()
	 * @model
	 * @generated
	 */
	Property getProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeProperty#getProperty <em>Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' reference.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(Property value);

} // FBTypeProperty
