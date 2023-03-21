package com.mateusgomes.luizalabs.controller;

import com.mateusgomes.luizalabs.data.domain.UserData;
import com.mateusgomes.luizalabs.handler.ErrorHandler;
import com.mateusgomes.luizalabs.data.model.Client;
import com.mateusgomes.luizalabs.data.domain.PageableClientList;
import com.mateusgomes.luizalabs.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Clients", description = "Endpoints for managing clients")
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
    @Operation(summary = "Find all clients", description = "Find all clients separated by pages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients were found",
                    content = { @Content(
                            schema = @Schema(implementation = PageableClientList.class),
                            mediaType = "application/json"
                    )}
            ),
            @ApiResponse(responseCode = "400", description = "No page was provided",
                    content = { @Content (schema = @Schema(defaultValue = "No page was provided"))}
            ),
            @ApiResponse(responseCode = "404", description = "No clients were found",
                    content = { @Content }
            ),
    })
    public ResponseEntity<PageableClientList> findAllClients(
            @RequestParam(value="page", required=true) int page
    ){
        PageableClientList pageableClientList = clientService.findAll(page);

        if (pageableClientList.getClients().isEmpty()){
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(pageableClientList);
    }

    @GetMapping(value = "/{idClient}")
    @Operation(summary = "Find a client", description = "Find a client based on id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client was found",
                    content = { @Content(
                            schema = @Schema(implementation = Client.class),
                            mediaType = "application/json"
                    )}
            ),
            @ApiResponse(responseCode = "404", description = "No client was found with the specified id",
                    content = { @Content }
            ),
    })
    public ResponseEntity<Optional<Client>> findClientById(@PathVariable UUID idClient){
        Optional<Client> optionalClient = clientService.findById(idClient);

        if(optionalClient.isPresent()){
            return ResponseEntity.status(200).body(optionalClient);
        }

        return ResponseEntity.status(404).build();
    }

    @PostMapping
    @Operation(summary = "Create a client", description = "Create a new record of client in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client was successfully created",
                    content = { @Content(
                            schema = @Schema(implementation = Client.class),
                            mediaType = "application/json"
                    )}
            ),
            @ApiResponse(responseCode = "400", description = "Email is already in use",
                    content = { @Content (schema = @Schema(defaultValue = "Email is already in use."))}
            ),
            @ApiResponse(responseCode = "422", description = "Validation error",
                    content = { @Content (schema = @Schema(
                            defaultValue = "The following errors were found: \n" +
                            "0 - Client name is a required field and should not be empty;\n" +
                            "1 - Client email is a required field and should not be empty;"
                    ))}
            )
    })
    public ResponseEntity createClient(@Valid @RequestBody UserData userData, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(errorHandler.buildValidationErrorMessage(bindingResult));
        }

        if(clientService.existsByEmail(userData.getClientEmail())){
            return ResponseEntity.status(400).body("Email is already in use.");
        }

        clientService.create(userData);
        return ResponseEntity.status(201).body(userData);
    }

    @DeleteMapping("/{idClient}")
    @Operation(summary = "Delete a client", description = "Delete the record of client in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client was successfully deleted",
                    content = { @Content }
            ),
            @ApiResponse(responseCode = "400", description = "Client does not exists",
                    content = { @Content (schema = @Schema(defaultValue = "Client does not exists."))}
            )
    })
    public ResponseEntity<String> deleteClient(@PathVariable UUID idClient){
        if(!clientService.existsById(idClient)){
            return ResponseEntity.status(400).body("Client does not exists.");
        }

        clientService.delete(idClient);
        return ResponseEntity.status(204).build();
    }

    @PutMapping
    @Operation(summary = "Update a client", description = "Update the record of client in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client was successfully updated",
                    content = { @Content }
            ),
            @ApiResponse(responseCode = "400", description = "Client does not exists",
                    content = { @Content (schema = @Schema(defaultValue = "Client does not exists."))}
            ),
            @ApiResponse(responseCode = "400", description = "Email is already in use",
                    content = { @Content (schema = @Schema(defaultValue = "Email is already in use."))}
            ),
            @ApiResponse(responseCode = "422", description = "Validation error",
                    content = { @Content (schema = @Schema(
                            defaultValue = "The following errors were found: \n" +
                                    "0 - Client name is a required field and should not be empty;\n" +
                                    "1 - Client email is a required field and should not be empty;"
                    ))}
            )
    })
    public ResponseEntity updateClient(@Valid @RequestBody Client client, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(422).body(errorHandler.buildValidationErrorMessage(bindingResult));
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
