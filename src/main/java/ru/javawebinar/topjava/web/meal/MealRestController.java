package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.web.meal.MealRestController.REST;

@RestController
@RequestMapping(value = REST, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST = "/rest/meals";

    @Override
    @GetMapping
    public List<MealTo> getAll() {
        log.info("getAll meals by userId {}", SecurityUtil.authUserId());
        return super.getAll();
    }

    @Override
    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        log.info("get by meal id {} and userId {}", id, SecurityUtil.authUserId());
        return super.get(id);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete by meal id {}", id);
        super.delete(id);
    }

    @Override
    @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        log.info("update by meal id {} and userId {}", id, SecurityUtil.authUserId());
        super.update(meal, id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createRestful(@RequestBody Meal meal) {
        log.info("getAll by userId {}", SecurityUtil.authUserId());
        return new ResponseEntity<>(super.create(meal), HttpStatus.CREATED);
    }

    @GetMapping("/filter")
    public List<MealTo> getBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return super.getBetween(startDate.toLocalDate(), startDate.toLocalTime(), endDate.toLocalDate(), endDate.toLocalTime());
    }
}