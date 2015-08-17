/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.TargetElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function Block Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl#getFunctionBlockSourceElements <em>Function Block Source Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.FunctionBlockMappingRuleImpl#getTargetElement <em>Target Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FunctionBlockMappingRuleImpl extends MinimalEObjectImpl.Container implements FunctionBlockMappingRule {
	/**
	 * The cached value of the '{@link #getFunctionBlockSourceElements() <em>Function Block Source Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionBlockSourceElements()
	 * @generated
	 * @ordered
	 */
	protected EList<FunctionBlockSourceElement> functionBlockSourceElements;

	/**
	 * The cached value of the '{@link #getTargetElement() <em>Target Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetElement()
	 * @generated
	 * @ordered
	 */
	protected TargetElement targetElement;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FunctionBlockMappingRuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.FUNCTION_BLOCK_MAPPING_RULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FunctionBlockSourceElement> getFunctionBlockSourceElements() {
		if (functionBlockSourceElements == null) {
			functionBlockSourceElements = new EObjectContainmentEList<FunctionBlockSourceElement>(FunctionBlockSourceElement.class, this, MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS);
		}
		return functionBlockSourceElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TargetElement getTargetElement() {
		return targetElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTargetElement(TargetElement newTargetElement, NotificationChain msgs) {
		TargetElement oldTargetElement = targetElement;
		targetElement = newTargetElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT, oldTargetElement, newTargetElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetElement(TargetElement newTargetElement) {
		if (newTargetElement != targetElement) {
			NotificationChain msgs = null;
			if (targetElement != null)
				msgs = ((InternalEObject)targetElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT, null, msgs);
			if (newTargetElement != null)
				msgs = ((InternalEObject)newTargetElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT, null, msgs);
			msgs = basicSetTargetElement(newTargetElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT, newTargetElement, newTargetElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS:
				return ((InternalEList<?>)getFunctionBlockSourceElements()).basicRemove(otherEnd, msgs);
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT:
				return basicSetTargetElement(null, msgs);
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
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS:
				return getFunctionBlockSourceElements();
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT:
				return getTargetElement();
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
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS:
				getFunctionBlockSourceElements().clear();
				getFunctionBlockSourceElements().addAll((Collection<? extends FunctionBlockSourceElement>)newValue);
				return;
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT:
				setTargetElement((TargetElement)newValue);
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
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS:
				getFunctionBlockSourceElements().clear();
				return;
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT:
				setTargetElement((TargetElement)null);
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
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS:
				return functionBlockSourceElements != null && !functionBlockSourceElements.isEmpty();
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT:
				return targetElement != null;
		}
		return super.eIsSet(featureID);
	}

} //FunctionBlockMappingRuleImpl
