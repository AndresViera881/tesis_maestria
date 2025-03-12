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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private PasswordEncoder _passwordEncoder;
    private IRolRepository _rolRepository;
    private IUserService _usuarioService;

    private IUserRepository _usuarioRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    public UsersController(
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

    @GetMapping
    public ResponseEntity<List<UserDto>> listar() throws Exception{
        List<UserDto> lista = _usuarioService.ListAll().stream().map(p -> mapper.map(p, UserDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }
    /*@PreAuthorize("hasRole('ADMIN')")*/
    @GetMapping("listarAdministradores")
    public ResponseEntity<List<UserDto>> listarAdministradores() throws Exception {
        List<UserDto> lista = _usuarioService.ListAll().stream()
                .filter(usuario -> usuario.isState() == true)
                .filter(usuario -> usuario.getRoles().stream()
                        .anyMatch(rol -> "ADMIN".equals(rol.getName())))
                .map(p -> mapper.map(p, UserDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /*@PreAuthorize("hasRole('ADMIN')")*/
    @PostMapping("registrarAdministrador")
    public ResponseEntity<Void> registrarAdministrador(@RequestBody UserDto dtoRequest) throws Exception {
        User usuario = mapper.map(dtoRequest, User.class);
        usuario.setPassword(_passwordEncoder.encode(dtoRequest.getPassword()));
        Rol rolAdministrador = _rolRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMINISTRADOR no encontrado"));
        usuario.setRoles(Collections.singletonList(rolAdministrador));
        User usuarioGuardado = _usuarioService.Register(usuario);
        UserDto dtoResponse = mapper.map(usuarioGuardado, UserDto.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dtoResponse.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    /*@PreAuthorize("hasRole('ADMIN')")*/
    @PutMapping("modificarAdministrador/{id}")
    public ResponseEntity<UserDto> modificarAdministrador(@PathVariable Integer id, @RequestBody UserDto dtoRequest) throws Exception {

        User bo = _usuarioService.ListById(id);
        if (bo == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO: " + id);
        }
        System.out.println("Usuario encontrado: " + bo); // Verifica si tiene un ID válido

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
        UserDto dtoResponse = mapper.map(usuarioActualizado, UserDto.class);
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    /*@PreAuthorize("hasRole('ADMIN')")*/
    @DeleteMapping("eliminarAdministrador/{id}")
    public ResponseEntity<Void> eliminarAdministrador(@PathVariable("id") Integer id) throws Exception {
        User obj = _usuarioService.ListById(id);
        if (obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO: " + id);
        }
        obj.setState(false);
        _usuarioService.Update(obj);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve 204 No Content para indicar éxito
    }
}
