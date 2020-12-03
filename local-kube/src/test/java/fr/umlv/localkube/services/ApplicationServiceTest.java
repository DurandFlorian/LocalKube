package fr.umlv.localkube.services;

import fr.umlv.localkube.model.Application;
import fr.umlv.localkube.services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationServiceTest {

    private ApplicationService repository;

    @BeforeEach
    void setUp() {
        repository = new ApplicationService();
    }

    @Test @Tag("save")
    void shouldThrowNullPointerExceptionWhenSaveNullApp() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
        assertEquals(0, repository.size());
    }

    @Test @Tag("remove")
    void shouldThrowNullPointerExceptionWhenRemoveNull() {
        assertThrows(NullPointerException.class, () -> repository.delete(null));
    }

    @Test @Tag("getNextId")
    void shouldReturnOneIfAppsIsEmpty() {
        assertEquals(1, repository.getNextId());
    }


    @Test @Tag("findById")
    void shouldNotFindAppWithFalseId() {
        var result = repository.findById(-1);

        assertTrue(result.isEmpty());
    }

    @Test @Tag("getNextId")
    void getNextIdShouldReturnOne() {
        var result = repository.getNextId();

        assertEquals(1, result);
    }

    @Test @Tag("getNextId")
    void getNextIdShouldReturnSix() {
        repository.save(new Application.ApplicationBuilder().setId(5).build());
        var result = repository.getNextId();

        assertEquals(6, result);
    }


}