/**
 */
package org.eclipse.vorto.core.api.model.mapping;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Type Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.DataTypeReference#getReference <em>Reference</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypeReference()
 * @model
 * @generated
 */
public interface DataTypeReference extends FunctionBlockTargetElement, EntityTargetElement {
	/**
	 * Returns the value of the '<em><b>Reference</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' reference.
	 * @see #setReference(DataTypeMapping)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypeReference_Reference()
	 * @model
	 * @generated
	 */
	DataTypeMapping getReference();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeReference#getReference <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(DataTypeMapping value);

} // DataTypeReference
