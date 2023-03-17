package com.mateusgomes.luizalabs.service;

import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }
}
