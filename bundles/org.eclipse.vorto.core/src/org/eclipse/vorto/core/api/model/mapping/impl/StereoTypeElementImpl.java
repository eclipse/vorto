/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Stereo Type Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.StereoTypeElementImpl#getStereoTypes <em>Stereo Types</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StereoTypeElementImpl extends MinimalEObjectImpl.Container implements StereoTypeElement {
	/**
	 * The cached value of the '{@link #getStereoTypes() <em>Stereo Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStereoTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<StereoType> stereoTypes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StereoTypeElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.STEREO_TYPE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StereoType> getStereoTypes() {
		if (stereoTypes == null) {
			stereoTypes = new EObjectContainmentEList<StereoType>(StereoType.class, this, MappingPackage.STEREO_TYPE_ELEMENT__STEREO_TYPES);
		}
		return stereoTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.STEREO_TYPE_ELEMENT__STEREO_TYPES:
				return ((InternalEList<?>)getStereoTypes()).basicRemove(otherEnd, msgs);
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
			case MappingPackage.STEREO_TYPE_ELEMENT__STEREO_TYPES:
				return getStereoTypes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MappingPackage.STEREO_TYPE_ELEMENT__STEREO_TYPES:
				getStereoTypes().clear();
				getStereoTypes().addAll((Collection<? extends StereoType>)newValue);
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
			case MappingPackage.STEREO_TYPE_ELEMENT__STEREO_TYPES:
				getStereoTypes().clear();
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
			case MappingPackage.STEREO_TYPE_ELEMENT__STEREO_TYPES:
				return stereoTypes != null && !stereoTypes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //StereoTypeElementImpl
