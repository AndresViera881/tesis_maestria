package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.exception.ModeloNotFoundException;
import com.application.seguridad.unir.model.Usuario;
import com.application.seguridad.unir.repositories.IGenericRepo;
import com.application.seguridad.unir.repositories.IUsuarioRepository;
import com.application.seguridad.unir.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class UserServiceImpl extends CRUDImpl<Usuario, Integer> implements IUserService {
    @Autowired
    private IUsuarioRepository repo;
    @Override
    protected IGenericRepo<Usuario, Integer> getRepo() {
        return repo;
    }

}
