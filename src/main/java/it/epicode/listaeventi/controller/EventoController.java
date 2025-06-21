package it.epicode.listaeventi.controller;

import it.epicode.listaeventi.dto.EventoDto;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.Evento;
import it.epicode.listaeventi.service.EventoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/eventi")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // Creazione evento - solo organizzatore evento

    @PostMapping
    public ResponseEntity<Evento> createEvento(@RequestBody EventoDto eventoDto) {
        try {
            Evento eventoCreato = eventoService.saveEvento(eventoDto);
            return ResponseEntity.ok(eventoCreato);
        } catch (NotFoundException e) {
            // Puoi restituire una risposta di errore personalizzata
            // Es. return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            // Oppure lanciare un'altra eccezione @ResponseStatus
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Aggiornamento evento - solo organizzatore evento
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTO')")
    @PutMapping("/{id}")
    public ResponseEntity<Evento> updateEvento(@PathVariable Long id, @RequestBody EventoDto eventoDto) throws NotFoundException {
        Evento eventoAggiornato = eventoService.updateEvento(id, eventoDto);
        return ResponseEntity.ok(eventoAggiornato);
    }

    // Cancellazione evento - solo organizzatore evento
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) throws NotFoundException {
        eventoService.deleteEvento(id);
        return ResponseEntity.noContent().build();
    }

    // Lista eventi (visibile a tutti)
    @GetMapping
    public ResponseEntity<List<Evento>> getAllEventi() {
        List<Evento> eventi = eventoService.getAllEventi();
        return ResponseEntity.ok(eventi);
    }

    // Evento singolo per id
    @GetMapping("/{id}")
    public ResponseEntity<Evento> getEvento(@PathVariable Long id) throws NotFoundException {
        Evento evento = eventoService.getEvento(id);
        return ResponseEntity.ok(evento);
    }
}
