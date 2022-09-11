package com.sisekelo.carrentalapi.services.response;

import com.sisekelo.carrentalapi.models.exceptions.NotFoundException;
import com.sisekelo.carrentalapi.models.tables.Car;
import com.sisekelo.carrentalapi.models.tables.Client;
import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import com.sisekelo.carrentalapi.models.response.Reservation;
import com.sisekelo.carrentalapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private CarManufacturerRepository carManufacturerRepository;
    private CarModelRepository carModelRepository;
    private CarCategoryRepository carCategoryRepository;
    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private RentalRecordRepository rentalRecordRepository;

    private Boolean missing = false;

    private Boolean problem = false;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");



    @Autowired
    public ReservationService(CarManufacturerRepository carManufacturerRepository, CarModelRepository carModelRepository, CarCategoryRepository carCategoryRepository, CarRepository carRepository, ClientRepository clientRepository, RentalRecordRepository rentalRecordRepository) {
        this.carManufacturerRepository = carManufacturerRepository;
        this.carModelRepository = carModelRepository;
        this.carCategoryRepository = carCategoryRepository;
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    // Method to calculate the total price of the booking
    // Total price = price * (returnDate - collectDate)
    public Float calcTotalPrice(Float price, LocalDate dateToCollect, LocalDate dateToReturn){
        return price * (dateToReturn.toEpochDay() - dateToCollect.toEpochDay() + 1);
    }

    // To Find booking conflicts
    // Not allowed to have a set of dates between another set
    public String getPreBookedDates(Car car, LocalDate dateToCollect, LocalDate dateToReturn){
        String[] bookedDates = car.getBookedDates().split(", ");
        String prebookedDates = "";
        for (String bookedDate: bookedDates) {
            if (bookedDate != ""){
                String[] dates = bookedDate.split("-");
                if (dates.length > 0 ){
                    LocalDate bookedDateCollect = LocalDate.parse(dates[0], formatter);
                    LocalDate bookedDateReturn = LocalDate.parse(dates[1], formatter);
                    // bookedCollect before preCollect OR after preReturn
                    // bookedReturn before preCollect OR after preReturn
                    if ((dateToCollect.isBefore(bookedDateCollect) || dateToCollect.isAfter(bookedDateReturn)) &&
                            (dateToReturn.isBefore(bookedDateCollect) || dateToReturn.isAfter(bookedDateReturn))){
                        prebookedDates = prebookedDates + dates[0] + "-" + dates[1]+", ";
                    }
                }
            }
        }
        return prebookedDates;
    }

    public ResponseEntity<Object> addNewRecord(Reservation reservation){

        // Checks if Referenced Client exists
        if(clientRepository.findById(reservation.getClientId()).isEmpty()){
            return ResponseEntity.badRequest().body(
                    "Error: Referenced Client not found");
        }
        Client client = clientRepository.getReferenceById(reservation.getClientId());

        // Checks if Referenced Car exists
        if(carRepository.findById(reservation.getCarId()).isEmpty()){
            return ResponseEntity.badRequest().body(
                    "Error: Referenced Car not found");
        }
        Car car = carRepository.getReferenceById(reservation.getCarId());

        // Checks if Collect Data is before Return Date
        LocalDate dateToCollect = LocalDate.parse(reservation.getDateToCollect(), formatter);
        LocalDate dateToReturn = LocalDate.parse(reservation.getDateToReturn(), formatter);
        if (dateToCollect.isAfter(dateToReturn)){
            return ResponseEntity.badRequest().body(
                    "Error: Collect Date is must be before Return Date");
        }

       // Checks if Referenced Car is already booked
        if (!(car.getBookedDates().isEmpty())){
            String prebookedDates = getPreBookedDates(car, dateToCollect, dateToReturn);
            if (prebookedDates != "") {
                return ResponseEntity.badRequest().body(
                        "Error: Car is unavailable - Booking conflicts: "+prebookedDates);
            }
        }

        // Get Total price of booking
        Float totalPrice = calcTotalPrice(car.getPrice(),dateToCollect, dateToReturn);

        // Set the Car to reserved, save the dates it has been reserved & for the client
        car.setIsReserved(true);
        String carBookedDates = car.getBookedDates() + ", "+reservation.getDateToCollect()+"-"+reservation.getDateToReturn();
        car.setBookedDates(carBookedDates);

        // Set the new Record
        RentalRecord newRecord = new RentalRecord();
        newRecord.setCar(car);
        newRecord.setClient(client);
        newRecord.setDateReservationMade(LocalDate.now());
        newRecord.setDateToCollect(dateToCollect);
        newRecord.setDateToReturn(dateToReturn);
        newRecord.setTotalPrice(totalPrice);

        // Save the new Record
        rentalRecordRepository.save(newRecord);

        // Success
        return ResponseEntity.accepted().body("Success: Record saved");
    }

    public ResponseEntity<Object> updateBooking(Long id, Reservation reservation){
        // Checks if Record exists
        if(rentalRecordRepository.findById(id).isEmpty()){
            return ResponseEntity.badRequest().body(
                    "Error: Record not found");
        }
        RentalRecord record = rentalRecordRepository.getReferenceById(id);

        // Checks if Referenced Client exists
        if(clientRepository.findById(reservation.getClientId()).isEmpty()){
            return ResponseEntity.badRequest().body(
                    "Error: Referenced Client not found");
        }
        Client client = clientRepository.getReferenceById(reservation.getClientId());

        // Checks if Referenced Car exists
        if(carRepository.findById(reservation.getCarId()).isEmpty()){
            return ResponseEntity.badRequest().body(
                    "Error: Referenced Car not found");
        }
        Car car = carRepository.getReferenceById(reservation.getCarId());

        // Checks if Collect Date is before Return Date
        LocalDate dateToCollect = LocalDate.parse(reservation.getDateToCollect(), formatter);
        LocalDate dateToReturn = LocalDate.parse(reservation.getDateToReturn(), formatter);
        if (dateToCollect.isAfter(dateToReturn)){
            return ResponseEntity.badRequest().body(
                    "Error: Collect Date is must be before Return Date");
        }

        // Checks if Referenced Car is already booked
        if (!(car.getBookedDates().isEmpty())){
            String prebookedDates = getPreBookedDates(car, dateToCollect, dateToReturn);
            if (prebookedDates != "") {
                System.out.println("Error: Date conflicts "+prebookedDates);
                return ResponseEntity.badRequest().body(
                        "Error: Car is unavailable - Booking conflicts: "+prebookedDates);
            }
        }

        // Get Total price of booking
        Float totalPrice = calcTotalPrice(car.getPrice(),dateToCollect, dateToReturn);

        // Set the Car to reserved, save the dates it has been reserved & for the client
        car.setIsReserved(true);

        // Remove old booking dates and replace them
        String carBookedDates = ", "+reservation.getDateToCollect()+"-"+reservation.getDateToReturn();
        String oldBookedDates = ", "+record.getDateToCollect().toString()+"-"+record.getDateToReturn().toString();
        String bookedDates = car.getBookedDates();
        bookedDates.replace(oldBookedDates, carBookedDates);
        car.setBookedDates(bookedDates);

        // Set the updated record
        record.setCar(car);
        record.setClient(client);
        record.setDateReservationMade(LocalDate.now());
        record.setDateToCollect(dateToCollect);
        record.setDateToReturn(dateToReturn);
        record.setTotalPrice(totalPrice);

        // Success
        return ResponseEntity.accepted().body("Success: record updated");
    }

    public ResponseEntity<Object> removeBooking(Long id){
        // Checks if Record exists
        if(rentalRecordRepository.findById(id).isEmpty()){
            return ResponseEntity.badRequest().body(
                    "Error: Record not found");
        }
        RentalRecord record = rentalRecordRepository.getReferenceById(id);

        // Get referenced Car
        Car car = record.getCar();

        // Find record Dates in Car and remove them
        String carBookingDates = car.getBookedDates();
        String datesToRemove = ", "+record.getDateToCollect().toString() +"-"+record.getDateToReturn();
        carBookingDates.replace(datesToRemove, "");
        car.setBookedDates(carBookingDates);

        // Check if Booked Dates were removed
        if (car.getBookedDates().contains(datesToRemove)){
            return ResponseEntity.badRequest().body(
                    "Error: Failed to remove booking dates from Car "+car.getCarId());
        }

        // Resets Car current availability
        car.setIsReserved(false);
        car.setInUse(false);

        // Delete record
        rentalRecordRepository.deleteById(id);

        // Check if record deleted
        if (rentalRecordRepository.findById(id).isPresent()){
            return ResponseEntity.badRequest().body(
                    "Error: Failed to remove record "+record.getRentalId());
        }

        // Success
        return ResponseEntity.accepted().body("Success: Record Removed");
    }

    public List<RentalRecord> searchRecord(String search){
        // Initialize our lists
        List<RentalRecord> records = rentalRecordRepository.findAll();
        List<RentalRecord> found = new ArrayList<>();

        // Loop through to find matches
        for (RentalRecord record:records){
            // Make an index to search by
            List<String> index = new ArrayList<>();
            Car car = record.getCar();
            Client client = record.getClient();

            // Add Car index
            index.add(car.getRegistrationNumber());
            index.add(car.getCarModel().getCarModel());
            index.add(car.getCarModel().getCarManufacturer().getManufacturer());
            index.add(car.getCarModel().getCarCategory().getCarCategory());
            index.add(car.getCarModel().getYear());
            index.add(car.getColor());

            // Add Client index
            index.add(client.getFirstName());
            index.add(client.getLastName());
            index.add(client.getHomeAddress());
            index.add(client.getEmailAddress());
            index.add(client.getMobileNumber());

            // Add Record index
            index.add(record.getDateToCollect().toString());
            index.add(record.getDateToReturn().toString());
            index.add(record.getDateReservationMade().toString());


            // find matches
            if (index.contains(search)){
                found.add(record);
            }
        }

        // Success
        return found;
    }
}
