package org.eclipse.vorto.repository.service;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class GeneratorsTest {

	/*
	 * Steps: Register a new generator with serviceKey, baseUrl and classifier
	 * Expected Result: A new Vorto Generator should be registered successfully. 
	 */
	@Ignore
	@Test
	public void toDotestRegisterGenerator1() {
		
		fail("To test registering a new generator");
	}
	/*
	 * Steps: Register a generator with duplicate serviceKey, baseUrl and classifier
	 * Expected Result: GeneratorAlreadyExistsException is thrown, coz, serviceKey already exists. 
	 */
	@Ignore
	@Test
	public void toDotestRegisterGenerator2() {
		
		fail("To test registering a new generator");
	}
	/*
	 * Steps: Unregister a generator based on the provided serviceKey
	 * Expected Result: The generator with the specific serviceKey is unregistered successfully. 
	 */
	@Ignore
	@Test
	public void toDotestUnregisteraGenerator() {		
		fail("To test unregistration of a generator with a specific serviceKey");
	}
	/*
	 * To test that unregistered generators will not be able to generate code from Information models. 
	 */
	@Ignore
	@Test
	public void toDotestUnregisteredGeneratorCannotGenerateCode() {		
		fail("To test unregistered generator will not be able to generate code");
	}
}
