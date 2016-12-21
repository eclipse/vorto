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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getStatus <em>Status</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getFault <em>Fault</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getEvents <em>Events</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getOperations <em>Operations</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionBlock()
 * @model
 * @generated
 */
public interface FunctionBlock extends EObject {
	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configuration</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Configuration</em>' containment reference.
	 * @see #setConfiguration(Configuration)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionBlock_Configuration()
	 * @model containment="true"
	 * @generated
	 */
	Configuration getConfiguration();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getConfiguration <em>Configuration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Configuration</em>' containment reference.
	 * @see #getConfiguration()
	 * @generated
	 */
	void setConfiguration(Configuration value);

	/**
	 * Returns the value of the '<em><b>Status</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Status</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' containment reference.
	 * @see #setStatus(Status)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionBlock_Status()
	 * @model containment="true"
	 * @generated
	 */
	Status getStatus();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getStatus <em>Status</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status</em>' containment reference.
	 * @see #getStatus()
	 * @generated
	 */
	void setStatus(Status value);

	/**
	 * Returns the value of the '<em><b>Fault</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fault</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fault</em>' containment reference.
	 * @see #setFault(Fault)
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionBlock_Fault()
	 * @model containment="true"
	 * @generated
	 */
	Fault getFault();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.functionblock.FunctionBlock#getFault <em>Fault</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fault</em>' containment reference.
	 * @see #getFault()
	 * @generated
	 */
	void setFault(Fault value);

	/**
	 * Returns the value of the '<em><b>Events</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.functionblock.Event}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Events</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionBlock_Events()
	 * @model containment="true"
	 * @generated
	 */
	EList<Event> getEvents();

	/**
	 * Returns the value of the '<em><b>Operations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.functionblock.Operation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Operations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Operations</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage#getFunctionBlock_Operations()
	 * @model containment="true"
	 * @generated
	 */
	EList<Operation> getOperations();

} // FunctionBlock
