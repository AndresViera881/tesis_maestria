package com.application.seguridad.unir.controller;

import com.application.seguridad.unir.dto.AuthResponseDto;
import com.application.seguridad.unir.dto.LoginDto;
import com.application.seguridad.unir.security.JwtGenerador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private AuthenticationManager _authenticationManager;
    private JwtGenerador _jwtGenerador;

    public LoginController(AuthenticationManager authenticationManager, JwtGenerador jwtGenerador) {
        this._authenticationManager = authenticationManager;
        this._jwtGenerador = jwtGenerador;
    }

    //METODO PARA LOGUEAR UN USUARIO
    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication = _authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = _jwtGenerador.generarToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }
}
