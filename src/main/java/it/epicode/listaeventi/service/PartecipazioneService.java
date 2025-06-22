
package it.epicode.listaeventi.service;

import it.epicode.listaeventi.dto.EventoDto;
import it.epicode.listaeventi.dto.PartecipazioneDto;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.exception.UnAuthorizedException;
import it.epicode.listaeventi.model.Partecipazione;
import it.epicode.listaeventi.model.Evento; // Importa Evento
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.repository.PartecipazioneRepository;
import it.epicode.listaeventi.repository.EventoRepository; // Importa EventoRepository
import it.epicode.listaeventi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartecipazioneService {

    @Autowired
    private PartecipazioneRepository partecipazioneRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UserRepository userRepository;



    public Partecipazione savePartecipazione(PartecipazioneDto partecipazioneDto) throws NotFoundException {

      Evento evento = eventoRepository.findById(partecipazioneDto.getEventoId())
              .orElseThrow(() -> new NotFoundException("Evento non trovato con ID: " + partecipazioneDto.getEventoId()));

        User user = userRepository.findById(partecipazioneDto.getUserId())
                .orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + partecipazioneDto.getUserId()));

        Partecipazione partecipazione = new Partecipazione();
        partecipazione.setEvento(evento);
        partecipazione.setUtente(user);
        partecipazione.setDataPartecipazione(LocalDateTime.now());


        return partecipazioneRepository.save(partecipazione);
    }

    public Page<Partecipazione> getAllPartecipazioni(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return partecipazioneRepository.findAll(pageable);
    }

    public Partecipazione getPartecipazione(Long id) throws NotFoundException {
        return partecipazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Partecipazione con id " + id + " non trovata"));
    }

    public List<Partecipazione> getPartecipazioniByUser(Long userId) {
        return partecipazioneRepository.findByUtenteId(userId);
    }

    public Partecipazione getPartecipazioneByUtenteAndEvento(Long userId, Long eventoId) throws NotFoundException {
        return partecipazioneRepository.findByUtenteIdAndEventoId(userId, eventoId)
                .orElseThrow(() -> new NotFoundException("Partecipazione non trovata"));
    }

    public boolean partecipazioneEsiste(Long userId, Long eventoId) {
        return partecipazioneRepository.existsByUtenteIdAndEventoId(userId, eventoId);
    }

    public Partecipazione updatePartecipazione(Long id, PartecipazioneDto partecipazioneDto) throws NotFoundException {
        Partecipazione partecipazione = partecipazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Partecipazione con ID " + id + " non trovata"));

        // Ottieni il nuovo evento solo se l'ID è fornito nel DTO e diverso dall'attuale
        if (partecipazioneDto.getEventoId() != null && !partecipazioneDto.getEventoId().equals(partecipazione.getEvento().getId())) {
            Evento nuovoEvento = eventoRepository.findById(partecipazioneDto.getEventoId())
                    .orElseThrow(() -> new NotFoundException("Evento con ID " + partecipazioneDto.getEventoId() + " non trovato"));
            partecipazione.setEvento(nuovoEvento);
        }


        return partecipazioneRepository.save(partecipazione);
    }
    public void deleteMyPartecipazione(Long partecipazioneId, Long userId) throws NotFoundException, UnAuthorizedException {
        Partecipazione partecipazione = partecipazioneRepository.findById(partecipazioneId)
                .orElseThrow(() -> new NotFoundException("Partecipazione con ID " + partecipazioneId + " non trovata"));

        if (!partecipazione.getUtente().getId().equals(userId)) {
            throw new UnAuthorizedException("Non sei autorizzato a cancellare questa partecipazione");
        }

        partecipazioneRepository.delete(partecipazione);
    }

}