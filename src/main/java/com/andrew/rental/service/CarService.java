package com.andrew.rental.service;

import com.andrew.rental.domain.Car;
import com.andrew.rental.domain.User;
import com.andrew.rental.domain.enumeration.CarStatus;
import com.andrew.rental.repository.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Car}.
 */
@Service
@Transactional
public class CarService {

    private final Logger log = LoggerFactory.getLogger(CarService.class);

    private final CarRepository carRepository;

    private final PoliceService policeService;

    public CarService(CarRepository carRepository,
                      PoliceService policeService) {
        this.carRepository = carRepository;
        this.policeService = policeService;
    }

    /**
     * Save a car.
     *
     * @param car the entity to save.
     * @return the persisted entity.
     */
    public Car save(Car car) {
        log.debug("Request to save Car : {}", car);
        return carRepository.save(car);
    }

    public Car setStatusById(Long id, CarStatus status) {
        Optional<Car> car = carRepository.findById(id);
        car.get().status(status);
        return carRepository.save(car.get());
    }
    /**
     * Get all the cars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Car> findAll(Pageable pageable) {
        log.debug("Request to get all Cars");
        return carRepository.findAll(pageable);
    }


    /**
     * Get one car by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Car> getCarById(Long id) {
        log.debug("Request to get Car : {}", id);
        return carRepository.findById(id);
    }

    /**
     * Delete the car by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        carRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public Page<Car> findAllAvailable(Pageable pageable) {
        log.debug("Request to get all Cars");

        return carRepository.findAllByStatus(pageable, CarStatus.AVAILABLE);
    }

    @Transactional(readOnly = true)
    public Page<Car> findAllByOwner(Pageable pageable, User owner) {
        log.debug("Request to get all Cars");

        return carRepository.findAllByOwner(pageable, owner);
    }

    public boolean checkCar(Car car) {
        return policeService.eligible(car);
    }
}
