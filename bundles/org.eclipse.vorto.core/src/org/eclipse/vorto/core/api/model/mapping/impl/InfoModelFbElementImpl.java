/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;

import org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Info Model Fb Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelFbElementImpl#getFunctionblock <em>Functionblock</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InfoModelFbElementImpl extends InfoModelChildImpl implements InfoModelFbElement {
	/**
	 * The cached value of the '{@link #getFunctionblock() <em>Functionblock</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionblock()
	 * @generated
	 * @ordered
	 */
	protected FunctionblockProperty functionblock;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InfoModelFbElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.INFO_MODEL_FB_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockProperty getFunctionblock() {
		if (functionblock != null && functionblock.eIsProxy()) {
			InternalEObject oldFunctionblock = (InternalEObject)functionblock;
			functionblock = (FunctionblockProperty)eResolveProxy(oldFunctionblock);
			if (functionblock != oldFunctionblock) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingPackage.INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK, oldFunctionblock, functionblock));
			}
		}
		return functionblock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockProperty basicGetFunctionblock() {
		return functionblock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunctionblock(FunctionblockProperty newFunctionblock) {
		FunctionblockProperty oldFunctionblock = functionblock;
		functionblock = newFunctionblock;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK, oldFunctionblock, functionblock));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK:
				if (resolve) return getFunctionblock();
				return basicGetFunctionblock();
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
			case MappingPackage.INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK:
				setFunctionblock((FunctionblockProperty)newValue);
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
			case MappingPackage.INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK:
				setFunctionblock((FunctionblockProperty)null);
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
			case MappingPackage.INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK:
				return functionblock != null;
		}
		return super.eIsSet(featureID);
	}

} //InfoModelFbElementImpl
