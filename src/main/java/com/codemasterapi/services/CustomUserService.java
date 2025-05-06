package com.codemasterapi.services;

import com.codemasterapi.models.UserEntity;
import com.codemasterapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            var springUser = User.withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
            return springUser;
        }
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}