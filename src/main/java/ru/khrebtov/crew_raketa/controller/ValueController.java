package ru.khrebtov.crew_raketa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.khrebtov.crew_raketa.dto.ValueDto;
import ru.khrebtov.crew_raketa.entity.Values;
import ru.khrebtov.crew_raketa.services.ValueService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/api/values")
@Validated
public class ValueController {
    private static final Logger logger = LoggerFactory.getLogger(ValueController.class);

    private final ValueService valueService;

    public ValueController(ValueService valueService) {
        this.valueService = valueService;
    }

    @PostMapping
    public ResponseEntity<Object> createValues(@RequestParam @Min(0) Integer number) {
        logger.info("createValues {} ", number);
        //я так и не понял, почему не проходит валидация, буду рад, если Вы подскажите, по всем канонам должно валидироваться-но нет
        if(number<0){
            return ResponseEntity.status(BAD_REQUEST).body("number must be > 0");
        }
        valueService.createValues(number);

        return ResponseEntity.status(CREATED).body(number);
    }

    @DeleteMapping
    public ResponseEntity<Object> removeAll() {
        logger.info("removeAll ");
        valueService.removeAll();

        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateValue(@PathVariable long id, @Valid @RequestBody ValueDto valueDto) {
        logger.info("updateValue id: {},  valueDto: {} ", id, valueDto);
        if (valueService.updateValue(id, valueDto)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<Values>> findAll(@RequestParam(required = false) Map<String, String> allRequestParams) {
        logger.info("getValues params: {}", allRequestParams);

        return ResponseEntity.ok(valueService.findAll(allRequestParams));
    }
}
