/**
 */
package org.eclipse.vorto.core.api.model.mapping;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.ConfigurationElement#getTypeRef <em>Type Ref</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getConfigurationElement()
 * @model
 * @generated
 */
public interface ConfigurationElement extends FunctionBlockChildElement {

	/**
	 * Returns the value of the '<em><b>Type Ref</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Ref</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Ref</em>' containment reference.
	 * @see #setTypeRef(FBTypeElement)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getConfigurationElement_TypeRef()
	 * @model containment="true"
	 * @generated
	 */
	FBTypeElement getTypeRef();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.ConfigurationElement#getTypeRef <em>Type Ref</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Ref</em>' containment reference.
	 * @see #getTypeRef()
	 * @generated
	 */
	void setTypeRef(FBTypeElement value);
} // ConfigurationElement
