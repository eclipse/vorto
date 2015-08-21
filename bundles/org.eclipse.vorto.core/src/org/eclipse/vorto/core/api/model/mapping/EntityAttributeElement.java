/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.datatype.Entity;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity Attribute Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EntityAttributeElement#getTypeRef <em>Type Ref</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EntityAttributeElement#getAttribute <em>Attribute</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityAttributeElement()
 * @model
 * @generated
 */
public interface EntityAttributeElement extends EntitySourceElement {
	/**
	 * Returns the value of the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Ref</em>' reference.
	 * @see #setTypeRef(Entity)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityAttributeElement_TypeRef()
	 * @model
	 * @generated
	 */
	Entity getTypeRef();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EntityAttributeElement#getTypeRef <em>Type Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Ref</em>' reference.
	 * @see #getTypeRef()
	 * @generated
	 */
	void setTypeRef(Entity value);

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
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityAttributeElement_Attribute()
	 * @model
	 * @generated
	 */
	ModelAttribute getAttribute();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EntityAttributeElement#getAttribute <em>Attribute</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.mapping.ModelAttribute
	 * @see #getAttribute()
	 * @generated
	 */
	void setAttribute(ModelAttribute value);

} // EntityAttributeElement
