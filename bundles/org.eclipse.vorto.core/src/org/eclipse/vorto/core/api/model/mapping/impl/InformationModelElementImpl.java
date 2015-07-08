/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.InformationModelElement;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Information Model Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.InformationModelElementImpl#getFunctionblockModel <em>Functionblock Model</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.InformationModelElementImpl#getTail <em>Tail</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InformationModelElementImpl extends MinimalEObjectImpl.Container implements InformationModelElement {
	/**
	 * The cached value of the '{@link #getFunctionblockModel() <em>Functionblock Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionblockModel()
	 * @generated
	 * @ordered
	 */
	protected FunctionblockModel functionblockModel;

	/**
	 * The cached value of the '{@link #getTail() <em>Tail</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTail()
	 * @generated
	 * @ordered
	 */
	protected FunctionBlockElement tail;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InformationModelElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.INFORMATION_MODEL_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockModel getFunctionblockModel() {
		if (functionblockModel != null && functionblockModel.eIsProxy()) {
			InternalEObject oldFunctionblockModel = (InternalEObject)functionblockModel;
			functionblockModel = (FunctionblockModel)eResolveProxy(oldFunctionblockModel);
			if (functionblockModel != oldFunctionblockModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingPackage.INFORMATION_MODEL_ELEMENT__FUNCTIONBLOCK_MODEL, oldFunctionblockModel, functionblockModel));
			}
		}
		return functionblockModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockModel basicGetFunctionblockModel() {
		return functionblockModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunctionblockModel(FunctionblockModel newFunctionblockModel) {
		FunctionblockModel oldFunctionblockModel = functionblockModel;
		functionblockModel = newFunctionblockModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.INFORMATION_MODEL_ELEMENT__FUNCTIONBLOCK_MODEL, oldFunctionblockModel, functionblockModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlockElement getTail() {
		return tail;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTail(FunctionBlockElement newTail, NotificationChain msgs) {
		FunctionBlockElement oldTail = tail;
		tail = newTail;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL, oldTail, newTail);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTail(FunctionBlockElement newTail) {
		if (newTail != tail) {
			NotificationChain msgs = null;
			if (tail != null)
				msgs = ((InternalEObject)tail).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL, null, msgs);
			if (newTail != null)
				msgs = ((InternalEObject)newTail).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL, null, msgs);
			msgs = basicSetTail(newTail, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL, newTail, newTail));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL:
				return basicSetTail(null, msgs);
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
			case MappingPackage.INFORMATION_MODEL_ELEMENT__FUNCTIONBLOCK_MODEL:
				if (resolve) return getFunctionblockModel();
				return basicGetFunctionblockModel();
			case MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL:
				return getTail();
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
			case MappingPackage.INFORMATION_MODEL_ELEMENT__FUNCTIONBLOCK_MODEL:
				setFunctionblockModel((FunctionblockModel)newValue);
				return;
			case MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL:
				setTail((FunctionBlockElement)newValue);
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
			case MappingPackage.INFORMATION_MODEL_ELEMENT__FUNCTIONBLOCK_MODEL:
				setFunctionblockModel((FunctionblockModel)null);
				return;
			case MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL:
				setTail((FunctionBlockElement)null);
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
			case MappingPackage.INFORMATION_MODEL_ELEMENT__FUNCTIONBLOCK_MODEL:
				return functionblockModel != null;
			case MappingPackage.INFORMATION_MODEL_ELEMENT__TAIL:
				return tail != null;
		}
		return super.eIsSet(featureID);
	}

} //InformationModelElementImpl
