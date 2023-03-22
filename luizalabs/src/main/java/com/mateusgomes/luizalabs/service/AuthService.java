package com.mateusgomes.luizalabs.service;

import com.mateusgomes.luizalabs.data.domain.AccountCredentials;
import com.mateusgomes.luizalabs.data.domain.Token;
import com.mateusgomes.luizalabs.data.model.User;
import com.mateusgomes.luizalabs.repository.UserRepository;
import com.mateusgomes.luizalabs.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity signin(AccountCredentials accountCredentials){
        try {
            String username = accountCredentials.getUserName();
            String password = accountCredentials.getPassword();

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            Optional<User> user = userRepository.findByUsername(username);

            if(user.isPresent()){
                Token tokenResponse = tokenProvider.createAccessToken(
                        username,
                        user.get().getRoles(),
                        user.get().getIdUser()
                );
                return ResponseEntity.ok(tokenResponse);
            } else {
                throw new UsernameNotFoundException("Username not found!");
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password!");
        }
    }

    public boolean isUserAuthorized(String requestIdUser, String authorizationHeader){
        String tokenIdUser = tokenProvider.extractIdUserFromToken(authorizationHeader);
        return requestIdUser.equals(tokenIdUser);
    }
}
