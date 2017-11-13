# HanYaodong
###### /java/seedu/address/logic/AutoCompleteTest.java
``` java
public class AutoCompleteTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void autoCompleteAdd_missingOneField_addMissingPrefix() {
        // missing phone
        String command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        String expected = AddCommand.COMMAND_WORD + NAME_DESC_AMY + " " + PREFIX_PHONE + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        assertAutoComplete(command, expected);

        // missing address
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + BIRTHDAY_DESC_AMY
                + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        expected = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + " " + PREFIX_ADDRESS
                + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteAdd_missingMultipleFields_addMissingPrefixes() {
        // missing name and email
        String command = AddCommand.COMMAND_WORD + PHONE_DESC_AMY + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY
                + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        String expected = AddCommand.COMMAND_WORD + " " + PREFIX_NAME + PHONE_DESC_AMY + " " + PREFIX_EMAIL
                + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        assertAutoComplete(command, expected);

        // all fields are missing
        command = AddCommand.COMMAND_WORD;
        expected = AddCommand.COMMAND_WORD + " " + PREFIX_NAME + " " + PREFIX_PHONE + " " + PREFIX_EMAIL
                + " " + PREFIX_ADDRESS + " " + PREFIX_BIRTHDAY + " " + PREFIX_FACEBOOK + " " + PREFIX_TAG;
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteAdd_invalidFields_addMissingPrefixes() {
        // invalid phone
        String command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + " " + PREFIX_PHONE + "abc" + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        String expected = AddCommand.COMMAND_WORD + NAME_DESC_AMY + " " + PREFIX_PHONE + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        assertAutoComplete(command, expected);

        // two name field -> choose the first one
        command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + NAME_DESC_BOB + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        expected = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteAdd_allValidFields_noChange() {
        String command = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY
                + BIRTHDAY_DESC_AMY + FACEBOOK_DESC_AMY + TAG_DESC_FRIEND;
        assertAutoComplete(command, command);
    }

    @Test
    public void autoCompleteDelete_invalidIndex_trimNonDigitChar() {
        String command = "delete 1a";
        String expected = "delete 1";
        assertAutoComplete(command, expected);

        command = "delete aa1bb   ";
        assertAutoComplete(command, expected);

        command = "delete a  1  b";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteDelete_validIndex_noChange() {
        String command = "delete 1";
        assertAutoComplete(command, command);
    }

    @Test
    public void autoCompleteEdit_invalidIndex_trimNonDigitChar() {
        String command = "edit 11a";
        String expected = "edit 11 ";
        assertAutoComplete(command, expected);

        command = "edit a1,1a ";
        assertAutoComplete(command, expected);

        command = "    edit   a1a 1   a    n/Some Name";
        expected = "edit 11 n/Some Name";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteEdit_firstIndexInvalidField_autoFillFields() {
        ReadOnlyPerson firstPerson = getTypicalPersons().get(INDEX_FIRST_PERSON.getZeroBased());
        String expected = EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
            + " " + getPersonDetails(firstPerson).trim() + " ";

        // empty field
        String command = "edit 1 n/";
        assertAutoComplete(command, expected);

        // invalid field
        command = "edit  1  p/a";
        assertAutoComplete(command, expected);

        // multiple invalid fields
        command = "edit  1 p/a e/aa";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteEdit_firstIndexValidFields_autoFillTheRestFields() throws Exception {
        ReadOnlyPerson firstPerson = getTypicalPersons().get(INDEX_FIRST_PERSON.getZeroBased());

        String validName = "Valid Name";
        String command = "edit 1 " + PREFIX_NAME_STRING + validName;
        Person expectedPerson = new Person(firstPerson);
        expectedPerson.setName(new Name(validName));
        String expected = EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
            + " " + getPersonDetails(expectedPerson).trim() + " ";
        assertAutoComplete(command, expected);

        String validPhone = "1234567";
        command = "edit 1 " + PREFIX_PHONE_STRING + validPhone;
        expectedPerson = new Person(firstPerson);
        expectedPerson.setPhone(new Phone(validPhone));
        expected = EditCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
            + " " + getPersonDetails(expectedPerson).trim() + " ";
        assertAutoComplete(command, expected);

        // index out of boundary -> leave the args
        assertEquals(command, AutoComplete.autoComplete(command, new ArrayList<>()));
    }

    @Test
    public void autoCompleteExport_noArg_trimSpaces() {
        String command = "export    ";
        String expected = "export ";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteExport_invalidIndexes_trimNonDigitChars() {
        String command = "export 1a 2a,, 3*.;   SomeFile.xml ";
        String expected = "export 1, 2, 3; SomeFile.xml";
        assertAutoComplete(command, expected);

        command = "export a1-3-10a,4 6aa ,,  ; SomeFile.xml";
        expected = "export 1-3, 4, 6; SomeFile.xml";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteExport_noDelimiter_trimNonDigitChars() {
        String command = "export 1, 2, 3 SomeFile.xml ";
        String expected = "export 1-3; SomeFile.xml";
        assertAutoComplete(command, expected);

        command = "export 1, 2, 3,SomeFile.xml ";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteExport_continuousIndexes_wrapContinuousIndexes() {
        String command = "export 1, 2, 3, 4; SomeFile.xml ";
        String expected = "export 1-4; SomeFile.xml";
        assertAutoComplete(command, expected);

        command = "export 1-3, 4; SomeFile.xml ";
        assertAutoComplete(command, expected);

        command = "export 3-1, 4; SomeFile.xml ";
        assertAutoComplete(command, expected);

        command = "export 1,2,3,6,10,12,11; SomeFile.xml ";
        expected = "export 1-3, 6, 10-12; SomeFile.xml";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteImport_anyArgument_trimWhitespaces() {
        String command = "import  someFile.xml  ";
        String expected = "import someFile.xml";
        assertAutoComplete(command, expected);

        command = "import     ";
        expected = "import ";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteLogin_invalidPrefix_ignoreInvalidField() {
        String command = "login n/name usr/admin pwd/password";
        String expected = "login usr/admin pwd/password";
        assertAutoComplete(command, expected);

        command = "login n/name pwd/password";
        expected = "login usr/ pwd/password";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteLogin_noField_autoFillFields() {
        String command = "login usr/admin ";
        String expected = "login usr/admin pwd/";
        assertAutoComplete(command, expected);

        command = "login pwd/password";
        expected = "login usr/ pwd/password";
        assertAutoComplete(command, expected);

        command = "login  ";
        expected = "login usr/ pwd/";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteRemark_invalidIndex_trimNonDigitChars() {
        String command = "remark aa1,1a";
        String expected = "remark 11 ";
        assertAutoComplete(command, expected);

        command = "   remark   a1, 1  a r/Some remark";
        expected = "remark 11 r/Some remark";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteRemark_firstIndexEmptyField_autoFillRemarkField() {
        List<ReadOnlyPerson> personList = new ArrayList<>();
        personList.add(new Person(getTypicalPersons().get(0)));

        String validRemark = "valid remark";
        String command = "remark 1 r/";
        personList.get(INDEX_FIRST_PERSON.getZeroBased()).setRemark(new Remark(validRemark));
        String expected = command + validRemark;
        assertAutoComplete(command, expected, personList);
    }

    @Test
    public void autoCompleteSelect_invalidIndex_trimNonDigitChar() {
        String command = "select 1a";
        String expected = "select 1";
        assertAutoComplete(command, expected);

        command = "select aa1bb   ";
        assertAutoComplete(command, expected);

        command = "select a  1  b";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteSelect_validIndex_noChange() {
        String command = "select 1";
        assertAutoComplete(command, command);
    }

    @Test
    public void autoCompleteSort_containMultiplePrefix_onlyReturnName() {
        String command = "sort name phone  ";
        String expected = "sort name";
        assertAutoComplete(command, expected);

        command = "sort reverse phone ";
        expected = "sort phone reverse";
        assertAutoComplete(command, expected);

        command = "sort name reverse phone ";
        expected = "sort name reverse";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteUnknownCommand_prefixMatch_autoCompleteCommand() {
        String command = "ad   n/Some Name  ";
        String expected = "add n/Some Name";
        assertAutoComplete(command, expected);

        command = "cle  ";
        expected = "clear ";
        assertAutoComplete(command, expected);

        command = "de  1  ";
        expected = "delete 1";
        assertAutoComplete(command, expected);

        command = "ed 1 n/Some Name  ";
        expected = "edit 1 n/Some Name";
        assertAutoComplete(command, expected);

        command = "exi  ";
        expected = "exit ";
        assertAutoComplete(command, expected);

        command = "exp   1, 2; m.xml   ";
        expected = "export 1, 2; m.xml";
        assertAutoComplete(command, expected);

        command = "fi x ";
        expected = "find x";
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteUnknownCommand_multiplePrefixMatches_autoCompleteCommand() {
        // exit and export
        String command = "ex";
        String expected = getUnknownCommandPrompt(ExitCommand.COMMAND_WORD, ExportCommand.COMMAND_WORD);
        assertAutoComplete(command, expected);

        // search and select
        command = "se";
        expected = getUnknownCommandPrompt(SearchCommand.COMMAND_WORD, SelectCommand.COMMAND_WORD);
        assertAutoComplete(command, expected);
    }

    @Test
    public void autoCompleteUnknownCommand_fuzzyMatches_autoCompleteCommand() {
        // single hit
        String command = "adda ";
        String expected = getUnknownCommandPrompt(AddCommand.COMMAND_WORD);
        assertAutoComplete(command, expected);

        // multiple hits
        command = "port";
        expected = getUnknownCommandPrompt(ExportCommand.COMMAND_WORD, ImportCommand.COMMAND_WORD,
                                           SortCommand.COMMAND_WORD);
        assertAutoComplete(command, expected);

        // no hit
        command = "zzzzz";
        assertAutoComplete(command, command);
    }

    @Test
    public void autoCompleteUnknownCommand_emptyInput_emptyReturn() {
        String command = "     ";
        assertAutoComplete(command, "");
    }

    /**
     * Asserts if the auto-complete of {@code command} equals to {@code expectedResult}.
     */
    private void assertAutoComplete(String command, String expectedResult) {
        String result = AutoComplete.autoComplete(command, getTypicalPersons());
        assertEquals(result, expectedResult);
    }

    /**
     * @see #assertAutoComplete(String, String).
     */
    private void assertAutoComplete(String command, String expectedResult, List<ReadOnlyPerson> personList) {
        String result = AutoComplete.autoComplete(command, personList);
        assertEquals(result, expectedResult);
    }

    /**
     * Returns the prompt String for suggestions.
     */
    private String getUnknownCommandPrompt(String... suggestions) {
        return "Do you mean: " + Arrays.stream(suggestions).collect(Collectors.joining(" or ")) + " ?";
    }
}
```
###### /java/seedu/address/logic/commands/ExportCommandTest.java
``` java
//TODO: the export command test depends on Storage part. Consider separation of components.
public class ExportCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    /**
     * Typical address model consists of ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE
     */
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());

```
###### /java/seedu/address/logic/commands/ExportCommandTest.java
``` java

    @Test
    public void equals() {
        List<Index> indexes = Arrays.asList(Index.fromOneBased(1), Index.fromOneBased(2));
        String filePath = "TestFile.xml";
        ExportCommand exportCommand = new ExportCommand(indexes, filePath);

        // same object -> true
        assertTrue(exportCommand.equals(exportCommand));

        // same value -> true
        ExportCommand exportCommandCopy = new ExportCommand(indexes, filePath);
        assertTrue(exportCommand.equals(exportCommandCopy));

        // different type -> false
        assertFalse(exportCommand.equals(1));

        // null -> false
        assertFalse(exportCommand.equals(null));

        // different index -> false
        List<Index> newIndexes = Collections.singletonList(Index.fromOneBased(1));
        ExportCommand exportCommandDifferentIndex = new ExportCommand(newIndexes, filePath);
        assertFalse(exportCommand.equals(exportCommandDifferentIndex));

        //different filePath -> false
        ExportCommand exportCommandDifferentFilePath = new ExportCommand(indexes, "OtherFile.xml");
        assertFalse(exportCommand.equals(exportCommandDifferentFilePath));
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        int outOfBoundaryOneBasedIndex = this.model.getFilteredPersonList().size() + 1;
        ExportCommand command = prepareCommand(new Integer[]{outOfBoundaryOneBasedIndex}, getTestFilePath());

        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        CommandResult result = command.execute();

    }

    @Test
    public void execute_validIndexesAndFilePath_success() throws Exception {
        // export typical person: Alice, Benson, Daniel
        ExportCommand command = prepareCommand(new Integer[]{1, 2, 4}, getTestFilePath());

        // test command output
        CommandResult commandResult = command.execute();
        assertEquals(commandResult.feedbackToUser, String.format(
            ExportCommand.MESSAGE_EXPORT_PERSON_SUCCESS, constructNameList(ALICE, BENSON, DANIEL), getTestFilePath()));

        // test file output
        UniquePersonList origin = new UniquePersonList();
        origin.setPersons(Arrays.asList(ALICE, BENSON, DANIEL));
        XmlPersonListStorage tmpStorage = new XmlPersonListStorage(getTestFilePath());
        UniquePersonList readBack = tmpStorage.readPersonList().get();
        assertEquals(readBack, origin);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() throws Exception {
        showFirstPersonOnly(model);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        prepareCommand(new Integer[]{outOfBoundIndex.getOneBased()}, getTestFilePath()).execute();
    }

    @Test
    public void execute_validIndexesAndFilePathFilteredList_success() throws Exception {
        showFirstPersonOnly(this.model);
        // export typical person: Alice
        ExportCommand command = prepareCommand(new Integer[]{1}, getTestFilePath());

        // test command output
        CommandResult commandResult = command.execute();
        assertEquals(commandResult.feedbackToUser, String.format(
            ExportCommand.MESSAGE_EXPORT_PERSON_SUCCESS, constructNameList(ALICE), getTestFilePath()));

        // test file output
        UniquePersonList origin = new UniquePersonList();
        origin.setPersons(Arrays.asList(ALICE));
        XmlPersonListStorage tmpStorage = new XmlPersonListStorage(getTestFilePath());
        UniquePersonList readBack = tmpStorage.readPersonList().get();
        assertEquals(readBack, origin);
    }

    /**
     * @return an {@code ExportCommand} with parameters {@code indexes} and {@code filePath}
     */
    private ExportCommand prepareCommand(Integer[] indexesInt, String filePath) {
        model.getUserCreds().validateCurrentSession(); // validate user
        List<Index> indexes = Arrays.stream(indexesInt).map(Index::fromOneBased).collect(Collectors.toList());
        ExportCommand export = new ExportCommand(indexes, filePath);
        export.setData(this.model, new CommandHistory(), new UndoRedoStack());
        return export;
    }

    /**
     * Builds a String consisting of {@code persons}'s name in the format of
     * "[person_1's name], [person_2's name], ..., [person_n's name] "
     */
    private String constructNameList(ReadOnlyPerson... persons) {
        StringBuilder personNameList = new StringBuilder();
        for (ReadOnlyPerson person : persons) {
            personNameList.append(person.getName().fullName).append(", ");
        }
        personNameList.deleteCharAt(personNameList.lastIndexOf(","));
        return personNameList.toString();
    }

    /**
     * @return a valid file path for testing.
     */
    private String getTestFilePath() {
        return testFolder.getRoot().getPath() + "TempPersonList.xml";
    }
}
```
###### /java/seedu/address/logic/commands/ImportCommandTest.java
``` java
public class ImportCommandTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/ImportCommandTest/");

    private static final String MESSAGE_DUPLICATED_PERSON_WARNING =
        "Duplicated persons are found in import process. Duplicated information is ignored.\n";
    private static final String MESSAGE_SUCCESS_WITH_DUPLICATED_PERSON_IN_FILE =
         MESSAGE_DUPLICATED_PERSON_WARNING + ImportCommand.MESSAGE_IMPORT_SUCCESS;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());

```
###### /java/seedu/address/logic/commands/ImportCommandTest.java
``` java

    @Test
    public void equals() {
        String filePath = "SomeFile.xml";
        ImportCommand importCommand = new ImportCommand(filePath);

        // the same object -> true
        assertTrue(importCommand.equals(importCommand));

        // different object same value -> true
        assertTrue(importCommand.equals(new ImportCommand(filePath)));

        // null -> false
        assertFalse(importCommand.equals(null));

        // different classes -> false
        assertFalse(importCommand.equals(1));

        // different values -> false
        assertFalse(importCommand.equals(new ImportCommand("OtherFile.xml")));
    }

    @Test
    public void constructor_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        new ImportCommand(null).executeUndoableCommand();
    }

    @Test
    public void execute_missingFile_failure() throws Exception {
        String filePath = addToTestDataPathIfNotNull("MissingFile.xml");
        assertCommandException(new ImportCommand(filePath),
            String.format(ImportCommand.MESSAGE_MISSING_FILE, filePath));
    }

    @Test
    public void execute_notXmlFormat_failure() throws Exception {
        String filePath = addToTestDataPathIfNotNull("NotXmlFormatExportFile.xml");
        assertCommandException(new ImportCommand(filePath),
            String.format(ImportCommand.MESSAGE_INVALID_XML_FILE, filePath));
    }

    @Test
    public void execute_emptyFile_failure() {
        String filePath = addToTestDataPathIfNotNull("EmptyFile.xml");
        //TODO: empty file should return Optional.empty()?
        assertCommandException(new ImportCommand(filePath),
            String.format(ImportCommand.MESSAGE_INVALID_XML_FILE, filePath));
    }

    @Test
    public void execute_duplicatedPersonInFile_failure() throws Exception {
        String filePath = addToTestDataPathIfNotNull("DuplicatedPersonsExportFile.xml");
        assertCommandException(new ImportCommand(filePath),
            String.format(ImportCommand.MESSAGE_DUPLICATED_PERSON_IN_FILE, filePath));
    }

    @Test
    public void execute_duplicatedPersonInAddressBook_successWithWarning() throws Exception {
        String filePath = addToTestDataPathIfNotNull("DuplicatedPersonInAddressBook.xml");
        ImportCommand command = prepareCommand(filePath);
        CommandResult result = command.executeUndoableCommand();
        assertEquals(result.feedbackToUser, String.format(MESSAGE_SUCCESS_WITH_DUPLICATED_PERSON_IN_FILE, 1));
    }

    @Test
    public void execute_validFilePathAndFile_success() throws Exception {
        String filePath = addToTestDataPathIfNotNull("ImportTestFile.xml");
        ImportCommand command = prepareCommand(filePath);
        CommandResult result = command.execute();

        // check CommandResult
        assertEquals(result.feedbackToUser, String.format(ImportCommand.MESSAGE_IMPORT_SUCCESS, 1));
        // check Model
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());
        expectedModel.getUserCreds().validateCurrentSession(); // validate user
        expectedModel.addPerson(TypicalPersons.HOON);
        assertEquals(command.model, expectedModel);
    }

    /**
     * @return an ImportCommand with given {@code filePath} and typical Model.
     */
    private ImportCommand prepareCommand(String filePath) {
        ImportCommand command = new ImportCommand(filePath);
        // typical address model consists of ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE
        Model typicalAddressBookModel = new ModelManager(getTypicalAddressBook(), new UserPrefs(), new UserCreds());
        typicalAddressBookModel.getUserCreds().validateCurrentSession(); // validate user
        command.setData(typicalAddressBookModel, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Asserts if executing the given command throws {@code CommandException} with {@code exceptionMessage}.
     */
    private void assertCommandException(ImportCommand command, String exceptionMessage) {
        model.getUserCreds().validateCurrentSession(); // validate user
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        try {
            command.executeUndoableCommand();
        } catch (CommandException ce) {
            assertEquals(ce.getMessage(), exceptionMessage);
        }
    }

    /**
     * Adds test folder path to {@code prefsFileInTestDataFolder}.
     */
    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
               ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
               : null;
    }
}
```
###### /java/seedu/address/logic/parser/ExportCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ExportCommand;

