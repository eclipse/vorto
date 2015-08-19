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

import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingImpl#getEntityMappingRules <em>Entity Mapping Rules</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EntityMappingImpl extends DataTypeMappingImpl implements EntityMapping {
	/**
	 * The cached value of the '{@link #getEntityMappingRules() <em>Entity Mapping Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntityMappingRules()
	 * @generated
	 * @ordered
	 */
	protected EList<EntityMappingRule> entityMappingRules;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntityMappingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.ENTITY_MAPPING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EntityMappingRule> getEntityMappingRules() {
		if (entityMappingRules == null) {
			entityMappingRules = new EObjectContainmentEList<EntityMappingRule>(EntityMappingRule.class, this, MappingPackage.ENTITY_MAPPING__ENTITY_MAPPING_RULES);
		}
		return entityMappingRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.ENTITY_MAPPING__ENTITY_MAPPING_RULES:
				return ((InternalEList<?>)getEntityMappingRules()).basicRemove(otherEnd, msgs);
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
			case MappingPackage.ENTITY_MAPPING__ENTITY_MAPPING_RULES:
				return getEntityMappingRules();
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
			case MappingPackage.ENTITY_MAPPING__ENTITY_MAPPING_RULES:
				getEntityMappingRules().clear();
				getEntityMappingRules().addAll((Collection<? extends EntityMappingRule>)newValue);
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
			case MappingPackage.ENTITY_MAPPING__ENTITY_MAPPING_RULES:
				getEntityMappingRules().clear();
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
			case MappingPackage.ENTITY_MAPPING__ENTITY_MAPPING_RULES:
				return entityMappingRules != null && !entityMappingRules.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //EntityMappingImpl
