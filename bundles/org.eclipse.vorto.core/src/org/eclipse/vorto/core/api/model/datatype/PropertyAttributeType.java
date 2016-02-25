/**
 */
package org.eclipse.vorto.core.api.model.datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Property Attribute Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getPropertyAttributeType()
 * @model
 * @generated
 */
public enum PropertyAttributeType implements Enumerator {
	/**
	 * The '<em><b>Measurement Unit</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MEASUREMENT_UNIT_VALUE
	 * @generated
	 * @ordered
	 */
	MEASUREMENT_UNIT(1, "MeasurementUnit", "MEASUREMENTUNIT"), /**
	 * The '<em><b>Readable</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READABLE_VALUE
	 * @generated
	 * @ordered
	 */
	READABLE(2, "Readable", "READABLE"), /**
	 * The '<em><b>Writable</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WRITABLE_VALUE
	 * @generated
	 * @ordered
	 */
	WRITABLE(3, "Writable", "WRITABLE"), /**
	 * The '<em><b>Eventable</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EVENTABLE_VALUE
	 * @generated
	 * @ordered
	 */
	EVENTABLE(4, "Eventable", "EVENTABLE");

	/**
	 * The '<em><b>Measurement Unit</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Measurement Unit</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MEASUREMENT_UNIT
	 * @model name="MeasurementUnit" literal="MEASUREMENTUNIT"
	 * @generated
	 * @ordered
	 */
	public static final int MEASUREMENT_UNIT_VALUE = 1;

	/**
	 * The '<em><b>Readable</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Readable</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READABLE
	 * @model name="Readable" literal="READABLE"
	 * @generated
	 * @ordered
	 */
	public static final int READABLE_VALUE = 2;

	/**
	 * The '<em><b>Writable</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Writable</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WRITABLE
	 * @model name="Writable" literal="WRITABLE"
	 * @generated
	 * @ordered
	 */
	public static final int WRITABLE_VALUE = 3;

	/**
	 * The '<em><b>Eventable</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Eventable</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EVENTABLE
	 * @model name="Eventable" literal="EVENTABLE"
	 * @generated
	 * @ordered
	 */
	public static final int EVENTABLE_VALUE = 4;

	/**
	 * An array of all the '<em><b>Property Attribute Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final PropertyAttributeType[] VALUES_ARRAY =
		new PropertyAttributeType[] {
			MEASUREMENT_UNIT,
			READABLE,
			WRITABLE,
			EVENTABLE,
		};

	/**
	 * A public read-only list of all the '<em><b>Property Attribute Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<PropertyAttributeType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Property Attribute Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PropertyAttributeType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PropertyAttributeType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Property Attribute Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PropertyAttributeType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			PropertyAttributeType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Property Attribute Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PropertyAttributeType get(int value) {
		switch (value) {
			case MEASUREMENT_UNIT_VALUE: return MEASUREMENT_UNIT;
			case READABLE_VALUE: return READABLE;
			case WRITABLE_VALUE: return WRITABLE;
			case EVENTABLE_VALUE: return EVENTABLE;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private PropertyAttributeType(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
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
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //PropertyAttributeType