public class ExportCommandParserTest {

    private static final String VALID_FILE_PATH = "TestFile.xml";
    private static final String EXPECTED_MATCH_ERROR_MESSAGE =
        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE);
    private static final String EXPECTED_FILE_MISSING_ERROR_MESSAGE = String.format(
        MESSAGE_INVALID_COMMAND_FORMAT, ExportCommandParser.MISSING_FILE_PATH + ExportCommand.MESSAGE_USAGE);
    private ExportCommandParser parser = new ExportCommandParser();

    @Test
    public void parse_missingSemicolon_failure() {
        String input = "1 " + VALID_FILE_PATH;
        assertParseFailure(parser, input, EXPECTED_MATCH_ERROR_MESSAGE);
    }

    @Test
    public void parse_missingIndex_failure() {
        String input = " ; " + VALID_FILE_PATH;
        assertParseFailure(parser, input, EXPECTED_MATCH_ERROR_MESSAGE);
    }

    @Test
    public void parse_missingFilePath_failure() {
        String input = "1 ; ";
        assertParseFailure(parser, input, EXPECTED_FILE_MISSING_ERROR_MESSAGE);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String input = "1, 2 3-4 a; " + VALID_FILE_PATH;
        assertParseFailure(parser, input, EXPECTED_MATCH_ERROR_MESSAGE);

        String negativeIndexInput = "-1 ; " + VALID_FILE_PATH;
        assertParseFailure(parser, negativeIndexInput, EXPECTED_MATCH_ERROR_MESSAGE);
    }

    @Test
    public void parse_filePathWithInvalidExtension_successWithModifiedFilePath() {
        String xmlExtension = ".xml";
        String validIndexList = "1, 2;";
        List<Index> validIndexes = getIndexListFromOneBasedArray(1, 2);

        // no extension -> add .xml to filePath
        String filePathWithoutExtension = "TestFile";
        assertParseSuccess(parser, validIndexList + filePathWithoutExtension,
            new ExportCommand(validIndexes, filePathWithoutExtension + xmlExtension));

        // wrong extension -> add .xml to filePath
        String filePathWithWrongExtension = "TestFile.txt";
        assertParseSuccess(parser, validIndexList + filePathWithWrongExtension,
            new ExportCommand(validIndexes, filePathWithWrongExtension + xmlExtension));

        // */.xml -> add .xml to filePath
        String filePathWithoutFileName = "./.xml";
        assertParseSuccess(parser, validIndexList + filePathWithoutFileName,
            new ExportCommand(validIndexes, filePathWithoutFileName + xmlExtension));
    }

    @Test
    public void parse_validArgs_success() {
        List<Index> indexes = getIndexListFromOneBasedArray(1, 2, 3);
        String input = "1, 2 3 ; " + VALID_FILE_PATH;
        ExportCommand exportCommand = new ExportCommand(indexes, VALID_FILE_PATH.trim());
        assertParseSuccess(parser, input, exportCommand);

        indexes = getIndexListFromOneBasedArray(1, 2, 3, 5, 7, 8, 9);
        input = "1, 2, 3 5 7-9;" + VALID_FILE_PATH;
        exportCommand = new ExportCommand(indexes, VALID_FILE_PATH.trim());
        assertParseSuccess(parser, input, exportCommand);
    }

    /**
     * Converts an One-based {@link Integer} Array to an {@link Index} List.
     */
    private List<Index> getIndexListFromOneBasedArray(Integer... integers) {
        return Arrays.stream(integers).map(Index::fromOneBased).collect(Collectors.toList());
    }
}
```
###### /java/seedu/address/logic/parser/ImportCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ImportCommandParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_emptyArg_throwsParserException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        parser.parse("  ");
    }

    @Test
    public void parse_validArg_success() throws Exception {
        // valid file path
        String validFilePath = "ValidFile.xml";
        assertEquals(parser.parse(validFilePath), new ImportCommand(validFilePath));

        // valid file path with leading and trailing whitespaces
        String validFilePathWithSpaces = "  ValidFile.xml   ";
        assertEquals(parser.parse(validFilePathWithSpaces), new ImportCommand(validFilePath));

    }
}
```
###### /java/seedu/address/model/person/FacebookTest.java
``` java
public class FacebookTest {

    @Test
    public void isValidFacebook() {
        // blank facebook
        assertFalse(Facebook.isValidFacebookId(""));
        assertFalse(Facebook.isValidFacebookId("  "));

        // not numerical facebook
        assertFalse(Facebook.isValidFacebookId("zuck"));
        assertFalse(Facebook.isValidFacebookId("some.user.name"));

        // valid facebook
        assertTrue(Facebook.isValidFacebookId("12345"));
    }
}
```
###### /java/seedu/address/storage/XmlPersonListStorageTest.java
``` java
package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.TypicalPersons;

public class XmlPersonListStorageTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlPersonListStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void savePersonList_nullPersonList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        savePersonListAsList(null, "SomeFile.xml");
    }

    @Test
    public void savePersonList_nullUniquePersonList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        savePersonListAsUniquePersonList(null, "SomeFile.xml");
    }

    @Test
    public void savePersonList_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        savePersonListAsList(new ArrayList<>(), null);
    }

    @Test
    public void read_missingFile_throwsFileNotFoundException() throws Exception {
        thrown.expect(FileNotFoundException.class);
        readPersonList("MissingFile.xml");
    }

    @Test
    public void read_notXmlFormat_throwsDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readPersonList("NotXmlFormatPersonList.xml");
    }

    @Test
    public void read_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readPersonList(null);
    }

    @Test
    public void read_duplicatedPersonListFile_throwsDuplicatePersonException() throws Exception {
        thrown.expect(DuplicatePersonException.class);
        readPersonList("DuplicatedPersonsList.xml");
    }

    @Test
    public void readAndSavePersonListAsList() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempPersonList.xml";
        XmlPersonListStorage xmlPersonListStorage = new XmlPersonListStorage(filePath);
        List<ReadOnlyPerson> originalList = new ArrayList<>(getTypicalPersonList());
        UniquePersonList originalUniquePersonList = new UniquePersonList();
        originalUniquePersonList.setPersons(originalList);

        // save in file and read back
        xmlPersonListStorage.savePersonList(originalList, filePath);
        UniquePersonList readBack = xmlPersonListStorage.readPersonList(filePath).get();
        assertEquals(originalUniquePersonList, readBack);

        // modify data and overwrite the file
        originalList.add(TypicalPersons.FIONA);
        originalList.add(TypicalPersons.GEORGE);
        originalUniquePersonList.setPersons(originalList);
        xmlPersonListStorage.savePersonList(originalList, filePath);
        readBack = xmlPersonListStorage.readPersonList(filePath).get();
        assertEquals(originalUniquePersonList, readBack);

        // save and read without specified file path
        originalList.remove(TypicalPersons.ALICE);
        originalUniquePersonList.setPersons(originalList);
        xmlPersonListStorage.savePersonList(originalList);
        readBack = xmlPersonListStorage.readPersonList().get();
        assertEquals(originalUniquePersonList, readBack);
    }

    @Test
    public void readAndSavePersonListAsUniquePersonList() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempPersonList.xml";
        XmlPersonListStorage xmlPersonListStorage = new XmlPersonListStorage(filePath);
        UniquePersonList originalUniquePersonList = getTypicalUniquePersonList();

        // save in file and read back
        xmlPersonListStorage.savePersonList(originalUniquePersonList, filePath);
        UniquePersonList readBack = xmlPersonListStorage.readPersonList(filePath).get();
        assertEquals(originalUniquePersonList, readBack);

        // modify data and overwrite the file
        originalUniquePersonList.add(TypicalPersons.FIONA);
        originalUniquePersonList.add(TypicalPersons.GEORGE);
        xmlPersonListStorage.savePersonList(originalUniquePersonList);
        readBack = xmlPersonListStorage.readPersonList(filePath).get();
        assertEquals(originalUniquePersonList, readBack);

        // save and read without specified file path
        originalUniquePersonList.remove(TypicalPersons.ALICE);
        xmlPersonListStorage.savePersonList(originalUniquePersonList);
        readBack = xmlPersonListStorage.readPersonList().get();
        assertEquals(originalUniquePersonList, readBack);
    }

    /**
     * Saves {@code persons} at specified {@code filePath}.
     */
    private void savePersonListAsList(List<ReadOnlyPerson> persons, String filePath) {
        try {
            new XmlPersonListStorage(filePath).savePersonList(persons, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    /**
     * Saves {@code persons} at specified {@code filePath}.
     */
    private void savePersonListAsUniquePersonList(UniquePersonList persons, String filePath) {
        try {
            new XmlPersonListStorage(filePath).savePersonList(persons, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    /**
     * Reads a PersonLit file in the {@code TEST_DATA_FOLDER}.
     */
    private Optional<UniquePersonList> readPersonList(String filePath) throws Exception {
        return new XmlPersonListStorage(addToTestDataPathIfNotNull(filePath)).readPersonList();
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
               ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
               : null;
    }

    /**
     * @return a list of {@link ReadOnlyPerson} for test.
     */
    private List<ReadOnlyPerson> getTypicalPersonList() {
        List<ReadOnlyPerson> typicalPersonList = new ArrayList<>(5);
        typicalPersonList.add(TypicalPersons.ALICE);
        typicalPersonList.add(TypicalPersons.BENSON);
        typicalPersonList.add(TypicalPersons.CARL);
        typicalPersonList.add(TypicalPersons.DANIEL);
        typicalPersonList.add(TypicalPersons.ELLE);
        return typicalPersonList;
    }

    /**
     * @return a {@link UniquePersonList} for test.
     */
    private UniquePersonList getTypicalUniquePersonList() {
        UniquePersonList typicalUniquePersonList = new UniquePersonList();
        try {
            typicalUniquePersonList.setPersons(getTypicalPersonList());
        } catch (IllegalValueException e) {
            assert false : "not possible";
        }
        return typicalUniquePersonList;
    }

}
```
###### /java/seedu/address/ui/CommandBoxTest.java
``` java
    @Test
    public void handleKeyPress_startingWithTab() {
        commandBoxHandle.runAutoComplete("ed");
        assertEquals(commandBoxHandle.getInput(), "edit ");

        commandBoxHandle.runAutoComplete("edit 1a");
        assertEquals(commandBoxHandle.getInput(), "edit 1 ");
    }
```
###### /java/systemtests/ExportCommandSystemTest.java
``` java
public class ExportCommandSystemTest extends AddressBookSystemTest {

    private static final String EXPECTED_MESSAGE_INVALID_COMMAND =
        String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE);

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Test
    public void export() throws Exception {
        String testFile = testFolder.getRoot().getPath() + "TestFile.xml";
        Model model = getModel();
        model.getUserCreds().validateCurrentSession(); // validate user

        /* ----------------- Performing export operation while an unfiltered list is being shown -------------------- */

        /* Case: export the first person in the list, command with leading and trailing whitespaces
         * -> first person exported
         */
        Index firstPersonIndex = INDEX_FIRST_PERSON;
        String command  = "    " + getExportCommand(testFile, firstPersonIndex) + "    ";
        String expectedMessage = getExpectedSuccessMessage(model, testFile, firstPersonIndex);
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: export the last person in the list -> last person exported */
        Index lastPersonIndex = getLastIndex(model);
        assertCommandSuccess(getExportCommand(testFile, lastPersonIndex), model,
            getExpectedSuccessMessage(model, testFile, lastPersonIndex));

        /* Case: export first and second person in the list with index separated by "," -> 2 persons exported */
        command = ExportCommand.COMMAND_WORD + " 1,2 ; " + testFile;
        expectedMessage = getExpectedSuccessMessage(model, testFile,  Index.fromOneBased(1), Index.fromOneBased(2));
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: repeat the previous command with index separated by whitespaces -> 2 persons exported */
        command = ExportCommand.COMMAND_WORD + " 1 2 ; " + testFile;
        expectedMessage = getExpectedSuccessMessage(model, testFile, Index.fromOneBased(1), Index.fromOneBased(2));
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: export first three persons in the list with index separated by a mix of "," and whitespaces
         * -> 3 persons exported
         */
        command = ExportCommand.COMMAND_WORD + " 1,, 2  3 ; " + testFile;
        expectedMessage = getExpectedSuccessMessage(model, testFile,
            Index.fromOneBased(1), Index.fromOneBased(2), Index.fromOneBased(3));
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: export first person to non-existing file -> exported and create new file */
        String nonExistingFile = testFolder.getRoot().getPath() + "NonExistingFile.xml";
        command = getExportCommand(nonExistingFile, firstPersonIndex);
        expectedMessage = getExpectedSuccessMessage(model, nonExistingFile, firstPersonIndex);
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: export last person to existed file -> exported to overwrite file */
        command = getExportCommand(nonExistingFile, lastPersonIndex);
        expectedMessage = getExpectedSuccessMessage(model, nonExistingFile, lastPersonIndex);
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: export first person with file path without extension -> exported and add .xml extension */
        String noExtensionFile = testFolder.getRoot().getPath() + "NoExtensionFile";
        command = getExportCommand(noExtensionFile, firstPersonIndex);
        expectedMessage = getExpectedSuccessMessage(model, noExtensionFile + ".xml", firstPersonIndex);
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: export first person with file path with wrong extension -> exported and add .xml extension */
        String wrongExtensionFile = testFolder.getRoot().getPath() + "WrongExtensionFile.txt";
        command = getExportCommand(wrongExtensionFile, firstPersonIndex);
        expectedMessage = getExpectedSuccessMessage(model, wrongExtensionFile + ".xml", firstPersonIndex);
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: export first person with file path with no file name -> exported and add .xml extension */
        String noNameFile = testFolder.getRoot().getPath() + File.separator + ".xml";
        command = getExportCommand(noNameFile, firstPersonIndex);
        expectedMessage = getExpectedSuccessMessage(model, noNameFile + ".xml", firstPersonIndex);
        assertCommandSuccess(command, model, expectedMessage);

        /* Case: export first person with file path with extension in upper case -> exported to given file name */
        String upperCaseExtensionFile = testFolder.getRoot().getPath() + "UpperCaseFile.XML";
        command = getExportCommand(upperCaseExtensionFile, firstPersonIndex);
        expectedMessage = getExpectedSuccessMessage(model, upperCaseExtensionFile, firstPersonIndex);
        assertCommandSuccess(command, model, expectedMessage);

        /* ------------------ Performing export operation while a filtered list is being shown ---------------------- */

        /* Case: filtered person list, export within boundary of address book and person list -> exported */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        model = getModel();
        command = getExportCommand(testFile, firstPersonIndex);
        assertTrue(firstPersonIndex.getZeroBased() < model.getFilteredPersonList().size());
        assertCommandSuccess(command, model, getExpectedSuccessMessage(model, testFile, firstPersonIndex));

        /* Case: filtered person list, export within boundary of address book but out of bounds of person list
         * -> rejected
         */
        showPersonsWithName(KEYWORD_MATCHING_MEIER);
        Index invalidIndex = Index.fromZeroBased(getModel().getAddressBook().getPersonList().size());
        assertCommandFailure(getExportCommand(testFile, invalidIndex), MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* --------------------- Performing export operation while a person card is selected ------------------------ */

        /* Case: export the selected person -> exported and selection remains */
        showAllPersons();
        model = getModel();
        Index selectedIndex = getLastIndex(model);
        selectPerson(selectedIndex);
        command = getExportCommand(testFile, selectedIndex);
        expectedMessage = getExpectedSuccessMessage(model, testFile, selectedIndex);
        assertCommandSuccess(command, model, expectedMessage);

        /* --------------------------------- Performing invalid delete operation ------------------------------------ */

        /* Case: invalid index (0) -> rejected */
        command = getExportCommand(testFile, 0);
        assertCommandFailure(command, EXPECTED_MESSAGE_INVALID_COMMAND);

        /* Case: invalid index (-1) -> rejected */
        command = getExportCommand(testFile, -1);
        assertCommandFailure(command, EXPECTED_MESSAGE_INVALID_COMMAND);

        /* Case: invalid index (size + 1)  */
        command = getExportCommand(testFile, model.getFilteredPersonList().size() + 1);
        assertCommandFailure(command, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        /* Case: invalid arguments (alphabets in index part) -> rejected */
        command = getExportCommand(testFile, "abc");
        assertCommandFailure(command, EXPECTED_MESSAGE_INVALID_COMMAND);

        /* Case: invalid arguments (indexes separated by invalid char) -> rejected */
        command = getExportCommand(testFile, "1 . 2");
        assertCommandFailure(command, EXPECTED_MESSAGE_INVALID_COMMAND);

        /* Case: missing index list -> rejected */
        command = getExportCommand(testFile, " ");
        assertCommandFailure(command, EXPECTED_MESSAGE_INVALID_COMMAND);

        /* Case: missing semicolon ";" -> rejected */
        command = ExportCommand.COMMAND_WORD + " 1, 2 " + testFile;
        assertCommandFailure(command, EXPECTED_MESSAGE_INVALID_COMMAND);

        /* Case: missing file path -> rejected */
        command = getExportCommand(" ", firstPersonIndex);
        assertCommandFailure(command, String.format(
            MESSAGE_INVALID_COMMAND_FORMAT, ExportCommandParser.MISSING_FILE_PATH + ExportCommand.MESSAGE_USAGE));

        /* Case: mixed case command word -> rejected */
        assertCommandFailure("ExpOrt 1;" + testFile, MESSAGE_UNKNOWN_COMMAND);

    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card remains unchanged.<br>
     * 5. Asserts that the status bar remains unchanged.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String)} except that the browser url
     * and selected card are expected to update accordingly depending on the card at {@code expectedSelectedCardIndex}.
     * @see ExportCommandSystemTest#assertCommandSuccess(String, Model, String)
     * @see AddressBookSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
                                      Index expectedSelectedCardIndex) {
        expectedModel.getUserCreds().validateCurrentSession(); // validate user
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(
            autoComplete(command, expectedModel.getFilteredPersonList()), expectedMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertSelectedCardUnchanged();
        assertStatusBarUnchanged();
    }

    private String getNameListString(Model model, Index... indexes) {
        List<ReadOnlyPerson> filteredList = model.getFilteredPersonList();
        StringBuilder personNameBuilder = new StringBuilder();
        for (Index index : indexes) {
            personNameBuilder.append(filteredList.get(index.getZeroBased()).getName().fullName).append(", ");
        }
        return personNameBuilder.deleteCharAt(personNameBuilder.lastIndexOf(",")).toString();
    }

    private String getExportCommand(String filePath, Index... indexes) {
        return ExportCommand.COMMAND_WORD + " "
            + Arrays.stream(indexes).map(Index::getOneBased).map(Object::toString).collect(Collectors.joining(","))
            + " ; " + filePath;
    }

    private String getExportCommand(String filePath, Integer... oneBasedIndexes) {
        return ExportCommand.COMMAND_WORD + " "
            + Arrays.stream(oneBasedIndexes).map(Object::toString).collect(Collectors.joining(","))
            + " ; " + filePath;
    }

    private String getExportCommand(String filePath, String oneBasedIndexesString) {
        return ExportCommand.COMMAND_WORD + " " + oneBasedIndexesString + " ; " + filePath;
    }

    private String getExpectedSuccessMessage(Model model, String filePath, Index... indexes) {
        return String.format(ExportCommand.MESSAGE_EXPORT_PERSON_SUCCESS, getNameListString(model, indexes), filePath);
    }

}
```
###### /java/systemtests/ImportCommandSystemTest.java
``` java
public class ImportCommandSystemTest extends AddressBookSystemTest {

    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/ImportCommandSystemTest/");

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void importCommand() throws Exception {
        Model expectedModel = getModel();
        expectedModel.getUserCreds().validateCurrentSession(); // validate user
        /* ----------------- Performing import operation while an unfiltered list is being shown -------------------- */

        /* Case: import one unique person into address book -> imported */
        String command = getCommandWord("Amy.xml");
        expectedModel.addPerson(AMY);
        assertCommandSuccess(command, expectedModel, 1);

        /* Case: import multiple unique persons into address book -> imported */
        Model modelBeforeAddingPersons = getModel();

        command = getCommandWord("Hoon_Bob.xml");
        expectedModel.addPerson(HOON);
        expectedModel.addPerson(BOB);
        assertCommandSuccess(command, expectedModel, 2);

        /* Case: undo import -> persons deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, modelBeforeAddingPersons, expectedMessage);

        /* Case: redo import -> persons imported again */
        command = RedoCommand.COMMAND_WORD;
        expectedMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, expectedModel, expectedMessage);

        /* Case: import multiple persons containing the same person in address book
         * -> imported with duplicated persons ignored
         */
        ReadOnlyPerson james = new PersonBuilder().withName("James Turner").withPhone("66666666")
                .withEmail("james@example.com").withAddress("Sydney").withBirthday("2000/01/01")
                .withFacebook("30").withTags("friends").build();
        expectedModel.addPerson(james);
        command = getCommandWord("DuplicatedPersonInAddressBook.xml");
        expectedMessage = ImportCommand.MESSAGE_DUPLICATED_PERSON_IN_ADDRESS_BOOK_WARNING
                + String.format(ImportCommand.MESSAGE_IMPORT_SUCCESS, 2);
        assertCommandSuccess(command, expectedModel, expectedMessage);

        /* Case: import to an empty address book -> imported */
        executeCommand(ClearCommand.COMMAND_WORD);
        expectedModel = getModel();
        assert expectedModel.getAddressBook().getPersonList().size() == 0;

        command = getCommandWord("Amy.xml");
        expectedModel.addPerson(AMY);
        assertCommandSuccess(command, expectedModel, 1);

        /* Case: missing arguments -> rejected */
        command = ImportCommand.COMMAND_WORD;
        expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE);
        assertCommandFailure(command, expectedMessage);

        /* Case: import file containing duplicated persons -> rejected */
        String filePath = addToTestDataPathIfNotNull("DuplicatedPersonsExportFile.xml");
        command = ImportCommand.COMMAND_WORD + " " + filePath;
        expectedMessage = String.format(ImportCommand.MESSAGE_DUPLICATED_PERSON_IN_FILE, filePath);
        assertCommandFailure(command, expectedMessage);

        /* Case: import missing file -> rejected */
        filePath = addToTestDataPathIfNotNull("MissingFile.xml");
        command = ImportCommand.COMMAND_WORD + " " + filePath;
        expectedMessage = String.format(ImportCommand.MESSAGE_MISSING_FILE, filePath);
        assertCommandFailure(command, expectedMessage);

        /* Case: import file not in xml format -> rejected */
        filePath = addToTestDataPathIfNotNull("NotXmlFormatExportFile.xml");
        command = ImportCommand.COMMAND_WORD + " " + filePath;
        expectedMessage = String.format(ImportCommand.MESSAGE_INVALID_XML_FILE, filePath);
        assertCommandFailure(command, expectedMessage);

        /* Case: mix case command word -> rejected */
        command = "ExPort SomeFile.xml";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);

    }

    /**
     * @see ImportCommandSystemTest#assertCommandSuccess(String, Model, String)
     */
    private void assertCommandSuccess(String command, Model expectedModel, int numberOfPersonsImported) {
        expectedModel.getUserCreds().validateCurrentSession(); // validatae user
        String expectedResultMessage = String.format(ImportCommand.MESSAGE_IMPORT_SUCCESS, numberOfPersonsImported);
        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the status bar's sync status changes.<br>
     * 5. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        expectedModel.getUserCreds().validateCurrentSession(); // validate user
        executeCommand(command);
        expectedModel.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();
        executeCommand(command);
        assertApplicationDisplaysExpected(
            autoComplete(command, expectedModel.getFilteredPersonList()), expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }

    private String getCommandWord(String filePath) {
        return ImportCommand.COMMAND_WORD + " " + addToTestDataPathIfNotNull(filePath);
    }

    /**
     * Add {@code prefsFileInTestDataFolder} to {@code testFolder}.
     */
    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
               ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
               : null;
    }

}
```
