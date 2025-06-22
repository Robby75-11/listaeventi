package it.epicode.listaeventi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "eventi")
@Data
public class Evento {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY) // Specifica la strategia di generazione
private Long id;



@Column(nullable = false)
private String titolo;

private String descrizione;

@Column(nullable = false)
private LocalDateTime dataEvento;// Modificato a LocalDateTime per consistenza con User

@Column(nullable = false)
private String luogo;

@Column(nullable = false)
private Integer postiDisponibili;// Potrebbe essere postiTotali, e postiDisponibili derivato

    // Il nome di questo campo ('organizzatore') DEVE corrispondere all'attributo 'mappedBy' in User.java
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore // <-- AGGIUNGI @JsonIgnore QUI se non vuoi serializzare l'organizzatore quando serializzi l'evento
    @JoinColumn(name = "organizzatore_id", nullable = false) // Nome della colonna della chiave esterna
    private User organizzatore;

    // Il valore di 'mappedBy' DEVE corrispondere al nome del campo nell'entità Partecipazione (evento)
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Partecipazione> partecipazioni = new HashSet<>(); // Inizializza la collezione
}
