package it.epicode.listaeventi.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "eventi")
@Data
public class Evento {

@Id
@GeneratedValue

private Long id;

private String titolo;
private String descrizione;
private LocalDate dataEvento;
private String luogo;
private Integer postiDisponibili;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizzatore; // MODIFICATO: organizer -> organizzatore

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true) // MODIFICATO: event -> evento (in mappedBy)
    private Set<Partecipazione> partecipazioni;
}
