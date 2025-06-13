package com.app.poseidon.services;

import com.app.poseidon.domain.User;
import com.app.poseidon.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


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
        u.setUsername(username);
        u.setFullname(fullname);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(role);
        userRepository.save(u);
    }
}

