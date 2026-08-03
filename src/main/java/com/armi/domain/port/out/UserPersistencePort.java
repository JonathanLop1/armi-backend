package com.armi.domain.port.out;

import com.armi.model.AppUser;
import java.util.List;
import java.util.Optional;

public interface UserPersistencePort {
    List<AppUser> findAllUsers();
    Optional<AppUser> findUserById(Long id);
    AppUser saveUser(AppUser user);
    void deleteAllUsers();
    void deleteUserById(Long id);
}
