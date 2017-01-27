/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
/**
 */
package org.eclipse.vorto.core.api.model.mapping.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.vorto.core.api.model.mapping.*;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage
 * @generated
 */
public class MappingAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MappingPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MappingPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingSwitch<Adapter> modelSwitch =
		new MappingSwitch<Adapter>() {
			@Override
			public Adapter caseMappingModel(MappingModel object) {
				return createMappingModelAdapter();
			}
			@Override
			public Adapter caseInfoModelMappingModel(InfoModelMappingModel object) {
				return createInfoModelMappingModelAdapter();
			}
			@Override
			public Adapter caseInfoModelMappingRule(InfoModelMappingRule object) {
				return createInfoModelMappingRuleAdapter();
			}
			@Override
			public Adapter caseInfomodelSource(InfomodelSource object) {
				return createInfomodelSourceAdapter();
			}
			@Override
			public Adapter caseInfoModelPropertySource(InfoModelPropertySource object) {
				return createInfoModelPropertySourceAdapter();
			}
			@Override
			public Adapter caseInfoModelAttributeSource(InfoModelAttributeSource object) {
				return createInfoModelAttributeSourceAdapter();
			}
			@Override
			public Adapter caseFunctionBlockMappingModel(FunctionBlockMappingModel object) {
				return createFunctionBlockMappingModelAdapter();
			}
			@Override
			public Adapter caseFunctionBlockMappingRule(FunctionBlockMappingRule object) {
				return createFunctionBlockMappingRuleAdapter();
			}
			@Override
			public Adapter caseFunctionBlockSource(FunctionBlockSource object) {
				return createFunctionBlockSourceAdapter();
			}
			@Override
			public Adapter caseFunctionBlockPropertySource(FunctionBlockPropertySource object) {
				return createFunctionBlockPropertySourceAdapter();
			}
			@Override
			public Adapter caseFunctionBlockAttributeSource(FunctionBlockAttributeSource object) {
				return createFunctionBlockAttributeSourceAdapter();
			}
			@Override
			public Adapter caseConfigurationSource(ConfigurationSource object) {
				return createConfigurationSourceAdapter();
			}
			@Override
			public Adapter caseStatusSource(StatusSource object) {
				return createStatusSourceAdapter();
			}
			@Override
			public Adapter caseOperationSource(OperationSource object) {
				return createOperationSourceAdapter();
			}
			@Override
			public Adapter caseEventSource(EventSource object) {
				return createEventSourceAdapter();
			}
			@Override
			public Adapter caseEntityMappingModel(EntityMappingModel object) {
				return createEntityMappingModelAdapter();
			}
			@Override
			public Adapter caseEntityMappingRule(EntityMappingRule object) {
				return createEntityMappingRuleAdapter();
			}
			@Override
			public Adapter caseEntitySource(EntitySource object) {
				return createEntitySourceAdapter();
			}
			@Override
			public Adapter caseEntityPropertySource(EntityPropertySource object) {
				return createEntityPropertySourceAdapter();
			}
			@Override
			public Adapter caseEntityAttributeSource(EntityAttributeSource object) {
				return createEntityAttributeSourceAdapter();
			}
			@Override
			public Adapter caseEnumMappingModel(EnumMappingModel object) {
				return createEnumMappingModelAdapter();
			}
			@Override
			public Adapter caseEnumMappingRule(EnumMappingRule object) {
				return createEnumMappingRuleAdapter();
			}
			@Override
			public Adapter caseEnumSource(EnumSource object) {
				return createEnumSourceAdapter();
			}
			@Override
			public Adapter caseEnumPropertySource(EnumPropertySource object) {
				return createEnumPropertySourceAdapter();
			}
			@Override
			public Adapter caseEnumAttributeSource(EnumAttributeSource object) {
				return createEnumAttributeSourceAdapter();
			}
			@Override
			public Adapter caseDataTypeMappingModel(DataTypeMappingModel object) {
				return createDataTypeMappingModelAdapter();
			}
			@Override
			public Adapter caseTarget(Target object) {
				return createTargetAdapter();
			}
			@Override
			public Adapter caseReferenceTarget(ReferenceTarget object) {
				return createReferenceTargetAdapter();
			}
			@Override
			public Adapter caseStereoTypeTarget(StereoTypeTarget object) {
				return createStereoTypeTargetAdapter();
			}
			@Override
			public Adapter caseAttribute(Attribute object) {
				return createAttributeAdapter();
			}
			@Override
			public Adapter caseSource(Source object) {
				return createSourceAdapter();
			}
			@Override
			public Adapter caseFaultSource(FaultSource object) {
				return createFaultSourceAdapter();
			}
			@Override
			public Adapter caseMappingRule(MappingRule object) {
				return createMappingRuleAdapter();
			}
			@Override
			public Adapter caseModel(Model object) {
				return createModelAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingModel
	 * @generated
	 */
	public Adapter createMappingModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingModel <em>Info Model Mapping Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMappingModel
	 * @generated
	 */
	public Adapter createInfoModelMappingModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule <em>Info Model Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule
	 * @generated
	 */
	public Adapter createInfoModelMappingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfomodelSource <em>Infomodel Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfomodelSource
	 * @generated
	 */
	public Adapter createInfomodelSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource <em>Info Model Property Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource
	 * @generated
	 */
	public Adapter createInfoModelPropertySourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource <em>Info Model Attribute Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource
	 * @generated
	 */
	public Adapter createInfoModelAttributeSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingModel <em>Function Block Mapping Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingModel
	 * @generated
	 */
	public Adapter createFunctionBlockMappingModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule <em>Function Block Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule
	 * @generated
	 */
	public Adapter createFunctionBlockMappingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource <em>Function Block Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource
	 * @generated
	 */
	public Adapter createFunctionBlockSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockPropertySource <em>Function Block Property Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockPropertySource
	 * @generated
	 */
	public Adapter createFunctionBlockPropertySourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource <em>Function Block Attribute Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource
	 * @generated
	 */
	public Adapter createFunctionBlockAttributeSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.ConfigurationSource <em>Configuration Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.ConfigurationSource
	 * @generated
	 */
	public Adapter createConfigurationSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.StatusSource <em>Status Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.StatusSource
	 * @generated
	 */
	public Adapter createStatusSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.OperationSource <em>Operation Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.OperationSource
	 * @generated
	 */
	public Adapter createOperationSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EventSource <em>Event Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EventSource
	 * @generated
	 */
	public Adapter createEventSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingModel <em>Entity Mapping Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMappingModel
	 * @generated
	 */
	public Adapter createEntityMappingModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EntityMappingRule <em>Entity Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityMappingRule
	 * @generated
	 */
	public Adapter createEntityMappingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EntitySource <em>Entity Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntitySource
	 * @generated
	 */
	public Adapter createEntitySourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EntityPropertySource <em>Entity Property Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityPropertySource
	 * @generated
	 */
	public Adapter createEntityPropertySourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource <em>Entity Attribute Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource
	 * @generated
	 */
	public Adapter createEntityAttributeSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingModel <em>Enum Mapping Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMappingModel
	 * @generated
	 */
	public Adapter createEnumMappingModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule <em>Enum Mapping Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumMappingRule
	 * @generated
	 */
	public Adapter createEnumMappingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EnumSource <em>Enum Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumSource
	 * @generated
	 */
	public Adapter createEnumSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EnumPropertySource <em>Enum Property Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumPropertySource
	 * @generated
	 */
	public Adapter createEnumPropertySourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource <em>Enum Attribute Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource
	 * @generated
	 */
	public Adapter createEnumAttributeSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingModel <em>Data Type Mapping Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.DataTypeMappingModel
	 * @generated
	 */
	public Adapter createDataTypeMappingModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.Target <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.Target
	 * @generated
	 */
	public Adapter createTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.ReferenceTarget <em>Reference Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.ReferenceTarget
	 * @generated
	 */
	public Adapter createReferenceTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget <em>Stereo Type Target</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget
	 * @generated
	 */
	public Adapter createStereoTypeTargetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.Attribute <em>Attribute</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.Attribute
	 * @generated
	 */
	public Adapter createAttributeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.Source <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.Source
	 * @generated
	 */
	public Adapter createSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.FaultSource <em>Fault Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.FaultSource
	 * @generated
	 */
	public Adapter createFaultSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.mapping.MappingRule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingRule
	 * @generated
	 */
	public Adapter createMappingRuleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.vorto.core.api.model.model.Model <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.vorto.core.api.model.model.Model
	 * @generated
	 */
	public Adapter createModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //MappingAdapterFactory
