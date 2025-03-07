package com.application.seguridad.unir.dto;

import lombok.Data;

@Data
public class RolDto {
    private Integer idRole;
    private String name;

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
