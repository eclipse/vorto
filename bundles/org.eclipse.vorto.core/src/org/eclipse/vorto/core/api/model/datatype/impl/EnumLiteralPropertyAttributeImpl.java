/**
 */
package org.eclipse.vorto.core.api.model.datatype.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteral;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttribute;
import org.eclipse.vorto.core.api.model.datatype.EnumLiteralPropertyAttributeType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Literal Property Attribute</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralPropertyAttributeImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.impl.EnumLiteralPropertyAttributeImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EnumLiteralPropertyAttributeImpl extends MinimalEObjectImpl.Container implements EnumLiteralPropertyAttribute {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final EnumLiteralPropertyAttributeType TYPE_EDEFAULT = EnumLiteralPropertyAttributeType.MEASUREMENT_UNIT;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected EnumLiteralPropertyAttributeType type = TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected EnumLiteral value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnumLiteralPropertyAttributeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatatypePackage.Literals.ENUM_LITERAL_PROPERTY_ATTRIBUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumLiteralPropertyAttributeType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(EnumLiteralPropertyAttributeType newType) {
		EnumLiteralPropertyAttributeType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumLiteral getValue() {
		if (value != null && value.eIsProxy()) {
			InternalEObject oldValue = (InternalEObject)value;
			value = (EnumLiteral)eResolveProxy(oldValue);
			if (value != oldValue) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__VALUE, oldValue, value));
			}
		}
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumLiteral basicGetValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(EnumLiteral newValue) {
		EnumLiteral oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__TYPE:
				return getType();
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__VALUE:
				if (resolve) return getValue();
				return basicGetValue();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__TYPE:
				setType((EnumLiteralPropertyAttributeType)newValue);
				return;
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__VALUE:
				setValue((EnumLiteral)newValue);
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
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__VALUE:
				setValue((EnumLiteral)null);
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
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__TYPE:
				return type != TYPE_EDEFAULT;
			case DatatypePackage.ENUM_LITERAL_PROPERTY_ATTRIBUTE__VALUE:
				return value != null;
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
		result.append(" (type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //EnumLiteralPropertyAttributeImpl
