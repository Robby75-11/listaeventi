package it.epicode.listaeventi.repository;

import it.epicode.listaeventi.model.Evento;
import it.epicode.listaeventi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Trova eventi per titolo (ignorando maiuscole/minuscole e con ricerca parziale)
    List<Evento> findByTitoloContainingIgnoreCase(String titolo);

    // Trova eventi organizzati da un utente specifico
    List<Evento> findByOrganizzatore(User organizzatore); // Aggiornato: organizzatore

    // Trova eventi con una data specifica o successiva a una data
    List<Evento> findByDataEventoAfter(LocalDateTime dataEvento); // Aggiornato: dataEvento

    // Trova eventi che hanno posti disponibili maggiori di 0
    List<Evento> findByPostiDisponibiliGreaterThan(Integer postiDisponibili); // Aggiornato: postiDisponibili

    // Potresti aggiungere metodi per la ricerca per luogo, intervallo di date, ecc.
}

