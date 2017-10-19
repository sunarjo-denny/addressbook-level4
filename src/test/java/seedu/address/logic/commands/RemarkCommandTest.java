package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

public class RemarkCommandTest {

    public static final String TEST_REMARK = "This is a test remark";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_executeUndoableCommand_throwsCommandException() throws CommandException {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand remarkCommand = prepareCommand(outOfBoundsIndex, new Remark(TEST_REMARK));

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_addRemark_success() throws Exception {
        Person personWithRemark = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())).withRemark("Test Remark").build();

        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, personWithRemark.getRemark());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), personWithRemark);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_SUCCESS, personWithRemark);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList().get(0), personWithRemark);
    }

    @Test
    public void equals() {
        RemarkCommand firstRemarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(TEST_REMARK));
        RemarkCommand secondRemarkCommand = new RemarkCommand(INDEX_SECOND_PERSON, new Remark(TEST_REMARK));

        // same object therefore same content -> returns true
        assertTrue(firstRemarkCommand.equals(firstRemarkCommand));

        // same values -> return true
        assertTrue(firstRemarkCommand.equals(new RemarkCommand(INDEX_FIRST_PERSON,
                new Remark("This is a test remark"))));

        // different Indices -> return False
        assertFalse(firstRemarkCommand.equals(secondRemarkCommand));

        // different types -> return False
        assertFalse(firstRemarkCommand.equals("test"));

        // null -> return false
        assertFalse(firstRemarkCommand.equals(null));

    }

    /**
     * Returns a {@code RemarkCommand} with the parameter {@code index and remark}.
     */
    private RemarkCommand prepareCommand(Index index, Remark rmk) {
        RemarkCommand remarkCommand = new RemarkCommand(index, rmk);
        remarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return remarkCommand;
    }

}
