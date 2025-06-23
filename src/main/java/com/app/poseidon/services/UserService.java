package com.app.poseidon.services;

import com.app.poseidon.domain.User;
import com.app.poseidon.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private static final Pattern PASSWORD_REGEX = Pattern.compile(
//            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found:" + username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                true, // compte non expiré
                true, // identifiants non expirés
                true, // compte non verrouillé
                user.getAuthorities() // rôles de l'utilisateur
        );
    }

    public void register(String username, String rawPassword, String fullname, String role) {
        System.out.println(">> INSCRIPTION SERVICE appelé avec : "
                + username + " / " + rawPassword + " / " + fullname + " / " + role);
        User u = new User();

//        if (!PASSWORD_REGEX.matcher(rawPassword).matches()) {
//            throw new IllegalArgumentException(
//                    "Le mot de passe doit contenir ≥8 caractères, 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial"
//            );
//        }

        u.setUsername(username);
        u.setFullname(fullname);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(role);
        userRepository.save(u);
    }
}

