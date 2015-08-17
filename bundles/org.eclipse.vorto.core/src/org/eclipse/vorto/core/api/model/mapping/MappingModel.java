/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getInfoModelMappingRules <em>Info Model Mapping Rules</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getFunctionBlockMappingRules <em>Function Block Mapping Rules</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getDataTypeMappingRules <em>Data Type Mapping Rules</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel()
 * @model
 * @generated
 */
public interface MappingModel extends Model {
	/**
	 * Returns the value of the '<em><b>Info Model Mapping Rules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Info Model Mapping Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Info Model Mapping Rules</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_InfoModelMappingRules()
	 * @model containment="true"
	 * @generated
	 */
	EList<InfoModelMappingRule> getInfoModelMappingRules();

	/**
	 * Returns the value of the '<em><b>Function Block Mapping Rules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function Block Mapping Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function Block Mapping Rules</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_FunctionBlockMappingRules()
	 * @model containment="true"
	 * @generated
	 */
	EList<FunctionBlockMappingRule> getFunctionBlockMappingRules();

	/**
	 * Returns the value of the '<em><b>Data Type Mapping Rules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Type Mapping Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Type Mapping Rules</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_DataTypeMappingRules()
	 * @model containment="true"
	 * @generated
	 */
	EList<DataTypeMappingRule> getDataTypeMappingRules();

} // MappingModel
