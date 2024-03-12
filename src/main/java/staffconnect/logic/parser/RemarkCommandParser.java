package staffconnect.logic.parser;

import staffconnect.commons.core.index.Index;
import staffconnect.commons.exceptions.IllegalValueException;
import staffconnect.logic.commands.RemarkCommand;

import staffconnect.logic.parser.exceptions.ParseException;
import static java.util.Objects.requireNonNull;
import static staffconnect.logic.parser.CliSyntax.PREFIX_REMARK;
import static staffconnect.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class RemarkCommandParser {

    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemarkCommand.MESSAGE_USAGE), ive);
        }

        String remark = argMultimap.getValue(PREFIX_REMARK).orElse("");

        return new RemarkCommand(index, remark);
    }
}
