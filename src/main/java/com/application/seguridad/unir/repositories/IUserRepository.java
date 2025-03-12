package com.application.seguridad.unir.repositories;

import com.application.seguridad.unir.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends IGenericRepo<User, Integer> {
    //Metodo para buscar un usuario por su nombre
    User findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    User findByUsernameWithRoles(@Param("username") String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.idUser = :id")
    User findByUserId(@Param("id") Integer id);
}
