/**
 */
package org.eclipse.vorto.codegen.ble.model.blegatt.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic;
import org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty;
import org.eclipse.vorto.codegen.ble.model.blegatt.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Characteristic</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl#getUuid <em>Uuid</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl#isIsReadable <em>Is Readable</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl#isIsWritable <em>Is Writable</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl#isIsEventable <em>Is Eventable</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl#getLength <em>Length</em>}</li>
 *   <li>{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharacteristicImpl extends MinimalEObjectImpl.Container implements Characteristic {
	/**
	 * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected static final String UUID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUuid()
	 * @generated
	 * @ordered
	 */
	protected String uuid = UUID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<CharacteristicProperty> properties;

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
	 * The default value of the '{@link #isIsReadable() <em>Is Readable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsReadable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_READABLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isIsReadable() <em>Is Readable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsReadable()
	 * @generated
	 * @ordered
	 */
	protected boolean isReadable = IS_READABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsWritable() <em>Is Writable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsWritable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_WRITABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsWritable() <em>Is Writable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsWritable()
	 * @generated
	 * @ordered
	 */
	protected boolean isWritable = IS_WRITABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsEventable() <em>Is Eventable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEventable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_EVENTABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsEventable() <em>Is Eventable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEventable()
	 * @generated
	 * @ordered
	 */
	protected boolean isEventable = IS_EVENTABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLength() <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLength()
	 * @generated
	 * @ordered
	 */
	protected static final int LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLength() <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLength()
	 * @generated
	 * @ordered
	 */
	protected int length = LENGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharacteristicImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.CHARACTERISTIC;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUuid(String newUuid) {
		String oldUuid = uuid;
		uuid = newUuid;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHARACTERISTIC__UUID, oldUuid, uuid));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CharacteristicProperty> getProperties() {
		if (properties == null) {
			properties = new EObjectResolvingEList<CharacteristicProperty>(CharacteristicProperty.class, this, ModelPackage.CHARACTERISTIC__PROPERTIES);
		}
		return properties;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHARACTERISTIC__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsReadable() {
		return isReadable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsReadable(boolean newIsReadable) {
		boolean oldIsReadable = isReadable;
		isReadable = newIsReadable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHARACTERISTIC__IS_READABLE, oldIsReadable, isReadable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsWritable() {
		return isWritable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsWritable(boolean newIsWritable) {
		boolean oldIsWritable = isWritable;
		isWritable = newIsWritable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHARACTERISTIC__IS_WRITABLE, oldIsWritable, isWritable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isIsEventable() {
		return isEventable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIsEventable(boolean newIsEventable) {
		boolean oldIsEventable = isEventable;
		isEventable = newIsEventable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHARACTERISTIC__IS_EVENTABLE, oldIsEventable, isEventable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLength() {
		return length;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLength(int newLength) {
		int oldLength = length;
		length = newLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHARACTERISTIC__LENGTH, oldLength, length));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.CHARACTERISTIC__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.CHARACTERISTIC__UUID:
				return getUuid();
			case ModelPackage.CHARACTERISTIC__PROPERTIES:
				return getProperties();
			case ModelPackage.CHARACTERISTIC__NAME:
				return getName();
			case ModelPackage.CHARACTERISTIC__IS_READABLE:
				return isIsReadable();
			case ModelPackage.CHARACTERISTIC__IS_WRITABLE:
				return isIsWritable();
			case ModelPackage.CHARACTERISTIC__IS_EVENTABLE:
				return isIsEventable();
			case ModelPackage.CHARACTERISTIC__LENGTH:
				return getLength();
			case ModelPackage.CHARACTERISTIC__VALUE:
				return getValue();
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
			case ModelPackage.CHARACTERISTIC__UUID:
				setUuid((String)newValue);
				return;
			case ModelPackage.CHARACTERISTIC__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends CharacteristicProperty>)newValue);
				return;
			case ModelPackage.CHARACTERISTIC__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.CHARACTERISTIC__IS_READABLE:
				setIsReadable((Boolean)newValue);
				return;
			case ModelPackage.CHARACTERISTIC__IS_WRITABLE:
				setIsWritable((Boolean)newValue);
				return;
			case ModelPackage.CHARACTERISTIC__IS_EVENTABLE:
				setIsEventable((Boolean)newValue);
				return;
			case ModelPackage.CHARACTERISTIC__LENGTH:
				setLength((Integer)newValue);
				return;
			case ModelPackage.CHARACTERISTIC__VALUE:
				setValue((String)newValue);
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
			case ModelPackage.CHARACTERISTIC__UUID:
				setUuid(UUID_EDEFAULT);
				return;
			case ModelPackage.CHARACTERISTIC__PROPERTIES:
				getProperties().clear();
				return;
			case ModelPackage.CHARACTERISTIC__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.CHARACTERISTIC__IS_READABLE:
				setIsReadable(IS_READABLE_EDEFAULT);
				return;
			case ModelPackage.CHARACTERISTIC__IS_WRITABLE:
				setIsWritable(IS_WRITABLE_EDEFAULT);
				return;
			case ModelPackage.CHARACTERISTIC__IS_EVENTABLE:
				setIsEventable(IS_EVENTABLE_EDEFAULT);
				return;
			case ModelPackage.CHARACTERISTIC__LENGTH:
				setLength(LENGTH_EDEFAULT);
				return;
			case ModelPackage.CHARACTERISTIC__VALUE:
				setValue(VALUE_EDEFAULT);
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
			case ModelPackage.CHARACTERISTIC__UUID:
				return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
			case ModelPackage.CHARACTERISTIC__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case ModelPackage.CHARACTERISTIC__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.CHARACTERISTIC__IS_READABLE:
				return isReadable != IS_READABLE_EDEFAULT;
			case ModelPackage.CHARACTERISTIC__IS_WRITABLE:
				return isWritable != IS_WRITABLE_EDEFAULT;
			case ModelPackage.CHARACTERISTIC__IS_EVENTABLE:
				return isEventable != IS_EVENTABLE_EDEFAULT;
			case ModelPackage.CHARACTERISTIC__LENGTH:
				return length != LENGTH_EDEFAULT;
			case ModelPackage.CHARACTERISTIC__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
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
		result.append(" (uuid: ");
		result.append(uuid);
		result.append(", name: ");
		result.append(name);
		result.append(", isReadable: ");
		result.append(isReadable);
		result.append(", isWritable: ");
		result.append(isWritable);
		result.append(", isEventable: ");
		result.append(isEventable);
		result.append(", length: ");
		result.append(length);
		result.append(", value: ");
		result.append(value);
		result.append(')');
		return result.toString();
	}

} //CharacteristicImpl
