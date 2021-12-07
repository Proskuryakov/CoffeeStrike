package ru.vsu.cs.proskuryakov.coffeestrike.app.controllers;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.proskuryakov.coffeestrike.api.UnitAPI;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Unit;
import ru.vsu.cs.proskuryakov.coffeestrike.app.services.UnitService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UnitController implements UnitAPI {

    private final UnitService unitService;
    private final MapperFacade mapper;

    @Override
    public ResponseEntity<List<Unit>> getAll() {
        return Optional.of(unitService.getAll())
                .map(ul -> mapper.mapAsList(ul, Unit.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

}
