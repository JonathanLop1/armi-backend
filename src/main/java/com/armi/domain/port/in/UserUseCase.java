package com.armi.domain.port.in;

import com.armi.model.AppUser;
import java.util.List;
import java.util.Optional;

public interface UserUseCase {
    List<AppUser> getAllUsers();
    AppUser createUser(AppUser user);
    List<AppUser> createUsersBulk(List<AppUser> users);
    Optional<AppUser> login(String emailOrId, String password);
    void deleteUser(Long id);
}
