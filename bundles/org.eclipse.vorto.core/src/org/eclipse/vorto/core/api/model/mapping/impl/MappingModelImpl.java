/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.vorto.core.api.model.mapping.DataTypeMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;

import org.eclipse.vorto.core.api.model.model.ModelReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl#getReferences <em>References</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl#getInfoModelMappingRules <em>Info Model Mapping Rules</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl#getFunctionBlockMappings <em>Function Block Mappings</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.impl.MappingModelImpl#getDataTypeMappings <em>Data Type Mappings</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MappingModelImpl extends MinimalEObjectImpl.Container implements MappingModel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMESPACE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected String namespace = NAMESPACE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReferences() <em>References</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferences()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelReference> references;

	/**
	 * The cached value of the '{@link #getInfoModelMappingRules() <em>Info Model Mapping Rules</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInfoModelMappingRules()
	 * @generated
	 * @ordered
	 */
	protected EList<InfoModelMappingRule> infoModelMappingRules;

	/**
	 * The cached value of the '{@link #getFunctionBlockMappings() <em>Function Block Mappings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionBlockMappings()
	 * @generated
	 * @ordered
	 */
	protected EList<FunctionBlockMapping> functionBlockMappings;

	/**
	 * The cached value of the '{@link #getDataTypeMappings() <em>Data Type Mappings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataTypeMappings()
	 * @generated
	 * @ordered
	 */
	protected EList<DataTypeMapping> dataTypeMappings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MappingPackage.Literals.MAPPING_MODEL;
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
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.MAPPING_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNamespace(String newNamespace) {
		String oldNamespace = namespace;
		namespace = newNamespace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.MAPPING_MODEL__NAMESPACE, oldNamespace, namespace));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MappingPackage.MAPPING_MODEL__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelReference> getReferences() {
		if (references == null) {
			references = new EObjectContainmentEList<ModelReference>(ModelReference.class, this, MappingPackage.MAPPING_MODEL__REFERENCES);
		}
		return references;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InfoModelMappingRule> getInfoModelMappingRules() {
		if (infoModelMappingRules == null) {
			infoModelMappingRules = new EObjectContainmentEList<InfoModelMappingRule>(InfoModelMappingRule.class, this, MappingPackage.MAPPING_MODEL__INFO_MODEL_MAPPING_RULES);
		}
		return infoModelMappingRules;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FunctionBlockMapping> getFunctionBlockMappings() {
		if (functionBlockMappings == null) {
			functionBlockMappings = new EObjectContainmentEList<FunctionBlockMapping>(FunctionBlockMapping.class, this, MappingPackage.MAPPING_MODEL__FUNCTION_BLOCK_MAPPINGS);
		}
		return functionBlockMappings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<DataTypeMapping> getDataTypeMappings() {
		if (dataTypeMappings == null) {
			dataTypeMappings = new EObjectContainmentEList<DataTypeMapping>(DataTypeMapping.class, this, MappingPackage.MAPPING_MODEL__DATA_TYPE_MAPPINGS);
		}
		return dataTypeMappings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MappingPackage.MAPPING_MODEL__REFERENCES:
				return ((InternalEList<?>)getReferences()).basicRemove(otherEnd, msgs);
			case MappingPackage.MAPPING_MODEL__INFO_MODEL_MAPPING_RULES:
				return ((InternalEList<?>)getInfoModelMappingRules()).basicRemove(otherEnd, msgs);
			case MappingPackage.MAPPING_MODEL__FUNCTION_BLOCK_MAPPINGS:
				return ((InternalEList<?>)getFunctionBlockMappings()).basicRemove(otherEnd, msgs);
			case MappingPackage.MAPPING_MODEL__DATA_TYPE_MAPPINGS:
				return ((InternalEList<?>)getDataTypeMappings()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MappingPackage.MAPPING_MODEL__NAME:
				return getName();
			case MappingPackage.MAPPING_MODEL__NAMESPACE:
				return getNamespace();
			case MappingPackage.MAPPING_MODEL__VERSION:
				return getVersion();
			case MappingPackage.MAPPING_MODEL__REFERENCES:
				return getReferences();
			case MappingPackage.MAPPING_MODEL__INFO_MODEL_MAPPING_RULES:
				return getInfoModelMappingRules();
			case MappingPackage.MAPPING_MODEL__FUNCTION_BLOCK_MAPPINGS:
				return getFunctionBlockMappings();
			case MappingPackage.MAPPING_MODEL__DATA_TYPE_MAPPINGS:
				return getDataTypeMappings();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MappingPackage.MAPPING_MODEL__NAME:
				setName((String)newValue);
				return;
			case MappingPackage.MAPPING_MODEL__NAMESPACE:
				setNamespace((String)newValue);
				return;
			case MappingPackage.MAPPING_MODEL__VERSION:
				setVersion((String)newValue);
				return;
			case MappingPackage.MAPPING_MODEL__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection<? extends ModelReference>)newValue);
				return;
			case MappingPackage.MAPPING_MODEL__INFO_MODEL_MAPPING_RULES:
				getInfoModelMappingRules().clear();
				getInfoModelMappingRules().addAll((Collection<? extends InfoModelMappingRule>)newValue);
				return;
			case MappingPackage.MAPPING_MODEL__FUNCTION_BLOCK_MAPPINGS:
				getFunctionBlockMappings().clear();
				getFunctionBlockMappings().addAll((Collection<? extends FunctionBlockMapping>)newValue);
				return;
			case MappingPackage.MAPPING_MODEL__DATA_TYPE_MAPPINGS:
				getDataTypeMappings().clear();
				getDataTypeMappings().addAll((Collection<? extends DataTypeMapping>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MappingPackage.MAPPING_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case MappingPackage.MAPPING_MODEL__NAMESPACE:
				setNamespace(NAMESPACE_EDEFAULT);
				return;
			case MappingPackage.MAPPING_MODEL__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case MappingPackage.MAPPING_MODEL__REFERENCES:
				getReferences().clear();
				return;
			case MappingPackage.MAPPING_MODEL__INFO_MODEL_MAPPING_RULES:
				getInfoModelMappingRules().clear();
				return;
			case MappingPackage.MAPPING_MODEL__FUNCTION_BLOCK_MAPPINGS:
				getFunctionBlockMappings().clear();
				return;
			case MappingPackage.MAPPING_MODEL__DATA_TYPE_MAPPINGS:
				getDataTypeMappings().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MappingPackage.MAPPING_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case MappingPackage.MAPPING_MODEL__NAMESPACE:
				return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
			case MappingPackage.MAPPING_MODEL__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case MappingPackage.MAPPING_MODEL__REFERENCES:
				return references != null && !references.isEmpty();
			case MappingPackage.MAPPING_MODEL__INFO_MODEL_MAPPING_RULES:
				return infoModelMappingRules != null && !infoModelMappingRules.isEmpty();
			case MappingPackage.MAPPING_MODEL__FUNCTION_BLOCK_MAPPINGS:
				return functionBlockMappings != null && !functionBlockMappings.isEmpty();
			case MappingPackage.MAPPING_MODEL__DATA_TYPE_MAPPINGS:
				return dataTypeMappings != null && !dataTypeMappings.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", namespace: ");
		result.append(namespace);
		result.append(", version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} //MappingModelImpl
