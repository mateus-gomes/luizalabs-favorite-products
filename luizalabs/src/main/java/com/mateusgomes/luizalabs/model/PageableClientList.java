package com.mateusgomes.luizalabs.model;

import java.util.List;

public class PageableClientList {

    private Meta meta;
    private List<Client> clients;

    public PageableClientList(Meta meta, List<Client> clients) {
        this.meta = meta;
        this.clients = clients;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
