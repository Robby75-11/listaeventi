package it.epicode.listaeventi.service;

import it.epicode.listaeventi.dto.UserDto;
import it.epicode.listaeventi.enumeration.Role;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(UserDto userDto){
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        //la password in chiaro che si trova nel dto, verrà passata come parametro al metodo encode dell'encoder
        //Bcrypt codificherà la password e generà un codice criptato
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.UTENTE_NORMALE);

        if (user.getDataCreazione() == null) {
            user.setDataCreazione(LocalDateTime.now());
        }
        user.setDataAggiornamento(LocalDateTime.now());

        return userRepository.save(user);

    }

    public List<User> getAllUser(){

        return userRepository.findAll();
    }

    public User getUser(Long id) throws NotFoundException {
        return userRepository.findById(id).
                orElseThrow(() -> new NotFoundException("User con id " + id + " non trovato"));
    }

    public User updateUser(Long id, UserDto userDto) throws NotFoundException {
        User userDaAggiornare = getUser(id);

        userDaAggiornare.setEmail(userDto.getEmail());
        userDaAggiornare.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank() &&
       !passwordEncoder.matches(userDto.getPassword(), userDaAggiornare.getPassword())) {
            userDaAggiornare.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }


        return userRepository.save(userDaAggiornare);
    }

    public void deleteUser(Long id) throws NotFoundException {
        User userDaCancellare = getUser(id);

        userRepository.delete(userDaCancellare);
    }
}