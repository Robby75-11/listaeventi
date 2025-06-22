
package it.epicode.listaeventi.service;

import it.epicode.listaeventi.dto.EventoDto;
import it.epicode.listaeventi.exception.UnAuthorizedException;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.Evento; // Importa Evento
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.repository.EventoRepository; // Importa EventoRepository
import it.epicode.listaeventi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventoRepository eventoRepository;

    public Evento saveEvento(EventoDto eventoDto) throws NotFoundException {
        Evento evento = new Evento();
        evento.setTitolo(eventoDto.getTitolo());
        evento.setDescrizione(eventoDto.getDescrizione());
        evento.setDataEvento(eventoDto.getDataEvento());
        evento.setLuogo(eventoDto.getLuogo());
        evento.setPostiDisponibili(eventoDto.getPostiDisponibili()); // !!! IMPORTANTE: Inizializza i posti disponibili

        // --- INIZIO: LOGICA PER IMPOSTARE L'ORGANIZZATORE ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameOrganizzatore = authentication.getName(); // Ottiene il username (o email) dell'utente loggato

        // Recupera l'oggetto User completo dal database
        // Assicurati che il tuo UserRepository abbia un findByUsername() o findByEmail()
        User organizzatore = userRepository.findByUsername(usernameOrganizzatore) // Adatta se usi findByEmail
                .orElseThrow(() -> new NotFoundException("Utente organizzatore non trovato: " + usernameOrganizzatore));

        evento.setOrganizzatore(organizzatore); // <-- IMPOSTA L'ORGANIZZATORE QUI!
        // --- FINE: LOGICA PER IMPOSTARE L'ORGANIZZATORE ---





        return eventoRepository.save(evento);
    }

    public List<Evento> getAllEventi() {
       return eventoRepository.findAll();
    }

    public Evento getEvento(Long id) throws NotFoundException {
      return eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento non trovato con ID: " + id));

    }


    public Evento updateEvento(Long id, EventoDto eventoDto) throws NotFoundException, UnAuthorizedException {
        Evento eventoDaAggiornare = getEvento(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Controllo se l’organizzatore è l’utente autenticato
        if (!eventoDaAggiornare.getOrganizzatore().getUsername().equals(username)) {
            throw new UnAuthorizedException("Non sei autorizzato ad aggiornare questo evento");
        }

        eventoDaAggiornare.setTitolo(eventoDto.getTitolo());
        eventoDaAggiornare.setLuogo(eventoDto.getLuogo());
        eventoDaAggiornare.setDescrizione(eventoDto.getDescrizione());
        eventoDaAggiornare.setPostiDisponibili(eventoDto.getPostiDisponibili());
        eventoDaAggiornare.setDataEvento(eventoDto.getDataEvento());

     return eventoRepository.save(eventoDaAggiornare);
    }

    public void deleteEvento(Long id) throws NotFoundException {
        Evento eventoDaCancellare = getEvento(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User userLoggato = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente non trovato: " + username));


        // Controllo se l’organizzatore è l’utente autenticato
        if (!eventoDaCancellare.getOrganizzatore().getId().equals(userLoggato.getId())) {
            throw new UnAuthorizedException("Non sei autorizzato a cancellare questo evento");
        }

       eventoRepository.delete(eventoDaCancellare);

    }

    }
