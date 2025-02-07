package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.repo.IGenericRepo;
import com.application.seguridad.unir.services.ICRUD;

import java.util.List;

public abstract class CRUDImpl<T,ID> implements ICRUD<T,ID> {

    protected abstract IGenericRepo<T, ID> getRepo();

    @Override
    public T Register(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public T Update(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public List<T> ListAll() throws Exception {
        return getRepo().findAll();
    }

    @Override
    public T ListById(ID id) throws Exception {
        return getRepo().findById(id).orElse(null);
    }

    @Override
    public void Delete(ID id) throws Exception {
        getRepo().deleteById(id);
    }
}
