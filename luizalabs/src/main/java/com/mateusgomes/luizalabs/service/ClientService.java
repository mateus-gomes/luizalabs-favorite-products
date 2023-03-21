package com.mateusgomes.luizalabs.service;

import com.mateusgomes.luizalabs.config.SecurityConfig;
import com.mateusgomes.luizalabs.data.domain.UserData;
import com.mateusgomes.luizalabs.data.model.Client;
import com.mateusgomes.luizalabs.data.domain.Meta;
import com.mateusgomes.luizalabs.data.domain.PageableClientList;
import com.mateusgomes.luizalabs.data.model.User;
import com.mateusgomes.luizalabs.repository.ClientRepository;
import com.mateusgomes.luizalabs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public PageableClientList findAll(int page) {
        final int PAGE_SIZE = 10;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Client> pageClient = clientRepository.findAll(pageable);

        return new PageableClientList(
                new Meta(page, PAGE_SIZE),
                pageClient.getContent()
        );
    }

    public Optional<Client> findById(UUID clientId) {
        return clientRepository.findById(clientId);
    }

    public boolean existsByEmail(String clientEmail) {
        return clientRepository.existsByClientEmail(clientEmail);
    }

    public void create(UserData userData) {
        User user = saveUser(userData);
        clientRepository.save(new Client(
                user.getIdUser(),
                userData.getClientName(),
                userData.getClientEmail()
        ));
    }

    private User saveUser(UserData userData){
        User user = new User(
                userData.getClientEmail(),
                userData.getClientName(),
                securityConfig.passwordEncoder().encode(userData.getPassword())
        );

        return userRepository.save(user);
    }

    public boolean existsById(UUID idClient) {
        return clientRepository.existsById(idClient);
    }

    public void delete(UUID idClient) {
        clientRepository.deleteById(idClient);
    }

    public void update(Client client) {
        clientRepository.save(client);
    }

    public boolean isEmailAvailableForUse(Client client) {
        Optional<Client> clientFound = findByEmail(client.getClientEmail());

        if(clientFound.isPresent()){
            return client.getIdClient().equals(clientFound.get().getIdClient());
        }

        return true;
    }

    private Optional<Client> findByEmail(String clientEmail){
        return clientRepository.findByClientEmail(clientEmail);
    }
}
