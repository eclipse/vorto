/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Info Model Attribute</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelAttribute()
 * @model
 * @generated
 */
public enum InfoModelAttribute implements Enumerator {
	/**
	 * The '<em><b>Name</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NAME_VALUE
	 * @generated
	 * @ordered
	 */
	NAME(0, "name", "name"),

	/**
	 * The '<em><b>Namespace</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NAMESPACE_VALUE
	 * @generated
	 * @ordered
	 */
	NAMESPACE(1, "namespace", "namespace"),

	/**
	 * The '<em><b>Version</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #VERSION_VALUE
	 * @generated
	 * @ordered
	 */
	VERSION(2, "version", "version"),

	/**
	 * The '<em><b>Displayname</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DISPLAYNAME_VALUE
	 * @generated
	 * @ordered
	 */
	DISPLAYNAME(3, "displayname", "displayname"),

	/**
	 * The '<em><b>Category</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CATEGORY_VALUE
	 * @generated
	 * @ordered
	 */
	CATEGORY(4, "category", "category"),

	/**
	 * The '<em><b>Description</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DESCRIPTION_VALUE
	 * @generated
	 * @ordered
	 */
	DESCRIPTION(5, "description", "description");

	/**
	 * The '<em><b>Name</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Name</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NAME
	 * @model name="name"
	 * @generated
	 * @ordered
	 */
	public static final int NAME_VALUE = 0;

	/**
	 * The '<em><b>Namespace</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Namespace</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NAMESPACE
	 * @model name="namespace"
	 * @generated
	 * @ordered
	 */
	public static final int NAMESPACE_VALUE = 1;

	/**
	 * The '<em><b>Version</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Version</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #VERSION
	 * @model name="version"
	 * @generated
	 * @ordered
	 */
	public static final int VERSION_VALUE = 2;

	/**
	 * The '<em><b>Displayname</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Displayname</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DISPLAYNAME
	 * @model name="displayname"
	 * @generated
	 * @ordered
	 */
	public static final int DISPLAYNAME_VALUE = 3;

	/**
	 * The '<em><b>Category</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Category</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #CATEGORY
	 * @model name="category"
	 * @generated
	 * @ordered
	 */
	public static final int CATEGORY_VALUE = 4;

	/**
	 * The '<em><b>Description</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>Description</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #DESCRIPTION
	 * @model name="description"
	 * @generated
	 * @ordered
	 */
	public static final int DESCRIPTION_VALUE = 5;

	/**
	 * An array of all the '<em><b>Info Model Attribute</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final InfoModelAttribute[] VALUES_ARRAY =
		new InfoModelAttribute[] {
			NAME,
			NAMESPACE,
			VERSION,
			DISPLAYNAME,
			CATEGORY,
			DESCRIPTION,
		};

	/**
	 * A public read-only list of all the '<em><b>Info Model Attribute</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<InfoModelAttribute> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Info Model Attribute</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InfoModelAttribute get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InfoModelAttribute result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Info Model Attribute</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InfoModelAttribute getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			InfoModelAttribute result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Info Model Attribute</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static InfoModelAttribute get(int value) {
		switch (value) {
			case NAME_VALUE: return NAME;
			case NAMESPACE_VALUE: return NAMESPACE;
			case VERSION_VALUE: return VERSION;
			case DISPLAYNAME_VALUE: return DISPLAYNAME;
			case CATEGORY_VALUE: return CATEGORY;
			case DESCRIPTION_VALUE: return DESCRIPTION;
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
	private InfoModelAttribute(int value, String name, String literal) {
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
	
} //InfoModelAttribute
