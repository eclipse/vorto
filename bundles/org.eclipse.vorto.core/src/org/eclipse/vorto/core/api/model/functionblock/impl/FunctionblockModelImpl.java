/**
 */
package org.eclipse.vorto.core.api.model.functionblock.impl;

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

import org.eclipse.vorto.core.api.model.datatype.Entity;

import org.eclipse.vorto.core.api.model.functionblock.FunctionBlock;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;

import org.eclipse.vorto.core.api.model.model.ModelReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl#getReferences <em>References</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl#getFunctionblock <em>Functionblock</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl#getEntities <em>Entities</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockModelImpl#getEnums <em>Enums</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FunctionblockModelImpl extends MinimalEObjectImpl.Container implements FunctionblockModel {
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
	 * The cached value of the '{@link #getFunctionblock() <em>Functionblock</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionblock()
	 * @generated
	 * @ordered
	 */
	protected FunctionBlock functionblock;

	/**
	 * The cached value of the '{@link #getEntities() <em>Entities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntities()
	 * @generated
	 * @ordered
	 */
	protected EList<Entity> entities;

	/**
	 * The cached value of the '{@link #getEnums() <em>Enums</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnums()
	 * @generated
	 * @ordered
	 */
	protected EList<org.eclipse.vorto.core.api.model.datatype.Enum> enums;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FunctionblockModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FunctionblockPackage.Literals.FUNCTIONBLOCK_MODEL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAMESPACE, oldNamespace, namespace));
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
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTIONBLOCK_MODEL__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelReference> getReferences() {
		if (references == null) {
			references = new EObjectContainmentEList<ModelReference>(ModelReference.class, this, FunctionblockPackage.FUNCTIONBLOCK_MODEL__REFERENCES);
		}
		return references;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionBlock getFunctionblock() {
		return functionblock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFunctionblock(FunctionBlock newFunctionblock, NotificationChain msgs) {
		FunctionBlock oldFunctionblock = functionblock;
		functionblock = newFunctionblock;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK, oldFunctionblock, newFunctionblock);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunctionblock(FunctionBlock newFunctionblock) {
		if (newFunctionblock != functionblock) {
			NotificationChain msgs = null;
			if (functionblock != null)
				msgs = ((InternalEObject)functionblock).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK, null, msgs);
			if (newFunctionblock != null)
				msgs = ((InternalEObject)newFunctionblock).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK, null, msgs);
			msgs = basicSetFunctionblock(newFunctionblock, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK, newFunctionblock, newFunctionblock));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Entity> getEntities() {
		if (entities == null) {
			entities = new EObjectContainmentEList<Entity>(Entity.class, this, FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENTITIES);
		}
		return entities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<org.eclipse.vorto.core.api.model.datatype.Enum> getEnums() {
		if (enums == null) {
			enums = new EObjectContainmentEList<org.eclipse.vorto.core.api.model.datatype.Enum>(org.eclipse.vorto.core.api.model.datatype.Enum.class, this, FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENUMS);
		}
		return enums;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__REFERENCES:
				return ((InternalEList<?>)getReferences()).basicRemove(otherEnd, msgs);
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK:
				return basicSetFunctionblock(null, msgs);
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENTITIES:
				return ((InternalEList<?>)getEntities()).basicRemove(otherEnd, msgs);
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENUMS:
				return ((InternalEList<?>)getEnums()).basicRemove(otherEnd, msgs);
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
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAME:
				return getName();
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAMESPACE:
				return getNamespace();
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__VERSION:
				return getVersion();
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__REFERENCES:
				return getReferences();
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK:
				return getFunctionblock();
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENTITIES:
				return getEntities();
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENUMS:
				return getEnums();
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
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAME:
				setName((String)newValue);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAMESPACE:
				setNamespace((String)newValue);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__VERSION:
				setVersion((String)newValue);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__REFERENCES:
				getReferences().clear();
				getReferences().addAll((Collection<? extends ModelReference>)newValue);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK:
				setFunctionblock((FunctionBlock)newValue);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENTITIES:
				getEntities().clear();
				getEntities().addAll((Collection<? extends Entity>)newValue);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENUMS:
				getEnums().clear();
				getEnums().addAll((Collection<? extends org.eclipse.vorto.core.api.model.datatype.Enum>)newValue);
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
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAMESPACE:
				setNamespace(NAMESPACE_EDEFAULT);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__REFERENCES:
				getReferences().clear();
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK:
				setFunctionblock((FunctionBlock)null);
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENTITIES:
				getEntities().clear();
				return;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENUMS:
				getEnums().clear();
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
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__NAMESPACE:
				return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__REFERENCES:
				return references != null && !references.isEmpty();
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__FUNCTIONBLOCK:
				return functionblock != null;
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENTITIES:
				return entities != null && !entities.isEmpty();
			case FunctionblockPackage.FUNCTIONBLOCK_MODEL__ENUMS:
				return enums != null && !enums.isEmpty();
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

} //FunctionblockModelImpl
