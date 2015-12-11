/**
 */
package org.eclipse.vorto.core.api.model.mapping;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Attribute Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource#getAttribute <em>Attribute</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumAttributeSource()
 * @model
 * @generated
 */
public interface EnumAttributeSource extends EnumSource {
	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.mapping.ModelAttribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.mapping.ModelAttribute
	 * @see #setAttribute(ModelAttribute)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumAttributeSource_Attribute()
	 * @model
	 * @generated
	 */
	ModelAttribute getAttribute();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource#getAttribute <em>Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.mapping.ModelAttribute
	 * @see #getAttribute()
	 * @generated
	 */
	void setAttribute(ModelAttribute value);

} // EnumAttributeSource
