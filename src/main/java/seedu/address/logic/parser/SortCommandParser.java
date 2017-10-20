package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.function.Consumer;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {
    private String attribute;
    private Boolean isReversed;

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE);

        if (argMultimap.size() > 2) {
            throw new ParseException(
                    String.format(SortCommand.MESSAGE_MULTIPLE_ATTRIBUTE_ERROR, SortCommand.MESSAGE_USAGE)
            );
        }

        /**
         * Invalid command arguments would result in a loaded preamble
         */
        if (!argMultimap.getPreamble().equals("")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        if (argMultimap.size() == 1) {
            attribute = PREFIX_NAME.getPrefix();
        }

        argMultimap.getValue(PREFIX_NAME).ifPresent(setOrder(PREFIX_NAME));
        argMultimap.getValue(PREFIX_PHONE).ifPresent(setOrder(PREFIX_PHONE));

        return new SortCommand(attribute, isReversed);
    }

    private Consumer<String> setOrder(Prefix prefix) {
        return s-> {
            attribute = prefix.toString();

            if (s.equals(SortCommand.REVERSE_SEQUENCE)) {
                isReversed = new Boolean(true);
                return;
            } else {
                isReversed = new Boolean(false);
                return;
            }
        };
    }
}
