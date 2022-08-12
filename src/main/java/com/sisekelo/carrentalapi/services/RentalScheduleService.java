package com.sisekelo.carrentalapi.services;

import com.sisekelo.carrentalapi.models.RentalSchedule;
import com.sisekelo.carrentalapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class RentalScheduleService {
    private CarCategoryRepository carCategoryRepository;
    private CarModelRepository carModelRepository;
    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private final RentalScheduleRepository rentalScheduleRepository;

    @Autowired
    public RentalScheduleService(CarCategoryRepository carCategoryRepository, CarModelRepository carModelRepository,
                                 CarRepository carRepository, ClientRepository clientRepository,
                                 RentalScheduleRepository rentalScheduleRepository) {
        this.carCategoryRepository = carCategoryRepository;
        this.carModelRepository = carModelRepository;
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.rentalScheduleRepository = rentalScheduleRepository;
    }

    public float getTotalPrice(Float price, Date returnDate, Date reserveDate){
        float totalPrice = price;
        Long diffInMilli = returnDate.toInstant().toEpochMilli() - reserveDate.toInstant().toEpochMilli();
        int diffInDays = diffInMilli.intValue() / 86400000 ;
        return totalPrice * diffInDays;
    }

    public ResponseEntity<Object> addRentalRecord(RentalSchedule record) {
        carRepository.getReferenceById(record.getCar().getCarId()).setIsReserved(true);
        record.setReservationDate(new Date());
        record.setTotalPrice(getTotalPrice(record.getCar().getPrice(), record.getReturnDate(),
                record.getReservationDate()));
        RentalSchedule savedRecord = rentalScheduleRepository.save(record);
        if(rentalScheduleRepository.findById(savedRecord.getRentalId()).isPresent()){
            return ResponseEntity.accepted().body("Successfully created Record");
        }
        else {
            return ResponseEntity.unprocessableEntity().body("Failed to Create specified record");
        }
    }

    public ResponseEntity<Object> deleteRentalRecord (Long id) {
        if(rentalScheduleRepository.findById(id).isPresent()){
            RentalSchedule record = rentalScheduleRepository.getReferenceById(id);
            record.getCar().setIsReserved(false);
            record.getCar().setInUse(false);
            rentalScheduleRepository.deleteById(id);
            if(rentalScheduleRepository.findById(id).isPresent()){
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

    public RentalSchedule getRentalRecord(Long id){
        if(rentalScheduleRepository.findById(id).isPresent()){
            RentalSchedule record = rentalScheduleRepository.findById(id).get();
            return record;
        }
        else{
            return null;
        }
    }

    public List<RentalSchedule> getAllRentalRecord(){
        List<RentalSchedule> records = rentalScheduleRepository.findAll();
        return records;
    }

    public RentalSchedule deleteRecordById (Long id) {
        RentalSchedule record = rentalScheduleRepository.getReferenceById(id);
        record.getCar().setIsReserved(false);
        record.getCar().setInUse(false);
        rentalScheduleRepository.delete(record);
        return record;
    }

    @Transactional
    public RentalSchedule updateRecordById(Long id, RentalSchedule record){
        RentalSchedule updatedRecord = rentalScheduleRepository.getReferenceById(id);
        updatedRecord.setCar(record.getCar());
        updatedRecord.setClient(record.getClient());
        updatedRecord.setCollectionDate(record.getCollectionDate());
        updatedRecord.setReturnDate(record.getReturnDate());
        updatedRecord.setTotalPrice(getTotalPrice(record.getCar().getPrice(), record.getReturnDate(),
                record.getReservationDate()));
        if (record.getCollectionDate().toInstant().toEpochMilli() > 0) {
            record.getCar().setInUse(true);
        }
        return updatedRecord;
    }
}
