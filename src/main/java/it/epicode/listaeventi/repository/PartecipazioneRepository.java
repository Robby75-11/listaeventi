package it.epicode.listaeventi.repository;

import it.epicode.listaeventi.model.Partecipazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartecipazioneRepository extends JpaRepository<Partecipazione, Long> {

    List<Partecipazione> findByUtenteId(Long Id);

    Optional<Partecipazione> findByUtenteIdAndEventoId(Long userId, Long eventoId);

    boolean existsByUtenteIdAndEventoId(Long userId, Long eventoId);



}
