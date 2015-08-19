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

import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EntitySourceElement;
import org.eclipse.vorto.core.api.model.mapping.EntityTargetElement;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Entity Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl#getEntityMappingElements <em>Entity Mapping Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl#getEntitySourceElement <em>Entity Source Element</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.EntityMappingRuleImpl#getTargetElement <em>Target Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EntityMappingRuleImpl extends MinimalEObjectImpl.Container implements EntityMappingRule {
	/**
	 * The cached value of the '{@link #getEntityMappingElements() <em>Entity Mapping Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntityMappingElements()
	 * @generated
	 * @ordered
	 */
	protected EList<EntitySourceElement> entityMappingElements;

	/**
	 * The cached value of the '{@link #getEntitySourceElement() <em>Entity Source Element</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntitySourceElement()
	 * @generated
	 * @ordered
	 */
	protected EList<EntitySourceElement> entitySourceElement;

	/**
	 * The cached value of the '{@link #getTargetElement() <em>Target Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetElement()
	 * @generated
	 * @ordered
	 */
	protected EntityTargetElement targetElement;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntityMappingRuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.ENTITY_MAPPING_RULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EntitySourceElement> getEntityMappingElements() {
		if (entityMappingElements == null) {
			entityMappingElements = new EObjectContainmentEList<EntitySourceElement>(EntitySourceElement.class, this, MappingPackage.ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS);
		}
		return entityMappingElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EntitySourceElement> getEntitySourceElement() {
		if (entitySourceElement == null) {
			entitySourceElement = new EObjectContainmentEList<EntitySourceElement>(EntitySourceElement.class, this, MappingPackage.ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT);
		}
		return entitySourceElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityTargetElement getTargetElement() {
		return targetElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTargetElement(EntityTargetElement newTargetElement, NotificationChain msgs) {
		EntityTargetElement oldTargetElement = targetElement;
		targetElement = newTargetElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT, oldTargetElement, newTargetElement);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetElement(EntityTargetElement newTargetElement) {
		if (newTargetElement != targetElement) {
			NotificationChain msgs = null;
			if (targetElement != null)
				msgs = ((InternalEObject)targetElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT, null, msgs);
			if (newTargetElement != null)
				msgs = ((InternalEObject)newTargetElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT, null, msgs);
			msgs = basicSetTargetElement(newTargetElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT, newTargetElement, newTargetElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS:
				return ((InternalEList<?>)getEntityMappingElements()).basicRemove(otherEnd, msgs);
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT:
				return ((InternalEList<?>)getEntitySourceElement()).basicRemove(otherEnd, msgs);
			case MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT:
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
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS:
				return getEntityMappingElements();
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT:
				return getEntitySourceElement();
			case MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT:
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
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS:
				getEntityMappingElements().clear();
				getEntityMappingElements().addAll((Collection<? extends EntitySourceElement>)newValue);
				return;
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT:
				getEntitySourceElement().clear();
				getEntitySourceElement().addAll((Collection<? extends EntitySourceElement>)newValue);
				return;
			case MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT:
				setTargetElement((EntityTargetElement)newValue);
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
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS:
				getEntityMappingElements().clear();
				return;
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT:
				getEntitySourceElement().clear();
				return;
			case MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT:
				setTargetElement((EntityTargetElement)null);
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
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS:
				return entityMappingElements != null && !entityMappingElements.isEmpty();
			case MappingPackage.ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT:
				return entitySourceElement != null && !entitySourceElement.isEmpty();
			case MappingPackage.ENTITY_MAPPING_RULE__TARGET_ELEMENT:
				return targetElement != null;
		}
		return super.eIsSet(featureID);
	}

} //EntityMappingRuleImpl
