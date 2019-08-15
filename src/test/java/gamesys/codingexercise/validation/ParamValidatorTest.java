package gamesys.codingexercise.validation;

import com.google.common.collect.ImmutableSet;
import gamesys.codingexercise.Registration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParamValidatorTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testUsername() {
        assertFalse(ParamValidator.validateUsername(null));
        assertFalse(ParamValidator.validateUsername("user name"));
        for (String symbol : ImmutableSet.of("?", "!", "{", ">", ":", ";", "@", "$", "£", "&", ".", "+", "_", "-", "/", "|"))
            assertFalse("Validation didn't fail for username: " + "user" + symbol,
                    ParamValidator.validateUsername("user" + symbol));

        assertTrue(ParamValidator.validateUsername("username"));
        assertTrue(ParamValidator.validateUsername("user123"));
        assertTrue(ParamValidator.validateUsername("userName"));
        assertTrue(ParamValidator.validateUsername("USER12"));
    }

    @Test
    public void testPassword() {
        assertFalse(ParamValidator.validatePassword(null));
        assertFalse(ParamValidator.validatePassword("pas")); // length
        assertFalse(ParamValidator.validatePassword("password")); // no uppercase
        assertFalse(ParamValidator.validatePassword("passWord"));  // no numbers

        assertTrue(ParamValidator.validatePassword("Pass1"));
    }

    @Test
    public void testDOB() {
        assertFalse(ParamValidator.validateDOB(null));
        assertFalse(ParamValidator.validateDOB("1980-01-01"));
        assertFalse(ParamValidator.validateDOB("12-30-1980"));
        assertFalse(ParamValidator.validateDOB("30/12/1980"));

        assertTrue(ParamValidator.validateDOB("30-12-1980"));
        assertTrue(ParamValidator.validateDOB("30-12-80"));
    }

    @Test
    public void testSSN() {
        assertFalse(ParamValidator.validateSSN(null));
        assertFalse(ParamValidator.validateSSN("12-345-678"));
        assertFalse(ParamValidator.validateSSN("123-456-78"));
        assertFalse(ParamValidator.validateSSN("901-56-678"));

        assertTrue(ParamValidator.validateSSN("123-45-6789"));
        assertTrue(ParamValidator.validateSSN("807-45-6789"));
    }

    @Test
    public void testValidateInvalidRegistrationUsername() throws ParamValidationException {
        Registration registration = getTestRegistration();
        registration.setUsername("user name");

        exceptionRule.expect(ParamValidationException.class);
        exceptionRule.expectMessage(ParamValidator.USERNAME_ERROR);
        ParamValidator.validate(registration);
    }

    @Test
    public void testValidateInvalidRegistrationPassword() throws ParamValidationException {
        Registration registration = getTestRegistration();
        registration.setPassword("pass1");

        exceptionRule.expect(ParamValidationException.class);
        exceptionRule.expectMessage(ParamValidator.PASSWORD_ERROR);
        ParamValidator.validate(registration);
    }

    @Test
    public void testValidateInvalidRegistrationDOB() throws ParamValidationException {
        Registration registration = getTestRegistration();
        registration.setDob("2010-01-30");

        exceptionRule.expect(ParamValidationException.class);
        exceptionRule.expectMessage(ParamValidator.DOB_ERROR);
        ParamValidator.validate(registration);
    }

    @Test
    public void testValidateInvalidRegistrationSSN() throws ParamValidationException {
        Registration registration = getTestRegistration();
        registration.setSsn("123-456-789");

        exceptionRule.expect(ParamValidationException.class);
        exceptionRule.expectMessage(ParamValidator.SSN_ERROR);
        ParamValidator.validate(registration);
    }

    @Test
    public void testInvalidRegistrationEmpty() throws ParamValidationException {
        exceptionRule.expect(ParamValidationException.class);
        exceptionRule.expectMessage(allOf(
                containsString(ParamValidator.USERNAME_ERROR),
                containsString(ParamValidator.PASSWORD_ERROR),
                containsString(ParamValidator.DOB_ERROR),
                containsString(ParamValidator.SSN_ERROR)
        ));
        ParamValidator.validate(new Registration());
    }

    private Registration getTestRegistration() {
        Registration registration = new Registration();
        registration.setUsername("username");
        registration.setPassword("Pass1");
        registration.setDob("30-12-1985");
        registration.setSsn("123-45-6788");
        return registration;
    }
}
