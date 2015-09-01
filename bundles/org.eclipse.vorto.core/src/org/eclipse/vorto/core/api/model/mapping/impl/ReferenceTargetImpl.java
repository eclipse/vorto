/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.ReferenceTarget;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Reference Target</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.ReferenceTargetImpl#getMappingModel <em>Mapping Model</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ReferenceTargetImpl extends TargetImpl implements ReferenceTarget {
	/**
	 * The cached value of the '{@link #getMappingModel() <em>Mapping Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappingModel()
	 * @generated
	 * @ordered
	 */
	protected MappingModel mappingModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ReferenceTargetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.REFERENCE_TARGET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingModel getMappingModel() {
		if (mappingModel != null && mappingModel.eIsProxy()) {
			InternalEObject oldMappingModel = (InternalEObject)mappingModel;
			mappingModel = (MappingModel)eResolveProxy(oldMappingModel);
			if (mappingModel != oldMappingModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MappingPackage.REFERENCE_TARGET__MAPPING_MODEL, oldMappingModel, mappingModel));
			}
		}
		return mappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingModel basicGetMappingModel() {
		return mappingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMappingModel(MappingModel newMappingModel) {
		MappingModel oldMappingModel = mappingModel;
		mappingModel = newMappingModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.REFERENCE_TARGET__MAPPING_MODEL, oldMappingModel, mappingModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.REFERENCE_TARGET__MAPPING_MODEL:
				if (resolve) return getMappingModel();
				return basicGetMappingModel();
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
			case MappingPackage.REFERENCE_TARGET__MAPPING_MODEL:
				setMappingModel((MappingModel)newValue);
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
			case MappingPackage.REFERENCE_TARGET__MAPPING_MODEL:
				setMappingModel((MappingModel)null);
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
			case MappingPackage.REFERENCE_TARGET__MAPPING_MODEL:
				return mappingModel != null;
		}
		return super.eIsSet(featureID);
	}

} //ReferenceTargetImpl
