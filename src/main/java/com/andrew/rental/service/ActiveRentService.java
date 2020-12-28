package com.andrew.rental.service;

import com.andrew.rental.domain.ActiveRent;
import com.andrew.rental.domain.Car;
import com.andrew.rental.domain.User;
import com.andrew.rental.domain.enumeration.CarStatus;
import com.andrew.rental.repository.ActiveRentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ActiveRent}.
 */
@Service
@Transactional
public class ActiveRentService {

    private final Logger log = LoggerFactory.getLogger(ActiveRentService.class);

    private final ActiveRentRepository activeRentRepository;

    private final PaymentService paymentService;

    private final CarService carService;

    private final UserService userService;

    public ActiveRentService(ActiveRentRepository activeRentRepository,
                             PaymentService paymentService,
                             CarService carService,
                             UserService userService) {
        this.activeRentRepository = activeRentRepository;
        this.paymentService = paymentService;
        this.carService = carService;
        this.userService = userService;
    }

    private final double tax = 0.3;

    /**
     * Save a activeRent.
     *
     * @param activeRent the entity to save.
     * @return the persisted entity.
     */
    public ActiveRent rent(ActiveRent activeRent) throws IllegalAccessException {


        if (activeRent.getDuration().isNegative() || activeRent.getDuration().isZero()) {
            throw new IllegalAccessException("Duration can't be negative");
        }
        Long carId = activeRent.getCar().getId();
        Long clientId = activeRent.getClient().getId();

//        User client = userService.;
        User client = userService.getUserById(clientId).get();
        Car car = carService.getCarById(carId).get();

//        Long cl
        if (car.getStatus() != CarStatus.AVAILABLE) {
            throw new IllegalAccessException("The car is not available");
        }

        if (!carService.checkCar(car)) {
            throw new IllegalAccessException("The car is not eligible for renting");
        }

        User owner = car.getOwner();

        Duration duration = activeRent.getDuration();

        BigDecimal amount = car.getPricePerHour().
            multiply(BigDecimal.valueOf(duration.toHours() * (1 - tax)));

        if (client.getIban()==null || owner.getIban()==null) {
            throw new IllegalAccessException("No bank card");

        }
        boolean success = paymentService.
            transaction(client.getIban(), owner.getIban(), amount);

        if (!success) {
            throw new IllegalAccessException("Try again");
        }

        carService.setStatusById(car.getId(), CarStatus.RENTED);

        activeRent.setCreatedAt(Instant.now());
        log.debug("Request to save ActiveRent : {}", activeRent);
        return activeRentRepository.save(activeRent);
    }

    /**
     * Get all the activeRents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ActiveRent> findAll(Pageable pageable) {
        log.debug("Request to get all ActiveRents");
        return activeRentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<ActiveRent> findAllByUser(Pageable pageable, User user) {
        log.debug("Request to get all ActiveRents");
        return activeRentRepository.findAllByClient(pageable, user);
    }


    /**
     * Get one activeRent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActiveRent> findOne(Long id) {
        log.debug("Request to get ActiveRent : {}", id);
        return activeRentRepository.findById(id);
    }

    /**
     * Delete the activeRent by id.
     *
     * @param id the id of the entity.
     */
    public void closeRent(Long id) throws IllegalAccessException {
        log.debug("Request to delete ActiveRent : {}", id);
        Optional<ActiveRent> rent = activeRentRepository.findById(id);
        if (!rent.isPresent()) {
            throw new IllegalAccessException("Bad request");
        }


        if (Duration.between(Instant.now(), rent.get().getCreatedAt())
            .compareTo(rent.get().getDuration()) > 0) {
            BigDecimal fine = BigDecimal.valueOf(50);
            paymentService.transaction("sender", "receiver",
                fine);
        }

        carService.setStatusById(rent.get().getCar().getId(),
            CarStatus.AVAILABLE);

        activeRentRepository.deleteById(id);
    }
}
