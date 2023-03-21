package com.mateusgomes.luizalabs.data.domain;

public class UserData {

    private String clientName;
    private String clientEmail;
    private String password;

    public UserData(String clientName, String clientEmail, String password) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.password = password;
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
