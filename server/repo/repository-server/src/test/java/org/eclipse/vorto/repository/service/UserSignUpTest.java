package org.eclipse.vorto.repository.service;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserSignUpTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	/*
	 * Steps: Leave the email field blank and click on Sign UP
	 * Expected Result: Sign Up button should be disabled
	 */
	@Test
	public void ToDotestMandatorySignUpEmail() {
		
		fail("To test that email field cannot be empty in the SignUp screen");
	}
	/*
	 * Steps: Enter .email@test.com in the email field
	 * Expected Result: Email should not be accepted. Leading dot in address is not allowed.
	 */
	@Test
	public void ToDotestValidSignUpEmail1() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email.@test.com in the email field
	 * Expected Result: Email should not be accepted. Trailing dot in address is not allowed. 
	 */
	@Test
	public void ToDotestValidSignUpEmail2() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email..email@test.com  in the email field
	 * Expected Result: Email should not be accepted. Multiple dots in address is not allowed. 
	 */
	@Test
	public void ToDotestValidSignUpEmail3() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter あいうえお@test.com  in the email field
	 * Expected Result: Email should not be accepted. Unicode characters in address is not allowed. 
	 */
	@Test
	public void ToDotestValidSignUpEmail4() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email@test  in the email field
	 * Expected Result: Email should not be accepted. Missing .com/.net/.org in address. 
	 */
	@Test
	public void ToDotestValidSignUpEmail5() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email@test.web  in the email field
	 * Expected Result: Email should not be accepted. .web is not valid address. 
	 */
	@Test
	public void ToDotestValidSignUpEmail6() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email@test..com  in the email field
	 * Expected Result: Email should not be accepted. Double dots in the email field. 
	 */
	@Test
	public void ToDotestValidSignUpEmail7() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter valid email in the email field. Enter the same email address in the email confirmation field
	 * Expected Result: Email should be accepted in email and the confirmation fields.
	 */
	@Test
	public void ToDotestMatchingSignUpEmail() {
		
		fail("Tests for matching email field value missing");
	}
	/*
	 * Steps: Enter valid email in the email field. Enter the different email address in the email confirmation field
	 * Expected Result: Email should not be accepted in the confirmation field. 
	 * A validation message that email ids do not match should be displayed.
	 */
	@Test
	public void ToDotestMisMatchingSignUpEmail() {
		
		fail("Tests for mismatching email field value missing");
	}
	/*
	 * Steps: Enter valid username >=5 e.g. testuser
	 * Expected Result: Username should be accepted
	 */
	@Test
	public void ToDotestValidUsername() {
		
		fail("Tests for valid username missing");
	}
	/*
	 * Steps: Enter Invalid Username with length < 5 e.g. test
	 * Expected Result: Username should not be accepted. Validation message that the username is too short should be displayed.
	 */
	@Test
	public void ToDotestInValidUsername() {
		
		fail("Tests for Invalid username missing");
	}
	/*
	 * Steps: Enter valid password with length >= 6 e.g. test123
	 * Expected Result: Password should be accepted.
	 */
	@Test
	public void ToDotestValidPassword() {
		
		fail("Tests for valid password missing");
	}
	/*
	 * Steps: Enter Invalid password with length < 6 e.g. test1
	 * Expected Result: Password should not be accepted.Validation message that the password is too short should be displayed.
	 */
	@Test
	public void ToDotestInValidPassword() {
		
		fail("Tests for Invalid password missing");
	}
	/*
	 * Steps: Enter different passwords in the password and the password confirmation fields
	 * Expected Result: Password should not be accepted.Validation message that the password do not match should be displayed.
	 */
	@Test
	public void ToDotestMismatchPassword() {
		
		fail("Tests for password mismatch is missing");
	}
	/*
	 * Steps: Enter an email address that exists in the system
	 * Expected Result: Email should not be accepted.Validation message that the email address already exists in the system should be displayed.
	 */
	@Test
	public void ToDotestExistingEmail() {
		
		fail("Tests for duplicate email missing");
	}
	/*
	 * Steps: Enter a username that exists in the system
	 * Expected Result: Password should not be accepted.Validation message that the password is too short should be displayed.
	 */
	@Test
	public void ToDotestExistingUser() {
		
		fail("Tests for duplicate username missing");
	}
}
