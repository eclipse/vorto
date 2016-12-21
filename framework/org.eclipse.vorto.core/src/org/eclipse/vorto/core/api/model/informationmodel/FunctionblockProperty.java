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
package org.eclipse.vorto.core.api.model.informationmodel;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Functionblock Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getType <em>Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getFunctionblockProperty()
 * @model
 * @generated
 */
public interface FunctionblockProperty extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getFunctionblockProperty_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getFunctionblockProperty_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(FunctionblockModel)
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getFunctionblockProperty_Type()
	 * @model
	 * @generated
	 */
	FunctionblockModel getType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(FunctionblockModel value);

} // FunctionblockProperty
