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
 * A representation of the model object '<em><b>Enum Literal Property Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getEnumLiteralPropertyAttribute()
 * @model
 * @generated
 */
public interface EnumLiteralPropertyAttribute extends PropertyAttribute {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType
	 * @see #setType(EnumLiteralPropertyAttributeType)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getEnumLiteralPropertyAttribute_Type()
	 * @model
	 * @generated
	 */
	EnumLiteralPropertyAttributeType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType
	 * @see #getType()
	 * @generated
	 */
	void setType(EnumLiteralPropertyAttributeType value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' reference.
	 * @see #setValue(EnumLiteral)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getEnumLiteralPropertyAttribute_Value()
	 * @model
	 * @generated
	 */
	EnumLiteral getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute#getValue <em>Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(EnumLiteral value);

} // EnumLiteralPropertyAttribute
