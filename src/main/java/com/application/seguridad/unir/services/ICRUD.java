package com.application.seguridad.unir.services;

import java.util.List;

public interface ICRUD<T, ID> {
    T Register(T t) throws Exception;
    T Update(T t) throws Exception;
    List<T> ListAll() throws Exception;
    T ListById(ID id) throws Exception;
    void Delete(ID id) throws Exception;
}
