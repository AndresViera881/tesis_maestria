package com.application.seguridad.unir.services;

import com.application.seguridad.unir.model.User;

import java.util.Optional;

public interface ILoginService {
    Optional<User> authenticate(String username, String password);
}
