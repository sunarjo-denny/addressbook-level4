package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.commons.core.index.Index;
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

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Remark: %2$d";

    private final Index index;
    private final String remark;

    /**
     * @param index of the person whose remark will be edited
     * @param remark of the person to be edited
     */
    public RemarkCommand(Index index, String remark) {
        requireNonNull(index);
        requireNonNull(remark);

        this.index = index;
        this.remark = remark;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        throw new CommandException(String.format(MESSAGE_ARGUMENTS, index.getOneBased(), remark));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        RemarkCommand e = (RemarkCommand) other;
        return index.equals(e.index) && remark.equals(e.remark);
    }
}
