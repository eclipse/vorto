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

import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function Block Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingImpl#getFunctionBlockMappingRules <em>Function Block Mapping Rules</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FunctionBlockMappingImpl extends MappingImpl implements FunctionBlockMapping {
	/**
	 * The cached value of the '{@link #getFunctionBlockMappingRules() <em>Function Block Mapping Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionBlockMappingRules()
	 * @generated
	 * @ordered
	 */
	protected EList<FunctionBlockMappingRule> functionBlockMappingRules;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FunctionBlockMappingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.FUNCTION_BLOCK_MAPPING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FunctionBlockMappingRule> getFunctionBlockMappingRules() {
		if (functionBlockMappingRules == null) {
			functionBlockMappingRules = new EObjectContainmentEList<FunctionBlockMappingRule>(FunctionBlockMappingRule.class, this, MappingPackage.FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES);
		}
		return functionBlockMappingRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES:
				return ((InternalEList<?>)getFunctionBlockMappingRules()).basicRemove(otherEnd, msgs);
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
			case MappingPackage.FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES:
				return getFunctionBlockMappingRules();
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
			case MappingPackage.FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES:
				getFunctionBlockMappingRules().clear();
				getFunctionBlockMappingRules().addAll((Collection<? extends FunctionBlockMappingRule>)newValue);
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
			case MappingPackage.FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES:
				getFunctionBlockMappingRules().clear();
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
			case MappingPackage.FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES:
				return functionBlockMappingRules != null && !functionBlockMappingRules.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //FunctionBlockMappingImpl
