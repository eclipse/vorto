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
package org.eclipse.vorto.core.api.model.functionblock;

import org.eclipse.emf.common.util.EList;

import org.eclipse.vorto.core.api.model.datatype.Entity;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getFunctionblock <em>Functionblock</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getEntities <em>Entities</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getEnums <em>Enums</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getSuperType <em>Super Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel()
 * @model
 * @generated
 */
public interface FunctionblockModel extends Model {
	/**
	 * Returns the value of the '<em><b>Functionblock</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Functionblock</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Functionblock</em>' containment reference.
	 * @see #setFunctionblock(FunctionBlock)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel_Functionblock()
	 * @model containment="true"
	 * @generated
	 */
	FunctionBlock getFunctionblock();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getFunctionblock <em>Functionblock</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Functionblock</em>' containment reference.
	 * @see #getFunctionblock()
	 * @generated
	 */
	void setFunctionblock(FunctionBlock value);

	/**
	 * Returns the value of the '<em><b>Entities</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.datatype.Entity}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entities</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entities</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel_Entities()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entity> getEntities();

	/**
	 * Returns the value of the '<em><b>Enums</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.datatype.Enum}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enums</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enums</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel_Enums()
	 * @model containment="true"
	 * @generated
	 */
	EList<org.eclipse.vorto.core.api.model.datatype.Enum> getEnums();

	/**
	 * Returns the value of the '<em><b>Super Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super Type</em>' reference.
	 * @see #setSuperType(FunctionblockModel)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionblockModel_SuperType()
	 * @model
	 * @generated
	 */
	FunctionblockModel getSuperType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel#getSuperType <em>Super Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Super Type</em>' reference.
	 * @see #getSuperType()
	 * @generated
	 */
	void setSuperType(FunctionblockModel value);

} // FunctionblockModel
