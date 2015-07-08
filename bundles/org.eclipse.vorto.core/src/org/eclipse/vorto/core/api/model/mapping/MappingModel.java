/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getInfomodel <em>Infomodel</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getRules <em>Rules</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel()
 * @model
 * @generated
 */
public interface MappingModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Infomodel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Infomodel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Infomodel</em>' reference.
	 * @see #setInfomodel(InformationModel)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_Infomodel()
	 * @model
	 * @generated
	 */
	InformationModel getInfomodel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getInfomodel <em>Infomodel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Infomodel</em>' reference.
	 * @see #getInfomodel()
	 * @generated
	 */
	void setInfomodel(InformationModel value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' attribute.
	 * @see #setTarget(String)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_Target()
	 * @model
	 * @generated
	 */
	String getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getTarget <em>Target</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' attribute.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(String value);

	/**
	 * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.Rule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rules</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_Rules()
	 * @model containment="true"
	 * @generated
	 */
	EList<Rule> getRules();

} // MappingModel
