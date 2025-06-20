package it.epicode.listaeventi.service;

import it.epicode.listaeventi.dto.LoginDto;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.repository.UserRepository;
import it.epicode.listaeventi.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTool jwtTool;
    /*
           1. verificare che l'utente esiste
           2. se l'utente non esite, lancia una eccezione
           3. se l'utente esiste, generare il token e inviarlo al client
            */

    public String login(LoginDto loginDto) throws NotFoundException {
        User user = userRepository.findByEmail(loginDto.getEmail()).
                orElseThrow(() ->new NotFoundException("Email/password  non trovati"));


        if(passwordEncoder.matches(loginDto.getPassword(),user.getPassword())) {
            //utente è autenticato, devo creare il token
            return jwtTool.createToken(user);
        }
        else{
            throw new NotFoundException("Utente con questo Email/password non trovato");


        }
    }
}
