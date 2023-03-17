package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity findAllClients(){
        List<Client> clientsList = clientService.findAll();

        if (clientsList.isEmpty()){
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(clientsList);
        }
    }

    @GetMapping("/{idClient}")
    public ResponseEntity findClientById(@PathVariable UUID idClient){
        Optional<Client> optionalClient = clientService.findById(idClient);

        if(optionalClient.isPresent()){
            return ResponseEntity.status(200).body(optionalClient);
        } else {
            return ResponseEntity.status(204).build();
        }
    }
}
