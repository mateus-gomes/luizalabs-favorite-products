package com.mateusgomes.luizalabs.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    private UUID idClient;

    @NotNull(message = "Client name is a required field and should not be empty")
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @NotNull(message = "Client email is a required field and should not be empty")
    @Column(name = "client_email", nullable = false)
    private String clientEmail;

    public Client() {
    }

    public Client(UUID idClient) {
        this.idClient = idClient;
    }

    public Client(UUID idClient, String clientName, String clientEmail) {
        this.idClient = idClient;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
    }

    public UUID getIdClient() {
        return idClient;
    }

    public void setIdClient(UUID idClient) {
        this.idClient = idClient;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }
}
