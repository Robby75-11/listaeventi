package it.epicode.listaeventi.repository;

import it.epicode.listaeventi.model.Partecipazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartecipazioneRepository extends JpaRepository<Partecipazione, Long> {

    // Trova tutte le partecipazioni per un utente specifico (tramite l'ID dell'utente)
    // Nota: findByUtenteId si basa sull'attributo 'utente' nel modello Partecipazione e poi il suo 'id'
    List<Partecipazione> findByUtenteId(Long utenteId); // Aggiornato: utenteId

    // Trova una partecipazione specifica di un utente per un evento specifico
    Optional<Partecipazione> findByUtenteIdAndEventoId(Long utenteId, Long eventoId); // Aggiornato: utenteId, eventoId

    // Controlla se una partecipazione esiste già per un utente e un evento specifici
    boolean existsByUtenteIdAndEventoId(Long utenteId, Long eventoId); // Aggiornato: utenteId, eventoId

    // Potresti aggiungere metodi per trovare partecipazioni per evento, o per dataPartecipazione
    List<Partecipazione> findByEventoId(Long eventoId); // Utile per ve
}
