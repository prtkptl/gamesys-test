package gamesys.codingexercise.validation;

import gamesys.codingexercise.Registration;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamValidator {
    public static final String USERNAME_ERROR = "Username must contain only alphanumerical characters and no spaces";
    public static final String PASSWORD_ERROR = "Password must be at least four characters and contain at least one upper case character and one number";
    public static final String DOB_ERROR = "Date of birth was not in a valid format - DD-MM-YYYY";
    public static final String SSN_ERROR = "SSN was invalid";

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static final Pattern SSN_PATTERN = Pattern.compile("^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$");

    static boolean validateUsername(String username) {
        return StringUtils.isAlphanumeric(username);
    }

    static boolean validatePassword(String password) {
        return (password != null
                && password.length() >= 4
                && password.chars().anyMatch(Character::isUpperCase)
                && password.chars().anyMatch(Character::isDigit));
    }

    static boolean validateDOB(String dob) {
        if (dob == null)
            return false;

        try {
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(dob);
            return true;
        }
        catch (ParseException e) {
            return false;
        }
    }

    static boolean validateSSN(String ssn) {
        if (ssn == null)
            return false;

        Matcher matcher = SSN_PATTERN.matcher(ssn);
        return matcher.matches();
    }

    public static void validate(Registration registration) throws ParamValidationException {
        List<String> errors = new ArrayList<>();

        if (!validateUsername(registration.getUsername()))
            errors.add(USERNAME_ERROR);

        if (!validatePassword(registration.getPassword()))
            errors.add(PASSWORD_ERROR);

        if (!validateDOB(registration.getDob()))
            errors.add(DOB_ERROR);

        if (!validateSSN(registration.getSsn()))
            errors.add(SSN_ERROR);

        if (!errors.isEmpty())
            throw new ParamValidationException("The following validation errors occurred: " + String.join(", ", errors));
    }
}
