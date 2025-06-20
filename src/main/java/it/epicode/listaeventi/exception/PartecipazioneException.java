package it.epicode.listaeventi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Indica che questa eccezione dovrebbe risultare in un HTTP 400 Bad Request

public class PartecipazioneException extends RuntimeException {
    public PartecipazioneException(String message) {
        super(message);
    }
}
