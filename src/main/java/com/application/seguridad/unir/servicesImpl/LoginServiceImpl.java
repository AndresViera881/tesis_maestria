package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.repositories.ILoginRepo;
import com.application.seguridad.unir.services.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private ILoginRepo loginRepo;
    @Override
    public Optional<User> authenticate(String username, String password) {
        return loginRepo.findByUsernameAndPassword(username, password);
    }
}
