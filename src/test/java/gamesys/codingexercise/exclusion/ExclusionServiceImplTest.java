package gamesys.codingexercise.exclusion;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExclusionServiceImplTest {
    @Test
    public void testValidate() {
        ExclusionService service = new ExclusionServiceImpl();
        assertFalse(service.validate("30-12-1970", "123-45-6789"));
        assertTrue(service.validate("30-12-1970", "678-45-6789"));
    }
}
