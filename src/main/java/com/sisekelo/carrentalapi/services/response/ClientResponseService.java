package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.response.ClientResponse;
import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.Client;
import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import com.sisekelo.carrentalapi.repository.ClientRepository;
import com.sisekelo.carrentalapi.repository.RentalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
        // Check if new Client is a duplicate
        List<Client> clients = clientRepository.findAll();
        for (Client existingClient : clients){
            if (existingClient.getFirstName().equals(client.getFirstName()) &&
            existingClient.getLastName().equals(client.getLastName()) &&
            existingClient.getEmailAddress().equals(client.getEmailAddress()) &&
            existingClient.getHomeAddress().equals(client.getHomeAddress()) &&
            existingClient.getMobileNumber().equals(client.getMobileNumber())){
                return ResponseEntity.unprocessableEntity().body("Error: Client already exists");
            }
        }

        // Set values for new Client
        Client newClient = new Client();
        newClient.setEmailAddress(client.getEmailAddress());
        newClient.setFirstName(client.getFirstName());
        newClient.setLastName(client.getLastName());
        newClient.setHomeAddress(client.getHomeAddress());
        newClient.setMobileNumber(client.getMobileNumber());

        // Save new Client
        clientRepository.save(newClient);

        // Success
        return ResponseEntity.accepted().body("Success: Client saved");
    }

    public ResponseEntity<Object> deleteClient (Long id) {
        // Check if referenced Client exists
        if (clientRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Client not found");
        }
        Client client = clientRepository.getReferenceById(id);

        // Check if Client is being used
        List<RentalRecord> records = rentalRecordRepository.findAll();
        for (RentalRecord record: records) {
            if (record.getClient().equals(client)){
                return ResponseEntity.unprocessableEntity().body("Error: Client is used by Record "+record.getRentalId());
            }
        }

        // Delete Client
        clientRepository.deleteById(id);

        // Check if Client was deleted
        if (clientRepository.findById(id).isPresent()){
            return ResponseEntity.unprocessableEntity().body("Error: Failed to delete Client");
        }

        // Success
        return ResponseEntity.ok().body("Success: Deleted client");
    }

    @Transactional
    public ResponseEntity<Object> updateClientById(Long id, ClientResponse client) {
        // Check if referenced Client exists
        if (clientRepository.findById(id).isEmpty()){
            return ResponseEntity.unprocessableEntity().body("Error: Client not found");
        }
        Client updatedClient = clientRepository.getReferenceById(id);

        // Check if new Client is a duplicate
        List<Client> clients = clientRepository.findAll();
        for (Client existingClient : clients){
            if (existingClient.getFirstName().equals(client.getFirstName()) &&
                    existingClient.getLastName().equals(client.getLastName()) &&
                    existingClient.getEmailAddress().equals(client.getEmailAddress()) &&
                    existingClient.getHomeAddress().equals(client.getHomeAddress()) &&
                    existingClient.getMobileNumber().equals(client.getMobileNumber())){
                return ResponseEntity.unprocessableEntity().body("Error: Client already exists");
            }
        }

        // Set values for updated Client
        updatedClient.setEmailAddress(client.getEmailAddress());
        updatedClient.setFirstName(client.getFirstName());
        updatedClient.setLastName(client.getLastName());
        updatedClient.setHomeAddress(client.getHomeAddress());
        updatedClient.setMobileNumber(client.getMobileNumber());

        // Success
        return ResponseEntity.accepted().body("Success: Client updated");
    }

    public List<Client> searchClient(String search){
        // Initialize our lists
        List<Client> clients = clientRepository.findAll();
        List<Client> found = new ArrayList<>();

        // Loop through to find matches
        for (Client client:clients){
            // Make an index to search by
            List<String> index = new ArrayList<>();
            index.add(client.getFirstName());
            index.add(client.getLastName());
            index.add(client.getHomeAddress());
            index.add(client.getEmailAddress());
            index.add(client.getMobileNumber());

            // find matches
            if (index.contains(search)){
                found.add(client);
            }
        }

        // Success
        return found;
    }
}
