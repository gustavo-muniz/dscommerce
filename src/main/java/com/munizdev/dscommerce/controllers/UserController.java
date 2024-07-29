package com.munizdev.dscommerce.controllers;

import com.munizdev.dscommerce.dto.UserDTO;
import com.munizdev.dscommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        UserDTO dto = service.getMe();

        return ResponseEntity.ok(dto);
    }
}
