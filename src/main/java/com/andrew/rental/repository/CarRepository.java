package com.andrew.rental.repository;

import com.andrew.rental.domain.Car;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Car entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("select car from Car car where car.owner.login = ?#{principal.username}")
    List<Car> findByOwnerIsCurrentUser();
}
