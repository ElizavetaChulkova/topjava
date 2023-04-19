package ru.javawebinar.topjava.to;

import org.hibernate.validator.constraints.Range;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.util.UsersUtil;
import ru.javawebinar.topjava.web.HasIdAndEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

import static ru.javawebinar.topjava.model.User.PASSWORD_SIZE_EXCEPTION;

public class UserTo extends BaseTo implements Serializable, HasIdAndEmail {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 100, message = AbstractNamedEntity.NAME_SIZE_EXCEPTION)
    private String name;

    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(min = 5, max = 128, message = PASSWORD_SIZE_EXCEPTION)
    private String password;

    @Range(min = 10, max = 10000)
    @NotNull
    private Integer caloriesPerDay = UsersUtil.DEFAULT_CALORIES_PER_DAY;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password, Integer caloriesPerDay) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.caloriesPerDay = caloriesPerDay;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCaloriesPerDay(Integer caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public Integer getCaloriesPerDay() {
        return caloriesPerDay;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", caloriesPerDay='" + caloriesPerDay + '\'' +
                '}';
    }
}
