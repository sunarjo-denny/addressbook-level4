# zenghou
###### /java/guitests/guihandles/PersonProfileHandle.java
``` java
package guitests.guihandles;

import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * Provides a handle to a person profile. Similar to {@see PersonCardHandle}
 */
public class PersonProfileHandle extends NodeHandle<Node> {
    public static final String PROFILE_FIELD_ID = "#profile";
    public static final String NAME_FIELD_ID = "#name";
    public static final String REMARK_FIELD_ID = "#remark";
    public static final String TAG_FIELD_ID = "#tags";

    private final Label profileLabel;
    private final Label nameLabel;
    private final Label remarkLabel;
    private final List<Label> tagLabels;

    public PersonProfileHandle(Node profileNode) {
        super(profileNode);

        this.profileLabel = getChildNode(PROFILE_FIELD_ID);
        this.nameLabel = getChildNode(NAME_FIELD_ID);
        this.remarkLabel = getChildNode(REMARK_FIELD_ID);

        Region tagsContainer = getChildNode(TAG_FIELD_ID);
        this.tagLabels = tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(Label.class::cast)
                .collect(Collectors.toList());
    }

    public String getProfile() {
        return profileLabel.getText();
    }

    public String getName() {
        return nameLabel.getText();
    }

    public String getRemark() {
        return remarkLabel.getText();
    }

    public List<String> getTags() {
        return tagLabels
                .stream()
                .map(Label::getText)
                .collect(Collectors.toList());
    }
}
```
###### /java/seedu/address/logic/commands/AddCommandIntegrationTest.java
``` java
    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());
        model.getUserCreds().validateCurrentSession(); // validate user
    }
```
###### /java/seedu/address/logic/commands/AddCommandIntegrationTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        Person validPerson = new PersonBuilder().build();

        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        assertCommandFailure(prepareCommand(validPerson, userCredsNotValidatedModel), userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/AddCommandTest.java
``` java
        @Override
        public void updateUserCreds() {
            fail("This method should not be called");
        }

        @Override
        public UserCreds getUserCreds() {
            // returns validated userCreds
            UserCreds userCreds = new UserCreds();
            userCreds.validateCurrentSession();
            return userCreds;
        }
```
###### /java/seedu/address/logic/commands/ClearCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());
        assertCommandFailure(prepareCommand(userCredsNotValidatedModel), userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/DeleteCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        assertCommandFailure(prepareCommand(INDEX_FIRST_PERSON), userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/EditCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        Command cmd = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        cmd.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(cmd, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/ExportCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());

        List<Index> indexes = Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(2));
        String filePath = "TestFile.xml";
        ExportCommand exportCommand = new ExportCommand(indexes, filePath);

        exportCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(exportCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/FindCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());

        NameContainsKeywordsPredicate firstPredicate =
                new NameContainsKeywordsPredicate(Collections.singletonList("first"));
        FindCommand findCommand = new FindCommand(firstPredicate);

        findCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(findCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/HistoryCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());

        HistoryCommand historyCommand = new HistoryCommand();

        historyCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(historyCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/ImportCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());

        String filePath = "SomeFile.xml";
        ImportCommand importCommand = new ImportCommand(filePath);

        importCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(importCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/ListCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        ListCommand listCommand = new ListCommand();

        listCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(listCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/LoginCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.user.UserCreds;
import seedu.address.model.user.UserPrefs;

public class LoginCommandTest {
    private Model model;

    @Before
    public void setup() {
        model = new ModelManager(new AddressBook(), new UserPrefs(),
                new UserCreds("test", "123"));
    }
    @Test
    public void execute_validUser_success() throws Exception {
        LoginCommand loginCommand = prepareCommand("test", "123");
        loginCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        Model expectedModel = new ModelManager();
        expectedModel.getUserCreds().validateCurrentSession();

        assertLoginSuccess(loginCommand, model, LoginCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_invalidUsername_failure() throws Exception {
        LoginCommand loginCommand = prepareCommand("wrongUserName", "123");
        loginCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandFailure(loginCommand, model, LoginCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_invalidPassword_failure() throws Exception {
        LoginCommand loginCommand = prepareCommand("test", "aslkdamlskd");
        loginCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandFailure(loginCommand, model, LoginCommand.MESSAGE_FAILURE);
    }

    @Test
    public void execute_blankUsernameAndPassword_failure() throws Exception {
        LoginCommand loginCommand = prepareCommand("", "");
        loginCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandFailure(loginCommand, model, LoginCommand.MESSAGE_FAILURE);
    }

    private LoginCommand prepareCommand(String username, String password) {
        return new LoginCommand(username, password);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel}'s UserCreds matches {@code expectedModel}'s UserCreds
     */
    public static void assertLoginSuccess(Command command, Model actualModel, String expectedMessage,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel.getUserCreds().isValidSession(), actualModel.getUserCreds().isValidSession());
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

}
```
###### /java/seedu/address/logic/commands/RedoCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        RedoCommand redoCommand = new RedoCommand();

        redoCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(redoCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/RemarkCommandTest.java
``` java
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
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.model.user.UserCreds;
import seedu.address.model.user.UserPrefs;
import seedu.address.testutil.PersonBuilder;

public class RemarkCommandTest {

    public static final String TEST_REMARK = "This is a test remark";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());

    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(TEST_REMARK));

        remarkCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(remarkCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }

    @Test
    public void execute_executeUndoableCommand_throwsCommandException() throws CommandException {
        model.getUserCreds().validateCurrentSession(); // validate user
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand remarkCommand = prepareCommand(outOfBoundsIndex, new Remark(TEST_REMARK));

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_addRemark_success() throws Exception {
        Person personWithRemark = new PersonBuilder(model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())).withRemark("Test Remark").build();

        RemarkCommand remarkCommand = prepareCommand(INDEX_FIRST_PERSON, personWithRemark.getRemark());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs(),
                new UserCreds());
        expectedModel.updateUserCreds(); // validate user
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
        model.getUserCreds().validateCurrentSession(); // validate user
        RemarkCommand remarkCommand = new RemarkCommand(index, rmk);
        remarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return remarkCommand;
    }

}
```
###### /java/seedu/address/logic/commands/SearchCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalPersons.ALICE;

import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.DetailsContainKeyphrasePredicate;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.user.UserCreds;
import seedu.address.model.user.UserPrefs;


public class SearchCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());

    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        SearchCommand searchCommand = new SearchCommand(new DetailsContainKeyphrasePredicate("testing one"));

        searchCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(searchCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }

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
        model.getUserCreds().validateCurrentSession(); // validate user
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
        model.getUserCreds().validateCurrentSession(); // validate user
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());
        try {
            CommandResult commandResult = command.execute();

            assertEquals(expectedMessage, commandResult.feedbackToUser);
            assertEquals(expected, model.getFilteredPersonList());
            assertEquals(expectedAddressBook, model.getAddressBook());
        } catch (CommandException ce) {
            ce.printStackTrace();
        }
    }
}
```
###### /java/seedu/address/logic/commands/SelectCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());
        SelectCommand selectCommand = new SelectCommand(INDEX_FIRST_PERSON);

        selectCommand.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(selectCommand, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/commands/UndoCommandTest.java
``` java
    @Test
    public void execute_invalidUser_failure() throws Exception {
        String userNotLoggedInMessage = "Invalid session! Please log in first! \n"
                + LoginCommand.MESSAGE_USAGE;

        Model userCredsNotValidatedModel = new ModelManager(model.getAddressBook(), new UserPrefs(), new UserCreds());

        deleteCommandOne.setData(userCredsNotValidatedModel, new CommandHistory(), new UndoRedoStack());
        assertCommandFailure(deleteCommandOne, userCredsNotValidatedModel,
                userNotLoggedInMessage);
    }
```
###### /java/seedu/address/logic/parser/LoginCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static junit.framework.TestCase.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class LoginCommandParserTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private LoginCommandParser parser = new LoginCommandParser();

    @Test
    public void parse_emptyArg_throwsParserException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoginCommand.MESSAGE_USAGE));
        parser.parse("  ");
    }

    @Test
    public void parse_correctArg_returnCorrectCommand() throws Exception {
        // no leading or trailing white space
        Command cmd = parser.parse("login usr/test pwd/123456");
        assertEquals(cmd, new LoginCommand("test", "123456"));

        // with leading and trailing white space
        Command cmdWithSpace = parser.parse("login     usr/ test    pwd/ 123456");
        assertEquals(cmdWithSpace, new LoginCommand("test", "123456"));
    }
}
```
###### /java/seedu/address/logic/parser/RemarkCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.person.Remark;

public class RemarkCommandParserTest {
    private RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_validArgs_returnsRemarkCommand() {
        // with remark
        RemarkCommand rmkCmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("Likes to drink coffee"));
        assertParseSuccess(parser, "1 " + PREFIX_REMARK + "Likes to drink coffee", rmkCmd);

        // without remark
        assertParseSuccess(parser, "1 " + PREFIX_REMARK, new RemarkCommand(INDEX_FIRST_PERSON,
                new Remark("")));
    }

    @Test
    public void parse_invalidPreamble_throwsParseException() {
        // negative index
        assertParseFailure(parser, "-1" + PREFIX_REMARK + "Likes to drink coffee",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));

        // zero index
        assertParseFailure(parser, "0" + PREFIX_REMARK + "Likes to drink coffee",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));

        // no index
        assertParseFailure(parser,  PREFIX_REMARK + "Likes to drink coffee",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));

        // alphabetical index
        assertParseFailure(parser, "a" + PREFIX_REMARK + "Likes to drink coffee",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));

        // invalid prefix
        assertParseFailure(parser, "1 /re Likes to drink coffee", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                RemarkCommand.MESSAGE_USAGE));
    }
}
```
###### /java/seedu/address/logic/parser/SearchCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.person.DetailsContainKeyphrasePredicate;

public class SearchCommandParserTest {

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        // userInput string cannot be empty
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsSearchCommand() {
        // with multiple spaces in between
        SearchCommand expectedSearchCommand =
                new SearchCommand(new DetailsContainKeyphrasePredicate("Alice     Bob"));
        assertParseSuccess(parser, "Alice     Bob ", expectedSearchCommand);
    }

}
```
###### /java/seedu/address/model/person/DetailsContainKeyphrasePredicateTest.java
``` java
package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.PersonBuilder;

public class DetailsContainKeyphrasePredicateTest {
    private ReadOnlyPerson samplePerson = new PersonBuilder().withName("Peter Lim")
            .withPhone("99887766")
            .withEmail("peterlim@gmail.com")
            .withAddress("Orchard Towers Block 39 #13-12")
            .withTags("business", "professional")
            .withRemark("met during a networking event")
            .build();

    @Test
    public void equals() {
        String keyphraseOne = "this is the first sample keyphrase";
        String keyphraseTwo = "this is the second sample keyphrase";

        DetailsContainKeyphrasePredicate firstPredicate = new DetailsContainKeyphrasePredicate(keyphraseOne);
        DetailsContainKeyphrasePredicate secondPredicate = new DetailsContainKeyphrasePredicate(keyphraseTwo);

        // same object
        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(secondPredicate.equals(secondPredicate));
        assertTrue(firstPredicate.equals(new DetailsContainKeyphrasePredicate("this is the first sample keyphrase")));

        // different object
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(secondPredicate.equals(firstPredicate));

        // different type
        assertFalse(firstPredicate.equals(1));
    }

    @Test
    public void testDetailsDoNotContainKeyphrase_returnsFalse() {
        // keyphrase (more than 1 word) neither matches details partially or in full
        DetailsContainKeyphrasePredicate predicate = new DetailsContainKeyphrasePredicate("pineapple banana");
        assertFalse(predicate.test(samplePerson));

        // keyphrase (1 word with extra alphabet) neither matches details partially or in full
        DetailsContainKeyphrasePredicate predicateTwo = new DetailsContainKeyphrasePredicate("professionals");
        assertFalse(predicateTwo.test(samplePerson));

        // keyphrase (2 word with extra alphabet) neither matches details partially or in full
        DetailsContainKeyphrasePredicate predicateThree = new DetailsContainKeyphrasePredicate("Peter Lim Tan");
        assertFalse(predicateThree.test(samplePerson));
    }

    @Test
    public void testDetailsContainKeyphrase_returnsTrue() {
        // matches Name in full
        DetailsContainKeyphrasePredicate predicateNameFull = new DetailsContainKeyphrasePredicate("Peter Lim");
        assertTrue(predicateNameFull.test(samplePerson));

        // matches Name partially
        DetailsContainKeyphrasePredicate predicateNamePartial = new DetailsContainKeyphrasePredicate("ter Li");
        assertTrue(predicateNamePartial.test(samplePerson));

        // matches Name partially
        DetailsContainKeyphrasePredicate predicateNameDifferentCase = new DetailsContainKeyphrasePredicate("ER Li");
        assertTrue(predicateNameDifferentCase.test(samplePerson));

        // matches Phone in full
        DetailsContainKeyphrasePredicate predicatePhoneFull = new DetailsContainKeyphrasePredicate("99887766");
        assertTrue(predicatePhoneFull.test(samplePerson));

        // matches Phone partially
        DetailsContainKeyphrasePredicate predicatePhonePartial = new DetailsContainKeyphrasePredicate("8877");
        assertTrue(predicatePhonePartial.test(samplePerson));

        // matches Email in full
        DetailsContainKeyphrasePredicate predicateEmailFull = new DetailsContainKeyphrasePredicate("peterlim@gmail."
                + "com");
        assertTrue(predicateEmailFull.test(samplePerson));

        // matches Email partially
        DetailsContainKeyphrasePredicate predicateEmailPartially = new DetailsContainKeyphrasePredicate("gmail.com");
        assertTrue(predicateEmailPartially.test(samplePerson));

        // matches Email different case
        DetailsContainKeyphrasePredicate predicateEmailDifferentCase = new DetailsContainKeyphrasePredicate("GMAIL."
                + "com");
        assertTrue(predicateEmailDifferentCase.test(samplePerson));

        // matches Address in full
        DetailsContainKeyphrasePredicate predicateAddressFull = new DetailsContainKeyphrasePredicate("Orchard Towers "
                + "Block 39 #13-12");
        assertTrue(predicateAddressFull.test(samplePerson));

        // matches Address partially
        DetailsContainKeyphrasePredicate predicateAddressPartially = new DetailsContainKeyphrasePredicate("39 #13-12");
        assertTrue(predicateAddressPartially.test(samplePerson));

        // matches Address in full, different case
        DetailsContainKeyphrasePredicate predicateAddressDifferentCase = new DetailsContainKeyphrasePredicate("Orchard "
                + "Towers Block 39 #13-12");
        assertTrue(predicateAddressDifferentCase.test(samplePerson));

        // matches Tag in full
        DetailsContainKeyphrasePredicate predicateTagFull = new DetailsContainKeyphrasePredicate("professional");
        assertTrue(predicateTagFull.test(samplePerson));

        // matches Tag partially
        DetailsContainKeyphrasePredicate predicateTagPartial = new DetailsContainKeyphrasePredicate("ssional");
        assertTrue(predicateTagPartial.test(samplePerson));

        // matches Tag in full, different case
        DetailsContainKeyphrasePredicate predicateTagFullDifferentCase = new DetailsContainKeyphrasePredicate("pro"
                + "FeSsIoNal");
        assertTrue(predicateTagFullDifferentCase.test(samplePerson));

        // matches Remark in full
        DetailsContainKeyphrasePredicate predicateRemarkFull = new DetailsContainKeyphrasePredicate("met during a "
                + "networking event");
        assertTrue(predicateRemarkFull.test(samplePerson));

        // matches Remark partially
        DetailsContainKeyphrasePredicate predicateRemarkPartial = new DetailsContainKeyphrasePredicate("networking"
                + " event");
        assertTrue(predicateRemarkPartial.test(samplePerson));

        // matches Remark in full, different Case
        DetailsContainKeyphrasePredicate predicateRemarkFullDifferentCase = new DetailsContainKeyphrasePredicate("met"
                + " during a nEtWorKing EVent");
        assertTrue(predicateRemarkFull.test(samplePerson));
    }
}
```
###### /java/seedu/address/ui/PersonProfileTest.java
``` java
package seedu.address.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertProfileEquals;

import org.junit.Test;

import guitests.guihandles.PersonProfileHandle;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

public class PersonProfileTest extends GuiUnitTest {
    @Test
    public void display() {
        // with tags
        Person personWithTags = new PersonBuilder().withTags().build();
        // set remarks
        personWithTags.setRemark(new Remark("Sample remark"));
        PersonProfile personProfile = new PersonProfile();
        EventsCenter.getInstance().post(new PersonPanelSelectionChangedEvent(
                new PersonCard(personWithTags, 1)));
        uiPartRule.setUiPart(personProfile);
        assertCardDisplay(personProfile, personWithTags);
    }

    @Test
    public void equals() {
        PersonProfile personProfile = new PersonProfile();
        // instance should not be null
        assertFalse(personProfile.equals(null));

        // different type -> return false
        assertFalse(personProfile.equals(1));

        // same default personProfile -> return true
        assertTrue(personProfile.equals(new PersonProfile()));
    }

    /**
     * Asserts that {@code personProfile} displays the details of {@code expectedPerson} correctly.
     */
    private void assertCardDisplay(PersonProfile personProfile, ReadOnlyPerson expectedPerson) {
        guiRobot.pauseForHuman();

        PersonProfileHandle personProfileHandle = new PersonProfileHandle(personProfile.getRoot());

        // verify person details are displayed correctly
        assertProfileEquals(expectedPerson, personProfileHandle);
    }
}
```