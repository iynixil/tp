package staffconnect.logic.parser;

import static java.util.Objects.requireNonNull;
import static staffconnect.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static staffconnect.logic.parser.CliSyntax.PREFIX_FACULTY;
import static staffconnect.logic.parser.CliSyntax.PREFIX_MODULE;
import static staffconnect.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import staffconnect.logic.commands.FilterCommand;
import staffconnect.logic.parser.exceptions.ParseException;
import staffconnect.model.person.Faculty;
import staffconnect.model.person.Module;
import staffconnect.model.person.PersonHasFacultyPredicate;
import staffconnect.model.person.PersonHasModulePredicate;
import staffconnect.model.person.PersonHasTagsPredicate;
import staffconnect.model.tag.Tag;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * FilterCommand and returns a FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_MODULE, PREFIX_FACULTY, PREFIX_TAG);

        if (!argMultimap.getValue(PREFIX_MODULE).isPresent() && !argMultimap.getValue(PREFIX_FACULTY).isPresent()
                && !argMultimap.getValue(PREFIX_TAG).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MODULE, PREFIX_FACULTY);

        Module module = null;
        Faculty faculty = null;
        Set<Tag> tags = null;

        if (argMultimap.getValue(PREFIX_MODULE).isPresent()) {
            module = ParserUtil.parseModule(argMultimap.getValue(PREFIX_MODULE).get());
        }
        if (argMultimap.getValue(PREFIX_FACULTY).isPresent()) {
            faculty = ParserUtil.parseFaculty(argMultimap.getValue(PREFIX_FACULTY).get());
        }
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            tags = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
        }

        PersonHasModulePredicate modulePredicate = new PersonHasModulePredicate(module);
        PersonHasFacultyPredicate facultyPredicate = new PersonHasFacultyPredicate(faculty);
        PersonHasTagsPredicate tagsPredicate = new PersonHasTagsPredicate(tags);

        return new FilterCommand(modulePredicate, facultyPredicate, tagsPredicate);
    }

}
