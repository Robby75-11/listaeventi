package it.epicode.listaeventi.model;


import jakarta.persistence.*;
import jdk.jfr.Event;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "partecipazioni")
@Data

public class Partecipazione {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Evento evento;
    private LocalDateTime dataPartecipazione;
    private LocalDateTime dataCreazione; // MODIFICATO
    private LocalDateTime dataAggiornamento; // MODIFICATO


}
