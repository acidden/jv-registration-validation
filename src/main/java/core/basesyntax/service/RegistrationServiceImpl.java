package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    private boolean checkLoginIsValid(String login) {
        return login != null && login.length() >= 6;
    }

    private boolean checkPasswordIsValid(String password) {
        return password != null && password.length() >= 6;
    }

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RegistrationException("User can not be null");
        }
        if (!checkLoginIsValid(user.getLogin())) {
            throw new RegistrationException("Login must be at least 6 character");
        }
        if (!checkPasswordIsValid(user.getPassword())) {
            throw new RegistrationException("Password must be at least 6 character");
        }
        if (user.getAge() == null || user.getAge() < 18) {
            throw new RegistrationException("User must be at least 18 years old");
        }
        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("User with this login already exists");
        }
        return storageDao.add(user);
    }
}
