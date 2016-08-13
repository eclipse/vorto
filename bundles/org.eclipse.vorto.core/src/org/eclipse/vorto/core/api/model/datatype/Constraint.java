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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Constraint#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Constraint#getConstraintValues <em>Constraint Values</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getConstraint()
 * @model
 * @generated
 */
public interface Constraint extends EObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
	 * @see #setType(ConstraintIntervalType)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getConstraint_Type()
	 * @model
	 * @generated
	 */
	ConstraintIntervalType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Constraint#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.vorto.core.api.model.datatype.ConstraintIntervalType
	 * @see #getType()
	 * @generated
	 */
	void setType(ConstraintIntervalType value);

	/**
	 * Returns the value of the '<em><b>Constraint Values</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraint Values</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraint Values</em>' attribute.
	 * @see #setConstraintValues(String)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getConstraint_ConstraintValues()
	 * @model
	 * @generated
	 */
	String getConstraintValues();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Constraint#getConstraintValues <em>Constraint Values</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint Values</em>' attribute.
	 * @see #getConstraintValues()
	 * @generated
	 */
	void setConstraintValues(String value);

} // Constraint
