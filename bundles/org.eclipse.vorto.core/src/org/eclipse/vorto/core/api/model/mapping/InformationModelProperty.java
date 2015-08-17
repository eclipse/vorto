/**
 */
package org.eclipse.vorto.core.api.model.mapping;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Information Model Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InformationModelProperty#getAttribute <em>Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInformationModelProperty()
 * @model
 * @generated
 */
public interface InformationModelProperty extends InfoModelChild {
	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute
	 * @see #setAttribute(InfoModelAttribute)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInformationModelProperty_Attribute()
	 * @model
	 * @generated
	 */
	InfoModelAttribute getAttribute();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelProperty#getAttribute <em>Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute
	 * @see #getAttribute()
	 * @generated
	 */
	void setAttribute(InfoModelAttribute value);

} // InformationModelProperty
