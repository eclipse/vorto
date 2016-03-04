/**
 */
package org.eclipse.vorto.core.api.model.datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Boolean Property Attribute Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getBooleanPropertyAttributeType()
 * @model
 * @generated
 */
public enum BooleanPropertyAttributeType implements Enumerator {
	/**
	 * The '<em><b>Readable</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #READABLE_VALUE
	 * @generated
	 * @ordered
	 */
	READABLE(0, "readable", "READABLE"),

	/**
	 * The '<em><b>Writable</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WRITABLE_VALUE
	 * @generated
	 * @ordered
	 */
	WRITABLE(1, "writable", "WRITABLE"),

	/**
	 * The '<em><b>Eventable</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EVENTABLE_VALUE
	 * @generated
	 * @ordered
	 */
	EVENTABLE(2, "eventable", "EVENTABLE");

	/**
	 * The '<em><b>Readable</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Readable</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #READABLE
	 * @model name="readable" literal="READABLE"
	 * @generated
	 * @ordered
	 */
	public static final int READABLE_VALUE = 0;

	/**
	 * The '<em><b>Writable</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Writable</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WRITABLE
	 * @model name="writable" literal="WRITABLE"
	 * @generated
	 * @ordered
	 */
	public static final int WRITABLE_VALUE = 1;

	/**
	 * The '<em><b>Eventable</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Eventable</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EVENTABLE
	 * @model name="eventable" literal="EVENTABLE"
	 * @generated
	 * @ordered
	 */
	public static final int EVENTABLE_VALUE = 2;

	/**
	 * An array of all the '<em><b>Boolean Property Attribute Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final BooleanPropertyAttributeType[] VALUES_ARRAY =
		new BooleanPropertyAttributeType[] {
			READABLE,
			WRITABLE,
			EVENTABLE,
		};

	/**
	 * A public read-only list of all the '<em><b>Boolean Property Attribute Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<BooleanPropertyAttributeType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Boolean Property Attribute Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static BooleanPropertyAttributeType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			BooleanPropertyAttributeType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Boolean Property Attribute Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static BooleanPropertyAttributeType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			BooleanPropertyAttributeType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Boolean Property Attribute Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static BooleanPropertyAttributeType get(int value) {
		switch (value) {
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
	private BooleanPropertyAttributeType(int value, String name, String literal) {
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
	
} //BooleanPropertyAttributeType
