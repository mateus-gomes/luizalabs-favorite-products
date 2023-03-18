package com.mateusgomes.luizalabs.service;

import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(UUID clientId) {
        return clientRepository.findById(clientId);
    }

    public boolean existsByEmail(String clientEmail) {
        return clientRepository.existsByClientEmail(clientEmail);
    }

    public void create(Client client) {
        clientRepository.save(client);
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
