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
package org.eclipse.vorto.core.api.model.functionblock.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.vorto.core.api.model.datatype.ConstraintRule;
import org.eclipse.vorto.core.api.model.datatype.PrimitiveType;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;
import org.eclipse.vorto.core.api.model.functionblock.ReturnPrimitiveType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Return Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.ReturnPrimitiveTypeImpl#getReturnType <em>Return Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.ReturnPrimitiveTypeImpl#getConstraintRule <em>Constraint Rule</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReturnPrimitiveTypeImpl extends ReturnTypeImpl implements ReturnPrimitiveType {
	/**
	 * The default value of the '{@link #getReturnType() <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnType()
	 * @generated
	 * @ordered
	 */
	protected static final PrimitiveType RETURN_TYPE_EDEFAULT = PrimitiveType.STRING;

	/**
	 * The cached value of the '{@link #getReturnType() <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnType()
	 * @generated
	 * @ordered
	 */
	protected PrimitiveType returnType = RETURN_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConstraintRule() <em>Constraint Rule</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraintRule()
	 * @generated
	 * @ordered
	 */
	protected ConstraintRule constraintRule;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReturnPrimitiveTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FunctionblockPackage.Literals.RETURN_PRIMITIVE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PrimitiveType getReturnType() {
		return returnType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReturnType(PrimitiveType newReturnType) {
		PrimitiveType oldReturnType = returnType;
		returnType = newReturnType == null ? RETURN_TYPE_EDEFAULT : newReturnType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.RETURN_PRIMITIVE_TYPE__RETURN_TYPE, oldReturnType, returnType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintRule getConstraintRule() {
		return constraintRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConstraintRule(ConstraintRule newConstraintRule, NotificationChain msgs) {
		ConstraintRule oldConstraintRule = constraintRule;
		constraintRule = newConstraintRule;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE, oldConstraintRule, newConstraintRule);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstraintRule(ConstraintRule newConstraintRule) {
		if (newConstraintRule != constraintRule) {
			NotificationChain msgs = null;
			if (constraintRule != null)
				msgs = ((InternalEObject)constraintRule).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE, null, msgs);
			if (newConstraintRule != null)
				msgs = ((InternalEObject)newConstraintRule).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE, null, msgs);
			msgs = basicSetConstraintRule(newConstraintRule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE, newConstraintRule, newConstraintRule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE:
				return basicSetConstraintRule(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__RETURN_TYPE:
				return getReturnType();
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE:
				return getConstraintRule();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__RETURN_TYPE:
				setReturnType((PrimitiveType)newValue);
				return;
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE:
				setConstraintRule((ConstraintRule)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__RETURN_TYPE:
				setReturnType(RETURN_TYPE_EDEFAULT);
				return;
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE:
				setConstraintRule((ConstraintRule)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__RETURN_TYPE:
				return returnType != RETURN_TYPE_EDEFAULT;
			case FunctionblockPackage.RETURN_PRIMITIVE_TYPE__CONSTRAINT_RULE:
				return constraintRule != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (returnType: "); //$NON-NLS-1$
		result.append(returnType);
		result.append(')');
		return result.toString();
	}

} //ReturnPrimitiveTypeImpl
