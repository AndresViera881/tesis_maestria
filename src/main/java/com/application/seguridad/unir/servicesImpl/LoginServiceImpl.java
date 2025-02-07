package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.repo.ILoginRepo;
import com.application.seguridad.unir.services.ILoginService;
import com.application.seguridad.unir.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private ILoginRepo _loginRepo;
    @Override
    public Optional<User> authenticate(String username, String password) {
        return _loginRepo.findByUsernameAndPassword(username, password);
    }
}
