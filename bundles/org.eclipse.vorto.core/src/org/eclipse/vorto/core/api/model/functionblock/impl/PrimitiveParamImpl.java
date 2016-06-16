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
import org.eclipse.vorto.core.api.model.functionblock.PrimitiveParam;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Primitive Param</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.PrimitiveParamImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.PrimitiveParamImpl#getConstraintRule <em>Constraint Rule</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PrimitiveParamImpl extends ParamImpl implements PrimitiveParam {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final PrimitiveType TYPE_EDEFAULT = PrimitiveType.STRING;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected PrimitiveType type = TYPE_EDEFAULT;

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
	protected PrimitiveParamImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FunctionblockPackage.Literals.PRIMITIVE_PARAM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PrimitiveType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(PrimitiveType newType) {
		PrimitiveType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.PRIMITIVE_PARAM__TYPE, oldType, type));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE, oldConstraintRule, newConstraintRule);
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
				msgs = ((InternalEObject)constraintRule).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE, null, msgs);
			if (newConstraintRule != null)
				msgs = ((InternalEObject)newConstraintRule).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE, null, msgs);
			msgs = basicSetConstraintRule(newConstraintRule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE, newConstraintRule, newConstraintRule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE:
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
			case FunctionblockPackage.PRIMITIVE_PARAM__TYPE:
				return getType();
			case FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE:
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
			case FunctionblockPackage.PRIMITIVE_PARAM__TYPE:
				setType((PrimitiveType)newValue);
				return;
			case FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE:
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
			case FunctionblockPackage.PRIMITIVE_PARAM__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE:
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
			case FunctionblockPackage.PRIMITIVE_PARAM__TYPE:
				return type != TYPE_EDEFAULT;
			case FunctionblockPackage.PRIMITIVE_PARAM__CONSTRAINT_RULE:
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
		result.append(" (type: "); //$NON-NLS-1$
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //PrimitiveParamImpl
