package it.epicode.listaeventi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventoDto {

    @NotEmpty(message = "il titolo non può essere vuoto")
    private String titolo;
    @NotEmpty(message = "la descrizione non può essere vuota")
    private String descrizione;
    @NotEmpty(message = "il luogo non può essere vuoto")
    private String luogo;
    @NotNull(message = "la data non può essere nulla")
    private LocalDateTime dataEvento;
    @NotNull(message = "Il numero di posti totali non può essere nullo")
    @Min(value = 1, message = "I posti totali devono essere almeno 1")
    private Integer postiTotali;

}
