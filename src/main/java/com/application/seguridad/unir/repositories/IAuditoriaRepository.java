package com.application.seguridad.unir.repositories;

import com.application.seguridad.unir.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAuditoriaRepository extends JpaRepository<Auditoria, Long> {
}