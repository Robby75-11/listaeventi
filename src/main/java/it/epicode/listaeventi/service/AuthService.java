package it.epicode.listaeventi.service;

import it.epicode.listaeventi.dto.LoginDto;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.repository.UserRepository;
import it.epicode.listaeventi.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service

public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTool jwtTool;
    /*
           1. verificare che l'utente esiste
           2. se l'utente non esite, lancia una eccezione
           3. se l'utente esiste, generare il token e inviarlo al client
            */

    public String login(LoginDto loginDto) throws NotFoundException {
        User utente = userRepository.findByEmail(loginDto.getEmail()).
                orElseThrow(() ->new NotFoundException("Email/password  non trovati"));


        if(passwordEncoder.matches(loginDto.getPassword(),utente.getPassword())) {
            //utente è autenticato, devo creare il token
            return jwtTool.createToken(utente);
        }
        else{
            throw new NotFoundException("Utente con questo Email/password non trovato");


        }
    }
}
