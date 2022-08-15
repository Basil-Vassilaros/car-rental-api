package com.sisekelo.carrentalapi.services.table;

import com.sisekelo.carrentalapi.models.tables.RentalRecord;
import com.sisekelo.carrentalapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
}
