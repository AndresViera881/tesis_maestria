package com.application.seguridad.unir.security;

import com.application.seguridad.unir.model.Rol;
import com.application.seguridad.unir.model.User;
import com.application.seguridad.unir.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private IUserRepository _userRepository;

    // Método para convertir roles en authorities
    public Collection<GrantedAuthority> mapToAuthorities(List<Rol> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario en el repositorio
        User user = _userRepository.findByUsernameWithRoles(username);

        // Obtener los roles
        List<Rol> roles = user.getRoles() != null ? user.getRoles() : List.of();

        // Convertir roles a authorities
        Collection<GrantedAuthority> authorities = mapToAuthorities(roles);

        // Devolver CustomUserDetails con ID
        return new CustomUserDetails(
                user.getId(),             // Asumiendo que user tiene getId()
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

}
