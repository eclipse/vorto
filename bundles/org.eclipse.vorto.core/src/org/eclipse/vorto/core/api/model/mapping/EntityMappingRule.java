/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getEntityMappingElements <em>Entity Mapping Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getEntitySourceElement <em>Entity Source Element</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityMappingRule()
 * @model
 * @generated
 */
public interface EntityMappingRule extends EObject {
	/**
	 * Returns the value of the '<em><b>Entity Mapping Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.EntitySourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity Mapping Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity Mapping Elements</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityMappingRule_EntityMappingElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<EntitySourceElement> getEntityMappingElements();

	/**
	 * Returns the value of the '<em><b>Entity Source Element</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.EntitySourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity Source Element</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity Source Element</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityMappingRule_EntitySourceElement()
	 * @model containment="true"
	 * @generated
	 */
	EList<EntitySourceElement> getEntitySourceElement();

	/**
	 * Returns the value of the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' containment reference.
	 * @see #setTarget(EntityTargetElement)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEntityMappingRule_Target()
	 * @model containment="true"
	 * @generated
	 */
	EntityTargetElement getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule#getTarget <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' containment reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(EntityTargetElement value);

} // EntityMappingRule
