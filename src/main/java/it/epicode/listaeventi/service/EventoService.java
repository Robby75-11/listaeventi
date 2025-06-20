// src/main/java/it/epicode/listaeventi/service/EventoService.java
package it.epicode.listaeventi.service;

import it.epicode.listaeventi.dto.EventoDto;
import it.epicode.listaeventi.exception.UnAuthorizedException;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.Evento; // Importa Evento
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.repository.EventoRepository; // Importa EventoRepository
import it.epicode.listaeventi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository; // Inietta EventoRepository
    private final UserRepository userRepository;

    @Autowired
    public EventoService(EventoRepository eventoRepository, UserRepository userRepository) {
        this.eventoRepository = eventoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public EventoDto createEvento(EventoDto dto, Long organizzatoreId) throws NotFoundException {
        User organizzatore = userRepository.findById(organizzatoreId)
                .orElseThrow(() -> new NotFoundException("Organizzatore non trovato con ID: " + organizzatoreId));

        Evento evento = new Evento();
        evento.setTitolo(dto.getTitolo());
        evento.setDescrizione(dto.getDescrizione());
        evento.setDataEvento(dto.getDataEvento());
        evento.setLuogo(dto.getLuogo());
        evento.setPostiTotali(dto.getPostiTotali());
        evento.setPostiDisponibili(dto.getPostiTotali()); // !!! IMPORTANTE: Inizializza i posti disponibili

        evento.setOrganizzatore(organizzatore);

        // Se avessimo dataCreazione/Aggiornamento nel modello, li setteremmo qui.
        // evento.setDataCreazione(LocalDateTime.now());
        // evento.setDataAggiornamento(LocalDateTime.now());

        Evento savedEvento = eventoRepository.save(evento);
        return convertToDto(savedEvento);
    }

    public List<EventoDto> getAllEventi() {
        return eventoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public EventoDto getEventoById(Long id) throws NotFoundException {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato con ID: " + id));
        return new EventoDto(eventoDto);
    }

    @Transactional
    public EventoDto updateEvento(Long id, EventoDto dto, Long currentUserId) throws NotFoundException {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato con ID: " + id));

        // Solo l'organizzatore dell'evento può modificarlo
        if (!evento.getOrganizzatore().getId().equals(currentUserId)) {
            throw new UnAuthorizedException("Non autorizzato a modificare questo evento.");
        }

        // Applica le modifiche solo se i campi nel DTO non sono nulli
        if (dto.getTitolo() != null) evento.setTitolo(dto.getTitolo());
        if (dto.getDescrizione() != null) evento.setDescrizione(dto.getDescrizione());
        if (dto.getDataEvento() != null) evento.setDataEvento(dto.getDataEvento());
        if (dto.getLuogo() != null) evento.setLuogo(dto.getLuogo());
        if (dto.getPostiTotali() != null) {
            // Logica per gestire l'aggiornamento dei posti totali
            // Si può voler ridurre i posti disponibili se i totali sono diminuiti
            // o aumentali se i totali sono aumentati.
            // Esempio semplificato:
            int oldTotal = evento.getPostiTotali();
            int newTotal = dto.getPostiTotali();
            int diff = newTotal - oldTotal;

            evento.setPostiTotali(newTotal);
            // Se i nuovi posti totali sono maggiori, aumenta i disponibili della differenza
            // Altrimenti, se sono minori, riduci i disponibili (ma non sotto zero o il numero di prenotazioni esistenti)
            if (diff > 0) {
                evento.setPostiDisponibili(evento.getPostiDisponibili() + diff);
            } else if (diff < 0) {
                // Gestire logicamente cosa succede se si riducono i posti totali
                // qui è un esempio semplice, si potrebbe avere una logica più complessa
                evento.setPostiDisponibili(Math.max(0, evento.getPostiDisponibili() + diff));
            }
        }
        // Se avessimo dataAggiornamento nel modello, lo aggiorneremmo qui.
        // evento.setDataAggiornamento(LocalDateTime.now());

        Evento updatedEvento = eventoRepository.save(evento);
        return convertToDto(updatedEvento);
    }

    @Transactional
    public void deleteEvento(Long id, Long currentUserId) throws NotFoundException {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato con ID: " + id));

        // Solo l'organizzatore dell'evento può eliminarlo
        if (!evento.getOrganizzatore().getId().equals(currentUserId)) {
            throw new UnAuthorizedException("Non autorizzato ad eliminare questo evento.");
        }

        // Se ci sono partecipazioni, JPA le gestirà con orphanRemoval=true e cascade=ALL
        // ma potresti voler aggiungere una logica per impedire l'eliminazione se ci sono partecipanti
        // o inviare notifiche.

        eventoRepository.delete(evento);
    }

    // Metodo helper per convertire entità in DTO
    private EventoDto convertToDto(Evento evento) {
        EventoDto dto = new EventoDto();
        dto.setId(evento.getId());
        dto.setTitolo(evento.getTitolo());
        dto.setDescrizione(evento.getDescrizione());
        dto.setDataEvento(evento.getDataEvento());
        dto.setLuogo(evento.getLuogo());
        dto.setPostiTotali(evento.getPostiTotali());
        dto.setPostiDisponibili(evento.getPostiDisponibili());
        dto.setOrganizzatoreId(evento.getOrganizzatore().getId());
        dto.setUsernameOrganizzatore(evento.getOrganizzatore().getUsername());
        return dto;
    }
}
