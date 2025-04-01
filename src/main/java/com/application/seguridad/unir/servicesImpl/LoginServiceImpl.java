package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.repositories.ILoginRepo;
import com.application.seguridad.unir.services.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private ILoginRepo loginRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // usa el mismo encoder que usaste para guardar el password

    @Override
    public Optional<User> authenticate(String username, String rawPassword) {
        Optional<User> userOptional = loginRepo.findByUsername(username); // ✅ solo buscar por username

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Comparar usando BCrypt
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return userOptional;
            }
        }

        return Optional.empty();
    }

}
