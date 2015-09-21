/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationSource;
import org.eclipse.vorto.core.api.model.mapping.DataTypeMappingModel;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingModel;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EntityPropertySource;
import org.eclipse.vorto.core.api.model.mapping.EntitySource;
import org.eclipse.vorto.core.api.model.mapping.EnumAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingModel;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumPropertySource;
import org.eclipse.vorto.core.api.model.mapping.EnumSource;
import org.eclipse.vorto.core.api.model.mapping.EventSource;
import org.eclipse.vorto.core.api.model.mapping.FaultSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingModel;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockPropertySource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttributeSource;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingModel;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelPropertySource;
import org.eclipse.vorto.core.api.model.mapping.InfomodelSource;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.OperationSource;
import org.eclipse.vorto.core.api.model.mapping.ReferenceTarget;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.mapping.StatusSource;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.core.api.model.mapping.Target;

import org.eclipse.vorto.core.api.model.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MappingPackageImpl extends EPackageImpl implements MappingPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappingModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelMappingModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infomodelSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelPropertySourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelAttributeSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockMappingModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockPropertySourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockAttributeSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass configurationSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass statusSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass operationSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityMappingModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entitySourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityPropertySourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityAttributeSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumMappingModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumPropertySourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumAttributeSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTypeMappingModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass targetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass referenceTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stereoTypeTargetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass faultSourceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum infoModelAttributeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum functionblockModelAttributeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum modelAttributeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MappingPackageImpl() {
		super(eNS_URI, MappingFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MappingPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MappingPackage init() {
		if (isInited) return (MappingPackage)EPackage.Registry.INSTANCE.getEPackage(MappingPackage.eNS_URI);

		// Obtain or create and register package
		MappingPackageImpl theMappingPackage = (MappingPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MappingPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MappingPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		InformationModelPackage.eINSTANCE.eClass();
		ModelPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theMappingPackage.createPackageContents();

		// Initialize created meta-data
		theMappingPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMappingPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MappingPackage.eNS_URI, theMappingPackage);
		return theMappingPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMappingModel() {
		return mappingModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMappingModel_Rules() {
		return (EReference)mappingModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMappingModel_TargetPlatform() {
		return (EAttribute)mappingModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelMappingModel() {
		return infoModelMappingModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelMappingRule() {
		return infoModelMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfomodelSource() {
		return infomodelSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfomodelSource_Model() {
		return (EReference)infomodelSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelPropertySource() {
		return infoModelPropertySourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelPropertySource_Property() {
		return (EReference)infoModelPropertySourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelAttributeSource() {
		return infoModelAttributeSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInfoModelAttributeSource_Attribute() {
		return (EAttribute)infoModelAttributeSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockMappingModel() {
		return functionBlockMappingModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockMappingRule() {
		return functionBlockMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockSource() {
		return functionBlockSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockSource_Model() {
		return (EReference)functionBlockSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockPropertySource() {
		return functionBlockPropertySourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockAttributeSource() {
		return functionBlockAttributeSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFunctionBlockAttributeSource_Attribute() {
		return (EAttribute)functionBlockAttributeSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConfigurationSource() {
		return configurationSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getConfigurationSource_Property() {
		return (EReference)configurationSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStatusSource() {
		return statusSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStatusSource_Property() {
		return (EReference)statusSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOperationSource() {
		return operationSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOperationSource_Operation() {
		return (EReference)operationSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEventSource() {
		return eventSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventSource_Event() {
		return (EReference)eventSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventSource_EventProperty() {
		return (EReference)eventSourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityMappingModel() {
		return entityMappingModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityMappingRule() {
		return entityMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntitySource() {
		return entitySourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntitySource_Model() {
		return (EReference)entitySourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityPropertySource() {
		return entityPropertySourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityPropertySource_Property() {
		return (EReference)entityPropertySourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityAttributeSource() {
		return entityAttributeSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntityAttributeSource_Attribute() {
		return (EAttribute)entityAttributeSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumMappingModel() {
		return enumMappingModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumMappingRule() {
		return enumMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumSource() {
		return enumSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumSource_Model() {
		return (EReference)enumSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumPropertySource() {
		return enumPropertySourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumPropertySource_Property() {
		return (EReference)enumPropertySourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumAttributeSource() {
		return enumAttributeSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnumAttributeSource_Attribute() {
		return (EAttribute)enumAttributeSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataTypeMappingModel() {
		return dataTypeMappingModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTarget() {
		return targetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReferenceTarget() {
		return referenceTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReferenceTarget_MappingModel() {
		return (EReference)referenceTargetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStereoTypeTarget() {
		return stereoTypeTargetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStereoTypeTarget_Name() {
		return (EAttribute)stereoTypeTargetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStereoTypeTarget_Attributes() {
		return (EReference)stereoTypeTargetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttribute() {
		return attributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttribute_Name() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttribute_Value() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSource() {
		return sourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFaultSource() {
		return faultSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFaultSource_Property() {
		return (EReference)faultSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMappingRule() {
		return mappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMappingRule_Target() {
		return (EReference)mappingRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMappingRule_Sources() {
		return (EReference)mappingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getInfoModelAttribute() {
		return infoModelAttributeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getFunctionblockModelAttribute() {
		return functionblockModelAttributeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getModelAttribute() {
		return modelAttributeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingFactory getMappingFactory() {
		return (MappingFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		mappingModelEClass = createEClass(MAPPING_MODEL);
		createEReference(mappingModelEClass, MAPPING_MODEL__RULES);
		createEAttribute(mappingModelEClass, MAPPING_MODEL__TARGET_PLATFORM);

		infoModelMappingModelEClass = createEClass(INFO_MODEL_MAPPING_MODEL);

		infoModelMappingRuleEClass = createEClass(INFO_MODEL_MAPPING_RULE);

		infomodelSourceEClass = createEClass(INFOMODEL_SOURCE);
		createEReference(infomodelSourceEClass, INFOMODEL_SOURCE__MODEL);

		infoModelPropertySourceEClass = createEClass(INFO_MODEL_PROPERTY_SOURCE);
		createEReference(infoModelPropertySourceEClass, INFO_MODEL_PROPERTY_SOURCE__PROPERTY);

		infoModelAttributeSourceEClass = createEClass(INFO_MODEL_ATTRIBUTE_SOURCE);
		createEAttribute(infoModelAttributeSourceEClass, INFO_MODEL_ATTRIBUTE_SOURCE__ATTRIBUTE);

		functionBlockMappingModelEClass = createEClass(FUNCTION_BLOCK_MAPPING_MODEL);

		functionBlockMappingRuleEClass = createEClass(FUNCTION_BLOCK_MAPPING_RULE);

		functionBlockSourceEClass = createEClass(FUNCTION_BLOCK_SOURCE);
		createEReference(functionBlockSourceEClass, FUNCTION_BLOCK_SOURCE__MODEL);

		functionBlockPropertySourceEClass = createEClass(FUNCTION_BLOCK_PROPERTY_SOURCE);

		functionBlockAttributeSourceEClass = createEClass(FUNCTION_BLOCK_ATTRIBUTE_SOURCE);
		createEAttribute(functionBlockAttributeSourceEClass, FUNCTION_BLOCK_ATTRIBUTE_SOURCE__ATTRIBUTE);

		configurationSourceEClass = createEClass(CONFIGURATION_SOURCE);
		createEReference(configurationSourceEClass, CONFIGURATION_SOURCE__PROPERTY);

		statusSourceEClass = createEClass(STATUS_SOURCE);
		createEReference(statusSourceEClass, STATUS_SOURCE__PROPERTY);

		operationSourceEClass = createEClass(OPERATION_SOURCE);
		createEReference(operationSourceEClass, OPERATION_SOURCE__OPERATION);

		eventSourceEClass = createEClass(EVENT_SOURCE);
		createEReference(eventSourceEClass, EVENT_SOURCE__EVENT);
		createEReference(eventSourceEClass, EVENT_SOURCE__EVENT_PROPERTY);

		entityMappingModelEClass = createEClass(ENTITY_MAPPING_MODEL);

		entityMappingRuleEClass = createEClass(ENTITY_MAPPING_RULE);

		entitySourceEClass = createEClass(ENTITY_SOURCE);
		createEReference(entitySourceEClass, ENTITY_SOURCE__MODEL);

		entityPropertySourceEClass = createEClass(ENTITY_PROPERTY_SOURCE);
		createEReference(entityPropertySourceEClass, ENTITY_PROPERTY_SOURCE__PROPERTY);

		entityAttributeSourceEClass = createEClass(ENTITY_ATTRIBUTE_SOURCE);
		createEAttribute(entityAttributeSourceEClass, ENTITY_ATTRIBUTE_SOURCE__ATTRIBUTE);

		enumMappingModelEClass = createEClass(ENUM_MAPPING_MODEL);

		enumMappingRuleEClass = createEClass(ENUM_MAPPING_RULE);

		enumSourceEClass = createEClass(ENUM_SOURCE);
		createEReference(enumSourceEClass, ENUM_SOURCE__MODEL);

		enumPropertySourceEClass = createEClass(ENUM_PROPERTY_SOURCE);
		createEReference(enumPropertySourceEClass, ENUM_PROPERTY_SOURCE__PROPERTY);

		enumAttributeSourceEClass = createEClass(ENUM_ATTRIBUTE_SOURCE);
		createEAttribute(enumAttributeSourceEClass, ENUM_ATTRIBUTE_SOURCE__ATTRIBUTE);

		dataTypeMappingModelEClass = createEClass(DATA_TYPE_MAPPING_MODEL);

		targetEClass = createEClass(TARGET);

		referenceTargetEClass = createEClass(REFERENCE_TARGET);
		createEReference(referenceTargetEClass, REFERENCE_TARGET__MAPPING_MODEL);

		stereoTypeTargetEClass = createEClass(STEREO_TYPE_TARGET);
		createEAttribute(stereoTypeTargetEClass, STEREO_TYPE_TARGET__NAME);
		createEReference(stereoTypeTargetEClass, STEREO_TYPE_TARGET__ATTRIBUTES);

		attributeEClass = createEClass(ATTRIBUTE);
		createEAttribute(attributeEClass, ATTRIBUTE__NAME);
		createEAttribute(attributeEClass, ATTRIBUTE__VALUE);

		sourceEClass = createEClass(SOURCE);

		faultSourceEClass = createEClass(FAULT_SOURCE);
		createEReference(faultSourceEClass, FAULT_SOURCE__PROPERTY);

		mappingRuleEClass = createEClass(MAPPING_RULE);
		createEReference(mappingRuleEClass, MAPPING_RULE__TARGET);
		createEReference(mappingRuleEClass, MAPPING_RULE__SOURCES);

		// Create enums
		infoModelAttributeEEnum = createEEnum(INFO_MODEL_ATTRIBUTE);
		functionblockModelAttributeEEnum = createEEnum(FUNCTIONBLOCK_MODEL_ATTRIBUTE);
		modelAttributeEEnum = createEEnum(MODEL_ATTRIBUTE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ModelPackage theModelPackage = (ModelPackage)EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);
		InformationModelPackage theInformationModelPackage = (InformationModelPackage)EPackage.Registry.INSTANCE.getEPackage(InformationModelPackage.eNS_URI);
		FunctionblockPackage theFunctionblockPackage = (FunctionblockPackage)EPackage.Registry.INSTANCE.getEPackage(FunctionblockPackage.eNS_URI);
		DatatypePackage theDatatypePackage = (DatatypePackage)EPackage.Registry.INSTANCE.getEPackage(DatatypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		mappingModelEClass.getESuperTypes().add(theModelPackage.getModel());
		infoModelMappingModelEClass.getESuperTypes().add(this.getMappingModel());
		infoModelMappingRuleEClass.getESuperTypes().add(this.getMappingRule());
		infomodelSourceEClass.getESuperTypes().add(this.getSource());
		infoModelPropertySourceEClass.getESuperTypes().add(this.getInfomodelSource());
		infoModelAttributeSourceEClass.getESuperTypes().add(this.getInfomodelSource());
		functionBlockMappingModelEClass.getESuperTypes().add(this.getMappingModel());
		functionBlockMappingRuleEClass.getESuperTypes().add(this.getMappingRule());
		functionBlockSourceEClass.getESuperTypes().add(this.getSource());
		functionBlockPropertySourceEClass.getESuperTypes().add(this.getFunctionBlockSource());
		functionBlockAttributeSourceEClass.getESuperTypes().add(this.getFunctionBlockSource());
		configurationSourceEClass.getESuperTypes().add(this.getFunctionBlockPropertySource());
		statusSourceEClass.getESuperTypes().add(this.getFunctionBlockPropertySource());
		operationSourceEClass.getESuperTypes().add(this.getFunctionBlockSource());
		eventSourceEClass.getESuperTypes().add(this.getFunctionBlockSource());
		entityMappingModelEClass.getESuperTypes().add(this.getDataTypeMappingModel());
		entityMappingRuleEClass.getESuperTypes().add(this.getMappingRule());
		entitySourceEClass.getESuperTypes().add(this.getSource());
		entityPropertySourceEClass.getESuperTypes().add(this.getEntitySource());
		entityAttributeSourceEClass.getESuperTypes().add(this.getEntitySource());
		enumMappingModelEClass.getESuperTypes().add(this.getDataTypeMappingModel());
		enumMappingRuleEClass.getESuperTypes().add(this.getMappingRule());
		enumSourceEClass.getESuperTypes().add(this.getSource());
		enumPropertySourceEClass.getESuperTypes().add(this.getEnumSource());
		enumAttributeSourceEClass.getESuperTypes().add(this.getEnumSource());
		dataTypeMappingModelEClass.getESuperTypes().add(this.getMappingModel());
		referenceTargetEClass.getESuperTypes().add(this.getTarget());
		stereoTypeTargetEClass.getESuperTypes().add(this.getTarget());
		faultSourceEClass.getESuperTypes().add(this.getFunctionBlockPropertySource());

		// Initialize classes, features, and operations; add parameters
		initEClass(mappingModelEClass, MappingModel.class, "MappingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMappingModel_Rules(), this.getMappingRule(), null, "rules", null, 0, -1, MappingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMappingModel_TargetPlatform(), ecorePackage.getEString(), "targetPlatform", null, 0, 1, MappingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelMappingModelEClass, InfoModelMappingModel.class, "InfoModelMappingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(infoModelMappingRuleEClass, InfoModelMappingRule.class, "InfoModelMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(infomodelSourceEClass, InfomodelSource.class, "InfomodelSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfomodelSource_Model(), theInformationModelPackage.getInformationModel(), null, "model", null, 0, 1, InfomodelSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelPropertySourceEClass, InfoModelPropertySource.class, "InfoModelPropertySource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfoModelPropertySource_Property(), theInformationModelPackage.getFunctionblockProperty(), null, "property", null, 0, 1, InfoModelPropertySource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelAttributeSourceEClass, InfoModelAttributeSource.class, "InfoModelAttributeSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInfoModelAttributeSource_Attribute(), this.getInfoModelAttribute(), "attribute", null, 0, 1, InfoModelAttributeSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockMappingModelEClass, FunctionBlockMappingModel.class, "FunctionBlockMappingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(functionBlockMappingRuleEClass, FunctionBlockMappingRule.class, "FunctionBlockMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(functionBlockSourceEClass, FunctionBlockSource.class, "FunctionBlockSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFunctionBlockSource_Model(), theFunctionblockPackage.getFunctionblockModel(), null, "model", null, 0, 1, FunctionBlockSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockPropertySourceEClass, FunctionBlockPropertySource.class, "FunctionBlockPropertySource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(functionBlockAttributeSourceEClass, FunctionBlockAttributeSource.class, "FunctionBlockAttributeSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFunctionBlockAttributeSource_Attribute(), this.getFunctionblockModelAttribute(), "attribute", null, 0, 1, FunctionBlockAttributeSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(configurationSourceEClass, ConfigurationSource.class, "ConfigurationSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConfigurationSource_Property(), theDatatypePackage.getProperty(), null, "property", null, 0, 1, ConfigurationSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statusSourceEClass, StatusSource.class, "StatusSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStatusSource_Property(), theDatatypePackage.getProperty(), null, "property", null, 0, 1, StatusSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(operationSourceEClass, OperationSource.class, "OperationSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOperationSource_Operation(), theFunctionblockPackage.getOperation(), null, "operation", null, 0, 1, OperationSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eventSourceEClass, EventSource.class, "EventSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEventSource_Event(), theFunctionblockPackage.getEvent(), null, "event", null, 0, 1, EventSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEventSource_EventProperty(), theDatatypePackage.getProperty(), null, "eventProperty", null, 0, 1, EventSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityMappingModelEClass, EntityMappingModel.class, "EntityMappingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(entityMappingRuleEClass, EntityMappingRule.class, "EntityMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(entitySourceEClass, EntitySource.class, "EntitySource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntitySource_Model(), theDatatypePackage.getEntity(), null, "model", null, 0, 1, EntitySource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityPropertySourceEClass, EntityPropertySource.class, "EntityPropertySource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityPropertySource_Property(), theDatatypePackage.getProperty(), null, "property", null, 0, 1, EntityPropertySource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityAttributeSourceEClass, EntityAttributeSource.class, "EntityAttributeSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEntityAttributeSource_Attribute(), this.getModelAttribute(), "attribute", null, 0, 1, EntityAttributeSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumMappingModelEClass, EnumMappingModel.class, "EnumMappingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(enumMappingRuleEClass, EnumMappingRule.class, "EnumMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(enumSourceEClass, EnumSource.class, "EnumSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnumSource_Model(), theDatatypePackage.getEnum(), null, "model", null, 0, 1, EnumSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumPropertySourceEClass, EnumPropertySource.class, "EnumPropertySource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnumPropertySource_Property(), theDatatypePackage.getEnumLiteral(), null, "property", null, 0, 1, EnumPropertySource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumAttributeSourceEClass, EnumAttributeSource.class, "EnumAttributeSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEnumAttributeSource_Attribute(), this.getModelAttribute(), "attribute", null, 0, 1, EnumAttributeSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataTypeMappingModelEClass, DataTypeMappingModel.class, "DataTypeMappingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(targetEClass, Target.class, "Target", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(referenceTargetEClass, ReferenceTarget.class, "ReferenceTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getReferenceTarget_MappingModel(), this.getMappingModel(), null, "mappingModel", null, 0, 1, ReferenceTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stereoTypeTargetEClass, StereoTypeTarget.class, "StereoTypeTarget", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStereoTypeTarget_Name(), ecorePackage.getEString(), "name", null, 0, 1, StereoTypeTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStereoTypeTarget_Attributes(), this.getAttribute(), null, "attributes", null, 0, -1, StereoTypeTarget.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributeEClass, Attribute.class, "Attribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAttribute_Name(), ecorePackage.getEString(), "name", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttribute_Value(), ecorePackage.getEString(), "value", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sourceEClass, Source.class, "Source", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(faultSourceEClass, FaultSource.class, "FaultSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFaultSource_Property(), theDatatypePackage.getProperty(), null, "property", null, 0, 1, FaultSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mappingRuleEClass, MappingRule.class, "MappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMappingRule_Target(), this.getTarget(), null, "target", null, 0, 1, MappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingRule_Sources(), this.getSource(), null, "sources", null, 0, -1, MappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(infoModelAttributeEEnum, InfoModelAttribute.class, "InfoModelAttribute");
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.NAME);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.NAMESPACE);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.VERSION);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.DISPLAYNAME);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.CATEGORY);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.DESCRIPTION);

		initEEnum(functionblockModelAttributeEEnum, FunctionblockModelAttribute.class, "FunctionblockModelAttribute");
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.NAME);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.NAMESPACE);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.VERSION);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.DISPLAYNAME);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.CATEGORY);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.DESCRIPTION);

		initEEnum(modelAttributeEEnum, ModelAttribute.class, "ModelAttribute");
		addEEnumLiteral(modelAttributeEEnum, ModelAttribute.NAME);
		addEEnumLiteral(modelAttributeEEnum, ModelAttribute.NAMESPACE);
		addEEnumLiteral(modelAttributeEEnum, ModelAttribute.VERSION);

		// Create resource
		createResource(eNS_URI);
	}

} //MappingPackageImpl
