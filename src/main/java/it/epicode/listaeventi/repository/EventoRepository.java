package it.epicode.listaeventi.repository;

import it.epicode.listaeventi.model.Evento;
import it.epicode.listaeventi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {



    // Trova eventi organizzati da un utente specifico
    List<Evento> findByOrganizzatore(User organizzatore); // Aggiornato: organizzatore




}

