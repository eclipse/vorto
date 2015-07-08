/**
 */
package org.eclipse.vorto.core.api.model.datatype;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Entity#getSuperType <em>Super Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Entity#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getEntity()
 * @model
 * @generated
 */
public interface Entity extends Type {
	/**
	 * Returns the value of the '<em><b>Super Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super Type</em>' reference.
	 * @see #setSuperType(Entity)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getEntity_SuperType()
	 * @model
	 * @generated
	 */
	Entity getSuperType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Entity#getSuperType <em>Super Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Super Type</em>' reference.
	 * @see #getSuperType()
	 * @generated
	 */
	void setSuperType(Entity value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.datatype.Property}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getEntity_Properties()
	 * @model containment="true"
	 * @generated
	 */
	EList<Property> getProperties();

} // Entity
