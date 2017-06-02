package org.eclipse.vorto.repository.service;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

public class UserSignUpTest {

	/*
	 * Steps: Leave the email field blank and click on Sign UP
	 * Expected Result: Sign Up button should be disabled
	 */
	@Ignore
	@Test
	public void toDotestMandatorySignUpEmail() {
		
		fail("To test that email field cannot be empty in the SignUp screen");
	}
	/*
	 * Steps: Enter .email@test.com in the email field
	 * Expected Result: Email should not be accepted. Leading dot in address is not allowed.
	 */
	@Ignore
	@Test
	public void toDotestValidSignUpEmail1() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email.@test.com in the email field
	 * Expected Result: Email should not be accepted. Trailing dot in address is not allowed. 
	 */
	@Ignore
	@Test
	public void toDotestValidSignUpEmail2() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email..email@test.com  in the email field
	 * Expected Result: Email should not be accepted. Multiple dots in address is not allowed. 
	 */
	@Ignore
	@Test
	public void toDotestValidSignUpEmail3() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter あいうえお@test.com  in the email field
	 * Expected Result: Email should not be accepted. Unicode characters in address is not allowed. 
	 */
	@Ignore
	@Test
	public void toDotestValidSignUpEmail4() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email@test  in the email field
	 * Expected Result: Email should not be accepted. Missing .com/.net/.org in address. 
	 */
	@Ignore
	@Test
	public void toDotestValidSignUpEmail5() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email@test.web  in the email field
	 * Expected Result: Email should not be accepted. .web is not valid address. 
	 */
	@Ignore
	@Test
	public void toDotestValidSignUpEmail6() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter email@test..com  in the email field
	 * Expected Result: Email should not be accepted. Double dots in the email field. 
	 */
	@Ignore
	@Test
	public void toDotestValidSignUpEmail7() {
		
		fail("To test that only valid email formats are accepted.");
	}
	/*
	 * Steps: Enter valid email in the email field. Enter the same email address in the email confirmation field
	 * Expected Result: Email should be accepted in email and the confirmation fields.
	 */
	@Ignore
	@Test
	public void toDotestMatchingSignUpEmail() {
		
		fail("Tests for matching email field value missing");
	}
	/*
	 * Steps: Enter valid email in the email field. Enter the different email address in the email confirmation field
	 * Expected Result: Email should not be accepted in the confirmation field. 
	 * A validation message that email ids do not match should be displayed.
	 */
	@Ignore
	@Test
	public void toDotestMisMatchingSignUpEmail() {
		
		fail("Tests for mismatching email field value missing");
	}
	/*
	 * Steps: Enter valid username >=5 e.g. testuser
	 * Expected Result: Username should be accepted
	 */
	@Ignore
	@Test
	public void toDotestValidUsername() {
		
		fail("Tests for valid username missing");
	}
	/*
	 * Steps: Enter Invalid Username with length < 5 e.g. test
	 * Expected Result: Username should not be accepted. Validation message that the username is too short should be displayed.
	 */
	@Ignore
	@Test
	public void toDotestInValidUsername() {
		
		fail("Tests for Invalid username missing");
	}
	/*
	 * Steps: Enter valid password with length >= 6 e.g. test123
	 * Expected Result: Password should be accepted.
	 */
	@Ignore
	@Test
	public void toDotestValidPassword() {
		
		fail("Tests for valid password missing");
	}
	/*
	 * Steps: Enter Invalid password with length < 6 e.g. test1
	 * Expected Result: Password should not be accepted.Validation message that the password is too short should be displayed.
	 */
	@Ignore
	@Test
	public void toDotestInValidPassword() {
		
		fail("Tests for Invalid password missing");
	}
	/*
	 * Steps: Enter different passwords in the password and the password confirmation fields
	 * Expected Result: Password should not be accepted.Validation message that the password do not match should be displayed.
	 */
	@Ignore
	@Test
	public void toDotestMismatchPassword() {
		
		fail("Tests for password mismatch is missing");
	}
	/*
	 * Steps: Enter an email address that exists in the system
	 * Expected Result: Email should not be accepted.Validation message that the email address already exists in the system should be displayed.
	 */
	@Ignore
	@Test
	public void toDotestExistingEmail() {
		
		fail("Tests for duplicate email missing");
	}
	/*
	 * Steps: Enter a username that exists in the system
	 * Expected Result: Password should not be accepted.Validation message that the password is too short should be displayed.
	 */
	@Ignore
	@Test
	public void toDotestExistingUser() {
		
		fail("Tests for duplicate username missing");
	}
}
