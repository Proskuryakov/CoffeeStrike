package ru.vsu.cs.proskuryakov.coffeestrike.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Unit;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        value = "/api/unit",
        produces = {APPLICATION_JSON_VALUE}
)
public interface UnitAPI {

    @GetMapping()
    ResponseEntity<List<Unit>> getAll();

}
