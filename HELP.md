# Car Rental Project
Project Date: 1 July 2022 

Project Presentation Date: 15 July 2022

## Instructions
  Create a simple car rental REST API using Java(Springboot), MySQL and Postman (for testing).
  Configure SwaggerUI in your project.
  Utilise 4 basic methods: GET, PUT, POST and DELETE

## Features
* Single user API (admin dashboard)
* Test API using Postman
* Test API using SwaggerUI


* Option to create,read, update & delete Car Category
* Option to create,read, update & delete Car Models
* Option to create,read, update & delete Car
* Option to create,read, update & delete Client
* Option to reserve a car for a particular client
* Option to edit and remove a reservation for a particular client
* Option to book a car for a particular client
* Option to list available cars
* Option to list reserved cars
* Option to list booked cars
* Option to list available cars by car model
* Option to list reserved cars by car model
* Option to list booked cars by car model

## Notes
  Include the price of the car, include the reservation schedule in your car entity

  Technologies to use: Java(Springboot), SwaggerUI, Springboot data jpa, Springboot web, Maven, MySQL.
________________________________________________________
# Planning
  We are going to make entities:
  
* Car Category
* Car Model
* Car
* Client
* RentalSchedule(rename me!!)

## Models
### CarCategory
###### Data Model
* categoryId - Id of the Category
* categoryType - Type of vehicle (Car, Bakkie, Combie, Truck, Bus, etc.)
###### Data Types
    private Long categoryId;

    private String categoryType;

### CarManufacturer
###### Data Model
* manufacturerId - Id of manufacturer
* manufacturerName - Name of the Car manufacture company (Audi, Mercedes, BMW, Toyota, Nissan, etc.)

###### Data Types
    private Long manufacturerId;

    private String manufacturerName;

### CarModel
###### Data Model
* modelId - Id of the Model
* manufacturer - (Many-to-One) Foreign Key to Car Manufacturer entity
* modelType - Model of the Car

###### Data Types
    private Long modelId;

    private CarManufacturer carManufacturer;

    private String modelType;

### Car
###### Data Model
* carId - Id of the Car
* registrationNumber - Registration Number/Number Plate
* carModel - (Many-to-One) Foreign Key to Car Model entity
* carCategory - (Many-to-One) Foreign Key to Car Category entity
* price - Price to Rent the Car
* isReserved - check to see if a car is reserved for use
* inUse - check to see if a client is using a car

NOTE: if isReserved = false & inUse = false then Car is available

###### Data Types
    private Long carId;

    private String registrationNumber;

    private CarModel carModel;

    private CarCategory carCategory;  

    private Currency price;

    private Boolean isReserved;

    private Boolean inUse;

### Client
###### Data Model
* clientId - Id of the Client
* fistName - First Name of the Client
* lastName - Last Name of the Client
* mobileNumber - Phone number of the Client
* emailAddress - Email of the Client
* homeAddress - Address of where the Client lives
###### Data Types
    private Long clientId;

    private String firstName;

    private String lastName;
    
    private String mobileNumber;
    
    private String emailAddress;

    private String homeAddress;

### Rental Schedule
###### Data Model
* rentalId - Id of the rental ledger
* car - (Many-to-Many) Foreign Key to Car entity
* client - (Many-to-Many) Foreign Key to Client entity
* collectionDate - Date for the Client to pick the car up from reservation
* returnDate - Date for the Client to return the car
###### Data Types
    private Long rentalId;

    private Car car;

    private Client client;

    private Date collectionDate;

    private Date returnDate;