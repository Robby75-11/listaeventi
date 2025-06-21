package it.epicode.listaeventi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PartecipazioneDto {


    @NotNull(message = "L'ID dell'evento non può essere nullo")
    @Min(value = 1, message = "L'ID dell'evento deve essere un numero positivo")
    private Long eventoId;

    @NotNull(message = "L'ID dell'utente non può essere nullo")
    @Min(value = 1, message = "L'ID dell'utente deve essere un numero positivo")
    private Long userId;
}
