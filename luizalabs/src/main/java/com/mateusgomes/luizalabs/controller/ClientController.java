package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.handler.ErrorHandler;
import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private ErrorHandler errorHandler = new ErrorHandler();

    @GetMapping
    public ResponseEntity<List<Client>> findAllClients(){
        List<Client> clientsList = clientService.findAll();

        if (clientsList.isEmpty()){
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(clientsList);
        }
    }

    @GetMapping("/{idClient}")
    public ResponseEntity<Optional<Client>> findClientById(@PathVariable UUID idClient){
        Optional<Client> optionalClient = clientService.findById(idClient);

        if(optionalClient.isPresent()){
            return ResponseEntity.status(200).body(optionalClient);
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @PostMapping
    public ResponseEntity createClient(@Valid @RequestBody Client client, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(errorHandler.buildErrorMessage(bindingResult));
        }

        if(clientService.existsByEmail(client.getClientEmail())){
            return ResponseEntity.status(400).body("Email is already in use.");
        } else {
            clientService.create(client);
            return ResponseEntity.status(201).body(client);
        }
    }
}
