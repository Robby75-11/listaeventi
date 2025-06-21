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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partecipazioni")
public class PartecipazioneController {

    @Autowired
    private PartecipazioneService partecipazioneService;

    // ✅ Crea nuova partecipazione
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

    // ✅ Recupera partecipazione per ID
    @GetMapping("/{id}")
    public ResponseEntity<Partecipazione> getPartecipazione(@PathVariable Long id) throws NotFoundException {
        Partecipazione p = partecipazioneService.getPartecipazione(id);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    // ✅ Elimina partecipazione per ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartecipazione(@PathVariable Long id) throws NotFoundException {
        partecipazioneService.deletePartecipazione(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // (Opzionale) ✅ Aggiorna partecipazione – se previsto nel progetto
    @PutMapping("/{id}")
    public ResponseEntity<Partecipazione> aggiornaPartecipazione(@PathVariable Long id, @Valid @RequestBody PartecipazioneDto partecipazioneDto) throws NotFoundException {
        Partecipazione aggiornata = partecipazioneService.updatePartecipazione(id, partecipazioneDto);
        return new ResponseEntity<>(aggiornata, HttpStatus.OK);
    }
}
