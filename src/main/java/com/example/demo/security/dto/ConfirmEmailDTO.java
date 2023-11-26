package com.example.demo.security.dto;

import javax.validation.constraints.NotBlank;

public class ConfirmEmailDTO {
    @NotBlank
    private String username;
    @NotBlank
    private Boolean confirmacion;

    public ConfirmEmailDTO() {
    }

    public ConfirmEmailDTO(String username, Boolean confirmacion) {
        this.username = username;
        this.confirmacion = confirmacion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(Boolean confirmacion) {
        this.confirmacion = confirmacion;
    }
}