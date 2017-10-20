package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.testutil.TypicalPersons.ALICE;

import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.DetailsContainKeyphrasePredicate;
import seedu.address.model.person.ReadOnlyPerson;

public class SearchCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        SearchCommand searchCommandOne = new SearchCommand(new DetailsContainKeyphrasePredicate("testing one"));
        SearchCommand searchCommandTwo = new SearchCommand(new DetailsContainKeyphrasePredicate("testing two"));

        // same object -> returns true
        assertTrue(searchCommandOne.equals(searchCommandOne));
        assertTrue(searchCommandOne.equals(new SearchCommand(new DetailsContainKeyphrasePredicate("testing one"))));

        // different object -> returns false
        assertFalse(searchCommandOne.equals(searchCommandTwo));

        // different type -> returns false
        assertFalse(searchCommandOne.equals(1));

        // null -> returns false
        assertFalse(searchCommandOne.equals(null));
    }

    @Test
    public void execute_keyPhrase_singlePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1);
        SearchCommand command = prepareCommand("Alice Pauline");
        assertCommandSuccess(command, expectedMessage, Arrays.asList(ALICE));
    }

    /**
     * Parses {@code userInput} into a {@code SearchCommand}.
     */
    private SearchCommand prepareCommand(String userInput) {
        String preppedInput = userInput.trim();
        SearchCommand command =
                new SearchCommand(new DetailsContainKeyphrasePredicate(preppedInput));
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     *     - the command feedback is equal to {@code expectedMessage}<br>
     *     - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     *     - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertCommandSuccess(SearchCommand command, String expectedMessage, List<ReadOnlyPerson> expected) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expected, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
    }
}
