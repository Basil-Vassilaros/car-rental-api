package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.Client;
import com.sisekelo.carrentalapi.models.RentalRecord;
import com.sisekelo.carrentalapi.repository.ClientRepository;
import com.sisekelo.carrentalapi.repository.RentalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    private ClientRepository clientRepository;
    private RentalRecordRepository rentalRecordRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, RentalRecordRepository rentalRecordRepository) {
        this.clientRepository = clientRepository;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    public Client addClient(Client client) {
        return clientRepository.save(client);
    }

    public ResponseEntity<Object> deleteClient (Long id) {
        if(clientRepository.findById(id).isPresent()){
             /*
                If the Client that I wish to delete is referenced to a Rental Record
                then I cannot delete the Client until it no longer is referenced
            */
            Boolean isReferenced = false;
            List<RentalRecord> listToReference = rentalRecordRepository.findAll();// the list to see if there are any references
            List<Long> referenceExistList = new ArrayList<Long>();// a list to save the IDs of the entities referencing
            Client reference = clientRepository.findById(id).get();// the entity I want to delete
            for (RentalRecord toReference : listToReference) {
                if (toReference.getClient() == reference) {
                    isReferenced = true;
                    referenceExistList.add(toReference.getRentalId());
                }
            }
            if (!isReferenced) {
                clientRepository.deleteById(id);
            } else {
                return ResponseEntity.unprocessableEntity().body("Failed to delete client: Reference to Rental Record (" + referenceExistList + ")");
            }
            if(clientRepository.findById(id).isPresent()){
                return ResponseEntity.unprocessableEntity().body("Failed to delete client: Unknown");
            }
            else {
                return ResponseEntity.ok().body("Success: Deleted client");
            }
        }
        else{
            return ResponseEntity.unprocessableEntity().body("Client not found");
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
