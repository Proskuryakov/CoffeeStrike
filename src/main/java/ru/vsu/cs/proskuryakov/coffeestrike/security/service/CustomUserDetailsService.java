package ru.vsu.cs.proskuryakov.coffeestrike.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.vsu.cs.proskuryakov.coffeestrike.db.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var userModel = userRepository.findUserItemByUsername(username);

        if (userModel.isEmpty()) {
            throw new UsernameNotFoundException("Unknown user: " + username);
        }

        return User.builder()
                .username(userModel.get().getUsername())
                .password(userModel.get().getPassword())
                .roles("ADMIN")
                .build();
    }

}
