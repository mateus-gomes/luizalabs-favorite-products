package com.mateusgomes.luizalabs.data.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserData {

    @NotNull(message = "Client name is a required field and should not be empty")
    @NotBlank(message = "Client name must not be blank")
    private String clientName;

    @NotNull(message = "Client email is a required field and should not be empty")
    @NotBlank(message = "Client email must not be blank")
    private String clientEmail;

    @NotNull(message = "Password is a required field and should not be empty")
    @NotBlank(message = "Password must not be blank")
    private String password;

    public UserData(String clientName, String clientEmail, String password) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.password = password;
    }

    public UserData() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
