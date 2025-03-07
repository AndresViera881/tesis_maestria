package com.application.seguridad.unir.repositories;

import com.application.seguridad.unir.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILoginRepo extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.username = :username AND u.password = :password")
    Optional<Usuario> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}
