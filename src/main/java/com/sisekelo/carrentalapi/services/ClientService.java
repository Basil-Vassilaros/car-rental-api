package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.Client;
import com.sisekelo.carrentalapi.models.RentalSchedule;
import com.sisekelo.carrentalapi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ResponseEntity<Object> addClient(Client client) {
        Client savedClient = clientRepository.save(client);
        if(clientRepository.findById(savedClient.getClientId()).isPresent()){
            return ResponseEntity.accepted().body("Successfully created Record");
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Failed to Create specified record");
        }
    }

    public ResponseEntity<Object> deleteClient (Long id) {
        if(clientRepository.findById(id).isPresent()){
            clientRepository.deleteById(id);
            if(clientRepository.findById(id).isPresent()){
                return ResponseEntity.unprocessableEntity().body("Failed to delete the specific record");
            }
            else {
                return ResponseEntity.ok().body("Successfully deleted specific record");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("No Record Found");
        }
    }
}
