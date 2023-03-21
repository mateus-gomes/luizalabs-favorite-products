package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.data.domain.AccountCredentials;
import com.mateusgomes.luizalabs.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Endpoint")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Authenticates an user and return a token")
    @PostMapping("/signin")
    public ResponseEntity signin(@RequestBody AccountCredentials accountCredentials){
        if(accountCredentials.getUserName() == null || accountCredentials.getPassword() == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request.");
        }

        return authService.signin(accountCredentials);
    }
}
