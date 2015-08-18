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
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getInfoModelMappingRules <em>Info Model Mapping Rules</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getFunctionBlockMappings <em>Function Block Mappings</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getDataTypeMappings <em>Data Type Mappings</em>}</li>
 * </ul>
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
	 * Returns the value of the '<em><b>Function Block Mappings</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function Block Mappings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function Block Mappings</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_FunctionBlockMappings()
	 * @model containment="true"
	 * @generated
	 */
	EList<FunctionBlockMapping> getFunctionBlockMappings();

	/**
	 * Returns the value of the '<em><b>Data Type Mappings</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.DataTypeMapping}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Type Mappings</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Type Mappings</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_DataTypeMappings()
	 * @model containment="true"
	 * @generated
	 */
	EList<DataTypeMapping> getDataTypeMappings();

} // MappingModel
