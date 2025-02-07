package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.repo.IGenericRepo;
import com.application.seguridad.unir.repo.IUserRepo;
import com.application.seguridad.unir.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends CRUDImpl<User, Integer> implements IUserService {
    @Autowired
    private IUserRepo repo;
    @Override
    protected IGenericRepo<User, Integer> getRepo() {
        return repo;
    }
}
