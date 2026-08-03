package com.armi.domain.service;

import com.armi.domain.port.in.UserUseCase;
import com.armi.domain.port.out.UserPersistencePort;
import com.armi.model.AppUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserDomainService implements UserUseCase {

    private final UserPersistencePort userPersistencePort;

    public UserDomainService(UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userPersistencePort.findAllUsers();
    }

    @Override
    @Transactional
    public AppUser createUser(AppUser user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio");
        }
        Optional<AppUser> existing = userPersistencePort.findAllUsers().stream()
                .filter(u -> user.getEmail().equalsIgnoreCase(u.getEmail()))
                .findFirst();
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese correo");
        }

        if (user.getAccumulatedEarnings() == null) {
            user.setAccumulatedEarnings(0.0);
        }
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("DRIVER");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            user.setPassword("123456");
        }

        return userPersistencePort.saveUser(user);
    }

    @Override
    @Transactional
    public List<AppUser> createUsersBulk(List<AppUser> users) {
        List<AppUser> existingUsers = userPersistencePort.findAllUsers();
        java.util.Set<String> existingEmails = existingUsers.stream()
                .map(u -> u.getEmail().trim().toLowerCase())
                .collect(java.util.stream.Collectors.toSet());

        List<AppUser> savedUsers = new java.util.ArrayList<>();
        for (AppUser u : users) {
            if (u.getEmail() == null || u.getEmail().trim().isEmpty()) continue;
            String cleanEmail = u.getEmail().trim().toLowerCase();
            if (existingEmails.contains(cleanEmail)) continue;

            if (u.getAccumulatedEarnings() == null) u.setAccumulatedEarnings(0.0);
            if (u.getRole() == null || u.getRole().isEmpty()) u.setRole("DRIVER");
            if (u.getPassword() == null || u.getPassword().isEmpty()) u.setPassword(u.getEmail());
            
            AppUser saved = userPersistencePort.saveUser(u);
            savedUsers.add(saved);
            existingEmails.add(cleanEmail);
        }
        return savedUsers;
    }

    @Override
    public Optional<AppUser> login(String emailOrId, String password) {
        if (emailOrId == null || password == null) {
            return Optional.empty();
        }
        return userPersistencePort.findAllUsers().stream()
                .filter(u -> (emailOrId.trim().equalsIgnoreCase(u.getEmail()) || emailOrId.trim().equalsIgnoreCase(u.getName())) 
                          && password.trim().equals(u.getPassword()))
                .findFirst();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userPersistencePort.deleteUserById(id);
    }
}
