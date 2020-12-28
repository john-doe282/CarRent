package com.andrew.rental.repository;

import com.andrew.rental.domain.ActiveRent;

import com.andrew.rental.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ActiveRent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActiveRentRepository extends JpaRepository<ActiveRent, Long> {
    Page<ActiveRent> findAllByClient(Pageable pageable, User client);
}
