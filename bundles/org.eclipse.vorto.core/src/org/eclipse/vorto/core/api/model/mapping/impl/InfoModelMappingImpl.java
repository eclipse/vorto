/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Info Model Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingImpl#getInfoModelMappingRules <em>Info Model Mapping Rules</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InfoModelMappingImpl extends MappingTypeImpl implements InfoModelMapping {
	/**
	 * The cached value of the '{@link #getInfoModelMappingRules() <em>Info Model Mapping Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInfoModelMappingRules()
	 * @generated
	 * @ordered
	 */
	protected EList<InfoModelMappingRule> infoModelMappingRules;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InfoModelMappingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.INFO_MODEL_MAPPING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InfoModelMappingRule> getInfoModelMappingRules() {
		if (infoModelMappingRules == null) {
			infoModelMappingRules = new EObjectContainmentEList<InfoModelMappingRule>(InfoModelMappingRule.class, this, MappingPackage.INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES);
		}
		return infoModelMappingRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES:
				return ((InternalEList<?>)getInfoModelMappingRules()).basicRemove(otherEnd, msgs);
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
			case MappingPackage.INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES:
				return getInfoModelMappingRules();
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
			case MappingPackage.INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES:
				getInfoModelMappingRules().clear();
				getInfoModelMappingRules().addAll((Collection<? extends InfoModelMappingRule>)newValue);
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
			case MappingPackage.INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES:
				getInfoModelMappingRules().clear();
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
			case MappingPackage.INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES:
				return infoModelMappingRules != null && !infoModelMappingRules.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //InfoModelMappingImpl
