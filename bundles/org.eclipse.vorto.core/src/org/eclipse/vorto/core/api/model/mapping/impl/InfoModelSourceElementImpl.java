/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

import org.eclipse.vorto.core.api.model.mapping.InfoModelChild;
import org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Info Model Source Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelSourceElementImpl#getInfoModel <em>Info Model</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelSourceElementImpl#getInfoModelChild <em>Info Model Child</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InfoModelSourceElementImpl extends MinimalEObjectImpl.Container implements InfoModelSourceElement {
	/**
	 * The cached value of the '{@link #getInfoModel() <em>Info Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInfoModel()
	 * @generated
	 * @ordered
	 */
	protected InformationModel infoModel;

	/**
	 * The cached value of the '{@link #getInfoModelChild() <em>Info Model Child</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInfoModelChild()
	 * @generated
	 * @ordered
	 */
	protected InfoModelChild infoModelChild;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InfoModelSourceElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.INFO_MODEL_SOURCE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModel getInfoModel() {
		if (infoModel != null && infoModel.eIsProxy()) {
			InternalEObject oldInfoModel = (InternalEObject)infoModel;
			infoModel = (InformationModel)eResolveProxy(oldInfoModel);
			if (infoModel != oldInfoModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL, oldInfoModel, infoModel));
			}
		}
		return infoModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModel basicGetInfoModel() {
		return infoModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInfoModel(InformationModel newInfoModel) {
		InformationModel oldInfoModel = infoModel;
		infoModel = newInfoModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL, oldInfoModel, infoModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InfoModelChild getInfoModelChild() {
		return infoModelChild;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInfoModelChild(InfoModelChild newInfoModelChild, NotificationChain msgs) {
		InfoModelChild oldInfoModelChild = infoModelChild;
		infoModelChild = newInfoModelChild;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD, oldInfoModelChild, newInfoModelChild);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInfoModelChild(InfoModelChild newInfoModelChild) {
		if (newInfoModelChild != infoModelChild) {
			NotificationChain msgs = null;
			if (infoModelChild != null)
				msgs = ((InternalEObject)infoModelChild).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD, null, msgs);
			if (newInfoModelChild != null)
				msgs = ((InternalEObject)newInfoModelChild).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD, null, msgs);
			msgs = basicSetInfoModelChild(newInfoModelChild, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD, newInfoModelChild, newInfoModelChild));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD:
				return basicSetInfoModelChild(null, msgs);
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
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL:
				if (resolve) return getInfoModel();
				return basicGetInfoModel();
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD:
				return getInfoModelChild();
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
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL:
				setInfoModel((InformationModel)newValue);
				return;
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD:
				setInfoModelChild((InfoModelChild)newValue);
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
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL:
				setInfoModel((InformationModel)null);
				return;
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD:
				setInfoModelChild((InfoModelChild)null);
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
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL:
				return infoModel != null;
			case MappingPackage.INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD:
				return infoModelChild != null;
		}
		return super.eIsSet(featureID);
	}

} //InfoModelSourceElementImpl
