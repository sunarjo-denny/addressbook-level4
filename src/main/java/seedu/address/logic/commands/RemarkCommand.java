package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Changes the remark of a person in the AddressBook
 */

public class RemarkCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "remark";
    public static final String COMMMAND_ALIAS = "r";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the person selected "
            + "by their index in the latest list.  Existing remark will be replaced with the latest remark.\n"
            + "Parameters: INDEX (must be positive integer) " + PREFIX_REMARK + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 " + PREFIX_REMARK + "Hates coffee.";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Remark command not implemented yet";

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        throw new CommandException(MESSAGE_NOT_IMPLEMENTED_YET);
    }
}
