package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.regex.Pattern;

import static ru.javawebinar.topjava.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

@Component
public class EmailValidator implements Validator {
    //    https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/Validator.html
//    https://www.logicbig.com/how-to/code-snippets/jcode-spring-framework-localvalidatorfactorybean.html

    private final UserRepository repository;

    public static final String EMPTY_EMAIL_FIELD_EXCEPTION = "email.field.empty";
    public static final String INVALID_EMAIL_EXCEPTION = "email.invalid";

    public EmailValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        HasIdAndEmail user = (HasIdAndEmail) target;
        String email = user.getEmail();
        if (!email.isEmpty()) {
            if (!matchEmailPattern(email)) {
                errors.rejectValue("email", INVALID_EMAIL_EXCEPTION);
            } else {
                if (repository.getByEmail(email) != null) {
                    errors.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
                }
            }
        } else {
            errors.rejectValue("email", EMPTY_EMAIL_FIELD_EXCEPTION);
        }
    }

    private static boolean matchEmailPattern(String email) {
        return Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
                .matcher(email).matches();
    }
}
