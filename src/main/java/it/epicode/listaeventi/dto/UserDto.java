package it.epicode.listaeventi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {


    @NotEmpty(message = "lo username non può essere vuoto")
    private String username;
    @NotEmpty(message = "la password non può essere vuota")
    private String password;
    @NotEmpty(message = "l'email non può essere vuota")
    @Email(message = "l'email deve essere ben strutturata")
    private String email;
}
