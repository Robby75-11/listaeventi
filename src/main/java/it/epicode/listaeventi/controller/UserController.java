package it.epicode.listaeventi.controller;

import it.epicode.listaeventi.dto.UserDto;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Creazione utente (solo per ORGANIZZATORE_EVENTO)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTO')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        User nuovoUtente = userService.saveUser(userDto);
        return ResponseEntity.ok(nuovoUtente);
    }

    // Aggiornamento utente (solo per ORGANIZZATORE_EVENTO)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTO')")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws NotFoundException {
        User utenteAggiornato = userService.updateUser(id, userDto);
        return ResponseEntity.ok(utenteAggiornato);
    }

    // Cancellazione utente (solo per ORGANIZZATORE_EVENTO)
    @PreAuthorize("hasAuthority('ORGANIZZATORE_EVENTO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws NotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Visualizzazione di tutti gli utenti (accesso pubblico o da personalizzare)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> utenti = userService.getAllUser();
        return ResponseEntity.ok(utenti);
    }

    // Recupero utente per ID (accesso pubblico o da personalizzare)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) throws NotFoundException {
        User utente = userService.getUser(id);
        return ResponseEntity.ok(utente);
    }
}
