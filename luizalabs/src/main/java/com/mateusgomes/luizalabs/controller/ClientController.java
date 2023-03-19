package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.handler.ErrorHandler;
import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.model.PageableClientList;
import com.mateusgomes.luizalabs.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private ErrorHandler errorHandler = new ErrorHandler();

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();

        return ResponseEntity.status(400).body(String.format("No %s was provided", name));
    }

    @GetMapping
    public ResponseEntity<PageableClientList> findAllClients(
            @RequestParam(value="page", required=true) int page
    ){
        PageableClientList pageableClientList = clientService.findAll(page);

        if (pageableClientList.getClients().isEmpty()){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(pageableClientList);
    }

    @GetMapping("/{idClient}")
    public ResponseEntity<Optional<Client>> findClientById(@PathVariable UUID idClient){
        Optional<Client> optionalClient = clientService.findById(idClient);

        if(optionalClient.isPresent()){
            return ResponseEntity.status(200).body(optionalClient);
        }

        return ResponseEntity.status(204).build();
    }

    @PostMapping
    public ResponseEntity createClient(@Valid @RequestBody Client client, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(errorHandler.buildErrorMessage(bindingResult));
        }

        if(clientService.existsByEmail(client.getClientEmail())){
            return ResponseEntity.status(400).body("Email is already in use.");
        }

        clientService.create(client);
        return ResponseEntity.status(201).body(client);
    }

    @DeleteMapping("/{idClient}")
    public ResponseEntity<String> deleteClient(@PathVariable UUID idClient){
        if(!clientService.existsById(idClient)){
            return ResponseEntity.status(400).body("Client does not exists.");
        }

        clientService.delete(idClient);
        return ResponseEntity.status(204).build();
    }

    @PutMapping
    public ResponseEntity updateClient(@Valid @RequestBody Client client, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(errorHandler.buildErrorMessage(bindingResult));
        }

        if(!clientService.existsById(client.getIdClient())){
            return ResponseEntity.status(400).body("Client does not exists.");
        }

        if(!clientService.isEmailAvailableForUse(client)){
            return ResponseEntity.status(400).body("Email is already in use.");
        }

        clientService.update(client);
        return ResponseEntity.status(204).build();
    }
}
