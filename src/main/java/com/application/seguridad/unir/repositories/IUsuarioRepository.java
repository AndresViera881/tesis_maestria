package com.application.seguridad.unir.repositories;

import com.application.seguridad.unir.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends IGenericRepo<Usuario, Integer> {
    //Metodo para buscar un usuario por su nombre
    Usuario findByUsername(String username);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles WHERE u.username = :username")
    Usuario findByUsernameWithRoles(@Param("username") String username);



}
