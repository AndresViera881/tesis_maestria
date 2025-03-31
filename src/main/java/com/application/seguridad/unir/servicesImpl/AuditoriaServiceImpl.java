package com.application.seguridad.unir.servicesImpl;

import com.application.seguridad.unir.model.Auditoria;
import com.application.seguridad.unir.repositories.IAuditoriaRepository;
import com.application.seguridad.unir.services.IAuditoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaServiceImpl implements IAuditoriaService {

    @Autowired
    private IAuditoriaRepository auditoriaRepository;

    @Override
    public void registrarAuditoria(Auditoria auditoria) {
        auditoriaRepository.save(auditoria);
    }
}
