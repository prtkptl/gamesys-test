package gamesys.codingexercise;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RegistrationTest {
    @Test
    public void testValidRegistration() {
        final String username = "test";
        final String password = "passWord1";
        final String dob = "30-12-2000";
        final String ssn = "123-45-6789";

        Registration registration = new Registration();
        registration.setUsername(username);
        registration.setPassword(password);
        registration.setDob(dob);
        registration.setSsn(ssn);

        assertThat(registration.getUsername(), is(username));
        assertThat(registration.getPassword(), is(password));
        assertThat(registration.getDob(), is(dob));
        assertThat(registration.getSsn(), is(ssn));
    }
}
