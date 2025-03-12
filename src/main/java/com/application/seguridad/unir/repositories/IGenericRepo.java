package com.application.seguridad.unir.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericRepo<T, Integer> extends JpaRepository<T, Integer> {
}
