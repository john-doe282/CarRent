package com.andrew.rental.repository;

import com.andrew.rental.domain.Car;

import com.andrew.rental.domain.User;
import com.andrew.rental.domain.enumeration.CarStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

///**
// * Spring Data  repository for the Car entity.
// */
//@SuppressWarnings("unused")
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Page<Car> findAllByOwnerAndStatus(Pageable pageable,
                                      User owner, CarStatus status);

    Page<Car> findAllByOwner(Pageable pageable, User owner);

    Page<Car> findAllByStatus(Pageable pageable, CarStatus status);
}
