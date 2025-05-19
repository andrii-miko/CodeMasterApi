package com.codemasterapi.controllers;

import com.codemasterapi.dtos.user.LoginDTO;
import com.codemasterapi.dtos.user.RegisterDTO;
import com.codemasterapi.dtos.user.UserResponseDto;
import com.codemasterapi.models.UserEntity;
import com.codemasterapi.repositories.UserRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AccountController {
    @Value(value = "${JWT_SECRET}")
    private String jwtSecretKey;

    @Value(value = "${JWT_ISSUER}")
    private String jwtIssuer;

    @Value(value = "${JWT_EXPIRATION}")
    private int jwtExpiration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO, BindingResult result) {
        if (result.hasErrors()) {
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();

            for (int i = 0; i < errorsList.size(); i++) {
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errorsMap);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(), loginDTO.getPassword()
                    )
            );

            Optional<UserEntity> optionalUser = userRepository.findByEmail(loginDTO.getEmail());
            if (optionalUser.isPresent()) {
                UserEntity user = optionalUser.get();
                String jwtToken = createJWTToken(user);
                var userResponseDto = toUserResponseDto(user);

                var response = new HashMap<String, Object>();
                response.put("token", jwtToken);
                response.put("user", userResponseDto);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("User not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Invalid email or password");
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication auth) {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(auth.getName());
        var response = new HashMap<String, Object>();
        response.put("Email", optionalUser.get().getEmail());
        response.put("Authorities", auth.getAuthorities());

        if (optionalUser.isPresent()) {
            var user = optionalUser.get();
            var userResponseDto = toUserResponseDto(user);
            response.put("User", userResponseDto);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO, BindingResult result) {
        if (result.hasErrors()) {
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();

            for (int i = 0; i < errorsList.size(); i++) {
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errorsMap);
        }

        var bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserEntity user = new UserEntity();
        user.setEmail(registerDTO.getEmail());
        user.setUsername(registerDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));
        user.setRoles(new HashSet<>(Set.of("USER")));

        try {
            var otherUser = userRepository.findByEmail(user.getEmail());
            if (otherUser.isPresent()) {
                return ResponseEntity.badRequest().body("Email already exist");
            }

            userRepository.save(user);
            String jwtToken = createJWTToken(user);
            var userResponseDto = toUserResponseDto(user);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", userResponseDto);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error while saving user");
    }

    private String createJWTToken(UserEntity user) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getUsername())
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtExpiration))
                .claim("roles", user.getRoles())
                .build();
        var encoder = new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecretKey.getBytes()));
        var params = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return encoder.encode(params).getTokenValue();
    }

    private UserResponseDto toUserResponseDto(UserEntity user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        dto.setTotalPoints(user.getTotalPoints());
        return dto;
    }
}