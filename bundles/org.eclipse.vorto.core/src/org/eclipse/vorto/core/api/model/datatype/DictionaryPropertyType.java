/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
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
	 * Returns the value of the '<em><b>Key Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Type</em>' containment reference.
	 * @see #setKeyType(PropertyType)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getDictionaryPropertyType_KeyType()
	 * @model containment="true"
	 * @generated
	 */
	PropertyType getKeyType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType#getKeyType <em>Key Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Type</em>' containment reference.
	 * @see #getKeyType()
	 * @generated
	 */
	void setKeyType(PropertyType value);

	/**
	 * Returns the value of the '<em><b>Value Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Type</em>' containment reference.
	 * @see #setValueType(PropertyType)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getDictionaryPropertyType_ValueType()
	 * @model containment="true"
	 * @generated
	 */
	PropertyType getValueType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.DictionaryPropertyType#getValueType <em>Value Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Type</em>' containment reference.
	 * @see #getValueType()
	 * @generated
	 */
	void setValueType(PropertyType value);

} // DictionaryPropertyType
