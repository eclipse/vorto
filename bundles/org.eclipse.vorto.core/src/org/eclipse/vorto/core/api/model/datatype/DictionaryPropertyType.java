/**
 */
package org.eclipse.vorto.core.api.model.datatype;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dictionary Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType#getKeyType <em>Key Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType#getValueType <em>Value Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getDictionaryPropertyType()
 * @model
 * @generated
 */
public interface DictionaryPropertyType extends ComplexPrimitivePropertyType {
	/**
	 * Returns the value of the '<em><b>Key Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.datatype.PrimitiveType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see #setKeyType(PrimitiveType)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getDictionaryPropertyType_KeyType()
	 * @model
	 * @generated
	 */
	PrimitiveType getKeyType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType#getKeyType <em>Key Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see #getKeyType()
	 * @generated
	 */
	void setKeyType(PrimitiveType value);

	/**
	 * Returns the value of the '<em><b>Value Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.datatype.PrimitiveType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see #setValueType(PrimitiveType)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getDictionaryPropertyType_ValueType()
	 * @model
	 * @generated
	 */
	PrimitiveType getValueType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType#getValueType <em>Value Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.PrimitiveType
	 * @see #getValueType()
	 * @generated
	 */
	void setValueType(PrimitiveType value);

} // DictionaryPropertyType
