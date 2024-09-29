package com.munizdev.dscommerce.services;

import com.munizdev.dscommerce.dto.EmailDTO;
import com.munizdev.dscommerce.dto.NewPasswordDTO;
import com.munizdev.dscommerce.entities.PasswordRecover;
import com.munizdev.dscommerce.entities.User;
import com.munizdev.dscommerce.repositories.PasswordRecoverRepository;
import com.munizdev.dscommerce.repositories.UserRepository;
import com.munizdev.dscommerce.services.exceptions.ForbiddenException;
import com.munizdev.dscommerce.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    public void validateSelfOrAdmin(long userId) {
        User me = userService.authenticated();
        if (!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Access denied");
        }
    }

    public void createRecoverToken(@Valid EmailDTO body) {
        Optional<User> result = userRepository.findByEmail(body.getEmail());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Email not found");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover passwordRecover = new PasswordRecover();
        passwordRecover.setEmail(body.getEmail());
        passwordRecover.setToken(token);
        passwordRecover.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        passwordRecoverRepository.save(passwordRecover);

        String text = "Click on the link to recover your password:\n\n"
                + recoverUri + token + "\n\n Valid for " + tokenMinutes + " minutes.";

        emailService.sendEmail(body.getEmail(), "Recover password", text);
    }

    public void saveNewPassword(NewPasswordDTO body) {
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(body.getToken(), Instant.now());
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Invalid token");
        }

        Optional<User> user = userRepository.findByEmail(result.getFirst().getEmail());
        if (user.isPresent()) {
            User entity = user.get();
            entity.setPassword(passwordEncoder.encode(body.getPassword()));
            userRepository.save(entity);
        }
    }
}
