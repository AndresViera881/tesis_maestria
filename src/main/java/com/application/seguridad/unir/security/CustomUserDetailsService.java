package com.application.seguridad.unir.security;

import com.application.seguridad.unir.model.Rol;
import com.application.seguridad.unir.model.Usuario;
import com.application.seguridad.unir.repositories.IUsuarioRepository;
import com.application.seguridad.unir.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository _userRepository;

    // Método para convertir roles en authorities
    public Collection<GrantedAuthority> mapToAuthorities(List<Rol> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    // Método para cargar el usuario por su username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario en el repositorio
        System.out.println("username : " + username);
        Usuario user = _userRepository.findByUsernameWithRoles(username);
        System.out.println("USER: " + user);

        // Obtener los roles del usuario
        List<Rol> roles = user.getRoles() != null ? user.getRoles() : List.of();

        // Mapear los roles a authorities y devolver un UserDetails con el usuario, la contraseña y los roles
        return new User(
                user.getUsername(),
                user.getPassword(),
                mapToAuthorities(roles)  // Convertir los roles en authorities
        );
    }
}
