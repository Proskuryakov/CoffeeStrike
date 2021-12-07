package ru.vsu.cs.proskuryakov.coffeestrike.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.cs.proskuryakov.coffeestrike.db.domains.UnitItem;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.UnitRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitServiceTest {

    @Mock
    private UnitRepository unitRepository;
    @InjectMocks
    private UnitService unitService;

    @BeforeEach
    private void initService() {
        unitService = new UnitService(unitRepository);
    }

    @Test
    @DisplayName("Get all units test")
    void getUnitsTest() {
        var unit1 = new UnitItem("1", "мл");
        var unit2 = new UnitItem("2", "гр");
        var unit3 = new UnitItem("3", "шт");
        when(unitRepository.findAll()).thenReturn(List.of(unit1, unit2, unit3));

        var res = unitService.getAll();
        assertEquals(3, res.size());
        assertIterableEquals(List.of(unit1, unit2, unit3), res);
    }

}