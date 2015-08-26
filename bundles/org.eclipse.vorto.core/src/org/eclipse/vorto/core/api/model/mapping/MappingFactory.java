/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage
 * @generated
 */
public interface MappingFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MappingFactory eINSTANCE = org.eclipse.vorto.core.api.model.mapping.impl.MappingFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	MappingModel createMappingModel();

	/**
	 * Returns a new object of class '<em>Info Model Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Info Model Mapping</em>'.
	 * @generated
	 */
	InfoModelMapping createInfoModelMapping();

	/**
	 * Returns a new object of class '<em>Info Model Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Info Model Mapping Rule</em>'.
	 * @generated
	 */
	InfoModelMappingRule createInfoModelMappingRule();

	/**
	 * Returns a new object of class '<em>Infomodel Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Infomodel Source</em>'.
	 * @generated
	 */
	InfomodelSource createInfomodelSource();

	/**
	 * Returns a new object of class '<em>Info Model Property Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Info Model Property Source</em>'.
	 * @generated
	 */
	InfoModelPropertySource createInfoModelPropertySource();

	/**
	 * Returns a new object of class '<em>Info Model Attribute Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Info Model Attribute Source</em>'.
	 * @generated
	 */
	InfoModelAttributeSource createInfoModelAttributeSource();

	/**
	 * Returns a new object of class '<em>Function Block Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Mapping</em>'.
	 * @generated
	 */
	FunctionBlockMapping createFunctionBlockMapping();

	/**
	 * Returns a new object of class '<em>Function Block Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Mapping Rule</em>'.
	 * @generated
	 */
	FunctionBlockMappingRule createFunctionBlockMappingRule();

	/**
	 * Returns a new object of class '<em>Function Block Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Source</em>'.
	 * @generated
	 */
	FunctionBlockSource createFunctionBlockSource();

	/**
	 * Returns a new object of class '<em>Function Block Property Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Property Source</em>'.
	 * @generated
	 */
	FunctionBlockPropertySource createFunctionBlockPropertySource();

	/**
	 * Returns a new object of class '<em>Function Block Attribute Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Function Block Attribute Source</em>'.
	 * @generated
	 */
	FunctionBlockAttributeSource createFunctionBlockAttributeSource();

	/**
	 * Returns a new object of class '<em>Configuration Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Configuration Source</em>'.
	 * @generated
	 */
	ConfigurationSource createConfigurationSource();

	/**
	 * Returns a new object of class '<em>Status Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Status Source</em>'.
	 * @generated
	 */
	StatusSource createStatusSource();

	/**
	 * Returns a new object of class '<em>Operation Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Operation Source</em>'.
	 * @generated
	 */
	OperationSource createOperationSource();

	/**
	 * Returns a new object of class '<em>Event Resource</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Event Resource</em>'.
	 * @generated
	 */
	EventResource createEventResource();

	/**
	 * Returns a new object of class '<em>Entity Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Mapping</em>'.
	 * @generated
	 */
	EntityMapping createEntityMapping();

	/**
	 * Returns a new object of class '<em>Entity Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Mapping Rule</em>'.
	 * @generated
	 */
	EntityMappingRule createEntityMappingRule();

	/**
	 * Returns a new object of class '<em>Entity Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Source</em>'.
	 * @generated
	 */
	EntitySource createEntitySource();

	/**
	 * Returns a new object of class '<em>Entity Property Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Property Source</em>'.
	 * @generated
	 */
	EntityPropertySource createEntityPropertySource();

	/**
	 * Returns a new object of class '<em>Entity Attribute Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Entity Attribute Source</em>'.
	 * @generated
	 */
	EntityAttributeSource createEntityAttributeSource();

	/**
	 * Returns a new object of class '<em>Enum Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Enum Mapping</em>'.
	 * @generated
	 */
	EnumMapping createEnumMapping();

	/**
	 * Returns a new object of class '<em>Enum Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Enum Mapping Rule</em>'.
	 * @generated
	 */
	EnumMappingRule createEnumMappingRule();

	/**
	 * Returns a new object of class '<em>Enum Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Enum Source</em>'.
	 * @generated
	 */
	EnumSource createEnumSource();

	/**
	 * Returns a new object of class '<em>Enum Property Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Enum Property Source</em>'.
	 * @generated
	 */
	EnumPropertySource createEnumPropertySource();

	/**
	 * Returns a new object of class '<em>Enum Attribute Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Enum Attribute Source</em>'.
	 * @generated
	 */
	EnumAttributeSource createEnumAttributeSource();

	/**
	 * Returns a new object of class '<em>Data Type Mapping</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Type Mapping</em>'.
	 * @generated
	 */
	DataTypeMapping createDataTypeMapping();

	/**
	 * Returns a new object of class '<em>Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Target</em>'.
	 * @generated
	 */
	Target createTarget();

	/**
	 * Returns a new object of class '<em>Reference Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Reference Target</em>'.
	 * @generated
	 */
	ReferenceTarget createReferenceTarget();

	/**
	 * Returns a new object of class '<em>Stereo Type Target</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Stereo Type Target</em>'.
	 * @generated
	 */
	StereoTypeTarget createStereoTypeTarget();

	/**
	 * Returns a new object of class '<em>Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Attribute</em>'.
	 * @generated
	 */
	Attribute createAttribute();

	/**
	 * Returns a new object of class '<em>Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Source</em>'.
	 * @generated
	 */
	Source createSource();

	/**
	 * Returns a new object of class '<em>Fault Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fault Source</em>'.
	 * @generated
	 */
	FaultSource createFaultSource();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	MappingPackage getMappingPackage();

} //MappingFactory
