package com.armi.config;

import com.armi.model.AppUser;
import com.armi.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;

    public DataInitializer(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(new AppUser("ARMI Admin", "admin@armi.com", "123456", "ADMIN"));
        }
    }
}
