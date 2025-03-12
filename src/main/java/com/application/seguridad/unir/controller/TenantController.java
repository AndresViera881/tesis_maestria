package com.application.seguridad.unir.controller;

import com.application.seguridad.unir.dto.UserDto;
import com.application.seguridad.unir.exception.ModeloNotFoundException;
import com.application.seguridad.unir.model.Rol;
import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.repositories.IRolRepository;
import com.application.seguridad.unir.repositories.IUserRepository;
import com.application.seguridad.unir.services.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inquilinos")
public class TenantController {

    private PasswordEncoder _passwordEncoder;
    private IRolRepository _rolRepository;
    private IUserService _usuarioService;

    private IUserRepository _usuarioRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    public TenantController(
            IUserService usuarioService,
            IUserRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            IRolRepository rolRepository,
            ModelMapper mapper
    ) {
        this._usuarioService = usuarioService;
        this._usuarioRepository = usuarioRepository;
        this._passwordEncoder = passwordEncoder;
        this._rolRepository = rolRepository;
        this.mapper = mapper;
    }

    @GetMapping("listarInquilinos")
    public ResponseEntity<List<UserDto>> listarInquilinos() throws Exception {
        List<UserDto> lista = _usuarioService.ListAll().stream()
                .filter(usuario -> usuario.isState() == true)
                .filter(usuario -> usuario.getRoles().stream()
                        .anyMatch(rol -> "INQUILINO".equals(rol.getName())))
                .map(p -> mapper.map(p, UserDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @PostMapping("registrarInquilino")
    public ResponseEntity<Void> registrarInquilino(@RequestBody UserDto dtoRequest) throws Exception {
        User usuario = mapper.map(dtoRequest, User.class);
        usuario.setPassword(_passwordEncoder.encode(dtoRequest.getPassword()));
        Rol rolInquilino = _rolRepository.findByName("INQUILINO")
                .orElseThrow(() -> new RuntimeException("Rol INQUILINO no encontrado"));
        usuario.setRoles(Collections.singletonList(rolInquilino));
        User usuarioGuardado = _usuarioService.Register(usuario);
        UserDto dtoResponse = mapper.map(usuarioGuardado, UserDto.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dtoResponse.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("modificarInquilino/{id}")
    public ResponseEntity<UserDto> modificarInquilino(@PathVariable Integer id, @RequestBody UserDto dtoRequest) throws Exception {
        User bo = _usuarioService.ListById(id);
        if (bo == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO: " + id);
        }
        // Mapear los datos del DTO a la entidad
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.map(dtoRequest, bo);
        // Verifica si la contraseña es nula o vacía antes de actualizar
        if (dtoRequest.getPassword() != null && !dtoRequest.getPassword().isEmpty()) {
            bo.setPassword(_passwordEncoder.encode(dtoRequest.getPassword()));
        }
        // No modificar los roles directamente si no se envían en la petición
        if (dtoRequest.getRoles() != null && !dtoRequest.getRoles().isEmpty()) {
            Set<Rol> roles = dtoRequest.getRoles().stream()
                    .map(rolDto -> _rolRepository.findById(rolDto.getIdRole())
                            .orElseThrow(() -> new ModeloNotFoundException("ROL NO ENCONTRADO: " + rolDto.getIdRole())))
                    .collect(Collectors.toSet());
            bo.setRoles(new ArrayList<>(roles)); // Convertimos el Set a List para evitar errores de casting
        }
        User usuarioActualizado = _usuarioService.Update(bo);
        // Mapeo de respuesta
        UserDto dtoResponse = mapper.map(usuarioActualizado, UserDto.class);
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @DeleteMapping("eliminarInquilino/{id}")
    public ResponseEntity<Void> eliminarInquilino(@PathVariable("id") Integer id) throws Exception {
        User obj = _usuarioService.ListById(id);
        if (obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO: " + id);
        }
        obj.setState(false);
        _usuarioService.Update(obj);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve 204 No Content para indicar éxito
    }
}
