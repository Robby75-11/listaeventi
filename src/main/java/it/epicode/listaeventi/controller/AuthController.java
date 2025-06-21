package it.epicode.listaeventi.controller;

import it.epicode.listaeventi.dto.LoginDto;
import it.epicode.listaeventi.dto.UserDto;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.exception.ValidationException;
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.service.AuthService;
import it.epicode.listaeventi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController

public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Endpoint per la registrazione
    @PostMapping("/auth/register")
    public User  register(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws ValidationException, NotFoundException {
        if(bindingResult.hasErrors()){
            throw  new NotFoundException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (String s, String e)->s+e));
        }
        return userService.saveUser(userDto);

    }
    @GetMapping("/auth/login")
    public String login(@RequestBody @Validated LoginDto loginDto, BindingResult bindingResult) throws ValidationException, NotFoundException {


        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (s, e) -> s + e));
        }
            return authService.login(loginDto);
        }

    }

