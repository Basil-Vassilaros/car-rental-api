package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.Client;
import com.sisekelo.carrentalapi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ClientService {
    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client addClient(Client client) {
        return clientRepository.save(client);
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

    public Client getClient(Long id){
        return clientRepository.getReferenceById(id);
    }

    public List<Client> getAllClient(){
        return clientRepository.findAll();
    }

    @Transactional
    public Client updateClientById(Long id, Client client){
        Client updatedClient = clientRepository.getReferenceById(id);

        updatedClient.setEmailAddress(client.getEmailAddress());
        updatedClient.setFirstName(client.getFirstName());
        updatedClient.setLastName(client.getLastName());
        updatedClient.setHomeAddress(client.getHomeAddress());
        updatedClient.setMobileNumber(client.getMobileNumber());

        return updatedClient;
    }
}
