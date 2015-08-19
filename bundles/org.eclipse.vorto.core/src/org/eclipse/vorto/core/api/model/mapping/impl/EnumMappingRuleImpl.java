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
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EnumMappingRuleImpl#getTargetElement <em>Target Element</em>}</li>
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
	 * The cached value of the '{@link #getTargetElement() <em>Target Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetElement()
	 * @generated
	 * @ordered
	 */
	protected EnumTargetElement targetElement;

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
	public EnumTargetElement getTargetElement() {
		return targetElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTargetElement(EnumTargetElement newTargetElement, NotificationChain msgs) {
		EnumTargetElement oldTargetElement = targetElement;
		targetElement = newTargetElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT, oldTargetElement, newTargetElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetElement(EnumTargetElement newTargetElement) {
		if (newTargetElement != targetElement) {
			NotificationChain msgs = null;
			if (targetElement != null)
				msgs = ((InternalEObject)targetElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT, null, msgs);
			if (newTargetElement != null)
				msgs = ((InternalEObject)newTargetElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT, null, msgs);
			msgs = basicSetTargetElement(newTargetElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT, newTargetElement, newTargetElement));
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
			case MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT:
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
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_ELEMENTS:
				return getEnumElements();
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT:
				return getEnumSourceElement();
			case MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT:
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
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_ELEMENTS:
				getEnumElements().clear();
				getEnumElements().addAll((Collection<? extends EnumSourceElement>)newValue);
				return;
			case MappingPackage.ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT:
				getEnumSourceElement().clear();
				getEnumSourceElement().addAll((Collection<? extends EnumSourceElement>)newValue);
				return;
			case MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT:
				setTargetElement((EnumTargetElement)newValue);
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
			case MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT:
				setTargetElement((EnumTargetElement)null);
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
			case MappingPackage.ENUM_MAPPING_RULE__TARGET_ELEMENT:
				return targetElement != null;
		}
		return super.eIsSet(featureID);
	}

} //EnumMappingRuleImpl
