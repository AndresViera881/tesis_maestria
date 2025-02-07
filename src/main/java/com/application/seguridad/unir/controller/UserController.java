package com.application.seguridad.unir.controller;

import com.application.seguridad.unir.dto.LoginDto;
import com.application.seguridad.unir.dto.UserDto;
import com.application.seguridad.unir.exception.ModeloNotFoundException;
import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.services.ILoginService;
import com.application.seguridad.unir.services.IUserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@CrossOrigin(origins = {"http://localhost:8080"})
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private IUserService service;
    @Autowired
    private ILoginService _loginService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> ListAll() throws Exception{
        List<UserDto> listar = service.ListAll().stream().map(p -> mapper.map(p, UserDto.class)).collect(Collectors.toList());
        return new ResponseEntity<>(listar, HttpStatus.OK);
    }

    @GetMapping("/users/{idUser}")
    public ResponseEntity<UserDto> ListById(@PathVariable("idUser") Integer idUser) throws Exception{
        UserDto dtoResponse;
        User obj = service.ListById(idUser);
        if(obj == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + idUser);
        }else {
            dtoResponse = mapper.map(obj, UserDto.class);
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<Void> Register(@Valid @RequestBody UserDto dtoRequest) throws Exception {
        User p = mapper.map(dtoRequest, User.class);
        User obj = service.Register(p);
        UserDto dtoResponse = mapper.map(obj, UserDto.class);
        //localhost:8080/pacientes/9
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{idUser}").buildAndExpand(dtoResponse.getIdUser()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDto loginRequest) {
        Optional<User> user = _loginService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PutMapping("/users/{idUser}")
    public ResponseEntity<UserDto> Update(@Valid @RequestBody UserDto dtoRequest) throws Exception {
        User pac = service.ListById(dtoRequest.getIdUser());
        if(pac == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + dtoRequest.getIdUser());
        }
        User p = mapper.map(dtoRequest, User.class);
        User obj = service.Update(p);
        UserDto dtoResponse = mapper.map(obj, UserDto.class);
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> Delete(@PathVariable("id") Integer id) throws Exception {
        User pac = service.ListById(id);
        if(pac == null) {
            throw new ModeloNotFoundException("ID NO ENCONTRADO " + id);
        }
        service.Delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
