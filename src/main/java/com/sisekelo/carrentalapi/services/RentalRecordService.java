package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.RentalRecord;
import com.sisekelo.carrentalapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class RentalRecordService {
    private CarCategoryRepository carCategoryRepository;
    private CarModelRepository carModelRepository;
    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private final RentalRecordRepository rentalRecordRepository;

    @Autowired
    public RentalRecordService(CarCategoryRepository carCategoryRepository, CarModelRepository carModelRepository,
                               CarRepository carRepository, ClientRepository clientRepository,
                               RentalRecordRepository rentalRecordRepository) {
        this.carCategoryRepository = carCategoryRepository;
        this.carModelRepository = carModelRepository;
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.rentalRecordRepository = rentalRecordRepository;
    }

    public float getTotalPrice(Float price, LocalDateTime returnDate, LocalDateTime reserveDate){
        float totalPrice = price;
        Long diffInDays = returnDate.toLocalDate().toEpochDay() - reserveDate.toLocalDate().toEpochDay();
        return totalPrice * diffInDays;
    }

    public ResponseEntity<Object> addRentalRecord(RentalRecord record) {
        record.setTotalPrice(getTotalPrice(record.getCar().getPrice(), record.getReturnDate(),
                record.getReservationDate()));
        RentalRecord savedRecord = rentalRecordRepository.save(record);
        if(rentalRecordRepository.findById(savedRecord.getRentalId()).isPresent()){
            return ResponseEntity.accepted().body("Successfully created Record");
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Failed to Create specified record");
        }
    }

    public ResponseEntity<Object> deleteRentalRecord (Long id) {
        if(rentalRecordRepository.findById(id).isPresent()){
            RentalRecord record = rentalRecordRepository.getReferenceById(id);
            rentalRecordRepository.deleteById(id);
            if(rentalRecordRepository.findById(id).isPresent()){
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

    public RentalRecord getRentalRecord(Long id){
        if(rentalRecordRepository.findById(id).isPresent()){
            RentalRecord record = rentalRecordRepository.findById(id).get();
            return record;
        }
        else{
            return null;
        }
    }

    public List<RentalRecord> getAllRentalRecord(){
        List<RentalRecord> records = rentalRecordRepository.findAll();
        return records;
    }

    public RentalRecord deleteRecordById (Long id) {
        RentalRecord record = rentalRecordRepository.getReferenceById(id);
        record.getCar().setIsReserved(false);
        record.getCar().setInUse(false);
        rentalRecordRepository.delete(record);
        return record;
    }

    @Transactional
    public RentalRecord updateRecordById(Long id, RentalRecord record){
        RentalRecord updatedRecord = rentalRecordRepository.getReferenceById(id);
        updatedRecord.setCar(record.getCar());
        updatedRecord.setClient(record.getClient());
        updatedRecord.setCollectionDate(record.getCollectionDate());
        updatedRecord.setReturnDate(record.getReturnDate());
        updatedRecord.setTotalPrice(getTotalPrice(record.getCar().getPrice(), record.getReturnDate(),
                record.getReservationDate()));
        return updatedRecord;
    }
}
