package com.application.seguridad.unir.dto;

import com.application.seguridad.unir.model.Rol;
import lombok.*;

import java.util.List;

@Data
public class UserDto {
    private Integer Id;
    private String Identification;
    private String name;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private List<Rol> roles;
    private boolean State;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getIdentification() {
        return Identification;
    }

    public void setIdentification(String identification) {
        Identification = identification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public boolean isState() {
        return State;
    }

    public void setState(boolean state) {
        State = state;
    }
}
