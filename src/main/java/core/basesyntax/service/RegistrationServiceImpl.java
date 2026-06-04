package core.basesyntax.service;

import core.basesyntax.dao.StorageDao;
import core.basesyntax.model.User;

public class RegistrationServiceImpl implements RegistrationService {
    private static final int MIN_AGE = 18;
    private static final int MIN_LOGIN_LENGTH = 6;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private final StorageDao storageDao;

    public RegistrationServiceImpl(StorageDao storageDao) {
        this.storageDao = storageDao;
    }

    private boolean checkLoginIsValid(String login) {
        return login != null && login.length() >= MIN_LOGIN_LENGTH;
    }

    private boolean checkPasswordIsValid(String password) {
        return password != null && password.length() >= MIN_PASSWORD_LENGTH;
    }

    @Override
    public User register(User user) {
        if (user == null) {
            throw new RegistrationException("User can not be null");
        }
        if (!checkLoginIsValid(user.getLogin())) {
            throw new RegistrationException("Login must be at least"
                    + MIN_LOGIN_LENGTH + "character");
        }
        if (!checkPasswordIsValid(user.getPassword())) {
            throw new RegistrationException("Password must be at least "
                    + MIN_PASSWORD_LENGTH + " character");
        }
        if (user.getAge() == null || user.getAge() < MIN_AGE) {
            throw new RegistrationException("User must be at least " + MIN_AGE + " years old");
        }
        if (storageDao.get(user.getLogin()) != null) {
            throw new RegistrationException("User with this login already exists");
        }
        return storageDao.add(user);
    }
}
