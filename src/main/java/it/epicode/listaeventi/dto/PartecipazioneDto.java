package it.epicode.listaeventi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PartecipazioneDto {

    /**
     * L'ID dell'evento a cui l'utente desidera partecipare.
     * Non può essere nullo e deve essere un numero positivo.
     * Corrisponde alla relazione con l'attributo 'evento' nel modello Partecipazione.
     */
    @NotNull(message = "L'ID dell'evento non può essere nullo")
    @Min(value = 1, message = "L'ID dell'evento deve essere un numero positivo")
    private Long eventoId;
}
