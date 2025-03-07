package com.application.seguridad.unir.repositories;

import com.application.seguridad.unir.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRolRepository extends IGenericRepo<Rol, Integer> {
    //Metodo para buscar un rol por su nombre
    Optional<Rol> findByName(String name);
}
