package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.dao.StorageDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        Storage.people.clear();
        StorageDao storageDao = new StorageDaoImpl();
        registrationService = new RegistrationServiceImpl(storageDao);
    }

    @Test
    void register_validUser_ok() {
        User user = new User("validLogin", "validPassword", 20);
        User actual = registrationService.register(user);
        assertNotNull(actual);
        assertEquals(user.getLogin(), actual.getLogin());
    }

    @Test
    void register_validUserExactBorderValues_Ok() {
        User user = new User("login6", "passwd", 18);
        User actual = registrationService.register(user);
        assertNotNull(actual);
    }

    @Test
    void register_nullUser_NotOk() {
        assertThrows(RegistrationException.class, () -> registrationService.register(null));
    }

    @Test
    void register_nullLogin_NotOk() {
        User user = new User(null, "password", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullPassword_NotOk() {
        User user = new User("login123", null, 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_nullAge_NotOk() {
        User user = new User("login123", "password", null);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortLogin_NotOk() {
        User user = new User("short", "password", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_shortPassword_NotOk() {
        User user = new User("login123", "pass", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_emptyPassword_NotOk() {
        User user = new User("login123", "", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_fiveCharacterPassword_NotOk() {
        User user = new User("login123", "abcde", 20);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_underAgeUser_NotOk() {
        User user = new User("login123", "password", 15);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_negativeAgeUser_NotOk() {
        User user = new User("login123", "password", -5);
        assertThrows(RegistrationException.class, () -> registrationService.register(user));
    }

    @Test
    void register_loginAlreadyExists_NotOk() {
        User user1 = new User("login123", "password1", 20);
        User user2 = new User("login123", "password2", 25);
        Storage.people.add(user1);
        assertThrows(RegistrationException.class, () -> registrationService.register(user2));
    }
}
