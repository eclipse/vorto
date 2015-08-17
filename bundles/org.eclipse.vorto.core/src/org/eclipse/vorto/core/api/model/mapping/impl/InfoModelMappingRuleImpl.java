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

import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.TargetElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Info Model Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl#getInfoModelSourceElements <em>Info Model Source Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.InfoModelMappingRuleImpl#getTargetElement <em>Target Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InfoModelMappingRuleImpl extends MinimalEObjectImpl.Container implements InfoModelMappingRule {
	/**
	 * The cached value of the '{@link #getInfoModelSourceElements() <em>Info Model Source Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInfoModelSourceElements()
	 * @generated
	 * @ordered
	 */
	protected EList<InfoModelSourceElement> infoModelSourceElements;

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
	protected InfoModelMappingRuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.INFO_MODEL_MAPPING_RULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InfoModelSourceElement> getInfoModelSourceElements() {
		if (infoModelSourceElements == null) {
			infoModelSourceElements = new EObjectContainmentEList<InfoModelSourceElement>(InfoModelSourceElement.class, this, MappingPackage.INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS);
		}
		return infoModelSourceElements;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT, oldTargetElement, newTargetElement);
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
				msgs = ((InternalEObject)targetElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT, null, msgs);
			if (newTargetElement != null)
				msgs = ((InternalEObject)newTargetElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT, null, msgs);
			msgs = basicSetTargetElement(newTargetElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT, newTargetElement, newTargetElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS:
				return ((InternalEList<?>)getInfoModelSourceElements()).basicRemove(otherEnd, msgs);
			case MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT:
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
			case MappingPackage.INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS:
				return getInfoModelSourceElements();
			case MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT:
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
			case MappingPackage.INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS:
				getInfoModelSourceElements().clear();
				getInfoModelSourceElements().addAll((Collection<? extends InfoModelSourceElement>)newValue);
				return;
			case MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT:
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
			case MappingPackage.INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS:
				getInfoModelSourceElements().clear();
				return;
			case MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT:
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
			case MappingPackage.INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS:
				return infoModelSourceElements != null && !infoModelSourceElements.isEmpty();
			case MappingPackage.INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT:
				return targetElement != null;
		}
		return super.eIsSet(featureID);
	}

} //InfoModelMappingRuleImpl
