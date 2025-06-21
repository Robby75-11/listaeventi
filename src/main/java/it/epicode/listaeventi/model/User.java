package it.epicode.listaeventi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import it.epicode.listaeventi.enumeration.Role;
import it.epicode.listaeventi.model.Evento;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true) // Aggiunti vincoli
    private String username;

    @Column(nullable = false, unique = true) // Aggiunti vincoli
    private String email;

    @Column(nullable = false) // Aggiunti vincoli
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // Il ruolo dovrebbe essere non nullo
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @CreationTimestamp
    private LocalDateTime dataCreazione; // MODIFICATO

    @UpdateTimestamp
    private LocalDateTime dataAggiornamento; // MODIFICATO

    // Il valore di 'mappedBy' DEVE corrispondere al NOME del campo nell'entità Evento (organizzatore)
    @OneToMany(mappedBy = "organizzatore", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Evento> eventiOrganizzati = new HashSet<>(); // Inizializza la collezione

    // Il valore di 'mappedBy' DEVE corrispondere al NOME del campo nell'entità Partecipazione (utente)
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Partecipazione> partecipazioni = new HashSet<>(); // Inizializza la collezione

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
