package it.epicode.listaeventi.controller;

import it.epicode.listaeventi.dto.PartecipazioneDto;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.Partecipazione;
import it.epicode.listaeventi.service.PartecipazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/partecipazioni")
public class PartecipazioneController {

    @Autowired
    private PartecipazioneService partecipazioneService;

    // ✅ Crea nuova partecipazione
    @PreAuthorize("hasAnyAuthority('UTENTE_NORMALE', 'ORGANIZZATORE_EVENTO')")
    @PostMapping
    public ResponseEntity<Partecipazione> creaPartecipazione(@Valid @RequestBody PartecipazioneDto partecipazioneDto) throws NotFoundException {
        Partecipazione nuova = partecipazioneService.savePartecipazione(partecipazioneDto);
        return new ResponseEntity<>(nuova, HttpStatus.CREATED);
    }

    // ✅ Elenca tutte le partecipazioni paginato
    @GetMapping

    public ResponseEntity<Page<Partecipazione>> getAllPartecipazioni(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Page<Partecipazione> partecipazioni = partecipazioneService.getAllPartecipazioni(page, size, sortBy);
        return new ResponseEntity<>(partecipazioni, HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkPartecipazione(
            @RequestParam Long userId,
            @RequestParam Long eventoId) {

        boolean esiste = partecipazioneService.partecipazioneEsiste(userId, eventoId);
        return new ResponseEntity<>(esiste, HttpStatus.OK);
    }

    // ✅ Recupera partecipazione per ID

    @GetMapping("/{id}")
    public ResponseEntity<Partecipazione> getPartecipazione(@PathVariable Long id) throws NotFoundException {
        Partecipazione p = partecipazioneService.getPartecipazione(id);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    // ✅ Elimina partecipazione per ID
    @PreAuthorize("hasAuthority('UTENTE_NORMALE')")
    @DeleteMapping("/my/{id}")
    public ResponseEntity<Void> deletePartecipazione(@PathVariable Long id, @RequestParam Long userId) throws NotFoundException {
        partecipazioneService.deleteMyPartecipazione(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // (Opzionale) ✅ Aggiorna partecipazione – se previsto nel progetto
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTO')")
    @PutMapping("/{id}")
    public ResponseEntity<Partecipazione> aggiornaPartecipazione(@PathVariable Long id, @Valid @RequestBody PartecipazioneDto partecipazioneDto) throws NotFoundException {
        Partecipazione aggiornata = partecipazioneService.updatePartecipazione(id, partecipazioneDto);
        return new ResponseEntity<>(aggiornata, HttpStatus.OK);
    }
}
