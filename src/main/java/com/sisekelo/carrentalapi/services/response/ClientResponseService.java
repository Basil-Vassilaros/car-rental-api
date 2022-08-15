package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.response.ClientResponse;
import com.sisekelo.carrentalapi.models.tables.Client;
import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import com.sisekelo.carrentalapi.repository.ClientRepository;
import com.sisekelo.carrentalapi.repository.RentalRecordRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ClientResponseService {
    private ClientRepository clientRepository;
    private RentalRecordRepository rentalRecordRepository;
    private Boolean missing = false;

    @Autowired
    public ClientResponseService(ClientRepository clientRepository, RentalRecordRepository rentalRecordRepository) {
        this.clientRepository = clientRepository;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    public ResponseEntity<Object> addClient(ClientResponse client){
        Client newClient = new Client();
        newClient.setEmailAddress(client.getEmailAddress());
        newClient.setFirstName(client.getFirstName());
        newClient.setLastName(client.getLastName());
        newClient.setHomeAddress(client.getHomeAddress());

        newClient.setMobileNumber(client.getMobileNumber());
        newClient.setCarsReserved(null);
        clientRepository.save(newClient);
        return ResponseEntity.accepted().body("Success: Client saved");
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

    @Transactional
    public ResponseEntity<Object> updateClientById(Long id, ClientResponse client) {
        if (clientRepository.findById(id).isPresent()) {
            Client updatedClient = clientRepository.getReferenceById(id);
            updatedClient.setEmailAddress(client.getEmailAddress());
            updatedClient.setFirstName(client.getFirstName());
            updatedClient.setLastName(client.getLastName());
            updatedClient.setHomeAddress(client.getHomeAddress());
            updatedClient.setMobileNumber(client.getMobileNumber());
            return ResponseEntity.accepted().body("Success: Car updated");
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Client not found");
        }
    }
}
