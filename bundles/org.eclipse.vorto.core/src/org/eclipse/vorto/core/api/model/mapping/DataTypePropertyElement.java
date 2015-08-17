/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.datatype.Type;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Type Property Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement#getTypeRef <em>Type Ref</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement#getAttribute <em>Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypePropertyElement()
 * @model
 * @generated
 */
public interface DataTypePropertyElement extends DataTypeSourceElement {
	/**
	 * Returns the value of the '<em><b>Type Ref</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Ref</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Ref</em>' reference.
	 * @see #setTypeRef(Type)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypePropertyElement_TypeRef()
	 * @model
	 * @generated
	 */
	Type getTypeRef();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement#getTypeRef <em>Type Ref</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Ref</em>' reference.
	 * @see #getTypeRef()
	 * @generated
	 */
	void setTypeRef(Type value);

	/**
	 * Returns the value of the '<em><b>Attribute</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Attribute</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Attribute</em>' containment reference.
	 * @see #setAttribute(DataTypeAttribute)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypePropertyElement_Attribute()
	 * @model containment="true"
	 * @generated
	 */
	DataTypeAttribute getAttribute();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement#getAttribute <em>Attribute</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Attribute</em>' containment reference.
	 * @see #getAttribute()
	 * @generated
	 */
	void setAttribute(DataTypeAttribute value);

} // DataTypePropertyElement
