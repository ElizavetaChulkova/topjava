package ru.javawebinar.topjava.web;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

import static ru.javawebinar.topjava.web.ExceptionInfoHandler.EXCEPTION_DUPLICATE_EMAIL;

public class EmailValidator implements Validator {
    //    https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/validation/Validator.html
//    https://www.logicbig.com/how-to/code-snippets/jcode-spring-framework-localvalidatorfactorybean.html
    private final UserService service;

    public EmailValidator(UserService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserTo user = (UserTo) target;
        User oldUser = service.getByEmail(user.getEmail());
        if (!user.getEmail().isEmpty()) {
            if (oldUser != null && !oldUser.getId().equals(user.id())) {
                errors.rejectValue("email", EXCEPTION_DUPLICATE_EMAIL);
            }
        }
    }
}
