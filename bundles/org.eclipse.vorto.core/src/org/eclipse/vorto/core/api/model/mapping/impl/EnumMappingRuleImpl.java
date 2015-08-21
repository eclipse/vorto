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

import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumSourceElement;
import org.eclipse.vorto.core.api.model.mapping.EnumTargetElement;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl#getEnumElements <em>Enum Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl#getEnumSourceElement <em>Enum Source Element</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnumMappingRuleImpl extends MinimalEObjectImpl.Container implements EnumMappingRule {
	/**
	 * The cached value of the '{@link #getEnumElements() <em>Enum Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnumElements()
	 * @generated
	 * @ordered
	 */
	protected EList<EnumSourceElement> enumElements;

	/**
	 * The cached value of the '{@link #getEnumSourceElement() <em>Enum Source Element</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnumSourceElement()
	 * @generated
	 * @ordered
	 */
	protected EList<EnumSourceElement> enumSourceElement;

	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected EnumTargetElement target;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnumMappingRuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.ENUM_MAPPING_RULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EnumSourceElement> getEnumElements() {
		if (enumElements == null) {
			enumElements = new EObjectContainmentEList<EnumSourceElement>(EnumSourceElement.class, this, MappingPackage.ENUM_MAPPING_RULE__ENUM_ELEMENTS);
		}
		return enumElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EnumSourceElement> getEnumSourceElement() {
		if (enumSourceElement == null) {
			enumSourceElement = new EObjectContainmentEList<EnumSourceElement>(EnumSourceElement.class, this, MappingPackage.ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT);
		}
		return enumSourceElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumTargetElement getTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTarget(EnumTargetElement newTarget, NotificationChain msgs) {
		EnumTargetElement oldTarget = target;
		target = newTarget;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MappingPackage.ENUM_MAPPING_RULE__TARGET, oldTarget, newTarget);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTarget(EnumTargetElement newTarget) {
		if (newTarget != target) {
			NotificationChain msgs = null;
			if (target != null)
				msgs = ((InternalEObject)target).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MappingPackage.ENUM_MAPPING_RULE__TARGET, null, msgs);
			if (newTarget != null)
				msgs = ((InternalEObject)newTarget).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MappingPackage.ENUM_MAPPING_RULE__TARGET, null, msgs);
			msgs = basicSetTarget(newTarget, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.ENUM_MAPPING_RULE__TARGET, newTarget, newTarget));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_ELEMENTS:
				return ((InternalEList<?>)getEnumElements()).basicRemove(otherEnd, msgs);
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT:
				return ((InternalEList<?>)getEnumSourceElement()).basicRemove(otherEnd, msgs);
			case MappingPackage.ENUM_MAPPING_RULE__TARGET:
				return basicSetTarget(null, msgs);
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
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_ELEMENTS:
				return getEnumElements();
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT:
				return getEnumSourceElement();
			case MappingPackage.ENUM_MAPPING_RULE__TARGET:
				return getTarget();
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
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_ELEMENTS:
				getEnumElements().clear();
				getEnumElements().addAll((Collection<? extends EnumSourceElement>)newValue);
				return;
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT:
				getEnumSourceElement().clear();
				getEnumSourceElement().addAll((Collection<? extends EnumSourceElement>)newValue);
				return;
			case MappingPackage.ENUM_MAPPING_RULE__TARGET:
				setTarget((EnumTargetElement)newValue);
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
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_ELEMENTS:
				getEnumElements().clear();
				return;
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT:
				getEnumSourceElement().clear();
				return;
			case MappingPackage.ENUM_MAPPING_RULE__TARGET:
				setTarget((EnumTargetElement)null);
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
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_ELEMENTS:
				return enumElements != null && !enumElements.isEmpty();
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT:
				return enumSourceElement != null && !enumSourceElement.isEmpty();
			case MappingPackage.ENUM_MAPPING_RULE__TARGET:
				return target != null;
		}
		return super.eIsSet(featureID);
	}

} //EnumMappingRuleImpl
