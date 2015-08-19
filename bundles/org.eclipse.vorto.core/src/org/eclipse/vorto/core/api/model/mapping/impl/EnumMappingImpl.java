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

import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingImpl#getEnumMappingRules <em>Enum Mapping Rules</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnumMappingImpl extends DataTypeMappingImpl implements EnumMapping {
	/**
	 * The cached value of the '{@link #getEnumMappingRules() <em>Enum Mapping Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnumMappingRules()
	 * @generated
	 * @ordered
	 */
	protected EList<EnumMappingRule> enumMappingRules;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnumMappingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.ENUM_MAPPING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EnumMappingRule> getEnumMappingRules() {
		if (enumMappingRules == null) {
			enumMappingRules = new EObjectContainmentEList<EnumMappingRule>(EnumMappingRule.class, this, MappingPackage.ENUM_MAPPING__ENUM_MAPPING_RULES);
		}
		return enumMappingRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.ENUM_MAPPING__ENUM_MAPPING_RULES:
				return ((InternalEList<?>)getEnumMappingRules()).basicRemove(otherEnd, msgs);
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
			case MappingPackage.ENUM_MAPPING__ENUM_MAPPING_RULES:
				return getEnumMappingRules();
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
			case MappingPackage.ENUM_MAPPING__ENUM_MAPPING_RULES:
				getEnumMappingRules().clear();
				getEnumMappingRules().addAll((Collection<? extends EnumMappingRule>)newValue);
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
			case MappingPackage.ENUM_MAPPING__ENUM_MAPPING_RULES:
				getEnumMappingRules().clear();
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
			case MappingPackage.ENUM_MAPPING__ENUM_MAPPING_RULES:
				return enumMappingRules != null && !enumMappingRules.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //EnumMappingImpl
