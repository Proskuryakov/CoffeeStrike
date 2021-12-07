package ru.vsu.cs.proskuryakov.coffeestrike.app.controllers;

import org.springframework.http.ResponseEntity;
import ru.vsu.cs.proskuryakov.coffeestrike.api.UnitAPI;
import ru.vsu.cs.proskuryakov.coffeestrike.api.models.Unit;

import java.util.List;

public class UnitController implements UnitAPI {
    @Override
    public ResponseEntity<List<Unit>> getAll() {
        return null;
    }
}
