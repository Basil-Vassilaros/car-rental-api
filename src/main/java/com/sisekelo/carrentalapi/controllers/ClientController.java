package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.response.ClientResponse;
import com.sisekelo.carrentalapi.models.tables.Client;
import com.sisekelo.carrentalapi.repository.ClientRepository;
import com.sisekelo.carrentalapi.services.response.ClientResponseService;
import com.sisekelo.carrentalapi.services.table.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private ClientService clientService;
    private ClientResponseService clientResponseService;
    private ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientService clientService, ClientResponseService clientResponseService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientResponseService = clientResponseService;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addClient(@RequestBody final ClientResponse client) {
        return clientResponseService.addClient(client);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteClient(@PathVariable final Long id){
        return clientResponseService.deleteClient(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateClient(@PathVariable final Long id, @RequestBody final ClientResponse client){
        return clientResponseService.updateClientById(id, client);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getClient(@PathVariable Long id) {
        if (clientRepository.findById(id).isPresent()){
            return new ResponseEntity<>(clientRepository.getReferenceById(id), HttpStatus.OK);
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Client not found");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClient() {
        return new ResponseEntity<>(clientRepository.findAll(), HttpStatus.OK);
    }
}
