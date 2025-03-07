package com.application.seguridad.unir.services;

import com.application.seguridad.unir.model.Usuario;

import java.util.Optional;

public interface ILoginService {
    Optional<Usuario> authenticate(String username, String password);
}
