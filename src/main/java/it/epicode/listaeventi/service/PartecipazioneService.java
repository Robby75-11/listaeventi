// src/main/java/it/epicode/listaeventi/service/PartecipazioneService.java
package it.epicode.listaeventi.service;

import it.epicode.listaeventi.dto.PartecipazioneDto;

import it.epicode.listaeventi.exception.PartecipazioneException;
import it.epicode.listaeventi.exception.UnAuthorizedException;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.exception.UnAuthorizedException;
import it.epicode.listaeventi.model.Partecipazione;
import it.epicode.listaeventi.model.Evento; // Importa Evento
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.repository.PartecipazioneRepository;
import it.epicode.listaeventi.repository.EventoRepository; // Importa EventoRepository
import it.epicode.listaeventi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartecipazioneService {

    private final PartecipazioneRepository partecipazioneRepository;
    private final EventoRepository eventoRepository; // Inietta EventoRepository
    private final UserRepository userRepository;

    @Autowired
    public PartecipazioneService(PartecipazioneRepository partecipazioneRepository, EventoRepository eventoRepository, UserRepository userRepository) {
        this.partecipazioneRepository = partecipazioneRepository;
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PartecipazioneDto createPartecipazione(PartecipazioneDto dto, Long utenteId) throws NotFoundException {
        User utente = userRepository.findById(utenteId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + utenteId));

        Evento evento = eventoRepository.findById(dto.getEventoId()) // !!! CORRETTO: getEventoId()
                .orElseThrow(() -> new NotFoundException("Evento non trovato con ID: " + dto.getEventoId()));

        if (evento.getPostiDisponibili() <= 0) { // !!! CORRETTO: getPostiDisponibili()
            throw new PartecipazioneException("Non ci sono più posti disponibili per questo evento.");
        }

        if (partecipazioneRepository.existsByUtenteIdAndEventoId(utenteId, dto.getEventoId())) { // !!! CORRETTO: existsByUtenteIdAndEventoId
            throw new PartecipazioneException("Hai già una partecipazione per questo evento.");
        }

        // Decrementa i posti disponibili
        evento.setPostiDisponibili(evento.getPostiDisponibili() - 1); // !!! CORRETTO
        eventoRepository.save(evento); // Salva l'evento aggiornato

        Partecipazione partecipazione = new Partecipazione();
        partecipazione.setUtente(utente); // !!! CORRETTO: setUtente()
        partecipazione.setEvento(evento); // !!! CORRETTO: setEvento()
        partecipazione.setDataPartecipazione(LocalDateTime.now()); // !!! IMPORTANTE: Imposta la data di partecipazione

        Partecipazione savedPartecipazione = partecipazioneRepository.save(partecipazione);
        return convertToDto(savedPartecipazione);
    }

    public List<PartecipazioneDto> getPartecipazioniUtente(Long utenteId) { // !!! CORRETTO: getPartecipazioniUtente
        return partecipazioneRepository.findByUtenteId(utenteId).stream() // !!! CORRETTO: findByUtenteId
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelPartecipazione(Long idPartecipazione, Long currentUserId) throws NotFoundException { // !!! CORRETTO: idPartecipazione
        Partecipazione partecipazione = partecipazioneRepository.findById(idPartecipazione) // !!! CORRETTO: idPartecipazione
                .orElseThrow(() -> new NotFoundException("Partecipazione non trovata con ID: " + idPartecipazione));

        // Controlla che l'utente che cerca di cancellare sia il proprietario della partecipazione
        if (!partecipazione.getUtente().getId().equals(currentUserId)) { // !!! CORRETTO: getUtente().getId()
            throw new UnAuthorizedException("Non autorizzato ad annullare questa partecipazione.");
        }

        // Incrementa i posti disponibili dell'evento
        Evento evento = partecipazione.getEvento(); // !!! CORRETTO: getEvento()
        evento.setPostiDisponibili(evento.getPostiDisponibili() + 1); // !!! CORRETTO
        eventoRepository.save(evento);

        partecipazioneRepository.delete(partecipazione);
    }

    // Metodo helper per convertire entità in DTO
    private PartecipazioneDto convertToDto(Partecipazione partecipazione) {
        PartecipazioneDto dto = new PartecipazioneDto();
        dto.setId(partecipazione.getId());
        dto.setUtenteId(partecipazione.getUtente().getId()); // !!! CORRETTO: getUtente().getId()
        dto.setUsernameUtente(partecipazione.getUtente().getUsername()); // !!! CORRETTO: getUtente().getUsername()
        dto.setEventoId(partecipazione.getEvento().getId()); // !!! CORRETTO: getEvento().getId()
        dto.setTitoloEvento(partecipazione.getEvento().getTitolo()); // !!! CORRETTO: getEvento().getTitolo()
        dto.setDataPartecipazione(partecipazione.getDataPartecipazione()); // !!! CORRETTO: getDataPartecipazione()
        return dto;
    }
}