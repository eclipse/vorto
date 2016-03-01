/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Info Model Property Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource#getProperty <em>Property</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelPropertySource()
 * @model
 * @generated
 */
public interface InfoModelPropertySource extends InfomodelSource {
	/**
	 * Returns the value of the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' reference.
	 * @see #setProperty(FunctionblockProperty)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelPropertySource_Property()
	 * @model
	 * @generated
	 */
	FunctionblockProperty getProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource#getProperty <em>Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' reference.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(FunctionblockProperty value);

} // InfoModelPropertySource
