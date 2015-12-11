/**
 */
package org.eclipse.vorto.core.api.model.mapping.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.vorto.core.api.model.mapping.*;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage
 * @generated
 */
public class MappingSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MappingPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingSwitch() {
		if (modelPackage == null) {
			modelPackage = MappingPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case MappingPackage.MAPPING_MODEL: {
				MappingModel mappingModel = (MappingModel)theEObject;
				T result = caseMappingModel(mappingModel);
				if (result == null) result = caseModel(mappingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_MAPPING_MODEL: {
				InfoModelMappingModel infoModelMappingModel = (InfoModelMappingModel)theEObject;
				T result = caseInfoModelMappingModel(infoModelMappingModel);
				if (result == null) result = caseMappingModel(infoModelMappingModel);
				if (result == null) result = caseModel(infoModelMappingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_MAPPING_RULE: {
				InfoModelMappingRule infoModelMappingRule = (InfoModelMappingRule)theEObject;
				T result = caseInfoModelMappingRule(infoModelMappingRule);
				if (result == null) result = caseMappingRule(infoModelMappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFOMODEL_SOURCE: {
				InfomodelSource infomodelSource = (InfomodelSource)theEObject;
				T result = caseInfomodelSource(infomodelSource);
				if (result == null) result = caseSource(infomodelSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_PROPERTY_SOURCE: {
				InfoModelPropertySource infoModelPropertySource = (InfoModelPropertySource)theEObject;
				T result = caseInfoModelPropertySource(infoModelPropertySource);
				if (result == null) result = caseInfomodelSource(infoModelPropertySource);
				if (result == null) result = caseSource(infoModelPropertySource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.INFO_MODEL_ATTRIBUTE_SOURCE: {
				InfoModelAttributeSource infoModelAttributeSource = (InfoModelAttributeSource)theEObject;
				T result = caseInfoModelAttributeSource(infoModelAttributeSource);
				if (result == null) result = caseInfomodelSource(infoModelAttributeSource);
				if (result == null) result = caseSource(infoModelAttributeSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_MAPPING_MODEL: {
				FunctionBlockMappingModel functionBlockMappingModel = (FunctionBlockMappingModel)theEObject;
				T result = caseFunctionBlockMappingModel(functionBlockMappingModel);
				if (result == null) result = caseMappingModel(functionBlockMappingModel);
				if (result == null) result = caseModel(functionBlockMappingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_MAPPING_RULE: {
				FunctionBlockMappingRule functionBlockMappingRule = (FunctionBlockMappingRule)theEObject;
				T result = caseFunctionBlockMappingRule(functionBlockMappingRule);
				if (result == null) result = caseMappingRule(functionBlockMappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_SOURCE: {
				FunctionBlockSource functionBlockSource = (FunctionBlockSource)theEObject;
				T result = caseFunctionBlockSource(functionBlockSource);
				if (result == null) result = caseSource(functionBlockSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_PROPERTY_SOURCE: {
				FunctionBlockPropertySource functionBlockPropertySource = (FunctionBlockPropertySource)theEObject;
				T result = caseFunctionBlockPropertySource(functionBlockPropertySource);
				if (result == null) result = caseFunctionBlockSource(functionBlockPropertySource);
				if (result == null) result = caseSource(functionBlockPropertySource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FUNCTION_BLOCK_ATTRIBUTE_SOURCE: {
				FunctionBlockAttributeSource functionBlockAttributeSource = (FunctionBlockAttributeSource)theEObject;
				T result = caseFunctionBlockAttributeSource(functionBlockAttributeSource);
				if (result == null) result = caseFunctionBlockSource(functionBlockAttributeSource);
				if (result == null) result = caseSource(functionBlockAttributeSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.CONFIGURATION_SOURCE: {
				ConfigurationSource configurationSource = (ConfigurationSource)theEObject;
				T result = caseConfigurationSource(configurationSource);
				if (result == null) result = caseFunctionBlockPropertySource(configurationSource);
				if (result == null) result = caseFunctionBlockSource(configurationSource);
				if (result == null) result = caseSource(configurationSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.STATUS_SOURCE: {
				StatusSource statusSource = (StatusSource)theEObject;
				T result = caseStatusSource(statusSource);
				if (result == null) result = caseFunctionBlockPropertySource(statusSource);
				if (result == null) result = caseFunctionBlockSource(statusSource);
				if (result == null) result = caseSource(statusSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.OPERATION_SOURCE: {
				OperationSource operationSource = (OperationSource)theEObject;
				T result = caseOperationSource(operationSource);
				if (result == null) result = caseFunctionBlockSource(operationSource);
				if (result == null) result = caseSource(operationSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.EVENT_SOURCE: {
				EventSource eventSource = (EventSource)theEObject;
				T result = caseEventSource(eventSource);
				if (result == null) result = caseFunctionBlockSource(eventSource);
				if (result == null) result = caseSource(eventSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_MAPPING_MODEL: {
				EntityMappingModel entityMappingModel = (EntityMappingModel)theEObject;
				T result = caseEntityMappingModel(entityMappingModel);
				if (result == null) result = caseDataTypeMappingModel(entityMappingModel);
				if (result == null) result = caseMappingModel(entityMappingModel);
				if (result == null) result = caseModel(entityMappingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_MAPPING_RULE: {
				EntityMappingRule entityMappingRule = (EntityMappingRule)theEObject;
				T result = caseEntityMappingRule(entityMappingRule);
				if (result == null) result = caseMappingRule(entityMappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_SOURCE: {
				EntitySource entitySource = (EntitySource)theEObject;
				T result = caseEntitySource(entitySource);
				if (result == null) result = caseSource(entitySource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_PROPERTY_SOURCE: {
				EntityPropertySource entityPropertySource = (EntityPropertySource)theEObject;
				T result = caseEntityPropertySource(entityPropertySource);
				if (result == null) result = caseEntitySource(entityPropertySource);
				if (result == null) result = caseSource(entityPropertySource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENTITY_ATTRIBUTE_SOURCE: {
				EntityAttributeSource entityAttributeSource = (EntityAttributeSource)theEObject;
				T result = caseEntityAttributeSource(entityAttributeSource);
				if (result == null) result = caseEntitySource(entityAttributeSource);
				if (result == null) result = caseSource(entityAttributeSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_MAPPING_MODEL: {
				EnumMappingModel enumMappingModel = (EnumMappingModel)theEObject;
				T result = caseEnumMappingModel(enumMappingModel);
				if (result == null) result = caseDataTypeMappingModel(enumMappingModel);
				if (result == null) result = caseMappingModel(enumMappingModel);
				if (result == null) result = caseModel(enumMappingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_MAPPING_RULE: {
				EnumMappingRule enumMappingRule = (EnumMappingRule)theEObject;
				T result = caseEnumMappingRule(enumMappingRule);
				if (result == null) result = caseMappingRule(enumMappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_SOURCE: {
				EnumSource enumSource = (EnumSource)theEObject;
				T result = caseEnumSource(enumSource);
				if (result == null) result = caseSource(enumSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_PROPERTY_SOURCE: {
				EnumPropertySource enumPropertySource = (EnumPropertySource)theEObject;
				T result = caseEnumPropertySource(enumPropertySource);
				if (result == null) result = caseEnumSource(enumPropertySource);
				if (result == null) result = caseSource(enumPropertySource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ENUM_ATTRIBUTE_SOURCE: {
				EnumAttributeSource enumAttributeSource = (EnumAttributeSource)theEObject;
				T result = caseEnumAttributeSource(enumAttributeSource);
				if (result == null) result = caseEnumSource(enumAttributeSource);
				if (result == null) result = caseSource(enumAttributeSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.DATA_TYPE_MAPPING_MODEL: {
				DataTypeMappingModel dataTypeMappingModel = (DataTypeMappingModel)theEObject;
				T result = caseDataTypeMappingModel(dataTypeMappingModel);
				if (result == null) result = caseMappingModel(dataTypeMappingModel);
				if (result == null) result = caseModel(dataTypeMappingModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.TARGET: {
				Target target = (Target)theEObject;
				T result = caseTarget(target);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.REFERENCE_TARGET: {
				ReferenceTarget referenceTarget = (ReferenceTarget)theEObject;
				T result = caseReferenceTarget(referenceTarget);
				if (result == null) result = caseTarget(referenceTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.STEREO_TYPE_TARGET: {
				StereoTypeTarget stereoTypeTarget = (StereoTypeTarget)theEObject;
				T result = caseStereoTypeTarget(stereoTypeTarget);
				if (result == null) result = caseTarget(stereoTypeTarget);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.ATTRIBUTE: {
				Attribute attribute = (Attribute)theEObject;
				T result = caseAttribute(attribute);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.SOURCE: {
				Source source = (Source)theEObject;
				T result = caseSource(source);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.FAULT_SOURCE: {
				FaultSource faultSource = (FaultSource)theEObject;
				T result = caseFaultSource(faultSource);
				if (result == null) result = caseFunctionBlockPropertySource(faultSource);
				if (result == null) result = caseFunctionBlockSource(faultSource);
				if (result == null) result = caseSource(faultSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case MappingPackage.MAPPING_RULE: {
				MappingRule mappingRule = (MappingRule)theEObject;
				T result = caseMappingRule(mappingRule);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingModel(MappingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Mapping Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Mapping Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelMappingModel(InfoModelMappingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Mapping Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelMappingRule(InfoModelMappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Infomodel Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Infomodel Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfomodelSource(InfomodelSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Property Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Property Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelPropertySource(InfoModelPropertySource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Info Model Attribute Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Info Model Attribute Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInfoModelAttributeSource(InfoModelAttributeSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Mapping Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Mapping Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockMappingModel(FunctionBlockMappingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Mapping Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockMappingRule(FunctionBlockMappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockSource(FunctionBlockSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Property Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Property Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockPropertySource(FunctionBlockPropertySource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Function Block Attribute Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Function Block Attribute Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFunctionBlockAttributeSource(FunctionBlockAttributeSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Configuration Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Configuration Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConfigurationSource(ConfigurationSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Status Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Status Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatusSource(StatusSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationSource(OperationSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Event Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Event Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEventSource(EventSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Mapping Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Mapping Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityMappingModel(EntityMappingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Mapping Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityMappingRule(EntityMappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntitySource(EntitySource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Property Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Property Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityPropertySource(EntityPropertySource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entity Attribute Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entity Attribute Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntityAttributeSource(EntityAttributeSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Mapping Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Mapping Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumMappingModel(EnumMappingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Mapping Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Mapping Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumMappingRule(EnumMappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumSource(EnumSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Property Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Property Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumPropertySource(EnumPropertySource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Enum Attribute Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Enum Attribute Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEnumAttributeSource(EnumAttributeSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Type Mapping Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Type Mapping Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataTypeMappingModel(DataTypeMappingModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTarget(Target object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reference Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reference Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReferenceTarget(ReferenceTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Stereo Type Target</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Stereo Type Target</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStereoTypeTarget(StereoTypeTarget object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Attribute</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Attribute</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAttribute(Attribute object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSource(Source object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fault Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fault Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFaultSource(FaultSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rule</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingRule(MappingRule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModel(Model object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //MappingSwitch
