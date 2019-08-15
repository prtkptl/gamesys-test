package gamesys.codingexercise.validation;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class ParamValidationExceptionTest {
    @Test
    public void testException() {
        final String testError = "test error message";

        ParamValidationException exception = new ParamValidationException(testError);
        assertThat(exception.getMessage(), containsString(testError));
    }
}
