/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
/**
 */
package org.eclipse.vorto.core.api.model.datatype.impl;

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

import org.eclipse.vorto.core.api.model.datatype.Constraint;
import org.eclipse.vorto.core.api.model.datatype.ConstraintRule;
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.core.api.model.datatype.Presence;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.datatype.PropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.PropertyType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl#getPresence <em>Presence</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl#isMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl#getConstraintRule <em>Constraint Rule</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.PropertyImpl#getPropertyAttributes <em>Property Attributes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PropertyImpl extends MinimalEObjectImpl.Container implements Property {
	/**
	 * The cached value of the '{@link #getPresence() <em>Presence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPresence()
	 * @generated
	 * @ordered
	 */
	protected Presence presence;

	/**
	 * The default value of the '{@link #isMultiplicity() <em>Multiplicity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MULTIPLICITY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMultiplicity() <em>Multiplicity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMultiplicity()
	 * @generated
	 * @ordered
	 */
	protected boolean multiplicity = MULTIPLICITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConstraintRule() <em>Constraint Rule</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraintRule()
	 * @generated
	 * @ordered
	 */
	protected ConstraintRule constraintRule;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected PropertyType type;

	/**
	 * The cached value of the '{@link #getPropertyAttributes() <em>Property Attributes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertyAttributes()
	 * @generated
	 * @ordered
	 */
	protected EList<PropertyAttribute> propertyAttributes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropertyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatatypePackage.Literals.PROPERTY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Presence getPresence() {
		return presence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPresence(Presence newPresence, NotificationChain msgs) {
		Presence oldPresence = presence;
		presence = newPresence;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__PRESENCE, oldPresence, newPresence);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPresence(Presence newPresence) {
		if (newPresence != presence) {
			NotificationChain msgs = null;
			if (presence != null)
				msgs = ((InternalEObject)presence).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DatatypePackage.PROPERTY__PRESENCE, null, msgs);
			if (newPresence != null)
				msgs = ((InternalEObject)newPresence).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DatatypePackage.PROPERTY__PRESENCE, null, msgs);
			msgs = basicSetPresence(newPresence, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__PRESENCE, newPresence, newPresence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMultiplicity() {
		return multiplicity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMultiplicity(boolean newMultiplicity) {
		boolean oldMultiplicity = multiplicity;
		multiplicity = newMultiplicity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__MULTIPLICITY, oldMultiplicity, multiplicity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintRule getConstraintRule() {
		return constraintRule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConstraintRule(ConstraintRule newConstraintRule, NotificationChain msgs) {
		ConstraintRule oldConstraintRule = constraintRule;
		constraintRule = newConstraintRule;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__CONSTRAINT_RULE, oldConstraintRule, newConstraintRule);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstraintRule(ConstraintRule newConstraintRule) {
		if (newConstraintRule != constraintRule) {
			NotificationChain msgs = null;
			if (constraintRule != null)
				msgs = ((InternalEObject)constraintRule).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DatatypePackage.PROPERTY__CONSTRAINT_RULE, null, msgs);
			if (newConstraintRule != null)
				msgs = ((InternalEObject)newConstraintRule).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DatatypePackage.PROPERTY__CONSTRAINT_RULE, null, msgs);
			msgs = basicSetConstraintRule(newConstraintRule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__CONSTRAINT_RULE, newConstraintRule, newConstraintRule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PropertyType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetType(PropertyType newType, NotificationChain msgs) {
		PropertyType oldType = type;
		type = newType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__TYPE, oldType, newType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(PropertyType newType) {
		if (newType != type) {
			NotificationChain msgs = null;
			if (type != null)
				msgs = ((InternalEObject)type).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DatatypePackage.PROPERTY__TYPE, null, msgs);
			if (newType != null)
				msgs = ((InternalEObject)newType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DatatypePackage.PROPERTY__TYPE, null, msgs);
			msgs = basicSetType(newType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatatypePackage.PROPERTY__TYPE, newType, newType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PropertyAttribute> getPropertyAttributes() {
		if (propertyAttributes == null) {
			propertyAttributes = new EObjectContainmentEList<PropertyAttribute>(PropertyAttribute.class, this, DatatypePackage.PROPERTY__PROPERTY_ATTRIBUTES);
		}
		return propertyAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DatatypePackage.PROPERTY__PRESENCE:
				return basicSetPresence(null, msgs);
			case DatatypePackage.PROPERTY__CONSTRAINT_RULE:
				return basicSetConstraintRule(null, msgs);
			case DatatypePackage.PROPERTY__TYPE:
				return basicSetType(null, msgs);
			case DatatypePackage.PROPERTY__PROPERTY_ATTRIBUTES:
				return ((InternalEList<?>)getPropertyAttributes()).basicRemove(otherEnd, msgs);
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
			case DatatypePackage.PROPERTY__PRESENCE:
				return getPresence();
			case DatatypePackage.PROPERTY__MULTIPLICITY:
				return isMultiplicity();
			case DatatypePackage.PROPERTY__NAME:
				return getName();
			case DatatypePackage.PROPERTY__DESCRIPTION:
				return getDescription();
			case DatatypePackage.PROPERTY__CONSTRAINT_RULE:
				return getConstraintRule();
			case DatatypePackage.PROPERTY__TYPE:
				return getType();
			case DatatypePackage.PROPERTY__PROPERTY_ATTRIBUTES:
				return getPropertyAttributes();
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
			case DatatypePackage.PROPERTY__PRESENCE:
				setPresence((Presence)newValue);
				return;
			case DatatypePackage.PROPERTY__MULTIPLICITY:
				setMultiplicity((Boolean)newValue);
				return;
			case DatatypePackage.PROPERTY__NAME:
				setName((String)newValue);
				return;
			case DatatypePackage.PROPERTY__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DatatypePackage.PROPERTY__CONSTRAINT_RULE:
				setConstraintRule((ConstraintRule)newValue);
				return;
			case DatatypePackage.PROPERTY__TYPE:
				setType((PropertyType)newValue);
				return;
			case DatatypePackage.PROPERTY__PROPERTY_ATTRIBUTES:
				getPropertyAttributes().clear();
				getPropertyAttributes().addAll((Collection<? extends PropertyAttribute>)newValue);
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
			case DatatypePackage.PROPERTY__PRESENCE:
				setPresence((Presence)null);
				return;
			case DatatypePackage.PROPERTY__MULTIPLICITY:
				setMultiplicity(MULTIPLICITY_EDEFAULT);
				return;
			case DatatypePackage.PROPERTY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DatatypePackage.PROPERTY__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DatatypePackage.PROPERTY__CONSTRAINT_RULE:
				setConstraintRule((ConstraintRule)null);
				return;
			case DatatypePackage.PROPERTY__TYPE:
				setType((PropertyType)null);
				return;
			case DatatypePackage.PROPERTY__PROPERTY_ATTRIBUTES:
				getPropertyAttributes().clear();
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
			case DatatypePackage.PROPERTY__PRESENCE:
				return presence != null;
			case DatatypePackage.PROPERTY__MULTIPLICITY:
				return multiplicity != MULTIPLICITY_EDEFAULT;
			case DatatypePackage.PROPERTY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DatatypePackage.PROPERTY__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DatatypePackage.PROPERTY__CONSTRAINT_RULE:
				return constraintRule != null;
			case DatatypePackage.PROPERTY__TYPE:
				return type != null;
			case DatatypePackage.PROPERTY__PROPERTY_ATTRIBUTES:
				return propertyAttributes != null && !propertyAttributes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (multiplicity: ");
		result.append(multiplicity);
		result.append(", name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //PropertyImpl
