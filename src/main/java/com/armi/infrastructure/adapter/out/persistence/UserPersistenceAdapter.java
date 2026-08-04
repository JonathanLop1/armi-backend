package com.armi.infrastructure.adapter.out.persistence;

import com.armi.domain.port.out.UserPersistencePort;
import com.armi.model.AppUser;
import com.armi.repository.AppUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserPersistencePort {

    private final AppUserRepository userRepository;

    public UserPersistenceAdapter(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AppUser> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<AppUser> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteAllUsers() {
        userRepository.truncateUsers();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
