/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/

package org.eclipse.vorto.codegen.examples.bosch.fbservice.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.mapping.InvocationContext
import org.eclipse.vorto.codegen.examples.bosch.common.FbModelWrapper
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel

class AbstractDeviceServiceTemplate extends AbstractSourceTemplate implements IFileTemplate<FunctionblockModel> {
	
	override getFileName(FunctionblockModel ctx) {
		return "AbstractDeviceService.java";
	}
		
	override getContent(FunctionblockModel context,InvocationContext invocationContext) {
		'''
package «new FbModelWrapper(context).javaPackageName».api;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import «new FbModelWrapper(context).javaPackageName».api.mapping.EventMappingContext;
import «new FbModelWrapper(context).javaPackageName».api.mapping.EventMappingsConfiguration;
import «new FbModelWrapper(context).javaPackageName».api.mapping.IEventMapping;

import com.bosch.ism.FunctionBlockInstanceId;
import com.bosch.ism.ICustomEvent;
import com.bosch.ism.ICustomEventBuilderFactory;
import com.bosch.ism.ICustomEventSender;
import com.bosch.ism.IFunctionBlockInstance;
import com.bosch.ism.IFunctionBlockInstanceService;
import com.bosch.ism.IInformationModelInstance;
import com.bosch.ism.IInformationModelInstanceRegistry;
import com.bosch.ism.ILifeCycleEvent;
import com.bosch.ism.IPropertyChangedEvent;
import com.bosch.ism.InformationModelConstants;

/**
 * Do not modify, unless you absolutely must! 
 *
 * @param <DeviceDriver> Driver that is used for the communication to the device
 * @param <FunctionblockProperties> FunctionBlock Properties containing configuration status and fault data
 */
public abstract class AbstractDeviceService<DeviceDriver,FunctionblockProperties> extends ServiceTracker<DeviceDriver,DeviceDriver> implements IFunctionBlockInstanceService , EventHandler {

	protected static final Logger logger = LoggerFactory.getLogger(AbstractDeviceService.class);
	
	private static final TimeUnit BIND_TIMEOUT_UNIT = TimeUnit.SECONDS;
	private static final int BIND_TIMEOUT = 10;
	
	private final CountDownLatch bindCountDownLatch = new CountDownLatch(1);
	private final BundleContext bundleContext;
	
	protected IInformationModelInstance informationModelInstance;
	protected IInformationModelInstanceRegistry informationModelInstanceRegistry = null;
	
	protected ICustomEventSender customEventSender;
	
	protected ServiceRegistration<DeviceDriver> serviceRegistration;
	
	protected DeviceDriver deviceDriver;
	
	protected FunctionblockProperties deviceProperties;
	
	private Class<FunctionblockProperties> devicePropertiesClass;
		
	public AbstractDeviceService(final BundleContext context, final ServiceReference<DeviceDriver> reference, Class<FunctionblockProperties> devicePropertiesClass) {
		super(context, reference, null);
		this.devicePropertiesClass = devicePropertiesClass;
		this.bundleContext = context;
		open();
	}
	
	/**
	 * bind method will be called once the FB Service is registered Initializes
	 * the Properties & Configuration of the instance
	 *
	 * @see com.bosch.ism.IFunctionBlockInstanceService#bind(com.bosch.ism.IInformationModelInstance,
	 *      com.bosch.ism.IInformationModelInstanceRegistry)
	 */
	public void bind( final IInformationModelInstance informationModelInstance,
		final FunctionBlockInstanceId functionBlockInstanceId,
		final IInformationModelInstanceRegistry informationModelInstanceRegistry) {
		logger.trace("method Binding is invoked");
		
		// the created information model instance for our function block instance service will be
		// provided from the ISM by this method. From here on the information model instance can be
		// used to change properties or call service on it.
		this.informationModelInstance = informationModelInstance;
		
		// get the function block properties from the created function block instance and initialize
		// the properties with valid values, it is especially important to set properties that are
		// defined as required in the schema otherwise validation will fail and the function block
		// instance cannot be created successfully
		final IFunctionBlockInstance functionBlockInstance = informationModelInstance.getFunctionBlockInstance(functionBlockInstanceId);
		
		deviceProperties = functionBlockInstance.createOrGetFunctionBlockProperties(devicePropertiesClass);				
		
		// Call update method to complete the binding of the information model instance and to persist
		// the new function block instance on the agent hub and synchronize it to the central
		// registry. Only if this update call is successful and no exception is thrown, the
		// registration started by registering this service is
		// completed and the function block instance is valid and usable by other information model
		// instances or via the ISM REST API.
		informationModelInstanceRegistry.update(informationModelInstance);
		
		// now it is safe to access the information model instance because it's bound.
		bindCountDownLatch.countDown();
		
		customEventSender = getService(ICustomEventSender.class);
		
		EventMappingsConfiguration.getInstance().setCustomEventBuilderFactory(getService(ICustomEventBuilderFactory.class));
		registerMappingRules(EventMappingsConfiguration.getInstance());
	}
	
	protected abstract void registerMappingRules(EventMappingsConfiguration configuration);
	
	/**
	 * Gets the service from the OSGI bundle context
	 */
	protected <Service> Service getService(Class<Service> serviceClass) {
		ServiceReference<Service> serviceReference = bundleContext.getServiceReference(serviceClass);
		return (Service) bundleContext.getService(serviceReference);
	}
	
	@Override
	public DeviceDriver addingService(ServiceReference<DeviceDriver> reference) {
		logger.trace("called by osgi to add service for ServiceReference '{}'", reference);
			
		deviceDriver = context.getService(reference);
		  if (deviceDriver != null) {
		      newDeviceDetected(bundleContext, reference);
		      registerEventHandler();
		  }
		  
		return deviceDriver;				 
	}
	
	/**
	 *
	 * @param bundleContext the bundle context of the bundle to register an OSGi service
	 */
	@SuppressWarnings("unchecked")
	public void newDeviceDetected(final BundleContext bundleContext, ServiceReference<DeviceDriver> reference) {
		final Dictionary<String,String> registrationProperties = new Hashtable<String,String>();
		String informationModelInstanceId = resolveInformationModelInstanceId(reference);
		registrationProperties.put(InformationModelConstants.INFORMATION_MODEL_INSTANCE_ID, informationModelInstanceId);
		registrationProperties.put(InformationModelConstants.FUNCTION_BLOCK_INSTANCE_ID, resolveFunctionBlockInstanceId(reference));
		registrationProperties.put(InformationModelConstants.SHARED_KEY, resolveSharedKey());
		registrationProperties.put(org.osgi.framework.Constants.SERVICE_ID, informationModelInstanceId);		
		serviceRegistration = (ServiceRegistration<DeviceDriver>) bundleContext.registerService(	
			new String[] { getFunctionBlockModelClass().getName(), IFunctionBlockInstanceService.class.getName() }, this, addInformationModelInstanceProperties(registrationProperties));
		logger.trace("newDeviceDetected with registrationProperties: {}", registrationProperties);			
	}

	/**
	 * Return Java Model class of the function block
	 */
	protected abstract Class<?> getFunctionBlockModelClass();	
		
	private void registerEventHandler() {	
		Set<String> eventTopics = getEventTopics();
		Dictionary<String,Object> props = new Hashtable<>();
		props.put(EventConstants.EVENT_TOPIC, eventTopics.toArray(new String[eventTopics.size()]));
		context.registerService(EventHandler.class.getName(), this,
				props);
		logger.trace("registerEventHandler topics {}, with properties {} ", eventTopics, props);				
		
	}
	
	protected abstract Set<String> getEventTopics();
		
	protected abstract Dictionary<String,String> addInformationModelInstanceProperties(Dictionary<String,String> properties); 
	
	/**
	 * This function should be overriden to return this instance's ID
	 * @param deviceService
	 * @return instance id
	 */
	protected String resolveInformationModelInstanceId(ServiceReference<DeviceDriver> deviceService) {
		return this.getFunctionBlockModelClass().getSimpleName() + "-" + deviceService.getProperty(org.osgi.service.device.Constants.DEVICE_SERIAL);
	}
		
	/**
	 * This function should be overriden to return the fb instance ID
	 * @param deviceService
	 * @return instance id
	 */
	protected String resolveFunctionBlockInstanceId(ServiceReference<DeviceDriver> deviceService) {
		return resolveInformationModelInstanceId(deviceService) + ".fb1";
	}
	
	/**
	 * This function should be overriden to return the shared key
	 *
	 * @return shared key
	 */
	protected String resolveSharedKey() {
		return "0000";
	}
	
	@Override
	public void modifiedService(ServiceReference<DeviceDriver> reference, DeviceDriver service) {
		logger.trace("called by osgi to modify service for ServiceReference '{}'", reference);
	}
	
	@Override
	public void removedService(ServiceReference<DeviceDriver> reference, DeviceDriver service) {
		logger.trace("called by osgi to remove service for ServiceReference '{}'", reference);
		serviceRegistration.unregister();
		context.ungetService(reference);
		deviceDriver = null;
	}
	
	@Override
	public void lifecycleChanged(ILifeCycleEvent event) {
		logger.trace("method lifecycleChanged is invoked");
		if (ILifeCycleEvent.LifeCycle.INFORMATION_MODEL_DECOMISSIONED.equals(event.getType())) {
			if (serviceRegistration != null) {
				// clean up resources when according information model instance got decommissioned. Here
				// we have to unregister the device service, otherwise this function block instance may
				// run into exceptions when it updates no longer existent information model instance.
				serviceRegistration.unregister();
			}
		}
		
	}
		
	/**
	 * A safe getter to retrieve the information model instance. Waits with timeout of {@link #BIND_TIMEOUT} until
	 * {@link #bind(IInformationModelInstance, FunctionBlockInstanceId, IInformationModelInstanceRegistry)} has been
	 * called before. If timeout expires an {@link IllegalStateException} gets thrown.
	 *
	 * @return the bound information model instance
	 * @throws IllegalStateException if
	 *         {@link #bind(IInformationModelInstance, FunctionBlockInstanceId, IInformationModelInstanceRegistry)} was
	 *         not called within timeout
	 */
	protected IInformationModelInstance getInformationModelInstance() {
		try {
			final boolean countDown = bindCountDownLatch.await(BIND_TIMEOUT, BIND_TIMEOUT_UNIT);
			if (!countDown) {
				throw new IllegalStateException("Bind of function block instance was not called within timeout of " + BIND_TIMEOUT + BIND_TIMEOUT_UNIT);
			}
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		return informationModelInstance;
	}
	
	/**
	 * Clean up method before destroying this instance
	 */
	public void destroy(){
		logger.trace("method destroy is invoked");
	}

	/**
	 * Method is invoked by OSGI Event Bus, if base driver event topic occurs
	 */
	@Override
	public void handleEvent(Event event) {
		try {
			logger.trace("handling event with topic: {}", event.getTopic());
			IEventMapping<FunctionblockProperties> mapping = EventMappingsConfiguration.getInstance().getMappingByTopic(event.getTopic(),devicePropertiesClass);
			ICustomEvent mappedCBE = mapping.map(event, new EventMappingContext<FunctionblockProperties>(event.getTopic(), informationModelInstance));
			handleDeviceEvent(mappedCBE, event);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Sends out the device event to the M2M Platform
	 */
	protected void handleDeviceEvent(ICustomEvent mappedEvent, Event originalEvent) {
		logger.trace("Sending event to M2M platform ");
		customEventSender.postEvent(mappedEvent);
	}
	
	@Override
	public void handleEvent(IPropertyChangedEvent propertyChangedEvent) {
		// can be implemented by the specific implementation
	}
}
		'''
	}
	
	override getSubPath() {
		return "api/";
	}
	
	
	
}
