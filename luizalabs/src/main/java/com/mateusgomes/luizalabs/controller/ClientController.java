package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.model.Client;
import com.mateusgomes.luizalabs.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity findAllClients(){
        List<Client> clientsList = clientService.findAll();

        if (clientsList.isEmpty()){
            return new ResponseEntity<>(clientsList, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(clientsList, HttpStatus.OK);
        }

    }
}
