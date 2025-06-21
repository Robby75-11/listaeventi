package it.epicode.listaeventi.security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.listaeventi.exception.NotFoundException;
import it.epicode.listaeventi.model.User;
import it.epicode.listaeventi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;



@Component
public class JwtTool {
    @Value("${jwt.duration}")
    private Long durata;

    @Value("${jwt.secret}")
    private String chiaveSegreta;

    @Autowired
    @Lazy
    private UserService userService;

    public String createToken(User user) {
        //per generare il token avremo bisogno della data di generazione del token, della durata e dell'id
        //dell'utente per il quale stiamo creando il token. Avremo inoltre bisogno anche della chiave segreta
        //per poter crittografare il token

        return Jwts.builder().issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + durata)).
                subject(String.valueOf(user.getId())).
                signWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).
                compact();
    }

    //  metodo per la verifica della validità del token
    public void validateToken(String token) {
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).
                build().parse(token);
    }

    public User getUserFromToken(String token) throws NotFoundException {
        //recuperare id dell'utente dal token
        Long id = Long.parseLong(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).
                build().parseSignedClaims(token).getPayload().getSubject());

        return userService.getUser(id);

    }
}
