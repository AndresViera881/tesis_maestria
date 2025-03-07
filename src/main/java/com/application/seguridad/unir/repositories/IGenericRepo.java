package com.application.seguridad.unir.repositories;

import com.application.seguridad.unir.dto.UserDto;
import com.application.seguridad.unir.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface IGenericRepo<T, Integer> extends JpaRepository<T, Integer> {
}
