package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.Client;
import com.sisekelo.carrentalapi.models.CarModel;
import com.sisekelo.carrentalapi.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody final Client Client) {
        return new ResponseEntity<>(clientService.addClient(Client), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable final Long id){
        return clientService.deleteClient(id);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<Client> updateClient(@PathVariable final Long id, @RequestBody final Client client){
        Client updatedClient = clientService.updateClientById(id, client);
        return new ResponseEntity<>(updatedClient, HttpStatus.OK);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClient() {
        return new ResponseEntity<>(clientService.getAllClient(), HttpStatus.OK);
    }
}
