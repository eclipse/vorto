/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.vorto.core.api.model.datatype.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FB Type Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElement#getProperty <em>Property</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElement#getChild <em>Child</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFBTypeElement()
 * @model
 * @generated
 */
public interface FBTypeElement extends EObject {
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
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFBTypeElement_Property()
	 * @model
	 * @generated
	 */
	Property getProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElement#getProperty <em>Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' reference.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(Property value);

	/**
	 * Returns the value of the '<em><b>Child</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child</em>' containment reference.
	 * @see #setChild(FBTypeElementChild)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFBTypeElement_Child()
	 * @model containment="true"
	 * @generated
	 */
	FBTypeElementChild getChild();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.FBTypeElement#getChild <em>Child</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child</em>' containment reference.
	 * @see #getChild()
	 * @generated
	 */
	void setChild(FBTypeElementChild value);

} // FBTypeElement
