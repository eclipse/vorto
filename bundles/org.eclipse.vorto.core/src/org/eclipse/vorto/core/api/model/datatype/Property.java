/**
 */
package org.eclipse.vorto.core.api.model.datatype;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Property#getPresence <em>Presence</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Property#isMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Property#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Property#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Property#getConstraintRule <em>Constraint Rule</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Property#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.datatype.Property#getPropertyAttributes <em>Property Attributes</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getProperty()
 * @model
 * @generated
 */
public interface Property extends EObject {
	/**
	 * Returns the value of the '<em><b>Presence</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Presence</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Presence</em>' containment reference.
	 * @see #setPresence(Presence)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getProperty_Presence()
	 * @model containment="true"
	 * @generated
	 */
	Presence getPresence();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Property#getPresence <em>Presence</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Presence</em>' containment reference.
	 * @see #getPresence()
	 * @generated
	 */
	void setPresence(Presence value);

	/**
	 * Returns the value of the '<em><b>Multiplicity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Multiplicity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Multiplicity</em>' attribute.
	 * @see #setMultiplicity(boolean)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getProperty_Multiplicity()
	 * @model
	 * @generated
	 */
	boolean isMultiplicity();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Property#isMultiplicity <em>Multiplicity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Multiplicity</em>' attribute.
	 * @see #isMultiplicity()
	 * @generated
	 */
	void setMultiplicity(boolean value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getProperty_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Property#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getProperty_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Property#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Constraint Rule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraint Rule</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraint Rule</em>' containment reference.
	 * @see #setConstraintRule(ConstraintRule)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getProperty_ConstraintRule()
	 * @model containment="true"
	 * @generated
	 */
	ConstraintRule getConstraintRule();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Property#getConstraintRule <em>Constraint Rule</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint Rule</em>' containment reference.
	 * @see #getConstraintRule()
	 * @generated
	 */
	void setConstraintRule(ConstraintRule value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' containment reference.
	 * @see #setType(PropertyType)
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getProperty_Type()
	 * @model containment="true"
	 * @generated
	 */
	PropertyType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.datatype.Property#getType <em>Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' containment reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(PropertyType value);

	/**
	 * Returns the value of the '<em><b>Property Attributes</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.datatype.PropertyAttribute}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property Attributes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property Attributes</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.datatype.DatatypePackage#getProperty_PropertyAttributes()
	 * @model containment="true"
	 * @generated
	 */
	EList<PropertyAttribute> getPropertyAttributes();

} // Property
