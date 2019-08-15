package gamesys.codingexercise;

import gamesys.codingexercise.exclusion.ExclusionService;
import gamesys.codingexercise.validation.ParamValidationException;
import gamesys.codingexercise.validation.ParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
public class RegistrationService {
    public static final String MESSAGE_OK = "ok";
    public static final String MESSAGE_DUPLICATE = "The user already exists";
    public static final String MESSAGE_BLACKLISTED = "The user is blacklisted";

    @Autowired
    ExclusionService exclusionService;

    private final Set<String> registeredUsers = new HashSet<>();

    @GetMapping(path = "/register", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> registerWithGet(Registration registration) {
        return register(registration);
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> registerWithPost(@RequestBody Registration registration) {
        return register(registration);
    }

    private ResponseEntity<String> register(Registration registration) {
        try {
            ParamValidator.validate(registration);
        }
        catch (ParamValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        if (registeredUsers.contains(registration.getUsername()))
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(MESSAGE_DUPLICATE);

        if (!exclusionService.validate(registration.getDob(), registration.getSsn()))
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(MESSAGE_BLACKLISTED);

        registeredUsers.add(registration.getUsername());
        return ResponseEntity.ok(MESSAGE_OK);
    }
}
