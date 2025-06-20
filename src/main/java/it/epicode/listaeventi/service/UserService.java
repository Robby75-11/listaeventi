// src/main/java/it/epicode/listaeventi/service/UserService.java
package it.epicode.listaeventi.service;

import it.epicode.listaeventi.dto.UserDto;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.exception.UnAuthorizedException;
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Potrebbe servire per cambiare password
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Utilizzato se si implementa cambio password

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + id));
        return convertToDto(user);
    }


    public UserDto saveUser(Long id, UserDto dto, Long currentUserId) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + id));

        // Esempio di logica di autorizzazione: un utente può modificare solo il proprio profilo
        // o un admin può modificare qualsiasi profilo.
        // Qui assumiamo che solo l'utente stesso o un admin (logica da implementare nel controller/security)
        // possa modificare il proprio profilo.
        if (!user.getId().equals(currentUserId)) {
            throw new UnAuthorizedException("Non autorizzato a modificare questo profilo utente.");
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        // Se avessimo dataAggiornamento nel modello, lo aggiorneremmo qui.
        // user.setDataAggiornamento(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }


    public void deleteUser(Long id, Long currentUserId) throws NotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + id));

        // Solo l'utente stesso o un admin può eliminare il profilo
        if (!user.getId().equals(currentUserId)) {
            throw new UnAuthorizedException("Non autorizzato ad eliminare questo profilo utente.");
        }

        userRepository.delete(user);
    }

    // Metodo helper per convertire entità in DTO
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRuolo(user.getRuolo());
        return dto;
    }
}