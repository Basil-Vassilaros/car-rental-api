package com.sisekelo.carrentalapi.controllers;

import com.sisekelo.carrentalapi.models.response.ClientResponse;
import com.sisekelo.carrentalapi.models.tables.CarModel;
import com.sisekelo.carrentalapi.models.tables.Client;
import com.sisekelo.carrentalapi.repository.ClientRepository;
import com.sisekelo.carrentalapi.services.response.ClientResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")

@RestController
@RequestMapping("/client")
public class ClientController {
    private ClientResponseService clientResponseService;
    private ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientResponseService clientResponseService, ClientRepository clientRepository) {
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
        if (clientRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Client not found");
        }
        return new ResponseEntity<>(clientRepository.getReferenceById(id), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> getAllClient() {
        return new ResponseEntity<>(clientRepository.findAll(), HttpStatus.OK);
    }
    @GetMapping("/search/{search}")
    public ResponseEntity<List<Client>> search(@PathVariable String search) {
        return new ResponseEntity<>(clientResponseService.searchClient(search), HttpStatus.OK);
    }
}
