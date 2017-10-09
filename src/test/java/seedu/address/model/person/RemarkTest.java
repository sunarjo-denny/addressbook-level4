package seedu.address.model.person;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RemarkTest {

    @Test
    public void equals() {
        Remark remark = new Remark("Some remark.");

        // test for same object
        assertTrue(remark.equals(remark));

        // test for same value
        Remark remarkOfSameValue = new Remark(remark.value);
        assertTrue(remark.equals(remarkOfSameValue));

        // test for different type
        assertFalse(remark.equals("String"));

        // test for null case
        assertFalse(remark.equals(null));

        // test for different person
        Remark differentRemark = new Remark("Few remarks.");
        assertFalse(remark.equals(differentRemark));
    }
}
