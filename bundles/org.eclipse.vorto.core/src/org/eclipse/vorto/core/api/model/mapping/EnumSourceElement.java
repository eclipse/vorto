/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Source Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EnumSourceElement#getTypeRef <em>Type Ref</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumSourceElement()
 * @model
 * @generated
 */
public interface EnumSourceElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Ref</em>' reference.
	 * @see #setTypeRef(org.eclipse.vorto.core.api.model.datatype.Enum)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumSourceElement_TypeRef()
	 * @model
	 * @generated
	 */
	org.eclipse.vorto.core.api.model.datatype.Enum getTypeRef();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EnumSourceElement#getTypeRef <em>Type Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Ref</em>' reference.
	 * @see #getTypeRef()
	 * @generated
	 */
	void setTypeRef(org.eclipse.vorto.core.api.model.datatype.Enum value);

} // EnumSourceElement
